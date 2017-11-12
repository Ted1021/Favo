package taewon.navercorp.integratedsns.feed;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.Toast;

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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.comment.CommentListAdapter;
import taewon.navercorp.integratedsns.interfaces.TwitchService;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.TwitchStreamingData;
import taewon.navercorp.integratedsns.model.TwitchUserData;
import taewon.navercorp.integratedsns.model.comment.FacebookCommentData;
import taewon.navercorp.integratedsns.model.comment.FavoCommentData;
import taewon.navercorp.integratedsns.model.comment.YoutubeCommentData;
import taewon.navercorp.integratedsns.model.feed.FavoFeedData;
import taewon.navercorp.integratedsns.model.feed.facebook.FacebookFeedData;
import taewon.navercorp.integratedsns.model.feed.twitch.TwitchFollowingData;
import taewon.navercorp.integratedsns.model.feed.twitch.TwitchVideoData;
import taewon.navercorp.integratedsns.model.feed.youtube.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.model.feed.youtube.YoutubeSubscriptionData;

import static android.content.Context.MODE_PRIVATE;
import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_IMAGE;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_VIDEO;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_BASE_URL;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

/**
 * @author 김태원
 * @file FacebookFragment.java
 * @brief show facebook contents, search & add facebook pages
 * @date 2017.09.28
 */

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // for managing tokens
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    // for pinterest client
    private PDKClient mPinterestClient;

    // BroadcastReceivers
    private BroadcastReceiver mTokenUpdateReceiver, mAsyncFinishReceiver, mScrollToTopReceiver, mCommentRequestReceiver;

    // Feed list components
    private RecyclerView mFeedList;
    private Vector<FavoFeedData> mFeedDataset = new Vector<>();
    private FeedListAdapter mFeedAdapter;
    private RecyclerView.LayoutManager mFeedLayoutManager;
    private SwipeRefreshLayout mRefreshLayout;
