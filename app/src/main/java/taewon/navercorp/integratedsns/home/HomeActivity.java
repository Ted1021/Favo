package taewon.navercorp.integratedsns.home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.youtube.YouTubeScopes;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.FeedFragment;
import taewon.navercorp.integratedsns.live.LiveStreamingFragment;
import taewon.navercorp.integratedsns.profile.ProfileFragment;
import taewon.navercorp.integratedsns.search.SearchFragment;
import taewon.navercorp.integratedsns.today.TodayFragment;

/**
 * @author 김태원
 * @file HomeActivity.java
 * @brief Show multiple platforms on viewPager
 * @date 2017.09.27
 */

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Fragment[] mFragmentList = new Fragment[MAX_FRAGMENT];
    private TabLayout mTabLayout;
    private FragmentManager mFragmentManager;
    private LinearLayout mTabStrip;

    private long mPressedTime = 0;

    // Auth for google (Youtube)
    public static GoogleApiClient mGoogleApiClient;

    // fragment index
    private static final int TAB_TODAY = 0;
    private static final int TAB_FEED = 1;
    private static final int TAB_SEARCH = 2;
    private static final int TAB_LIVE = 3;
    private static final int TAB_PROFILE = 4;
    private static final int MAX_FRAGMENT = 5;

    private static final int TIME_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initData();
        initView();
        addFragmentOnTop();
        setAction();

    }

    private void initData() {

        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new Fragment[MAX_FRAGMENT];

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
        mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()).getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
        mTabStrip = (LinearLayout) mTabLayout.getChildAt(0);
    }

    private void addFragmentOnTop() {

        Fragment fragment = TodayFragment.newInstance();
        mFragmentList[TAB_TODAY] = fragment;
        mFragmentManager.beginTransaction()
                .add(R.id.layout_container, fragment)
                .commit();
    }

    private void setAction() {

        // set tabLayout action
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Fragment fragment;
                switch (tab.getPosition()) {

                    case TAB_TODAY:
                        if (mFragmentList[TAB_TODAY] == null) {
                            fragment = TodayFragment.newInstance();
                            mFragmentList[TAB_TODAY] = fragment;
                        }
                        break;

                    case TAB_FEED:
                        if (mFragmentList[TAB_FEED] == null) {
                            fragment = FeedFragment.newInstance();
                            mFragmentList[TAB_FEED] = fragment;
                        }
                        break;

                    case TAB_SEARCH:
                        if (mFragmentList[TAB_SEARCH] == null) {
                            fragment = SearchFragment.newInstance();
                            mFragmentList[TAB_SEARCH] = fragment;
                        }
                        break;

                    case TAB_LIVE:
                        if (mFragmentList[TAB_LIVE] == null) {
                            fragment = LiveStreamingFragment.newInstance();
                            mFragmentList[TAB_LIVE] = fragment;
                        }
                        break;

                    case TAB_PROFILE:
                        if (mFragmentList[TAB_PROFILE] == null) {
                            fragment = ProfileFragment.newInstance();
                            mFragmentList[TAB_PROFILE] = fragment;
                        }
                        break;
                }
                replaceFragment(mFragmentList[tab.getPosition()], tab.getPosition()+"");
                tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.unselected_color), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mTabStrip.getChildAt(TAB_FEED).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callScrollToTop();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void replaceFragment(Fragment fragment, String tag) {

        mFragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment, tag)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // app 이 종료되면 cache 삭제
        Glide.get(getApplicationContext()).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getApplicationContext()).clearDiskCache();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {

        if (mTabLayout.getTabAt(TAB_TODAY) != null) {
            if (mTabLayout.getTabAt(TAB_TODAY).isSelected()) {
                alertClosingApp();
            } else {
                mTabLayout.getTabAt(TAB_TODAY).select();
            }
        }
    }

    private void alertClosingApp() {

        if (mPressedTime == 0) {
            Toast.makeText(HomeActivity.this, getString(R.string.app_closing_alert), Toast.LENGTH_LONG).show();
            mPressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - mPressedTime);

            if (seconds > TIME_LENGTH) {
                Toast.makeText(HomeActivity.this, getString(R.string.app_closing_alert), Toast.LENGTH_LONG).show();
                mPressedTime = 0;
            } else {
                super.onBackPressed();
            }
        }
    }

    private void callScrollToTop() {

        Intent intent = new Intent(getString(R.string.scroll_to_top_status));
        LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(intent);
    }
}
