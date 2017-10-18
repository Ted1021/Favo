package taewon.navercorp.integratedsns.feed.FeedDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.FacebookFeedData;

public class FeedDetailActivity extends AppCompatActivity {

    private RecyclerView mCommentList;
    private RecyclerView.Adapter mAdapter;

    private ArrayList mDataset;
    private FacebookFeedData mFeedDetail;

    private int mContentType, mPlatformType;
    private String mArticleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        initData();
        initView();
        getFeedDetail();
    }

    private void initData() {

        Intent intent = getIntent();

        mContentType = intent.getIntExtra("CONTENT_TYPE", 0);
        mPlatformType = intent.getIntExtra("PLATFORM_TYPE", 0);
        mArticleId = intent.getStringExtra("ARTICLE_ID");
    }

    private void initView() {

        mCommentList = (RecyclerView) findViewById(R.id.recyclerView_commentList);
        mAdapter = new CommentListAdapter(FeedDetailActivity.this, mDataset, mContentType, mPlatformType);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FeedDetailActivity.this);
        mCommentList.setLayoutManager(layoutManager);
    }

    private void getFeedDetail(){

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                mArticleId,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        mAdapter.notifyDataSetChanged();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "created_time,message,full_picture,from{name, picture{url}},attachments{subattachments},source,comments{from{name, picture{url}},message}");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
