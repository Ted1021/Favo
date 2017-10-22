package taewon.navercorp.integratedsns.home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.youtube.YouTubeScopes;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.FeedFragment;
import taewon.navercorp.integratedsns.profile.ProfileFragment;
import taewon.navercorp.integratedsns.search.SearchFragment;

/**
 * @author 김태원
 * @file HomeActivity.java
 * @brief Show multiple platforms on viewPager
 * @date 2017.09.27
 */

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TabLayout mTabLayout;
    ViewPager mViewPager;

    // Auth for google (Youtube)
    public static GoogleApiClient mGoogleApiClient;
    private static final String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBEPARTNER};

    private static final int FRAG_COUNT = 3;

    // fragment index
    private static final int TAB_FEED = 0;
    private static final int TAB_SEARCH = 1;
    private static final int TAB_PROFILE = 2;

    // Auth Request Code
    private static final int REQ_FACEBOOK_SIGN_IN = 100;
    private static final int REQ_GOOGLE_SIGN_IN = 101;

    // state check
    private static boolean isInitLoadFacebook = true;
    private static boolean isInitLoadYoutube = true;

    private static int REQ_REFRESH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initData();
        initView();
        setAction();
    }

    private void initData() {

        // init google client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestScopes(new Scope(YouTubeScopes.YOUTUBE), new Scope(YouTubeScopes.YOUTUBE_READONLY), new Scope(YouTubeScopes.YOUTUBEPARTNER))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(HomeActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initView() {

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.tumblr_color), PorterDuff.Mode.SRC_IN);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void setAction() {

        // set viewPager action
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(4);
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

                tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.tumblr_color), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.unselected_color), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // set interaction between viewPager & tabLayout
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                    fragment = new FeedFragment();
                    break;

                case TAB_SEARCH:
                    fragment = new SearchFragment();
                    break;

                case TAB_PROFILE:
                    fragment = new ProfileFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return FRAG_COUNT;
        }
    }
}
