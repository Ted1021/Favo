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

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.facebook.FacebookFragment;
import taewon.navercorp.integratedsns.instagram.InstagramFragment;
import taewon.navercorp.integratedsns.settings.SettingsFragment;
import taewon.navercorp.integratedsns.youtube.YoutubeFragment;

/**
 * @author 김태원
 * @file HomeActivity.java
 * @brief Show multiple platforms on viewPager
 * @date 2017.09.27
 */

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TabLayout mTabLayout;
    ViewPager mViewPager;

    private static final int FRAG_COUNT = 4;

    private static final int TAB_FACEBOOK = 0;
    private static final int TAB_YOUTUBE = 1;
    private static final int TAB_INSTA = 2;
    private static final int TAB_SETTINGS = 3;

    // TODO - API Client 를 매 Activity 마다 호출해 주어야 하는가? 한곳에서 선언하고 외부에서 가져올 방법 고려해 보기
    public static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        setAction();
        initGoogleSignInclient();
    }

    private void initGoogleSignInclient(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                .enableAutoManage(HomeActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initView() {

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.facebook_color), PorterDuff.Mode.SRC_IN);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void setAction() {

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
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

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {

                    case TAB_FACEBOOK:
                        tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.facebook_color), PorterDuff.Mode.SRC_IN);
                        break;

                    case TAB_YOUTUBE:
                        tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.youtube_color), PorterDuff.Mode.SRC_IN);
                        break;

                    case TAB_INSTA:
                        tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.insta_color), PorterDuff.Mode.SRC_IN);
                        break;

                    case TAB_SETTINGS:
                        tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.settings_color), PorterDuff.Mode.SRC_IN);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.unselected_color), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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

            Fragment fragment = new FacebookFragment();

            switch (position) {

                case TAB_FACEBOOK:
                    fragment = new FacebookFragment();
                    break;

                case TAB_YOUTUBE:
                    fragment = new YoutubeFragment();
                    break;

                case TAB_INSTA:
                    fragment = new InstagramFragment();
                    break;

                case TAB_SETTINGS:
                    fragment = new SettingsFragment();
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
