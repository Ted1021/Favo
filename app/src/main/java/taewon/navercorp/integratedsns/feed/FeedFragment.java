package taewon.navercorp.integratedsns.feed;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.TwitchService;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.facebook.FacebookFeedData;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;
import taewon.navercorp.integratedsns.model.favo.Photo;
import taewon.navercorp.integratedsns.model.twitch.TwitchUserFollowingData;
import taewon.navercorp.integratedsns.model.twitch.TwitchVideoData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSubscriptionData;
import taewon.navercorp.integratedsns.search.SearchActivity;
import taewon.navercorp.integratedsns.util.EndlessRecyclerViewScrollListener;
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_IMAGE;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_MULTI_IMAGE;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_VIDEO;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_ACCEPT_CODE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_BASE_URL;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

/**
 * @author 김태원
 * @file FacebookFragment.java
 * @brief show facebook contents, search & add facebook pages
 * @date 2017.09.28
 */

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FavoTokenManager mFavoTokenManager;

    // key : platform + pageId
    // value : nextPageToken
    private HashMap<String, String> mPlatformPagingInfo = new HashMap<>();
    private EndlessRecyclerViewScrollListener mPagingListener;

    // for pinterest client
    private PDKClient mPinterestClient;

    // BroadcastReceivers
    private BroadcastReceiver mTokenUpdateReceiver, mAsyncFinishReceiver, mScrollToTopReceiver;

    // Feed list components
    private RecyclerView mFeedList;
    private Vector<FavoFeedData> mFeedDataset = new Vector<>();
    private FeedListAdapter mFeedAdapter;
    private RecyclerView.LayoutManager mFeedLayoutManager;
    private SwipeRefreshLayout mRefreshLayout;
