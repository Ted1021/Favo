package taewon.navercorp.integratedsns.page;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.widget.LikeView;
import com.google.gson.Gson;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.FeedFragment;
import taewon.navercorp.integratedsns.interfaces.TwitchService;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.facebook.FacebookPageInfoData;
import taewon.navercorp.integratedsns.model.favo.FavoPageInfoData;
import taewon.navercorp.integratedsns.model.twitch.TwitchFollowingData;
import taewon.navercorp.integratedsns.model.twitch.TwitchUserData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeChannelInfoData;
import taewon.navercorp.integratedsns.model.youtube.YoutubePostSubscriptionData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSubscriptionData;
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static android.view.View.VISIBLE;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_ACCEPT_CODE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_BASE_URL;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

public class PageDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private FavoTokenManager mFavoTokenManager;

    // ui components
    private ImageView mCover, mProfile;
    private ImageButton mBack;
    private TextView mTitle, mTitleToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private LikeView mFacebookPageLikeButton;
    private Button mFollow, mUnfollow;
    private AppBarLayout mAppbar;
    private CoordinatorLayout mActivityLayout;

    // page data
    private String mPageId, mUserName, mPlatformType, mProfileImage, mYoutubeSubscriptionId;
    private FavoPageInfoData mPageData = new FavoPageInfoData();

    private boolean isFollowing = false;

    // fragment index
    private static final int TAB_FEED = 0;
    private static final int TAB_VIDEO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_detail);

        initData();
        initView();
        setAction();
    }

    private void initData() {

        // init preference
        mFavoTokenManager = FavoTokenManager.getInstance();

        // init platform type & page ID
        Intent intent = getIntent();
        mPlatformType = intent.getStringExtra("PLATFORM_TYPE");
        switch (mPlatformType) {

            case PLATFORM_FACEBOOK:
                mPageId = intent.getStringExtra("PAGE_ID");
                getFacebookPageInfo();
                break;

            case PLATFORM_YOUTUBE:
                mPageId = intent.getStringExtra("CHANNEL_ID");
                mProfileImage = intent.getStringExtra("PROFILE_URL");
                getYoutubeChannelInfo();
                checkYoutubeFollowing();
                break;

            case PLATFORM_TWITCH:
                mPageId = intent.getStringExtra("USER_ID");
                mProfileImage = intent.getStringExtra("PROFILE_URL");
                mUserName = intent.getStringExtra("USER_NAME");
                getTwitchStreamerInfo();
                checkTwitchFollowing();
                break;
        }
    }

    private void initView() {

        mActivityLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mProfile = (ImageView) findViewById(R.id.imageView_pageProfile);
        mCover = (ImageView) findViewById(R.id.imageView_pageCover);
        mCover.setColorFilter(Color.parseColor("#FF3B3B3B"), PorterDuff.Mode.MULTIPLY);
        mTitle = (TextView) findViewById(R.id.textView_pageName);
        mTitleToolbar = (TextView) findViewById(R.id.textView_pageName_toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mAppbar = (AppBarLayout) findViewById(R.id.appBar);

        mFacebookPageLikeButton = (LikeView) findViewById(R.id.button_pageLike);
        mFacebookPageLikeButton.setObjectIdAndType(mPageId, LikeView.ObjectType.PAGE);
        if (mPlatformType.equals(PLATFORM_FACEBOOK)) {
            mFacebookPageLikeButton.setVisibility(VISIBLE);
        }

        mBack = (ImageButton) findViewById(R.id.button_back);
        mBack.setOnClickListener(this);

        mFollow = (Button) findViewById(R.id.button_follow);
        mFollow.setOnClickListener(this);

        mUnfollow = (Button) findViewById(R.id.button_unFollow);
        mUnfollow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_back:
                Glide.get(this).clearMemory();
                this.finish();
                break;

            case R.id.button_follow:
                if (mPlatformType.equals(PLATFORM_YOUTUBE)) {
                    followYoutubeChannel();
                } else {
                    followTwitchStream();
                }
                break;

            case R.id.button_unFollow:
                if (mPlatformType.equals(PLATFORM_YOUTUBE)) {
                    unFollowYoutubeChannel();
                } else {
                    unFollowTwitchStream();
                }
                break;
        }
    }

    private void setAction() {

        // set viewPager action
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);

        // set interaction between viewPager & tabLayout
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange());
                mTitleToolbar.setAlpha(percentage);
            }
        });
    }

    private void bindItem() {

        mTitle.setText(mPageData.getPageName());
        mTitleToolbar.setText(mPageData.getPageName());

        Glide.with(PageDetailActivity.this).load(mPageData.getProfileImage())
                .transition(new DrawableTransitionOptions().crossFade()).into(mProfile);

        Glide.with(PageDetailActivity.this).load(mPageData.getCoverImage())
                .transition(new DrawableTransitionOptions().crossFade()).into(mCover);
    }

    private void getFacebookPageInfo() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                mPageId,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() == null) {

                            FacebookPageInfoData pageInfo = new Gson().fromJson(response.getJSONObject().toString(), FacebookPageInfoData.class);

                            mPageData.setProfileImage(pageInfo.getPicture().getData().getUrl());
                            if (pageInfo.getCover() != null) {
                                mPageData.setCoverImage(pageInfo.getCover().getSource());
                            }
                            mPageData.setPageName(pageInfo.getName());
                            mPageData.setDescription(pageInfo.getDescription());
                            mPageData.setSubscriptionCount(pageInfo.getFan_count());

                            bindItem();

                        } else {
                            Log.e(getClass().getName(), "Error load facebook page : " + response.getRawResponse());
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,about,picture.height(2048){url},cover{source},fan_count,description");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getYoutubeChannelInfo() {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeChannelInfoData> call = service.getChannelInfo(accessToken, "snippet,brandingSettings", mPageId);
        call.enqueue(new Callback<YoutubeChannelInfoData>() {
            @Override
            public void onResponse(Call<YoutubeChannelInfoData> call, Response<YoutubeChannelInfoData> response) {

                if (response.isSuccessful()) {
                    YoutubeChannelInfoData channelInfo = response.body();

                    mPageData.setProfileImage(channelInfo.getItems().get(0).getSnippet().getThumbnails().getHigh().getUrl());
                    mPageData.setCoverImage(channelInfo.getItems().get(0).getBrandingSettings().getImage().getBannerMobileExtraHdImageUrl());
                    mPageData.setPageName(channelInfo.getItems().get(0).getSnippet().getTitle());
                    mPageData.setDescription(channelInfo.getItems().get(0).getSnippet().getDescription());

                    bindItem();

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

    private void getTwitchStreamerInfo() {

        String currentToken = "Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_TWITCH);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);

        Call<TwitchUserData> call = service.getTwitchUserInfo(getString(R.string.twitch_client_id), currentToken, mPageId);
        call.enqueue(new Callback<TwitchUserData>() {
            @Override
            public void onResponse(Call<TwitchUserData> call, Response<TwitchUserData> response) {
                if (response.isSuccessful()) {
                    TwitchUserData.UserInfo result = response.body().getData().get(0);

                    mPageData.setProfileImage(result.getProfileImageUrl());
                    mPageData.setPageName(result.getDisplayName());
                    mPageData.setCoverImage(result.getOfflineImageUrl());
                    mPageData.setDescription(result.getDescription());
                    mPageData.setSubscriptionCount(String.format(Locale.KOREA, "%d 명", result.getViewCount()));

                    bindItem();

                } else {
                    Log.e("ERROR_TWITCH", "Feed Fragment >>>>> " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<TwitchUserData> call, Throwable t) {
                t.printStackTrace();
                Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to get user ");
            }
        });
    }

    private void checkYoutubeFollowing() {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSubscriptionData> call = service.getSubscriptionList(accessToken, "snippet", 1, true, mPageId);
        call.enqueue(new Callback<YoutubeSubscriptionData>() {
            @Override
            public void onResponse(Call<YoutubeSubscriptionData> call, Response<YoutubeSubscriptionData> response) {
                if (response.isSuccessful()) {
                    if (response.body().getItems().isEmpty()) {
                        isFollowing = false;
                        mFollow.setVisibility(VISIBLE);
                    } else {
                        mYoutubeSubscriptionId = response.body().getItems().get(0).getId();
                        isFollowing = true;
                        mUnfollow.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<YoutubeSubscriptionData> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void followYoutubeChannel() {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        YoutubePostSubscriptionData requestData = new YoutubePostSubscriptionData();
        requestData.getSnippet().getResource().setChannelId(mPageId);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<ResponseBody> call = service.insertSubscription(accessToken, "snippet", requestData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Snackbar snackbar;
                if (response.isSuccessful()) {
                    snackbar = Snackbar.make(mActivityLayout, "Successfully following", Snackbar.LENGTH_SHORT);
                } else {
                    snackbar = Snackbar.make(mActivityLayout, "fail to follow", Snackbar.LENGTH_SHORT);
                }
                snackbar.show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void unFollowYoutubeChannel() {
        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<ResponseBody> call = service.deleteSubscription(accessToken, mYoutubeSubscriptionId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Snackbar snackbar;
                if (response.isSuccessful()) {
                    snackbar = Snackbar.make(mActivityLayout, "Successfully unFollowing", Snackbar.LENGTH_SHORT);
                } else {
                    snackbar = Snackbar.make(mActivityLayout, "fail to unFollow", Snackbar.LENGTH_SHORT);
                }
                snackbar.show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void checkTwitchFollowing() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);

        Call<TwitchFollowingData> call = service.checkUserFollowingStatus(getString(R.string.twitch_client_id),
                mFavoTokenManager.getUserId(PLATFORM_TWITCH + "_id"), mPageId);
        call.enqueue(new Callback<TwitchFollowingData>() {
            @Override
            public void onResponse(Call<TwitchFollowingData> call, Response<TwitchFollowingData> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData().isEmpty()) {
                        isFollowing = false;
                        mFollow.setVisibility(VISIBLE);
                    } else {
                        isFollowing = true;
                        mUnfollow.setVisibility(VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<TwitchFollowingData> call, Throwable t) {

            }
        });
    }

    private void followTwitchStream() {

        String currentToken = "OAuth " + mFavoTokenManager.getCurrentToken(PLATFORM_TWITCH);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);

        Call<ResponseBody> call = service.insertTwitchFollow(TWITCH_ACCEPT_CODE,
                getString(R.string.twitch_client_id),
                currentToken,
                mFavoTokenManager.getUserId(PLATFORM_TWITCH + "_id"),
                mPageId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Snackbar snackbar;
                    if (response.isSuccessful()) {
                        snackbar = Snackbar.make(mActivityLayout, "Successfully Following", Snackbar.LENGTH_SHORT);
                    } else {
                        snackbar = Snackbar.make(mActivityLayout, "fail to Follow", Snackbar.LENGTH_SHORT);
                    }
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void unFollowTwitchStream() {
        String currentToken = "OAuth " + mFavoTokenManager.getCurrentToken(PLATFORM_TWITCH);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);

        Call<ResponseBody> call = service.deleteTwitchFollow(TWITCH_ACCEPT_CODE,
                currentToken,
                getString(R.string.twitch_client_id),
                mFavoTokenManager.getUserId(PLATFORM_TWITCH + "_id"),
                mPageId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Snackbar snackbar;
                    if (response.isSuccessful()) {
                        snackbar = Snackbar.make(mActivityLayout, "Successfully Following", Snackbar.LENGTH_SHORT);
                    } else {
                        snackbar = Snackbar.make(mActivityLayout, "fail to Follow", Snackbar.LENGTH_SHORT);
                    }
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new FeedFragment();
            switch (position) {
                case TAB_FEED:
                    fragment = PageFeedFragment.newInstance(mPageId, mPlatformType, mProfileImage, mUserName);
                    break;

                case TAB_VIDEO:
                    fragment = PageVideoFragment.newInstance(mPageId, mPlatformType);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }
}
