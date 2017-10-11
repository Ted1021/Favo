package taewon.navercorp.integratedsns.youtube;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

public class YoutubeDetailActivity extends AppCompatActivity {

    private RecyclerView mVideoList;
    private YoutubeDetailAdapter mAdapter;

    private ArrayList mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_detail);

        initView();
    }

    private void initView(){

        mVideoList = (RecyclerView) findViewById(R.id.recyclerView_videoList);
        mAdapter = new YoutubeDetailAdapter(YoutubeDetailActivity.this, mDataset);
        mVideoList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(YoutubeDetailActivity.this);
        mVideoList.setLayoutManager(layoutManager);
    }
}
