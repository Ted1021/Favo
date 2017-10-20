package taewon.navercorp.integratedsns.youtube;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.feed.YoutubeSearchVideoData;

/**
 * @author 김태원
 * @file YoutubeDetailActivity.java
 * @brief get videos from Youtube api using 'channelId'
 * @date 2017.10.11
 */

public class YoutubeDetailActivity extends AppCompatActivity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private RecyclerView mVideoList;
    private YoutubeDetailAdapter mAdapter;

    private ArrayList<YoutubeSearchVideoData.Item> mDataset = new ArrayList<>();

    private String mChannelId;

    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    private static final int MAX_COUNTS = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_detail);

        initData();
        initView();
        getVideoList();
    }

    private void initData() {

        // managing tokens
        mPref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        // get channelId from YoutubeListAdapter
        mChannelId = getIntent().getStringExtra("CHANNEL_ID");
        Log.d("CHECK_YOUTUBE", "YoutubeDetailActivity >>>>> " + mChannelId);
    }

    private void initView() {

        mVideoList = (RecyclerView) findViewById(R.id.recyclerView_videoList);
        mAdapter = new YoutubeDetailAdapter(YoutubeDetailActivity.this, mDataset);
        mVideoList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(YoutubeDetailActivity.this);
        mVideoList.setLayoutManager(layoutManager);
    }

    private void getVideoList() {

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchVideoData> call = service.getVideoList(accessToken, "snippet", MAX_COUNTS, mChannelId);
        call.enqueue(new Callback<YoutubeSearchVideoData>() {
            @Override
            public void onResponse(Call<YoutubeSearchVideoData> call, Response<YoutubeSearchVideoData> response) {
                if (response.isSuccessful()) {

                    mDataset.addAll(response.body().getItems());
                    mAdapter.notifyDataSetChanged();

                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to get json for video");
                }
            }

            @Override
            public void onFailure(Call<YoutubeSearchVideoData> call, Throwable t) {
                t.printStackTrace();
                Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to access youtube api server");
            }
        });
    }
}
