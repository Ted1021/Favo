package taewon.navercorp.integratedsns.feed.comment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.facebook.FacebookCommentData;
import taewon.navercorp.integratedsns.model.favo.FavoCommentData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeCommentData;
import taewon.navercorp.integratedsns.model.youtube.YoutubePostCommentData;
import taewon.navercorp.integratedsns.util.EndlessRecyclerViewScrollListener;
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

public class CommentActivity extends AppCompatActivity {

    private FavoTokenManager mFavoTokenManager;

    private ImageButton mCamera, mSend;
    private EndlessRecyclerViewScrollListener mScrollListener;
    private EditText mUserComment;

    private String mNext, mPlatformType, mFeedId;

    // Comment list components
    private SlidingUpPanelLayout mCommentSlidingLayout;
    private RecyclerView mCommentList;
    private RecyclerView.LayoutManager mLayoutManager;
    private CommentListAdapter mCommentAdapter;
    private ArrayList<FavoCommentData> mCommentDataset = new ArrayList<>();

    private static final int MAX_COUNTS = 10;
    private static final int TRANSITION_TIME = 700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);

        initView();
        initData();
    }

    private void initView() {

        setSlidingLayout();
        setRecyclerView();
    }

    private void initData() {

        Intent intent = getIntent();
        mPlatformType = intent.getStringExtra("PLATFORM_TYPE");
        mFavoTokenManager = FavoTokenManager.getInstance();

        switch (mPlatformType) {

            case PLATFORM_FACEBOOK:
                mFeedId = intent.getStringExtra("ARTICLE_ID");
                Log.d("CHECK_FEED_ID", mFeedId);
                getFacebookComment();
                break;

            case PLATFORM_YOUTUBE:
                mFeedId = intent.getStringExtra("VIDEO_ID");
                getYoutubeComment();
                break;

            case PLATFORM_PINTEREST:

                break;
        }
    }

    private void setSlidingLayout() {

        mCommentSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
        mCommentSlidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    CommentActivity.this.finish();
                    CommentActivity.this.overridePendingTransition(0, 0);
                }
            }
        });

        mCommentSlidingLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommentSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                CommentActivity.this.finish();
                CommentActivity.this.overridePendingTransition(0, 0);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCommentSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        }, TRANSITION_TIME);
    }

    private void setRecyclerView() {

        mCommentList = (RecyclerView) findViewById(R.id.recyclerView_commentList);
        mCommentAdapter = new CommentListAdapter(CommentActivity.this, mCommentDataset);
        mCommentList.setAdapter(mCommentAdapter);

        mLayoutManager = new LinearLayoutManager(CommentActivity.this);
        mCommentList.setLayoutManager(mLayoutManager);
        mScrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                switch (mPlatformType) {

                    case PLATFORM_FACEBOOK:
                        getFacebookComment();
                        break;

                    case PLATFORM_YOUTUBE:
                        getYoutubeComment();
                        break;
                }
            }
        };
        mCommentList.addOnScrollListener(mScrollListener);
    }

    private void getFacebookComment() {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                String.format("%s/comments", mFeedId),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if(response.getError() == null){

                            FacebookCommentData result = new Gson().fromJson(response.getJSONObject().toString(), FacebookCommentData.class);
                            if(result.getPaging() != null){
                                mNext = result.getPaging().getCursors().getAfter();
                            }
                            for(FacebookCommentData.Comment item : result.getData()){

                                FavoCommentData data = new FavoCommentData();

                                data.setProfileImage(item.getFrom().getPicture().getData().getUrl());
                                data.setUserName(item.getFrom().getName());
                                data.setCreatedTime(item.getCreatedTime());
                                data.setMessage(item.getMessage());

                                mCommentDataset.add(data);
                            }
                            mCommentAdapter.notifyDataSetChanged();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        if(mNext != null){
            parameters.putString("after", mNext);
        }
        parameters.putString("fields", "created_time,message,full_picture,from{name, picture{url}},attachments{subattachments},source,comments.limit(1){from{name, picture{url}},message}");
        parameters.putString("limit", MAX_COUNTS+"");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getYoutubeComment() {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeCommentData> call = service.getCommentListNext(accessToken, "snippet", mNext, MAX_COUNTS, mFeedId);
        call.enqueue(new Callback<YoutubeCommentData>() {
            @Override
            public void onResponse(Call<YoutubeCommentData> call, Response<YoutubeCommentData> response) {

                if (response.isSuccessful()) {

                    YoutubeCommentData result = response.body();
                    if (result.getNextPageToken() == null) {
                        return;
                    } else {
                        mNext = result.getNextPageToken();
                    }
                    for (YoutubeCommentData.Item item : result.getItems()) {

                        YoutubeCommentData.Item.TopLevelComment.Author comment = item.getSnippet().getTopLevelComment().getSnippet();
                        FavoCommentData data = new FavoCommentData();

                        data.setProfileImage(comment.getAuthorProfileImageUrl());
                        data.setCreatedTime(comment.getPublishedAt());
                        data.setMessage(comment.getTextOriginal());
                        data.setUserName(comment.getAuthorDisplayName());

                        mCommentDataset.add(data);
                    }
                    mCommentAdapter.notifyDataSetChanged();

                } else {
                    Log.e("ERROR_YOUTUBE", "Comment Activity >>>>> Fail to get json for video " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<YoutubeCommentData> call, Throwable t) {
                t.printStackTrace();
                Log.e("ERROR_YOUTUBE", "Comment Activity >>>>> Fail to access youtube api server");
            }
        });
    }

    private void setYoutubeComment(String userComment) {

        YoutubePostCommentData commentData = new YoutubePostCommentData();
        commentData.getSnippet().setVideoId(mFeedId);
        commentData.getSnippet().getTopLevelComment().getSnippet().setTextOriginal(userComment);

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<Void> call = service.setComment(accessToken, "snippet", commentData);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                mScrollListener.resetState();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        mCommentSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommentActivity.this.finish();
                CommentActivity.this.overridePendingTransition(0, 0);
            }
        }, TRANSITION_TIME);
    }
}
