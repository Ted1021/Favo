package taewon.navercorp.integratedsns.home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, FragmentManager.OnBackStackChangedListener{

    TabLayout mTabLayout;

    // Auth for google (Youtube)
    public static GoogleApiClient mGoogleApiClient;
    private static final String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBEPARTNER};

    private static final int FRAG_COUNT = 3;

    // fragment index
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
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
        addFragmentOnTop(new FeedFragment());
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
    }

    private void setAction() {

        // set tabLayout action
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Fragment fragment = new FeedFragment();
                switch (tab.getPosition()) {

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

                replaceFragment(fragment);
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
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_container, fragment).addToBackStack(null).commit();
    }

    public void addFragmentOnTop(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackStackChanged() {

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
}
