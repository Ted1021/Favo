package taewon.navercorp.integratedsns.live;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.TwitchService;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;
import taewon.navercorp.integratedsns.model.twitch.TwitchStreamingDataV5;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSubscriptionData;
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_VIDEO;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_ACCEPT_CODE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_BASE_URL;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;


public class LiveStreamingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FavoTokenManager mFavoTokenManager;

    private RecyclerView mLiveStreamingList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutmanager;

    private SwipeRefreshLayout mRefreshLayout;

    private ArrayList<FavoFeedData> mDataset = new ArrayList<>();

    private SimpleDateFormat mStringFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static boolean isInit;
    private static final int MAX_COUNTS = 10;

    public static LiveStreamingFragment newInstance() {

        LiveStreamingFragment fragment = new LiveStreamingFragment();
        isInit = true;
        return fragment;
    }

    public LiveStreamingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_streaming, container, false);

        initView(view);
        if (isInit) {
            loadData();
            isInit = false;
        }
        return view;
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mLiveStreamingList = (RecyclerView) view.findViewById(R.id.recyclerView_liveStreamingList);
        mAdapter = new LiveStreamingListAdapter(getContext(), mDataset);
        mLiveStreamingList.setAdapter(mAdapter);

        mLayoutmanager = new LinearLayoutManager(getContext());
        mLiveStreamingList.setLayoutManager(mLayoutmanager);
    }

    private void loadData() {

        mRefreshLayout.setRefreshing(true);
        mFavoTokenManager = FavoTokenManager.getInstance();

        String facebookToken = mFavoTokenManager.getCurrentToken(PLATFORM_FACEBOOK);
        String googleToken = mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE);
        String twitchToken = mFavoTokenManager.getCurrentToken(PLATFORM_TWITCH);

        mDataset.clear();
        mAdapter.notifyDataSetChanged();

        if (!facebookToken.equals("")) {
//            getFacebookUserPages();
        }

        if (!googleToken.equals("")) {
            getYoutubeSubscriptionList();
        }

        if (!twitchToken.equals("")) {
            getTwitchStreams();
        }
    }

    // Youtube API Call
    private void getYoutubeSubscriptionList() {

//        mRefreshLayout.setRefreshing(true);

        // get google credential access token
        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

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

                    for (YoutubeSubscriptionData.Item item : response.body().getItems()) {

                        String[] params = {item.getSnippet().getResourceId().getChannelId(), item.getSnippet().getThumbnails().getHigh().getUrl()};
                        new GetYoutubeChannelStreams().executeOnExecutor(THREAD_POOL_EXECUTOR, params);
                    }

                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> Token is expired" + response.toString());
                }
            }

            @Override
            public void onFailure(Call<YoutubeSubscriptionData> call, Throwable t) {
                Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> fail to access youtube api server");
                Toast.makeText(getContext(), "Fail to access youtube server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class GetYoutubeChannelStreams extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... params) {

            String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));
            final String profileUrl = params[1];

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(YOUTUBE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            YoutubeService service = retrofit.create(YoutubeService.class);
            Call<YoutubeSearchVideoData> call = service.getVideoList(accessToken, "snippet", 5, params[0], null, "live", null, "viewCount", "video", null, null, null);
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

                            mDataset.add(data);
                            mAdapter.notifyItemInserted(mDataset.size() + 1);
                        }
                    } else {
                        Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to get json for video");
                    }
                    mRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<YoutubeSearchVideoData> call, Throwable t) {
                    t.printStackTrace();
                    Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to access youtube api server");
                    mRefreshLayout.setRefreshing(false);
                }
            });
            return null;
        }
    }

    private void getTwitchStreams() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwitchService service = retrofit.create(TwitchService.class);
        Call<TwitchStreamingDataV5> call = service.getTwitchFollowingStreams(TWITCH_ACCEPT_CODE,
                getString(R.string.twitch_client_id),
                "OAuth " + FavoTokenManager.getInstance().getCurrentToken(PLATFORM_TWITCH), 10);
        call.enqueue(new Callback<TwitchStreamingDataV5>() {
            @Override
            public void onResponse(Call<TwitchStreamingDataV5> call, Response<TwitchStreamingDataV5> response) {
                if (response.isSuccessful()) {

                    TwitchStreamingDataV5 result = response.body();

                    for (TwitchStreamingDataV5.Stream item : result.getStreams()) {

                        FavoFeedData data = new FavoFeedData();

                        data.setPlatformType(PLATFORM_TWITCH);
                        data.setUserName(item.getChannel().getName());
                        data.setDescription(item.getChannel().getStatus());
                        data.setPicture(item.getPreview().getLarge());
                        data.setProfileImage(item.getChannel().getLogo());
                        data.setFeedId(item.getChannel().getName());
                        data.setPageId(item.getChannel().getId() + "");
                        Log.d("CHECK_SEARCH", item.getChannel().getId() + "");
                        data.setVideoUrl(item.getChannel().getName());

                        mDataset.add(data);
                        mAdapter.notifyItemInserted(mDataset.size() + 1);
                    }

                } else {
                    Log.e("ERROR_SEARCH", response.raw().toString());
                }
                mRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<TwitchStreamingDataV5> call, Throwable t) {
                t.printStackTrace();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {

        mDataset.clear();
        mAdapter.notifyDataSetChanged();
        loadData();
    }
}