//    private RelativeLayout mLayoutDisconnection;
    private FrameLayout mLayoutTitle;
    private ImageButton mSearch;

    private Realm mRealm;
    private SimpleDateFormat mStringFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private int mAsyncCount = 0;
    private int mLastPosition = 0;

    // recyclerView scroll state
    public static final int SCROLL_UP = 0;
    public static final int SCROLL_DOWN = 1;

    private static final String BOARD_FIELDS = "id,name";
    private static final String PIN_FIELDS = "board,created_at,creator,id,image,media,note,original_link";
    private static final int MAX_PAGE_COUNT = 10;
    private static final int MAX_ARTICLE_COUNT = 2;

    private static boolean isInit;

    public FeedFragment() {
    }

    public static FeedFragment newInstance() {

        FeedFragment fragment = new FeedFragment();
        isInit = true;
        return fragment;
    }

    @Override
    public void onDestroyView() {
        // close Realm Instance
        mRealm.close();

        // destroy broadcast receiver along with fragment life cycle
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTokenUpdateReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mAsyncFinishReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mScrollToTopReceiver);

        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        initData();
        initView(view);

        if (isInit) {
            isInit = false;
            loadMore();
        }
        return view;
    }

    private void initData() {

        // realm Instance
        mRealm = Realm.getDefaultInstance();

        // init token manager
        mFavoTokenManager = FavoTokenManager.getInstance();

        // pinterest client
        PDKClient.configureInstance(getContext(), getString(R.string.pinterest_app_id));
        mPinterestClient = PDKClient.getInstance();

        // update token status receiver
        mTokenUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadMore();
            }
        };

        // check async requests status receiver
        mAsyncFinishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshDataset();
            }
        };

        // check scroll to top request status receiver
        mScrollToTopReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (!mRefreshLayout.isRefreshing()) {
                    int currentPosition = ((LinearLayoutManager) mFeedLayoutManager).findFirstVisibleItemPosition();
                    if (currentPosition == 0) {
                        scrollToLastPosition(mLastPosition);
                        mLastPosition = 0;
                    } else {
                        mLastPosition = currentPosition;
                        scrollToTopPosition(currentPosition);
                    }
                }
            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTokenUpdateReceiver, new IntentFilter(getString(R.string.update_token_status)));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mAsyncFinishReceiver, new IntentFilter(getString(R.string.async_finish_status)));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mScrollToTopReceiver, new IntentFilter(getString(R.string.scroll_to_top_status)));
    }

    private void initView(View view) {

        mSearch = (ImageButton) view.findViewById(R.id.button_search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mLayoutTitle = (FrameLayout) view.findViewById(R.id.layout_title);

        // view for disconnection
//        mLayoutDisconnection = (RelativeLayout) view.findViewById(R.id.layout_disconnection);

        // set feed recyclerView
        mFeedList = (RecyclerView) view.findViewById(R.id.recyclerView_feed);
        mFeedAdapter = new FeedListAdapter(getContext(), mFeedDataset, mRealm);
        mFeedList.setAdapter(mFeedAdapter);
        mFeedLayoutManager = new LinearLayoutManager(getContext());
        mFeedLayoutManager.setItemPrefetchEnabled(true);
        mFeedList.setLayoutManager(mFeedLayoutManager);

        mPagingListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) mFeedLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.d("CHECK_PAGE", "page : " + page + "");
                Log.d("CHECK_PAGE", "totalItemCount : " + totalItemsCount + "");
                Log.d("CHECK_PAGE", "VisibleItemCount : " + view.getChildCount() + "");

                loadMore();
                Glide.get(getContext()).clearMemory();
            }
        };
        mFeedList.addOnScrollListener(mPagingListener);
    }

    private void loadMore() {

        mAsyncCount = 0;
        mRefreshLayout.setRefreshing(true);
        if (mFavoTokenManager.isTokenVaild(PLATFORM_FACEBOOK)) {
            getFacebookUserPages();
        }

        if (mFavoTokenManager.isTokenVaild(PLATFORM_YOUTUBE)) {
            getYoutubeSubscriptionList();
        }

        if (mFavoTokenManager.isTokenVaild(PLATFORM_PINTEREST)) {
            getPinterestFollowingBoards();
        }

        if (mFavoTokenManager.isTokenVaild(PLATFORM_TWITCH)) {
            getTwitchFollowingUserInfo();
        }
    }

    private void refreshDataset() {

        mFeedAdapter.notifyDataSetChanged();
        if (mLastPosition > mFeedDataset.size() - 1) {
            mLastPosition = mFeedDataset.size() - 1;
        }
        mRefreshLayout.setRefreshing(false);
    }

    private void scrollToTopPosition(int position) {

        if (position > 5) {
            mFeedLayoutManager.smoothScrollToPosition(mFeedList, null, 5);
            mFeedLayoutManager.scrollToPosition(5);
        }
        mFeedLayoutManager.smoothScrollToPosition(mFeedList, null, 0);
//        mFeedLayoutManager.scrollToPosition(1);
    }

    private void scrollToLastPosition(int position) {

        Log.d("POSITION", String.valueOf(position));
        if (position < 0) {
            return;
        }

        if (position > 5) {
            mFeedLayoutManager.smoothScrollToPosition(mFeedList, null, position - 5);
            mFeedLayoutManager.scrollToPosition(position - 5);
        }
        mFeedLayoutManager.smoothScrollToPosition(mFeedList, null, position);
    }

    // send status of asyncTasks
    private void sendAsyncStatus() {
        Intent intent = new Intent(getString(R.string.async_finish_status));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    // Facebook API Call
    private void getFacebookUserPages() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/likes",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() == null) {
                            try {

                                JSONArray results = response.getJSONObject().getJSONArray("data");
                                JSONObject pageInfo;

                                mAsyncCount = mAsyncCount + results.length();
                                for (int i = 0; i < results.length(); i++) {
                                    pageInfo = results.getJSONObject(i);
                                    getFacebookPageFeed(pageInfo.getString("id"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("ERROR_FACEBOOK", "Feed Fragment >>>>> getFacebookUserPages() " + response.getError().getErrorMessage());
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("limit", MAX_PAGE_COUNT + "");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getFacebookPageFeed(final String pageId) {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String path = String.format("/%s/feed", pageId);
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                path,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() == null) {
                            try {
                                FacebookFeedData result = new Gson().fromJson(response.getJSONObject().toString(), FacebookFeedData.class);
                                if (result.getPaging() != null) {
                                    mPlatformPagingInfo.put(PLATFORM_FACEBOOK + pageId, result.getPaging().getCursors().getAfter());
                                }
                                FacebookFeedData.ArticleData article;
                                for (int i = 0; i < result.getData().size(); i++) {

                                    article = result.getData().get(i);
                                    FavoFeedData data = new FavoFeedData();

                                    data.setPlatformType(PLATFORM_FACEBOOK);

                                    if (!(article.getSource() == null)) {
                                        data.setContentsType(CONTENTS_VIDEO);
                                        data.setVideoUrl(article.getSource());
                                    } else {
                                        if (article.getAttachments() == null) {
                                            data.setContentsType(CONTENTS_IMAGE);
                                        } else {
                                            data.setContentsType(CONTENTS_MULTI_IMAGE);
                                            ArrayList<Photo> imageset = new ArrayList<>();
                                            for (FacebookFeedData.ArticleData.Attachments.PhotoSet.Subattachments.Photo item : article.getAttachments().getData().get(0).getSubattachments().getData()) {
                                                Photo image = new Photo();
                                                image.setHeight(item.getMedia().getImage().getHeight());
                                                image.setWidth(item.getMedia().getImage().getWidth());
                                                image.setSrc(item.getMedia().getImage().getSrc());
                                                imageset.add(image);
                                            }
                                            data.setSubAttatchments(imageset);
                                        }
                                    }
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date date = format.parse(article.getCreatedTime());
                                    data.setPubDate(date);

                                    data.setPageId(article.getFrom().getId());
                                    data.setFeedId(article.getId());
                                    data.setProfileImage(article.getFrom().getPicture().getProfileData().getUrl());
                                    data.setUserName(article.getFrom().getName());
                                    data.setCreatedTime(mStringFormat.format(date));
                                    data.setPicture(article.getFullPicture());
                                    data.setLink(article.getLink());
                                    data.setDescription(article.getMessage());
                                    data.setLikeCount(article.getLikes().getSummary().getTotalCount());
                                    data.setCommentCount(article.getComments().getSummary().getTotalCount());

                                    mFeedDataset.add(data);
//                                    mFeedAdapter.notifyItemInserted(mFeedAdapter.getItemCount() + 1);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        synchronized ((Integer) mAsyncCount) {
                            mAsyncCount = mAsyncCount - 1;
                        }

                        if (mAsyncCount <= 0) {
                            sendAsyncStatus();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        if (mPlatformPagingInfo.get(PLATFORM_FACEBOOK + pageId) != null) {
            parameters.putString("after", mPlatformPagingInfo.get(PLATFORM_FACEBOOK + pageId));
        }
        parameters.putString("fields", "link,created_time,message,full_picture,likes.limit(0).summary(true),comments.limit(0).summary(true),from{name, picture.height(2048){url}},attachments{subattachments},source");
        parameters.putString("limit", MAX_ARTICLE_COUNT+"");
        request.setParameters(parameters);
        request.executeAsync();
    }

    // Pinterest API Call
    private void getPinterestFollowingBoards() {

        mPinterestClient.getMyFollowedBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);

                mAsyncCount = mAsyncCount + response.getBoardList().size();
                for (PDKBoard board : response.getBoardList()) {
                    new GetPinterestFollowingPins().executeOnExecutor(THREAD_POOL_EXECUTOR, board.getUid());
                }
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
            }
        });
    }

    private class GetPinterestFollowingPins extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            mPinterestClient.getBoardPins(params[0], PIN_FIELDS, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    super.onSuccess(response);

                    PDKPin pin = response.getPinList().get(0);

                    FavoFeedData data = new FavoFeedData();

                    data.setPlatformType(PLATFORM_PINTEREST);
                    data.setContentsType(CONTENTS_IMAGE);
                    data.setPubDate(pin.getCreatedAt());

                    data.setFeedId(pin.getUid());
                    data.setProfileImage(pin.getImageUrl());
                    data.setUserName(pin.getUid());
                    data.setCreatedTime(mStringFormat.format(pin.getCreatedAt()));
                    data.setPicture(pin.getImageUrl());
                    data.setLink(pin.getLink());
                    data.setDescription(pin.getNote());

                    mFeedDataset.add(data);
//                    mFeedAdapter.notifyItemInserted(mFeedAdapter.getItemCount() + 1);

                    synchronized ((Integer) mAsyncCount) {
                        mAsyncCount = mAsyncCount - 1;
                    }
                    if (mAsyncCount <= 0) {
                        sendAsyncStatus();
                    }
                }

                @Override
                public void onFailure(PDKException exception) {
                    super.onFailure(exception);
                    exception.printStackTrace();
                    synchronized ((Integer) mAsyncCount) {
                        mAsyncCount = mAsyncCount - 1;
                    }
                    if (mAsyncCount <= 0) {
                        sendAsyncStatus();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    // Youtube API Call
    private void getYoutubeSubscriptionList() {

        // get google credential access token
        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        // set retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // get 'subscriptions' from youtube data api v3
        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSubscriptionData> call = service.getSubscriptionList(accessToken, "snippet", MAX_PAGE_COUNT, true, null);
        call.enqueue(new Callback<YoutubeSubscriptionData>() {
            @Override
            public void onResponse(Call<YoutubeSubscriptionData> call, Response<YoutubeSubscriptionData> response) {
                if (response.isSuccessful()) {

                    mAsyncCount = mAsyncCount + response.body().getItems().size();
                    for (YoutubeSubscriptionData.Item item : response.body().getItems()) {
                        getYoutubeChannelVideos(item.getSnippet().getResourceId().getChannelId(), item.getSnippet().getThumbnails().getHigh().getUrl());
                    }
                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> Token is expired" + response.toString());
                    mFavoTokenManager.removeToken(PLATFORM_YOUTUBE);
                }
            }

            @Override
            public void onFailure(Call<YoutubeSubscriptionData> call, Throwable t) {
                Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> fail to access youtube api server");
                Toast.makeText(getContext(), "Fail to access youtube server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getYoutubeChannelVideos(final String channelId, final String profileUrl) {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));
        Log.d("CHECK_PAGING", "youtube page id : " + channelId + " / " + mPlatformPagingInfo.get(PLATFORM_YOUTUBE + channelId));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchVideoData> call = service.getVideoList(accessToken, "snippet", MAX_ARTICLE_COUNT, channelId, mPlatformPagingInfo.get(PLATFORM_YOUTUBE + channelId), null, null, "date", "video", null, null, null);
        call.enqueue(new Callback<YoutubeSearchVideoData>() {
            @Override
            public void onResponse(Call<YoutubeSearchVideoData> call, Response<YoutubeSearchVideoData> response) {
                if (response.isSuccessful()) {

                    YoutubeSearchVideoData result = response.body();
                    if (result.getNextPageToken() == null) {
                        return;
                    } else {
                        mPlatformPagingInfo.put(PLATFORM_YOUTUBE + channelId, result.getNextPageToken());
                    }
                    for (YoutubeSearchVideoData.Item item : result.getItems()) {

                        FavoFeedData data = new FavoFeedData();
                        Date date = null;

                        data.setPlatformType(PLATFORM_YOUTUBE);
                        data.setContentsType(CONTENTS_VIDEO);

                        try {
                            date = mDateFormat.parse(item.getSnippet().getPublishedAt());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        data.setPubDate(date);

                        data.setPageId(item.getSnippet().getChannelId());
                        data.setFeedId(item.getId().getVideoId());
                        data.setProfileImage(profileUrl);
                        data.setUserName(item.getSnippet().getChannelTitle());
                        data.setCreatedTime(mStringFormat.format(date));
                        data.setPicture(item.getSnippet().getThumbnails().getHigh().getUrl());
                        data.setVideoUrl(item.getId().getVideoId());
                        data.setDescription(item.getSnippet().getTitle());

                        mFeedDataset.add(data);
//                        mFeedAdapter.notifyItemInserted(mFeedAdapter.getItemCount() + 1);
                    }
                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to get json for video");
                }

                synchronized ((Integer) mAsyncCount) {
                    mAsyncCount = mAsyncCount - 1;
                }
                if (mAsyncCount <= 0) {
                    sendAsyncStatus();
                }
            }

            @Override
            public void onFailure(Call<YoutubeSearchVideoData> call, Throwable t) {
                t.printStackTrace();
                Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to access youtube api server");

                synchronized ((Integer) mAsyncCount) {
                    mAsyncCount = mAsyncCount - 1;
                }
                if (mAsyncCount <= 0) {
                    sendAsyncStatus();
                }
            }
        });
    }

    // twitch api v5
    private void getTwitchFollowingUserInfo() {
        Log.d("CHECK_ID", mFavoTokenManager.getUserId(PLATFORM_TWITCH+"_id"));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);
        Call<TwitchUserFollowingData> call = service.getTwitchFollowingUser(TWITCH_ACCEPT_CODE, getString(R.string.twitch_client_id), mFavoTokenManager.getUserId(PLATFORM_TWITCH+"_id"), MAX_PAGE_COUNT);
        call.enqueue(new Callback<TwitchUserFollowingData>() {
            @Override
            public void onResponse(Call<TwitchUserFollowingData> call, Response<TwitchUserFollowingData> response) {

                if (response.isSuccessful()) {
                    String userName, userProfile, userId;
                    mAsyncCount = mAsyncCount + response.body().getFollows().size();
                    for (TwitchUserFollowingData.Follow data : response.body().getFollows()) {

                        userName = data.getChannel().getDisplayName();
                        userProfile = data.getChannel().getLogo();
                        userId = data.getChannel().getId();
                        getTwitchVideoList(userProfile, userName, userId);
                    }
                }
            }

            @Override
            public void onFailure(Call<TwitchUserFollowingData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getTwitchVideoList(final String profileUrl, final String userName, final String userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);

        Call<TwitchVideoData> call = service.getTwitchVideoInfo(getString(R.string.twitch_client_id), userId, MAX_ARTICLE_COUNT, mPlatformPagingInfo.get(PLATFORM_TWITCH + userId));
        call.enqueue(new Callback<TwitchVideoData>() {
            @Override
            public void onResponse(Call<TwitchVideoData> call, Response<TwitchVideoData> response) {
                if (response.isSuccessful()) {

                    TwitchVideoData result = response.body();
                    if (result.getPagination() != null) {
                        mPlatformPagingInfo.put(PLATFORM_TWITCH + userId, result.getPagination().getCursor());
                    }

                    for (TwitchVideoData.VideoInfo item : result.getData()) {

                        FavoFeedData data = new FavoFeedData();

                        try {
                            data.setPubDate(mDateFormat.parse(item.getPublishedAt()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        data.setFeedId(item.getId());
                        data.setPlatformType(PLATFORM_TWITCH);
                        data.setContentsType(CONTENTS_VIDEO);
                        data.setPageId(item.getUserId());
                        data.setProfileImage(profileUrl);
                        data.setUserName(userName);
                        data.setCreatedTime(mStringFormat.format(data.getPubDate()));
                        int position = item.getThumbnailUrl().indexOf("{width}");
                        String thumbnail = item.getThumbnailUrl().substring(0, position - 1) + "1280x720.jpg";
                        data.setPicture(thumbnail);
                        data.setVideoUrl(item.getId());
                        data.setDescription(item.getTitle());

                        mFeedDataset.add(data);
//                        mFeedAdapter.notifyItemInserted(mFeedAdapter.getItemCount() + 1);
                    }

                } else {
                    Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to login // " + response.raw().toString());
                }

                synchronized ((Integer) mAsyncCount) {
                    mAsyncCount = mAsyncCount - 1;
                }

                if (mAsyncCount <= 0) {
                    sendAsyncStatus();
                }
            }

            @Override
            public void onFailure(Call<TwitchVideoData> call, Throwable t) {
                Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to login ");
                t.printStackTrace();

                synchronized ((Integer) mAsyncCount) {
                    mAsyncCount = mAsyncCount - 1;
                }

                if (mAsyncCount <= 0) {
                    sendAsyncStatus();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        Glide.get(getContext()).clearMemory();
        mPagingListener.resetState();
        mPlatformPagingInfo.clear();
        mFeedDataset.clear();
        mFeedAdapter.notifyDataSetChanged();
        loadMore();
    }
}