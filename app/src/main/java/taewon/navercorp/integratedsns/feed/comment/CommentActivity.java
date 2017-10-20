package taewon.navercorp.integratedsns.feed.comment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.FacebookFeedDetailData;
import taewon.navercorp.integratedsns.model.YoutubeCommentData;
import taewon.navercorp.integratedsns.model.YoutubeSearchVideoData;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    // for managing tokens
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private RecyclerView mCommentList;
    private RecyclerView.Adapter mAdapter;

    private FacebookFeedDetailData mFeedDetail = new FacebookFeedDetailData();

    private YoutubeCommentData mYoutubeCommentData = new YoutubeCommentData();
    private YoutubeSearchVideoData.Item mYoutubeSearchVideoData;

    private int mContentType, mPlatformType;
    private String mArticleId, mVideoId, mPinId;
    private EditText mUserComment;
    private ImageButton mCamera, mSend;

    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI = 3;

    private static final int PLATFORM_FACEBOOK = 1;
    private static final int PLATFORM_YOUTUBE = 2;
    private static final int PLATFORM_PINTEREST = 3;

    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    private static final int MAX_COUNTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        initData();
        initView();
    }

    private void initData() {

        // init preference
        mPref = CommentActivity.this.getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        Intent intent = getIntent();

        mContentType = intent.getIntExtra("CONTENT_TYPE", 0);
        mPlatformType = intent.getIntExtra("PLATFORM_TYPE", 0);

        switch (mPlatformType) {
            case PLATFORM_FACEBOOK:
                mArticleId = intent.getStringExtra("ARTICLE_ID");
                getFacebookFeedDetail();
                break;

            case PLATFORM_YOUTUBE:

                mVideoId = intent.getStringExtra("VIDEO_ID");
                mYoutubeSearchVideoData = (YoutubeSearchVideoData.Item) intent.getSerializableExtra("VIDEO_CONTENT");
                getYoutubeVideoComments();
                break;

            case PLATFORM_PINTEREST:
                mPinId = intent.getStringExtra("PIN_ID");

                break;
        }

    }

    private void initView() {

        mCommentList = (RecyclerView) findViewById(R.id.recyclerView_commentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CommentActivity.this);
        mCommentList.setLayoutManager(layoutManager);

        mUserComment = (EditText) findViewById(R.id.editText_userComment);

        mCamera = (ImageButton) findViewById(R.id.button_camera);
        mCamera.setOnClickListener(this);

        mSend = (ImageButton) findViewById(R.id.button_send);
        mSend.setOnClickListener(this);
    }

    private void getFacebookFeedDetail() {

        Log.d("CHECK_ID", "FeedDetailActivity >>>>> " + mArticleId);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                mArticleId,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() == null) {
                            mFeedDetail = new Gson().fromJson(response.getJSONObject().toString(), FacebookFeedDetailData.class);
                            mAdapter = new FacebookCommentAdapter(CommentActivity.this, mFeedDetail, mContentType, mPlatformType);
                            mCommentList.setAdapter(mAdapter);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "created_time,message,full_picture,from{name, picture{url}},attachments{subattachments},source,comments{from{name, picture{url}},message,created_time}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getYoutubeVideoComments() {

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeCommentData> call = service.getCommentList(accessToken, "snippet", mVideoId);
        call.enqueue(new Callback<YoutubeCommentData>() {
            @Override
            public void onResponse(Call<YoutubeCommentData> call, Response<YoutubeCommentData> response) {

                if (response.isSuccessful()) {

                    mYoutubeCommentData = response.body();
                    mAdapter = new YoutubeCommentAdapter(CommentActivity.this, mYoutubeSearchVideoData, mYoutubeCommentData);
                    mCommentList.setAdapter(mAdapter);

                } else {
                    Log.e("ERROR_YOUTUBE", "Comment Activity >>>>> Fail to get json for video "+ response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<YoutubeCommentData> call, Throwable t) {
                t.printStackTrace();
                Log.e("ERROR_YOUTUBE", "Comment Activity >>>>> Fail to access youtube api server");
            }
        });
    }

    private void getPinterestPinComments() {


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_camera:

                break;

            case R.id.button_send:
                break;
        }
    }
}
