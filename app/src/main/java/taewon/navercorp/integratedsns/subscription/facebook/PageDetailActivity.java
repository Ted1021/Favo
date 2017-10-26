package taewon.navercorp.integratedsns.subscription.facebook;

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
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.FeedFragment;
import taewon.navercorp.integratedsns.model.page.FacebookPageInfoData;

public class PageDetailActivity extends AppCompatActivity{

    private String mPageId;

    private ImageView mCover, mProfile;
    private TextView mTitle, mTitleToolbar, mFollowerCount;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private FacebookPageInfoData mPageInfo = new FacebookPageInfoData();

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

    private void initData(){

        mPageId = getIntent().getStringExtra("PAGE_ID");
        getPageInfo();
    }

    private void initView(){

        mProfile = (ImageView) findViewById(R.id.imageView_pageProfile);
        mCover = (ImageView) findViewById(R.id.imageView_pageCover);
        mCover.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);

        mTitle = (TextView) findViewById(R.id.textView_pageName);
        mTitleToolbar = (TextView) findViewById(R.id.textView_pageName_toolbar);
        mFollowerCount = (TextView) findViewById(R.id.textView_followers);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }

    private void getPageInfo(){

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                mPageId,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if(response.getError() == null){

                            mPageInfo = new Gson().fromJson(response.getJSONObject().toString(), FacebookPageInfoData.class);

                            Glide.with(PageDetailActivity.this).load(mPageInfo.getPicture().getData().getUrl()).into(mProfile);
                            Glide.with(PageDetailActivity.this).load(mPageInfo.getCover().getSource()).apply(new RequestOptions().override(mCover.getMaxWidth())).into(mCover);

                            mTitle.setText(mPageInfo.getName());
                            mTitleToolbar.setText(mPageInfo.getName());
                            mFollowerCount.setText(String.format("팔로워 : %s 명",mPageInfo.getFan_count()));

                        } else {
                            Log.e(getClass().getName(), "Error load facebook page : "+response.getRawResponse());
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,about,picture.height(2048){url},cover{source},fan_count,description");
        request.setParameters(parameters);
        request.executeAsync();
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

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new FeedFragment();
            switch (position) {

                case TAB_FEED:
                    fragment = PageFeedFragment.newInstance(mPageId);
                    break;

                case TAB_VIDEO:
                    fragment = PageVideoFragment.newInstance(mPageId);
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
