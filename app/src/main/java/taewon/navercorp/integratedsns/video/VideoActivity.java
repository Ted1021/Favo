package taewon.navercorp.integratedsns.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String mVideoId;

    private YouTubePlayerView mPlayerView;

    private RecyclerView mNextVideoList;
    private ArrayList mDataset = new ArrayList<>();
    private NextVideoListAdapter mAdapter;

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

        mPlayerView = (YouTubePlayerView) findViewById(R.id.videoView_youtube);
        mPlayerView.initialize(getString(R.string.google_api_key), this);

        mNextVideoList = (RecyclerView) findViewById(R.id.recyclerView_nextVideo);
        mAdapter = new NextVideoListAdapter(VideoActivity.this, mDataset);
        mNextVideoList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(VideoActivity.this);
        mNextVideoList.setLayoutManager(layoutManager);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.loadVideo(mVideoId);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
