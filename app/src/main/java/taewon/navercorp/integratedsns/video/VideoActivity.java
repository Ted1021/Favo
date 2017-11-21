package taewon.navercorp.integratedsns.video;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import taewon.navercorp.integratedsns.R;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView mYoutubePlayer;
    private WebView mTwitchPlayer;

    private String mPlatformType, mVideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mPlatformType = intent.getStringExtra("PLATFORM_TYPE");
        mVideoUrl = intent.getStringExtra("VIDEO_ID");
    }

    private void initView(){

        if(mPlatformType.equals(PLATFORM_YOUTUBE)){
            mYoutubePlayer = (YouTubePlayerView) findViewById(R.id.youtube_player);
            mYoutubePlayer.initialize(getString(R.string.google_api_key), this);
        } else {
            mTwitchPlayer = (WebView) findViewById(R.id.twitch_player);
            mTwitchPlayer.setVisibility(View.VISIBLE);
            mTwitchPlayer.getSettings().setJavaScriptEnabled(true);
            mTwitchPlayer.loadUrl(mVideoUrl);
            Log.d("CHECK_URL", mVideoUrl);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.loadVideo(mVideoUrl);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
