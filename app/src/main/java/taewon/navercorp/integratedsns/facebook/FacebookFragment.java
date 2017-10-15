package taewon.navercorp.integratedsns.facebook;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.FacebookFeedData;
import taewon.navercorp.integratedsns.model.FavoFeedData;

import static android.content.Context.MODE_PRIVATE;
import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

/**
 * @author 김태원
 * @file FacebookFragment.java
 * @brief show facebook contents, search & add facebook pages
 * @date 2017.09.28
 */

public class FacebookFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private RecyclerView mFacebookList;
    private ArrayList<FavoFeedData> mDataset = new ArrayList<>();
    private FacebookListAdapter mAdapter;

    private OnRequestFacebookTokenListener mCallback;
    private FacebookHandler mHandler;

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mLayoutDisconnection;
    private Button mConnectFacebook;

    private PDKClient mPinterestClient;

    private static final String BOARD_FIELDS = "id,name";
    private static final String PIN_FIELDS = "created_at,creator,id,image, media,note,original_link";

    private static final int REQ_REFRESH = 100;

    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI = 3;

    private static final int PLATFORM_FACEBOOK = 1;
    private static final int PLATFORM_YOUTUBE = 2;
    private static final int PLATFORM_PINTEREST = 3;

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

    public FacebookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_facebook, container, false);

        initData();
        initView(view);
        checkToken();

        return view;
    }

    private void initData() {

        // get preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();


        // Pinterest client init
        PDKClient.configureInstance(getContext(), getString(R.string.pinterest_app_id));
        mPinterestClient = PDKClient.getInstance();

        mHandler = new FacebookHandler();
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        // view for disconnection
        mLayoutDisconnection = (RelativeLayout) view.findViewById(R.id.layout_disconnection);
        mConnectFacebook = (Button) view.findViewById(R.id.button_connect_facebook);
        mConnectFacebook.setOnClickListener(this);

        // set recyclerView
        mFacebookList = (RecyclerView) view.findViewById(R.id.recyclerView_facebook);
        mAdapter = new FacebookListAdapter(getContext(), mDataset);
        mFacebookList.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFacebookList.setLayoutManager(layoutManager);
    }

    private void checkToken() {

        Log.e("CHCEK_TOKEN", "facebook token >>>>>> "+mPref.getString(getString(R.string.facebook_token), ""));
        String facebookToken = mPref.getString(getString(R.string.facebook_token), "");
        if (!facebookToken.equals("")) {
            mLayoutDisconnection.setVisibility(View.GONE);
            getUserPages();
            getFollowingBoards();

        } else {
            mLayoutDisconnection.setVisibility(View.VISIBLE);
        }
    }

    // TODO - 여기에서부터 리팩토링 필수!!!!
    // Facebook API Call
    private void getUserPages() {

        mDataset.clear();
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

                                for (int i = 0; i < results.length(); i++) {
                                    pageInfo = results.getJSONObject(i);
                                    getPageFeed(pageInfo.getString("id"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("limit", "10");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getPageFeed(String pageId) {

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
                                JSONArray results = response.getJSONObject().getJSONArray("data");
                                JSONObject article;

                                for (int i = 0; i < results.length(); i++) {

                                    article = results.getJSONObject(i);
                                    FavoFeedData data = new FavoFeedData();
                                    FacebookFeedData feed = new FacebookFeedData();

                                    if (article.getJSONObject("from").has("name")) {
                                        feed.setName(article.getJSONObject("from").getString("name"));
                                    }
                                    if (article.getJSONObject("from").getJSONObject("picture").getJSONObject("data").has("url")) {
                                        feed.setProfileImage(article.getJSONObject("from").getJSONObject("picture").getJSONObject("data").getString("url"));
                                    }
                                    if (article.has("created_time")) {
                                        feed.setUploadTime(article.getString("created_time"));
                                    }
                                    if (article.has("message")) {
                                        feed.setDescription(article.getString("message"));
                                    }
                                    if (article.has("full_picture")) {
                                        feed.setPicture(article.getString("full_picture"));
                                    }
                                    if (article.has("source")) {
                                        data.setContentsType(CONTENTS_VIDEO);
                                        feed.setVideo(article.getString("source"));
                                    } else {
                                        data.setContentsType(CONTENTS_IMAGE);
                                    }

                                    data.setFacebookData(feed);
                                    data.setFlatformType(PLATFORM_FACEBOOK);

                                    mDataset.add(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            Collections.sort(mDataset, new Comparator<FavoFeedData>() {
//                                @Override
//                                public int compare(FavoFeedData o1, FavoFeedData o2) {
//                                    return o2.getFacebookData().getUploadTime().compareToIgnoreCase(o1.getFacebookData().getUploadTime());
//                                }
//                            });
                            mAdapter.notifyDataSetChanged();
                            mRefreshLayout.setRefreshing(false);
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
    private void getFollowingBoards() {

        mPinterestClient.getMyFollowedBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);

                mDataset.clear();
                for (PDKBoard board : response.getBoardList()) {
                    Log.d("CHECK_BOARD", " >>>>> "+board.getName());
                    new GetFollowingPins().executeOnExecutor(THREAD_POOL_EXECUTOR, board.getUid());
                }
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
            }
        });
    }

    private class GetFollowingPins extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            mPinterestClient.getBoardPins(params[0], PIN_FIELDS, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    super.onSuccess(response);

                    for(PDKPin pin : response.getPinList()){

                        FavoFeedData data = new FavoFeedData();

                        data.setFlatformType(PLATFORM_PINTEREST);
                        data.setContentsType(CONTENTS_IMAGE);
                        data.setPinterestData(pin);

                        mDataset.add(data);
                    }

//                    Collections.sort(mDataset, new Comparator<FavoFeedData>() {
//                        @Override
//                        public int compare(FavoFeedData o1, FavoFeedData o2) {
//                            return (int)(o2.getPinterestData().getCreatedAt().getTime() - o1.getPinterestData().getCreatedAt().getTime());
//                        }
//                    });
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(PDKException exception) {
                    super.onFailure(exception);
                    exception.printStackTrace();
                }
            });

            return null;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_connect_facebook:
                mCallback.onRequestFacebookToken(mHandler);
                break;
        }
    }

    @Override
    public void onRefresh() {
        checkToken();
    }

    private class FacebookHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == REQ_REFRESH) {
                getUserPages();
                checkToken();
            }
        }
    }

    // for facebook
