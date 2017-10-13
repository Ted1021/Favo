package taewon.navercorp.integratedsns.home;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;

import java.io.IOException;
import java.util.Arrays;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.facebook.FacebookFragment;
import taewon.navercorp.integratedsns.facebook.OnRequestFacebookTokenListener;
import taewon.navercorp.integratedsns.tumblr.TumblrFragment;
import taewon.navercorp.integratedsns.settings.SettingsFragment;
import taewon.navercorp.integratedsns.youtube.OnRequestYoutubeTokenListener;
import taewon.navercorp.integratedsns.youtube.YoutubeFragment;

/**
 * @author 김태원
 * @file HomeActivity.java
 * @brief Show multiple platforms on viewPager
 * @date 2017.09.27
 */

public class HomeActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, OnRequestYoutubeTokenListener, OnRequestFacebookTokenListener {

    TabLayout mTabLayout;
    ViewPager mViewPager;

    // Auth for facebook
    private CallbackManager mCallbackManager;

    // Auth for google (Youtube)
    public static GoogleApiClient mGoogleApiClient;
    private static final String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBEPARTNER};

    // managing tokens
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private Handler mFacebookHandler;
    private Handler mYoutubeHandler;

    private static final int FRAG_COUNT = 4;

    // fragment index
    private static final int TAB_FACEBOOK = 0;
    private static final int TAB_YOUTUBE = 1;
    private static final int TAB_TUMBLR = 2;
    private static final int TAB_SETTINGS = 3;

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

        // init Preference
        mPref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    private void initView() {

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.facebook_color), PorterDuff.Mode.SRC_IN);
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
                switch (tab.getPosition()) {

                    case TAB_FACEBOOK:
                        tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.facebook_color), PorterDuff.Mode.SRC_IN);
                        break;

                    case TAB_YOUTUBE:
                        tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.youtube_color), PorterDuff.Mode.SRC_IN);
                        break;

                    case TAB_TUMBLR:
                        tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.tumblr_color), PorterDuff.Mode.SRC_IN);
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

            Fragment fragment = new FacebookFragment();
            switch (position) {

                case TAB_FACEBOOK:
                    fragment = new FacebookFragment();
                    break;

                case TAB_YOUTUBE:
                    fragment = new YoutubeFragment();
                    break;

                case TAB_TUMBLR:
                    fragment = new TumblrFragment();
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

    @Override
    public void onRequestFacebookToken(Handler handler) {
        getFacebookToken();
        isInitLoadFacebook = false;
        mFacebookHandler = handler;
    }

    @Override
    public void onRequestYoutubeToken(Handler handler) {
        getGoogleToken();
        isInitLoadYoutube = false;
        mYoutubeHandler = handler;
    }

    private void getFacebookToken() {

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // set facebook preference
                mEditor.putString(getString(R.string.facebook_token), loginResult.getAccessToken().getToken());
                mEditor.commit();
                Log.d("CHECK_PREF", "Home Activity >>>>" + mPref.getString(getString(R.string.facebook_token), ""));

                if(!isInitLoadFacebook){
                    mFacebookHandler.sendEmptyMessage(REQ_REFRESH);
                    isInitLoadFacebook = true;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(HomeActivity.this, getString(R.string.facebook_login_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(HomeActivity.this, getString(R.string.facebook_login_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGoogleToken() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQ_GOOGLE_SIGN_IN);
    }

    // google auth callback method
    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // have to call 'getToken' in working thread
            GoogleSignInAccount account = result.getSignInAccount();
            new GetGoogleTokenAsync().execute(account.getAccount());

        } else {
            Log.d("ERROR_LOGIN", "Home Activity >>>>> fail to get google Account");
            Toast.makeText(HomeActivity.this, getString(R.string.google_login_fail), Toast.LENGTH_SHORT).show();
        }
    }

    private class GetGoogleTokenAsync extends AsyncTask<Account, Void, Void> {

        @Override
        protected Void doInBackground(Account... params) {

            // get credential info from google account
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES));
            credential.setSelectedAccount(params[0]);

            // set google preference
            try {
                mEditor.putString(getString(R.string.google_token), credential.getToken());
                mEditor.commit();
                Log.d("CHECK_TOKEN", "Home Activity >>>>> " + credential.getToken());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR_LOGIN", "Home Activity >>>>> fail to get credential token");
            } catch (GoogleAuthException e) {
                e.printStackTrace();
                Log.e("ERROR_LOGIN", "Home Activity >>>>> fail to get credential token");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(!isInitLoadYoutube){
                mYoutubeHandler.sendEmptyMessage(REQ_REFRESH);
                isInitLoadYoutube = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // google login
        if (requestCode == REQ_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        // facebook login
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERROR_LOGOUT", "Home Activity >>>>> " + connectionResult.getErrorMessage());
    }
}
