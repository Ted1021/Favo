package taewon.navercorp.integratedsns.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.widget.LikeView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.FeedFragment;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.page.FacebookPageInfoData;
import taewon.navercorp.integratedsns.model.page.FavoPageInfoData;
import taewon.navercorp.integratedsns.model.page.YoutubeChannelInfoData;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

public class PageDetailActivity extends AppCompatActivity {

    // ui components
    private ImageView mCover, mProfile;
    private TextView mTitle, mTitleToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private LikeView mPageLikeButton;
    private Button mSubscribe;

    // page data
    private SharedPreferences mPref;
    private String mPageId;
    private int mPlatformType;
    private String mProfileImage;
    private FavoPageInfoData mPageData = new FavoPageInfoData();

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
        mPref = PageDetailActivity.this.getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);

        // init platform type & page ID
        Intent intent = getIntent();
        mPlatformType = intent.getIntExtra("PLATFORM_TYPE", 1);
        switch (mPlatformType) {

            case PLATFORM_FACEBOOK:
                mPageId = intent.getStringExtra("PAGE_ID");
                getFacebookPageInfo();
                break;

            case PLATFORM_YOUTUBE:
                mPageId = intent.getStringExtra("CHANNEL_ID");
                mProfileImage = intent.getStringExtra("PROFILE_URL");
                getYoutubeChannelInfo();
                break;
        }
    }

    private void initView() {

        mProfile = (ImageView) findViewById(R.id.imageView_pageProfile);
        mCover = (ImageView) findViewById(R.id.imageView_pageCover);
        mCover.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
        mTitle = (TextView) findViewById(R.id.textView_pageName);
        mTitleToolbar = (TextView) findViewById(R.id.textView_pageName_toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mPageLikeButton = (LikeView) findViewById(R.id.button_pageLike);
        mPageLikeButton.setObjectIdAndType(mPageId, LikeView.ObjectType.PAGE);
    }

    private void setAction() {

        // set viewPager action
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);

        // set interaction between viewPager & tabLayout
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void bindItem() {

        mTitle.setText(mPageData.getPageName());
        mTitleToolbar.setText(mPageData.getPageName());

        Glide.with(PageDetailActivity.this).load(mPageData.getProfileImage()).into(mProfile);
        Glide.with(PageDetailActivity.this).load(mPageData.getCoverImage()).into(mCover);
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
                            mPageData.setCoverImage(pageInfo.getCover().getSource());
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

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));

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

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new FeedFragment();
            switch (position) {
                case TAB_FEED:
                    fragment = PageFeedFragment.newInstance(mPageId, mPlatformType, mProfileImage);
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
}