//    private void getUserFeedList() {
//
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        GraphRequest request = GraphRequest.newMeRequest(
//                accessToken,
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//
//                        mDataset.clear();
//                        if (response.getError() == null) {
//
//                            JSONArray result; // article list
//                            JSONObject article; // single article
//
//                            try {
//                                result = response.getJSONObject().getJSONObject("posts").getJSONArray("data");
//
//                                for (int i = 0; i < result.length(); i++) {
//
//                                    article = result.getJSONObject(i);
//                                    FacebookFeedData data = new FacebookFeedData();
//
//                                    // TODO - gson converter 를 써야 ......
//                                    if (!article.has("name")) {
//                                        continue;
//                                    }
//                                    data.setName(article.getString("name"));
//                                    if (article.has("description")) {
//                                        data.setDescription(article.getString("description"));
//                                    }
//                                    if (article.has("created_time")) {
//                                        data.setUploadTime(article.getString("created_time"));
//                                    }
//                                    if (article.has("full_picture")) {
//                                        data.setPicture(article.getString("full_picture"));
//                                    }
//                                    if (article.has("source")) {
//                                        data.setVideo(article.getString("source"));
//                                    }
//
////                                    mDataset.add(data);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                Log.e("ERROR_FACEBOOK", "Facebook Fragment >>>>> fail to get JSONObject from facebook api");
//                            }
//
//                        } else {
//                            checkToken();
//                            Log.e("ERROR_FACEBOOK", "Facebook Fragment >>>> fail to connect facebook server" + response.getError().getErrorMessage());
//                        }
//
//                        // mAdapter.notifyDataSetChanged();
//                        mRefreshLayout.setRefreshing(false);
//                    }
//                });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "posts{name,created_time,description,source,full_picture}");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }
}