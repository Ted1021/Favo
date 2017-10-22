package taewon.navercorp.integratedsns.subscription.facebook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.feed.FacebookFeedData;

public class PageFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mPageFeedList;

    private static final String ARG_PARAM1 = "PAGE_ID";
    private static final String ARG_PARAM2 = "CONTENT_TYPE";
    private String mPageId;

    private ArrayList<FacebookFeedData.ArticleData> mDataset = new ArrayList<>();
    private PageFeedAdapter mAdapter;

    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI = 3;

    public PageFeedFragment() {
    }

    public static PageFeedFragment newInstance(String param1) {

        PageFeedFragment fragment = new PageFeedFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPageId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page_feed, container, false);

        initView(view);
        getFacebookPageFeed();

        return view;
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);

        mPageFeedList = (RecyclerView) view.findViewById(R.id.recyclerView_feed);
        mAdapter = new PageFeedAdapter(getContext(), mDataset);
        mPageFeedList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mPageFeedList.setLayoutManager(layoutManager);
    }

    private void getFacebookPageFeed() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String path = String.format("/%s/feed", mPageId);
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                path,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if(response.getError() == null){

                            FacebookFeedData result = new Gson().fromJson(response.getJSONObject().toString(), FacebookFeedData.class);
                            for(FacebookFeedData.ArticleData data : result.getData()){

                                if(data.getSource() != null){
                                    data.setContentsType(CONTENTS_VIDEO);
                                } else {
                                    data.setContentsType(CONTENTS_IMAGE);
                                }
                                mDataset.add(data);
                            }

                            mAdapter.notifyDataSetChanged();

                        } else {
                            Log.e(getClass().getName(), "Fail to get facebook page feed >>>>> " + response.getRawResponse());
                        }

                        mRefreshLayout.setRefreshing(false);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "link,created_time,from{name, picture.height(2048){url}},message,description,full_picture,id,likes.limit(0).summary(true),comments.limit(0).summary(true),source");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onRefresh() {

        mDataset.clear();
        getFacebookPageFeed();
    }
}
