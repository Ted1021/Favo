package taewon.navercorp.integratedsns.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeChannelInfoData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

public class VideoActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener {

    private FavoTokenManager mFavoTokenManager;

    private RecyclerView mMoreVideoList;
    private ArrayList<FavoFeedData> mDataset = new ArrayList<>();
    private MoreVideoListAdapter mAdapter;

    private static final int MAX_COUNT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initView();
        initData();
    }

    private void initView() {

        mMoreVideoList = (RecyclerView) findViewById(R.id.recyclerView_moreVideoList);
        mAdapter = new MoreVideoListAdapter(VideoActivity.this, mDataset);
        mMoreVideoList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(VideoActivity.this);
        mMoreVideoList.setLayoutManager(layoutmanager);

//        mPlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
//        mPlayerView.initialize(getString(R.string.google_api_key), this);
    }

    private void initData() {

        mFavoTokenManager = FavoTokenManager.getInstance();

        FavoFeedData originVideo = (FavoFeedData) getIntent().getSerializableExtra("FEED_DATA");
        mDataset.add(originVideo);
        mAdapter.notifyDataSetChanged();

        searchRelatedVideoList(originVideo);
    }

    private void searchRelatedVideoList(FavoFeedData target) {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchVideoData> call;
        if (target.getPlatformType() == PLATFORM_YOUTUBE) {
            call = service.getVideoList(accessToken, "snippet", MAX_COUNT, null, null, null, null, "viewCount", "video", null, "KR", target.getVideoUrl());
        } else {
            call = service.getVideoList(accessToken, "snippet", MAX_COUNT, null, null, null, target.getUserName(), "viewCount", "video", null, "KR", null);
        }
        call.enqueue(new Callback<YoutubeSearchVideoData>() {
            @Override
            public void onResponse(Call<YoutubeSearchVideoData> call, Response<YoutubeSearchVideoData> response) {
                if (response.isSuccessful()) {
                    YoutubeSearchVideoData result = response.body();
                    for (YoutubeSearchVideoData.Item item : result.getItems()) {
                        getYoutubeChannelInfo(item, item.getSnippet().getChannelId());
                    }
                } else {
                    Log.e("ERROR_SEARCH", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<YoutubeSearchVideoData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getYoutubeChannelInfo(final YoutubeSearchVideoData.Item item, String channelId) {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeChannelInfoData> call = service.getChannelInfo(accessToken, "snippet", channelId);
        call.enqueue(new Callback<YoutubeChannelInfoData>() {
            @Override
            public void onResponse(Call<YoutubeChannelInfoData> call, Response<YoutubeChannelInfoData> response) {

                if (response.isSuccessful()) {

                    YoutubeChannelInfoData channelInfo = response.body();
                    FavoFeedData data = new FavoFeedData();

                    data.setPlatformType(PLATFORM_YOUTUBE);
                    data.setProfileImage(channelInfo.getItems().get(0).getSnippet().getThumbnails().getHigh().getUrl());
                    data.setUserName(channelInfo.getItems().get(0).getSnippet().getTitle());
                    data.setCreatedTime(item.getSnippet().getPublishedAt());
                    data.setPageId(item.getSnippet().getChannelId());
                    data.setPicture(item.getSnippet().getThumbnails().getHigh().getUrl());
                    data.setDescription(item.getSnippet().getTitle());
                    data.setVideoUrl(item.getId().getVideoId());

                    mDataset.add(data);
                    mAdapter.notifyDataSetChanged();

                } else {
                    Log.e(getClass().getName(), "Error load youtube page : " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<YoutubeChannelInfoData> call, Throwable t) {
                t.printStackTrace();
                Log.e(getClass().getName(), "Error load youtube page ");
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//        youTubePlayer.loadVideo(mVideoData.getVideoUrl());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }
}
