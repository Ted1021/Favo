package taewon.navercorp.integratedsns.facebook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

/**
 * @author 김태원
 * @file FacebookFragment.java
 * @brief show facebook contents, search & add facebook pages
 * @date 2017.09.28
 */

public class FacebookFragment extends Fragment {

    private RecyclerView mFacebookList;
    private ArrayList<JSONObject> mDataset = new ArrayList<>();
    private FacebookListAdapter mAdapter;

    public FacebookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);

        initView(view);
        setRecyclerView();
        getFacebookFeed();

        return view;
    }

    private void initView(View view) {

        mFacebookList = (RecyclerView) view.findViewById(R.id.recyclerView_facebook);
    }

    private void setRecyclerView() {

        mAdapter = new FacebookListAdapter(getContext(), mDataset);
        mFacebookList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFacebookList.setLayoutManager(layoutManager);
    }

    private void getFacebookFeed() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/feed",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() == null) {
                            JSONArray data;
                            JSONObject article;
                            try {
                                data = response.getJSONObject().getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    article = data.getJSONObject(i);
                                    mDataset.add(article);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("ERROR_FACEBOOK", "fail to get JSONObject");
                            }
                        } else {
                            Log.e("ERROR_FACEBOOK", "fail to connect facebook >>> " + response.getError().getErrorMessage());
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "full_picture,description,link,source,name,created_time");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
