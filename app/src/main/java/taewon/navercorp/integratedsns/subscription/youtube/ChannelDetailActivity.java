package taewon.navercorp.integratedsns.subscription.youtube;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.FeedFragment;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.page.YoutubeChannelInfoData;

public class ChannelDetailActivity extends AppCompatActivity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private String mChannelId, mProfileUrl;

    private ImageView mCover, mProfile;
    private TextView mTitle, mTitleToolbar, mFollowerCount;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private YoutubeChannelInfoData mChannelInfo = new YoutubeChannelInfoData();

    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";

    // fragment index
    private static final int TAB_FEED = 0;
    private static final int TAB_VIDEO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_detail);

        initData();
        initView();
        setAction();
    }

    private void initData() {

        // init preference
        mPref = ChannelDetailActivity.this.getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        mChannelId = getIntent().getStringExtra("CHANNEL_ID");
        mProfileUrl = getIntent().getStringExtra("PROFILE_URL");
        getChannelInfo();
    }

    private void initView() {

        mProfile = (ImageView) findViewById(R.id.imageView_pageProfile);
        mCover = (ImageView) findViewById(R.id.imageView_pageCover);
        mCover.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);

        mTitle = (TextView) findViewById(R.id.textView_pageName);
        mTitleToolbar = (TextView) findViewById(R.id.textView_pageName_toolbar);
        mFollowerCount = (TextView) findViewById(R.id.textView_followers);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }


    private void setAction() {

        // set viewPager action
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // set tabLayout action
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {

                    case TAB_FEED:
                        break;

                    case TAB_VIDEO:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // set interaction between viewPager & tabLayout
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void getChannelInfo() {

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));
        Log.d("CHECK_TOKEN", accessToken);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeChannelInfoData> call = service.getChannelInfo(accessToken, "snippet,brandingSettings", mChannelId);
        call.enqueue(new Callback<YoutubeChannelInfoData>() {
            @Override
            public void onResponse(Call<YoutubeChannelInfoData> call, Response<YoutubeChannelInfoData> response) {

                if(response.isSuccessful()){
                    mChannelInfo = response.body();

                    mTitle.setText(mChannelInfo.getItems().get(0).getSnippet().getTitle());
                    mTitleToolbar.setText(mChannelInfo.getItems().get(0).getSnippet().getTitle());

                    Glide.with(ChannelDetailActivity.this).load(mChannelInfo.getItems().get(0).getBrandingSettings().getImage().getBannerMobileExtraHdImageUrl()).apply(new RequestOptions().override(mCover.getMaxWidth())).into(mCover);
                    Glide.with(ChannelDetailActivity.this).load(mProfileUrl).into(mProfile);
                } else {
                    Log.e(getClass().getName(), "Error load youtube page : "+response.raw().toString());
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
                    fragment = ChannelFeedFragment.newInstance(mChannelId, mProfileUrl);
                    break;

                case TAB_VIDEO:
                    fragment = ChannelPlaylistFragment.newInstance(mChannelId);
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
