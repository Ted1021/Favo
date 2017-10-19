package taewon.navercorp.integratedsns.feed.FeedDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.FacebookFeedDetailData;

public class FeedDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mCommentList;
    private RecyclerView.Adapter mAdapter;

    private FacebookFeedDetailData mFeedDetail = new FacebookFeedDetailData();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        initData();
        initView();
        getFacebookFeedDetail();
    }

    private void initData() {

        Intent intent = getIntent();

        mContentType = intent.getIntExtra("CONTENT_TYPE", 0);
        mPlatformType = intent.getIntExtra("PLATFORM_TYPE", 0);

        switch(mPlatformType){
            case PLATFORM_FACEBOOK:
                mArticleId = intent.getStringExtra("ARTICLE_ID");
                getFacebookFeedDetail();
                break;

            case PLATFORM_YOUTUBE:
                mVideoId = intent.getStringExtra("VIDEO_ID");
                getYoutubeVideoComments();
                break;

            case PLATFORM_PINTEREST:
                mPinId = intent.getStringExtra("PIN_ID");

                break;
        }

    }

    private void initView() {

        mCommentList = (RecyclerView) findViewById(R.id.recyclerView_commentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FeedDetailActivity.this);
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
                            mAdapter = new CommentListAdapter(FeedDetailActivity.this, mFeedDetail, mContentType, mPlatformType);
                            mCommentList.setAdapter(mAdapter);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "created_time,message,full_picture,from{name, picture{url}},attachments{subattachments},source,comments{from{name, picture{url}},message,created_time}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setFacebookComment() {

        Bundle params = new Bundle();
        String comment = mUserComment.getText().toString();
        params.putString("message", comment);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                String.format("/%s/comments", mArticleId),
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if(response.getError()==null){
                            Toast.makeText(FeedDetailActivity.this, "comment post success !!!", Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("ERROR_POST_COMMENT", response.getError().getErrorMessage());
                        }
                    }
                }
        ).executeAsync();
    }

    private void getYoutubeVideoComments() {

    }

    private void getPinterestPinComments() {

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.button_camera:

                break;

            case R.id.button_send:
                setFacebookComment();
                break;
        }
    }
}