//    private RelativeLayout mLayoutDisconnection;

    // Comment list components
    private SlidingUpPanelLayout mCommentSlidingLayout;
    private RecyclerView mCommentList;
    private CommentListAdapter mCommentAdapter;
    private ArrayList<FavoCommentData> mCommentDataset = new ArrayList<>();

    private Realm mRealm;
    private SimpleDateFormat mStringFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private String tempUserId = "102859695";

    private int mAsyncCount = 0;
    private int mLastPosition = 0;

    private static final String BOARD_FIELDS = "id,name";
    private static final String PIN_FIELDS = "board,created_at,creator,id,image,media,note,original_link";
    private static final int MAX_COUNTS = 10;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        initData();
        initView(view);

        if (isInit) {
            isInit = false;
            checkToken();
        }
        return view;
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        // view for disconnection
//        mLayoutDisconnection = (RelativeLayout) view.findViewById(R.id.layout_disconnection);

        // set feed recyclerView
        mFeedList = (RecyclerView) view.findViewById(R.id.recyclerView_feed);
        mFeedAdapter = new FeedListAdapter(getContext(), mFeedDataset, mRealm);
        mFeedList.setAdapter(mFeedAdapter);
        mFeedLayoutManager = new LinearLayoutManager(getContext());
        mFeedList.setLayoutManager(mFeedLayoutManager);

        // set comment recyclerView
        mCommentList = (RecyclerView) view.findViewById(R.id.recyclerView_commentList);
        mCommentAdapter = new CommentListAdapter(getContext(), mCommentDataset);
        mCommentList.setAdapter(mCommentAdapter);
        RecyclerView.LayoutManager commentLayoutManager = new LinearLayoutManager(getContext());
        mCommentList.setLayoutManager(commentLayoutManager);

        // set sliding panel for comment
        mCommentSlidingLayout = (SlidingUpPanelLayout) view.findViewById(R.id.slidingLayout);
        mCommentSlidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
        mCommentSlidingLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommentSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    private void initData() {

        // realm Instance
        mRealm = Realm.getDefaultInstance();

        // preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        // pinterest client
        PDKClient.configureInstance(getContext(), getString(R.string.pinterest_app_id));
        mPinterestClient = PDKClient.getInstance();

        // update token status receiver
        mTokenUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkToken();
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
                    int currentPosition = ((LinearLayoutManager) mFeedLayoutManager).findFirstCompletelyVisibleItemPosition();
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

        // comment request receiver
        mCommentRequestReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int platformType = intent.getIntExtra("PLATFORM_TYPE", 0);
                String feedId = intent.getStringExtra("FEED_ID");

                loadComments(platformType, feedId);
            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTokenUpdateReceiver, new IntentFilter(getString(R.string.update_token_status)));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mAsyncFinishReceiver, new IntentFilter(getString(R.string.async_finish_status)));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mScrollToTopReceiver, new IntentFilter(getString(R.string.scroll_to_top_status)));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mCommentRequestReceiver, new IntentFilter(getString(R.string.comment_request)));
    }

    private void checkToken() {

        mAsyncCount = 0;
        String facebookToken = mPref.getString(getString(R.string.facebook_token), "");
        String googleToken = mPref.getString(getString(R.string.google_token), "");
        String pinterestToken = mPref.getString(getString(R.string.pinterest_token), "");
        String twitchToken = mPref.getString(getString(R.string.twitch_token), "");

        mFeedDataset.clear();
        mFeedAdapter.notifyDataSetChanged();

        if (!facebookToken.equals("")) {
            getFacebookUserPages();
        }

        if (!googleToken.equals("")) {
            getYoutubeSubscriptionList();
        }

        if (!pinterestToken.equals("")) {
            getPinterestFollowingBoards();
        }

        if (!twitchToken.equals("")) {
            getTwitchFollowingList();
        }
    }

    private void refreshDataset() {

        Collections.sort(mFeedDataset, new Comparator<FavoFeedData>() {
            @Override
            public int compare(FavoFeedData o1, FavoFeedData o2) {
                return o2.getPubDate().compareTo(o1.getPubDate());
            }
        });

        mFeedAdapter.notifyDataSetChanged();
        if (mLastPosition > mFeedDataset.size() - 1) {
            mLastPosition = mFeedDataset.size() - 1;
        }
        mRefreshLayout.setRefreshing(false);
    }

    private void scrollToTopPosition(int position) {
        if (position > 10) {
            mFeedLayoutManager.smoothScrollToPosition(mFeedList, null, 10);
            mFeedLayoutManager.scrollToPosition(10);
        }
        mFeedLayoutManager.smoothScrollToPosition(mFeedList, null, 0);
    }

    private void scrollToLastPosition(int position) {
        if (position >= 10) {
            mFeedLayoutManager.smoothScrollToPosition(mFeedList, null, position - 10);
            mFeedLayoutManager.scrollToPosition(position - 10);
        }
        mFeedLayoutManager.smoothScrollToPosition(mFeedList, null, position);
    }

    private void loadComments(int platformType, String feedId) {

        mCommentDataset.clear();
        mCommentAdapter.notifyDataSetChanged();

        mCommentSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        switch (platformType) {
            case PLATFORM_FACEBOOK:
                getFacebookComment(feedId);
                break;

            case PLATFORM_YOUTUBE:
                getYoutubeComment(feedId);
                break;
        }
    }

    // send status of asyncTasks
    private void sendAsyncStatus() {
        Intent intent = new Intent(getString(R.string.async_finish_status));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    // Facebook API Call
    private void getFacebookUserPages() {

        mRefreshLayout.setRefreshing(true);
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
        parameters.putString("limit", "10");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getFacebookPageFeed(String pageId) {

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
                                FacebookFeedData.ArticleData article;
                                for (int i = 0; i < result.getData().size(); i++) {

                                    article = result.getData().get(i);
                                    FavoFeedData data = new FavoFeedData();

                                    data.setPlatformType(PLATFORM_FACEBOOK);
                                    if (!(article.getSource() == null)) {
                                        data.setContentsType(CONTENTS_VIDEO);
                                        data.setVideoUrl(article.getSource());
                                    } else {
                                        data.setContentsType(CONTENTS_IMAGE);
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
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        synchronized ((Integer) mAsyncCount) {
                            mAsyncCount = mAsyncCount - 1;
                        }

                        if (mAsyncCount == 0) {
                            sendAsyncStatus();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "link,created_time,message,full_picture,likes.limit(0).summary(true),comments.limit(0).summary(true),from{name, picture.height(2048){url}},attachments{subattachments},source");
        parameters.putString("limit", "10");
        request.setParameters(parameters);
        request.executeAsync();
    }

    // Pinterest API Call
    private void getPinterestFollowingBoards() {

        mRefreshLayout.setRefreshing(true);
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

                    for (PDKPin pin : response.getPinList()) {

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
                    }

                    synchronized ((Integer) mAsyncCount) {
                        mAsyncCount = mAsyncCount - 1;
                    }
                    if (mAsyncCount == 0) {
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
                    if (mAsyncCount == 0) {
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

        mRefreshLayout.setRefreshing(true);

        // get google credential access token
        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), null));

        // set retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // get 'subscriptions' from youtube data api v3
        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSubscriptionData> call = service.getSubscriptionList(accessToken, "snippet", MAX_COUNTS, true);
        call.enqueue(new Callback<YoutubeSubscriptionData>() {
            @Override
            public void onResponse(Call<YoutubeSubscriptionData> call, Response<YoutubeSubscriptionData> response) {
                if (response.isSuccessful()) {

                    mAsyncCount = mAsyncCount + response.body().getItems().size();
                    for (YoutubeSubscriptionData.Item item : response.body().getItems()) {

                        String[] params = {item.getSnippet().getResourceId().getChannelId(), item.getSnippet().getThumbnails().getHigh().getUrl()};
                        new GetYoutubeChannelVideos().executeOnExecutor(THREAD_POOL_EXECUTOR, params);
                    }
                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> Token is expired" + response.toString());

                    mEditor.putString(getString(R.string.google_token), "");
                    mEditor.commit();
                    checkToken();
                }
            }

            @Override
            public void onFailure(Call<YoutubeSubscriptionData> call, Throwable t) {
                Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> fail to access youtube api server");
                Toast.makeText(getContext(), "Fail to access youtube server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class GetYoutubeChannelVideos extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... params) {

            String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));
            final String profileUrl = params[1];

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(YOUTUBE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            YoutubeService service = retrofit.create(YoutubeService.class);
            Call<YoutubeSearchVideoData> call = service.getVideoList(accessToken, "snippet", MAX_COUNTS, params[0], "date", "video", null, null);
            call.enqueue(new Callback<YoutubeSearchVideoData>() {
                @Override
                public void onResponse(Call<YoutubeSearchVideoData> call, Response<YoutubeSearchVideoData> response) {
                    if (response.isSuccessful()) {

                        for (YoutubeSearchVideoData.Item item : response.body().getItems()) {

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
                        }
                    } else {
                        Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to get json for video");
                    }

                    synchronized ((Integer) mAsyncCount) {
                        mAsyncCount = mAsyncCount - 1;
                    }
                    if (mAsyncCount == 0) {
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
                    if (mAsyncCount == 0) {
                        sendAsyncStatus();
                    }
                }
            });
            return null;
        }
    }

    // call twitch api
    private void getTwitchFollowingList() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);

        Call<TwitchFollowingData> call = service.getTwitchFollowingInfo(getString(R.string.twitch_client_id), "102859695");
        call.enqueue(new Callback<TwitchFollowingData>() {
            @Override
            public void onResponse(Call<TwitchFollowingData> call, Response<TwitchFollowingData> response) {
                if (response.isSuccessful()) {
                    for (TwitchFollowingData.FollowingInfo result : response.body().getData()) {
                        getTwitchStreamerInfo(result.getToId());
                    }
                } else {
                    Log.e("ERROR_TWTICH", "get following list error " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<TwitchFollowingData> call, Throwable t) {
                t.printStackTrace();
                Log.e("ERROR_TWTICH", "get following list error !!!!!!!!!!!!!!");
            }
        });
    }

    private void getTwitchStreamerInfo(String userId) {

        String currentToken = "Bearer " + mPref.getString(getString(R.string.twitch_token), "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);

        Call<TwitchUserData> call = service.getTwitchUserInfo(getString(R.string.twitch_client_id), currentToken, userId);
        call.enqueue(new Callback<TwitchUserData>() {
            @Override
            public void onResponse(Call<TwitchUserData> call, Response<TwitchUserData> response) {
                if (response.isSuccessful()) {
                    TwitchUserData.UserInfo result = response.body().getData().get(0);
                    getTwitchVideoList(result.getProfileImageUrl(), result.getDisplayName(), result.getId());
                } else {
                    Log.e("ERROR_TWITCH", "Feed Fragment >>>>> " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<TwitchUserData> call, Throwable t) {
                t.printStackTrace();
                Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to get user ");
            }
        });
    }

    private void getTwitchVideoList(final String profileUrl, final String userName, String userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);

        Call<TwitchVideoData> call = service.getTwitchVideoInfo(getString(R.string.twitch_client_id), userId);
        call.enqueue(new Callback<TwitchVideoData>() {
            @Override
            public void onResponse(Call<TwitchVideoData> call, Response<TwitchVideoData> response) {
                if (response.isSuccessful()) {

                    for (TwitchVideoData.VideoInfo result : response.body().getData()) {

                        FavoFeedData data = new FavoFeedData();

                        try {
                            data.setPubDate(mDateFormat.parse(result.getPublishedAt()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        data.setFeedId(result.getId());
                        data.setPlatformType(PLATFORM_TWITCH);
                        data.setContentsType(CONTENTS_VIDEO);
                        data.setPageId(result.getUserId());
                        data.setProfileImage(profileUrl);
                        data.setUserName(userName);
                        data.setCreatedTime(mStringFormat.format(data.getPubDate()));
                        int position = result.getThumbnailUrl().indexOf("{width}");
                        String thumbnail = result.getThumbnailUrl().substring(0, position - 1) + "1280x720.jpg";
                        data.setPicture(thumbnail);
                        data.setVideoUrl(result.getId());
                        data.setDescription(result.getTitle());

                        mFeedDataset.add(data);
                    }
                } else {
                    Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to login // " + response.raw().toString());
                }
//                mFeedAdapter.notifyDataSetChanged();
                mFeedAdapter.notifyItemInserted(mFeedAdapter.getItemCount() + 1);
            }

            @Override
            public void onFailure(Call<TwitchVideoData> call, Throwable t) {
                Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to login ");
                t.printStackTrace();
            }
        });
    }

    private void getTwitchStreams(final String profileUrl, final String userName) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwitchService service = retrofit.create(TwitchService.class);
        Call<TwitchStreamingData> call = service.getTwitchStreams(getString(R.string.twitch_client_id), 20);
        call.enqueue(new Callback<TwitchStreamingData>() {
            @Override
            public void onResponse(Call<TwitchStreamingData> call, Response<TwitchStreamingData> response) {
                if (response.isSuccessful()) {

                    for (TwitchStreamingData.StreamInfo result : response.body().getData()) {

                        FavoFeedData data = new FavoFeedData();

                        try {
                            data.setPubDate(mDateFormat.parse(result.getStartedAt()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        data.setFeedId(result.getId());
                        data.setPlatformType(PLATFORM_TWITCH);
                        data.setContentsType(CONTENTS_VIDEO);
                        data.setPageId(result.getUserId());
                        data.setProfileImage(profileUrl);
                        data.setUserName(userName);
                        data.setCreatedTime(mStringFormat.format(data.getPubDate()));
                        int position = result.getThumbnailUrl().indexOf("{width}");
                        String thumbnail = result.getThumbnailUrl().substring(0, position) + "1280x720.jpg";
                        data.setPicture(thumbnail);
                        data.setVideoUrl(result.getId());
                        data.setDescription(result.getTitle());

                        mFeedDataset.add(data);
                    }
                    mFeedAdapter.notifyDataSetChanged();

                } else {
                    Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to login // " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<TwitchStreamingData> call, Throwable t) {
                Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to login ");
                t.printStackTrace();
            }
        });
    }

    private void getFacebookComment(String feedId) {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                feedId,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() == null) {

                            FacebookCommentData result = new Gson().fromJson(response.getJSONObject().toString(), FacebookCommentData.class);
                            for (FacebookCommentData.Comments.CommentData comment : result.getComments().getData()) {

                                FavoCommentData data = new FavoCommentData();

                                data.setProfileImage(comment.getFrom().getPicture().getData().getUrl());
                                data.setCreatedTime(comment.getUploadTime());
                                data.setMessage(comment.getMessage());
                                data.setUserName(comment.getFrom().getName());

                                mCommentDataset.add(data);
                            }
                            mCommentAdapter.notifyDataSetChanged();

                        } else {
                            Log.e("CHECK_COMMENT", response.getError().toString());
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "created_time,message,full_picture,from{name, picture.height(1024){url}},attachments{subattachments},source,likes.limit(0).summary(true),comments.summary(true){from{name, picture.height(1024){url}},message,created_time}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getYoutubeComment(String feedId) {

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeCommentData> call = service.getCommentListNext(accessToken, "snippet", null, MAX_COUNTS, feedId);
        call.enqueue(new Callback<YoutubeCommentData>() {
            @Override
            public void onResponse(Call<YoutubeCommentData> call, Response<YoutubeCommentData> response) {

                if (response.isSuccessful()) {

//                    if (response.body().getNextPageToken() == null) {
//                        return;
//                    }

//                    mNextPage = response.body().getNextPageToken();

                    for (YoutubeCommentData.Item result : response.body().getItems()) {

                        YoutubeCommentData.Item.TopLevelComment.Author comment = result.getSnippet().getTopLevelComment().getSnippet();
                        FavoCommentData data = new FavoCommentData();

                        data.setProfileImage(comment.getAuthorProfileImageUrl());
                        data.setCreatedTime(comment.getPublishedAt());
                        data.setMessage(comment.getTextOriginal());
                        data.setUserName(comment.getAuthorDisplayName());

                        mCommentDataset.add(data);
                    }
                    mCommentAdapter.notifyDataSetChanged();

                } else {
                    Log.e("ERROR_YOUTUBE", "Comment Activity >>>>> Fail to get json for video " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<YoutubeCommentData> call, Throwable t) {
                t.printStackTrace();
                Log.e("ERROR_YOUTUBE", "Comment Activity >>>>> Fail to access youtube api server");
            }
        });
    }

    @Override
    public void onRefresh() {

        mFeedDataset.clear();
        mFeedAdapter.notifyDataSetChanged();
        checkToken();
    }
}