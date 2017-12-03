package taewon.navercorp.integratedsns.video;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

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

import static android.view.View.VISIBLE;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

public class RecommendVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private FavoTokenManager mFavoTokenManager;
    private FavoFeedData mOriginVideo;

    private RecyclerView mMoreVideoList;
    private ArrayList<FavoFeedData> mDataset = new ArrayList<>();
    private RecommendVideoListAdapter mAdapter;

    private YouTubePlayerView mYoutubePlayerView;
    private WebView mTwitchPlayerView;
    private VideoView mFacebookPlayerView;

    private static final int MAX_COUNT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_video);

        initView();
        initData();
        loadVideo();
    }

    private void initView() {

        mTwitchPlayerView = (WebView) findViewById(R.id.twitch_player);
        mFacebookPlayerView = (VideoView) findViewById(R.id.facebook_player);
        mYoutubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        mMoreVideoList = (RecyclerView) findViewById(R.id.recyclerView_moreVideoList);
        mAdapter = new RecommendVideoListAdapter(RecommendVideoActivity.this, mDataset);
        mMoreVideoList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecommendVideoActivity.this);
        mMoreVideoList.setLayoutManager(layoutManager);
    }

    private void initData() {

        mFavoTokenManager = FavoTokenManager.getInstance();

        mOriginVideo = (FavoFeedData) getIntent().getSerializableExtra("FEED_DATA");
        mDataset.add(mOriginVideo);
        mAdapter.notifyDataSetChanged();

        searchRelatedVideoList(mOriginVideo);
    }

    private void loadVideo(){

        switch(mOriginVideo.getPlatformType()){

            case PLATFORM_FACEBOOK:
                final MediaController mediaController = new MediaController(this);
                mFacebookPlayerView.setVisibility(VISIBLE);
                mFacebookPlayerView.setMediaController(mediaController);
                Log.e("CHECK_VIDEO_URL", mOriginVideo.getVideoUrl());
                mFacebookPlayerView.setVideoURI(Uri.parse(mOriginVideo.getVideoUrl()));
                mFacebookPlayerView.start();
                break;

            case PLATFORM_YOUTUBE:
                mYoutubePlayerView.initialize(getString(R.string.google_api_key), this);
                break;

            case PLATFORM_TWITCH:
                mTwitchPlayerView.setVisibility(VISIBLE);
                mTwitchPlayerView.getSettings().setJavaScriptEnabled(true);
                mTwitchPlayerView.loadUrl(String.format("http://player.twitch.tv?video=%s", mOriginVideo.getVideoUrl()));
                Log.d("CHECK_URL", mOriginVideo.getVideoUrl());
                break;
        }
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
        youTubePlayer.loadVideo(mOriginVideo.getVideoUrl());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
