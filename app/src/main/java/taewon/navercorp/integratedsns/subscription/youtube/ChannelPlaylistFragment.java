package taewon.navercorp.integratedsns.subscription.youtube;


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
import taewon.navercorp.integratedsns.model.page.YoutubeChannelPlaylistData;

import static android.content.Context.MODE_PRIVATE;

public class ChannelPlaylistFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mChannelPlaylist;

    private String mChannelId;

    private ArrayList<YoutubeChannelPlaylistData.Item> mDataset = new ArrayList<>();
    private ChannelPlaylistAdapter mAdapter;

    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    private static final String ARG_PARAM1 = "CHANNEL_ID";
    private static final int MAX_COUNT = 50;

    public ChannelPlaylistFragment() {

    }

    public static ChannelPlaylistFragment newInstance(String param1) {
        ChannelPlaylistFragment fragment = new ChannelPlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChannelId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_playlist, container, false);

        initData();
        initView(view);
        getChannelPlaylist();
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

        mChannelPlaylist = (RecyclerView) view.findViewById(R.id.recyclerView_playlist);
        mAdapter = new ChannelPlaylistAdapter(getContext(), mDataset);
        mChannelPlaylist.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mChannelPlaylist.setLayoutManager(layoutManager);
    }

    private void getChannelPlaylist() {

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeChannelPlaylistData> call = service.getChannelPlaylist(accessToken, "snippet,contentDetails", MAX_COUNT, mChannelId);
        call.enqueue(new Callback<YoutubeChannelPlaylistData>() {
            @Override
            public void onResponse(Call<YoutubeChannelPlaylistData> call, Response<YoutubeChannelPlaylistData> response) {

                if (response.isSuccessful()) {

                    mDataset.addAll(response.body().getItems());
                    mAdapter.notifyDataSetChanged();

                } else {
                    Log.e(getClass().getName(), response.raw().toString());
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<YoutubeChannelPlaylistData> call, Throwable t) {

                t.printStackTrace();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        mDataset.clear();
        getChannelPlaylist();
    }
}
