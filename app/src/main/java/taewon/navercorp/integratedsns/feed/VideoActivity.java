package taewon.navercorp.integratedsns.feed;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import taewon.navercorp.integratedsns.R;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String mVideoId;

    private YouTubePlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initData();
        initView();
    }

    private void initData() {

        mVideoId = getIntent().getStringExtra("VIDEO_ID");
    }

    private void initView() {

        mPlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        mPlayerView.initialize(getString(R.string.google_api_key), this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.loadVideo(mVideoId);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
