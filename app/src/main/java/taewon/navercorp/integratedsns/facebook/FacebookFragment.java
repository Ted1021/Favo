package taewon.navercorp.integratedsns.facebook;


import android.content.Context;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.FacebookFeedData;

import static android.content.Context.MODE_PRIVATE;

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
    private ArrayList<FacebookFeedData> mDataset = new ArrayList<>();
    private FacebookListAdapter mAdapter;

    private OnRequestFacebookTokenListener mCallback;
    private FacebookHandler mHandler;

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mLayoutDisconnection;
    private Button mConnectFacebook;

    private static final int REQ_REFRESH = 100;

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

        String facebookToken = mPref.getString(getString(R.string.facebook_token), "");
        if (!facebookToken.equals("")) {
            mLayoutDisconnection.setVisibility(View.GONE);
            getFeedList();

        } else {
            mLayoutDisconnection.setVisibility(View.VISIBLE);
        }
    }

    private void getFeedList() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        mDataset.clear();
                        if (response.getError() == null) {

                            JSONArray result; // article list
                            JSONObject article; // single article

                            try {
                                result = response.getJSONObject().getJSONObject("posts").getJSONArray("data");

                                for (int i = 0; i < result.length(); i++) {

                                    article = result.getJSONObject(i);
                                    FacebookFeedData data = new FacebookFeedData();

                                    // TODO - gson converter 를 써야 ......
                                    if (!article.has("name")) {continue;}
                                    data.setName(article.getString("name"));
                                    if (article.has("description")) {data.setDescription(article.getString("description"));}
                                    if (article.has("created_time")) {data.setUpload_time(article.getString("created_time"));}
                                    if (article.has("full_picture")) {data.setPicture(article.getString("full_picture"));}
                                    if (article.has("source")) {data.setVideo(article.getString("source"));}

                                    mDataset.add(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("ERROR_FACEBOOK", "Facebook Fragment >>>>> fail to get JSONObject from facebook api");
                            }

                        } else {
                            checkToken();
                            Log.e("ERROR_FACEBOOK", "Facebook Fragment >>>> fail to connect facebook server" + response.getError().getErrorMessage());
                        }
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "posts{name,created_time,description,source,full_picture}");
        request.setParameters(parameters);
        request.executeAsync();
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
        getFeedList();
    }

    private class FacebookHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == REQ_REFRESH) {
                getFeedList();
                checkToken();
            }
        }
    }
}