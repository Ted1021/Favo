package taewon.navercorp.integratedsns.page;


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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.facebook.FacebookPageVideoData;
import taewon.navercorp.integratedsns.model.favo.FavoPageVideoData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeChannelPlaylistData;
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;


public class PageVideoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FavoTokenManager mFavoTokenManager;

    // recyclerView components
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mPageVideoList;
    private ArrayList<FavoPageVideoData> mDataset = new ArrayList<>();
    private PageVideoAdapter mAdapter;

    // platform & video data
    private String mPlatformType;
    private String mPageId;

    private static final String ARG_PARAM1 = "PAGE_ID";
    private static final String ARG_PARAM2 = "PLATFORM_TYPE";
    private static final int MAX_COUNT = 50;

    public PageVideoFragment() {
    }


    public static PageVideoFragment newInstance(String param1, String param2) {

        PageVideoFragment fragment = new PageVideoFragment();

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
            mPageId = getArguments().getString(ARG_PARAM1);
            mPlatformType = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page_video, container, false);

        initView(view);
        bindData();

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

    private void bindData() {

        // get access tokens
        mFavoTokenManager = FavoTokenManager.getInstance();

        // call video lists along with platform
        switch (mPlatformType) {
            case PLATFORM_FACEBOOK:
                getFacebookVideoList();
                break;

            case PLATFORM_YOUTUBE:
                getYoutubePlaylist();
                break;
        }
    }

    private void getFacebookVideoList() {

        String path = String.format("/%s/videos", mPageId);
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                path,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() == null) {

                            FacebookPageVideoData result = new Gson().fromJson(response.getJSONObject().toString(), FacebookPageVideoData.class);
                            for (FacebookPageVideoData.Video video : result.getData()) {

                                FavoPageVideoData data = new FavoPageVideoData();

                                data.setPlatformType(PLATFORM_FACEBOOK);
                                data.setPicture(video.getPicture());
                                data.setTitle(video.getDescription());
                                data.setPubDate(video.getCreated_time());
                                data.setRunTime(video.getLength());
                                data.setVideoUrl(video.getSource());

                                mDataset.add(data);
                            }
                            mAdapter.notifyDataSetChanged();
                        }

                        mRefreshLayout.setRefreshing(false);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "description,picture.height(1024),source,length,created_time");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getYoutubePlaylist() {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeChannelPlaylistData> call = service.getChannelPlaylist(accessToken, "snippet,contentDetails", MAX_COUNT, mPageId);
        call.enqueue(new Callback<YoutubeChannelPlaylistData>() {
            @Override
            public void onResponse(Call<YoutubeChannelPlaylistData> call, Response<YoutubeChannelPlaylistData> response) {

                if (response.isSuccessful()) {

                    for (YoutubeChannelPlaylistData.Item video : response.body().getItems()) {

                        FavoPageVideoData data = new FavoPageVideoData();

                        data.setPlatformType(PLATFORM_YOUTUBE);
                        if (video.getSnippet().getThumbnails() != null) {
                            data.setPicture(video.getSnippet().getThumbnails().getHigh().getUrl());
                        }
                        data.setTitle(video.getSnippet().getTitle());
                        data.setPubDate(video.getSnippet().getPublishedAt());
                        data.setVideoCount(video.getContentDetails().getItemCount());

                        mDataset.add(data);
                    }
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
        mAdapter.notifyDataSetChanged();
        bindData();
    }
}
