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
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.FacebookFeedData;
import taewon.navercorp.integratedsns.model.FavoFeedData;
import taewon.navercorp.integratedsns.model.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.model.YoutubeSubscriptionData;

import static android.content.Context.MODE_PRIVATE;
import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

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

    // for facebook client
    private OnRequestFacebookTokenListener mCallback;

    // for pinterest client
    private PDKClient mPinterestClient;

    // BroadcastReceiver for updating status of tokens to "FeedFragment"
    private BroadcastReceiver mTokenUpdateReceiver;
    private BroadcastReceiver mAsyncFinishReceiver;

    // UI Components
    private RecyclerView mFacebookList;
    private Vector<FavoFeedData> mDataset = new Vector<>();
    private FeedListAdapter mAdapter;

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mLayoutDisconnection;

    private static final String BOARD_FIELDS = "id,name";
    private static final String PIN_FIELDS = "created_at,creator,id,image, media,note,original_link";

    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    private static final int MAX_COUNTS = 10;

    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI_IMAGE = 3;

    private static final int PLATFORM_FACEBOOK = 1;
    private static final int PLATFORM_YOUTUBE = 2;
    private static final int PLATFORM_PINTEREST = 3;

    private int mAsyncCount = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRequestFacebookTokenListener) context;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR_CALLBACK", "Facebook Fragment >>>>> check callback logic");
        }
    }

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        initData();
        initView(view);
        checkToken();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // destroy broadcast receiver along with fragment life cycle
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTokenUpdateReceiver);
    }

    private void initData() {

        // init preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        // init pinterest client
        PDKClient.configureInstance(getContext(), getString(R.string.pinterest_app_id));
        mPinterestClient = PDKClient.getInstance();

        // init update token status receiver
        mTokenUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkToken();
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTokenUpdateReceiver, new IntentFilter(getString(R.string.update_token_status)));

        // init check async requests status receiver
        mAsyncFinishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshDataset();
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mAsyncFinishReceiver, new IntentFilter(getString(R.string.async_finish_status)));
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        // view for disconnection
        mLayoutDisconnection = (RelativeLayout) view.findViewById(R.id.layout_disconnection);

        // set recyclerView
        mFacebookList = (RecyclerView) view.findViewById(R.id.recyclerView_facebook);
        mAdapter = new FeedListAdapter(getContext(), mDataset);
        mFacebookList.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFacebookList.setLayoutManager(layoutManager);
    }

    private void checkToken() {

        mAsyncCount = 0;
        String facebookToken = mPref.getString(getString(R.string.facebook_token), "");
        String googleToken = mPref.getString(getString(R.string.google_token), "");
        String pinterestToken = mPref.getString(getString(R.string.pinterest_token), "");

        mDataset.clear();
        mLayoutDisconnection.setVisibility(View.VISIBLE);
        if (!facebookToken.equals("")) {
            mLayoutDisconnection.setVisibility(View.GONE);
            getFacebookUserPages();
        }

        if (!googleToken.equals("")) {
            mLayoutDisconnection.setVisibility(View.GONE);
            getYoutubeSubscriptionList();
        }

        if (!pinterestToken.equals("")) {
            mLayoutDisconnection.setVisibility(View.GONE);
            getPinterestFollowingBoards();
        }
    }

    private void refreshDataset() {

        Collections.sort(mDataset, new Comparator<FavoFeedData>() {
            @Override
            public int compare(FavoFeedData o1, FavoFeedData o2) {
                return o2.getPubDate().compareTo(o1.getPubDate());
            }
        });

        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    // send status of asyncTasks
    private void sendAsyncStatus() {
        Intent intent = new Intent(getString(R.string.async_finish_status));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    // TODO - 여기에서부터 리팩토링 필수!!!!
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
                                    Log.d("CHECK_PAGE_ID", ">>>>>>> "+ pageInfo.getString("id"));
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

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date date = format.parse(article.getCreatedTime());
                                    if (!(article.getSource() == null)) {
                                        data.setContentsType(CONTENTS_VIDEO);
                                    } else {
                                        data.setContentsType(CONTENTS_IMAGE);
                                    }

                                    data.setPubDate(date);
                                    data.setFacebookData(result.getData().get(i));
                                    data.setPlatformType(PLATFORM_FACEBOOK);

                                    mDataset.add(data);
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
        parameters.putString("fields", "created_time,message,full_picture,from{name, picture{url}},attachments{subattachments},source");
        parameters.putString("limit", "5");
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
                        data.setPinterestData(pin);
                        data.setPubDate(pin.getCreatedAt());

                        mDataset.add(data);
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
        Call<YoutubeSubscriptionData> call = service.subscriptionList(accessToken, "snippet", MAX_COUNTS, true);
        call.enqueue(new Callback<YoutubeSubscriptionData>() {
            @Override
            public void onResponse(Call<YoutubeSubscriptionData> call, Response<YoutubeSubscriptionData> response) {
                if (response.isSuccessful()) {

                    mAsyncCount = mAsyncCount + response.body().getItems().size();
                    for (YoutubeSubscriptionData.Item item : response.body().getItems()) {
                        new GetYoutubeChannelVideos().executeOnExecutor(THREAD_POOL_EXECUTOR, item.getSnippet().getResourceId().getChannelId());
                    }
                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> Token is expired" + response.toString());

                    // TODO - Google Token Refresh 로직이 구현되기 전까지의 임시방편...
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
        protected Void doInBackground(String... params) {

            String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(YOUTUBE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            YoutubeService service = retrofit.create(YoutubeService.class);
            Call<YoutubeSearchVideoData> call = service.searchVideoList(accessToken, "snippet", MAX_COUNTS, params[0]);
            call.enqueue(new Callback<YoutubeSearchVideoData>() {
                @Override
                public void onResponse(Call<YoutubeSearchVideoData> call, Response<YoutubeSearchVideoData> response) {
                    if (response.isSuccessful()) {

                        for (YoutubeSearchVideoData.Item item : response.body().getItems()) {

                            FavoFeedData data = new FavoFeedData();

                            data.setPlatformType(PLATFORM_YOUTUBE);
                            data.setContentsType(CONTENTS_VIDEO);
                            data.setYoutubeData(item);

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            try {
                                Date date = format.parse(item.getSnippet().getPublishedAt());
                                data.setPubDate(date);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            mDataset.add(data);
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

    @Override
    public void onRefresh() {
        checkToken();
    }
}