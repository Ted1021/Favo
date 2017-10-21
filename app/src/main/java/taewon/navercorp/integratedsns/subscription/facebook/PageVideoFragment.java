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
import taewon.navercorp.integratedsns.model.page.FacebookPageVideoData;


public class PageVideoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mPageVideoList;

    private static final String ARG_PARAM1 = "PAGE_ID";
    private String mPageId;

    private ArrayList<FacebookPageVideoData.Video> mDataset = new ArrayList<>();
    private PageVideoAdapter mAdapter;

    public PageVideoFragment() {
    }


    public static PageVideoFragment newInstance(String param1) {

        PageVideoFragment fragment = new PageVideoFragment();

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

        View view = inflater.inflate(R.layout.fragment_page_video, container, false);

        initView(view);
        getPageVideoList();

        return view;
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);

        mPageVideoList = (RecyclerView) view.findViewById(R.id.recyclerView_videoList);
        mAdapter = new PageVideoAdapter(getContext(), mDataset);
        mPageVideoList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mPageVideoList.setLayoutManager(layoutManager);
    }

    private void getPageVideoList() {

        String path = String.format("/%s/videos", mPageId);
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                path,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() == null) {

                            Log.e(getClass().getName(), "Error load facebook page : " + response.getRawResponse());
                            FacebookPageVideoData data = new Gson().fromJson(response.getJSONObject().toString(), FacebookPageVideoData.class);
                            mDataset.addAll(data.getData());
                            mAdapter.notifyDataSetChanged();

                        } else {
                            Log.e(getClass().getName(), "Error load facebook page : " + response.getRawResponse());
                        }
                        mRefreshLayout.setRefreshing(false);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "description,picture.height(1024),source,length,created_time");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onRefresh() {
        mDataset.clear();
        getPageVideoList();
    }
}
