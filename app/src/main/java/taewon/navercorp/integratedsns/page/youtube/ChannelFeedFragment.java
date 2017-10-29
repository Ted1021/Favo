package taewon.navercorp.integratedsns.page.youtube;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.feed.YoutubeSearchVideoData;

import static android.content.Context.MODE_PRIVATE;

public class ChannelFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mChannelFeedList;

    private ArrayList<YoutubeSearchVideoData.Item> mDataset = new ArrayList<>();
    private ChannelFeedAdapter mAdapter;

    private String mChannelId, mProfileUrl;

    private static final String ARG_PARAM1 = "CHANNEL_ID";
    private static final String ARG_PARAM2 = "PROFILE_URL";

    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    private static final int MAX_COUNTS = 50;

    public ChannelFeedFragment() {

    }

    public static ChannelFeedFragment newInstance(String param1, String param2) {
        ChannelFeedFragment fragment = new ChannelFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChannelId = getArguments().getString(ARG_PARAM1);
            mProfileUrl = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_feed, container, false);

        initData();
        initView(view);
        getChannelVideos();

        return view;
    }

    private void initData() {

        // init preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);

        mChannelFeedList = (RecyclerView) view.findViewById(R.id.recyclerView_feed);
        mAdapter = new ChannelFeedAdapter(getContext(), mDataset, mProfileUrl);
        mChannelFeedList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mChannelFeedList.setLayoutManager(layoutManager);
    }

    private void getChannelVideos() {

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchVideoData> call = service.getVideoList(accessToken, "snippet", MAX_COUNTS, mChannelId, "date", "video");
        call.enqueue(new Callback<YoutubeSearchVideoData>() {
            @Override
            public void onResponse(Call<YoutubeSearchVideoData> call, Response<YoutubeSearchVideoData> response) {

                if(response.isSuccessful()){

                    for(YoutubeSearchVideoData.Item data : response.body().getItems()){

                        data.getSnippet().setProfileImage(mProfileUrl);
                        mDataset.add(data);
                    }
                    mAdapter.notifyDataSetChanged();

                } else {
                    Log.e(getClass().getName(), "fail to get youtube video " + response.raw().toString());
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<YoutubeSearchVideoData> call, Throwable t) {

                t.printStackTrace();
                mRefreshLayout.setRefreshing(false);
            }

        });

    }

    @Override
    public void onRefresh() {
        mDataset.clear();
        getChannelVideos();
    }
}
