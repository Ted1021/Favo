package taewon.navercorp.integratedsns.login;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.home.HomeActivity;

/**
 * @author 김태원
 * @file LoginActivity.java
 * @brief Login service from multiple platforms
 * @date 2017.09.27
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private Button mFacebookLogin, mGoogleLogin, mTumblrLogin;

    // Auth for facebook
    private CallbackManager mCallbackManager;

    // Auth for google (Youtube)
    private GoogleApiClient mGoogleApiClient;
    private static final String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBEPARTNER};

    // Auth for tumblr
    private CommonsHttpOAuthConsumer mConsumer;
    private CommonsHttpOAuthProvider mProvider;
    private String mAuthUrl;

    // Auth Request Code
    private static final int REQ_FACEBOOK_SIGN_IN = 100;
    private static final int REQ_GOOGLE_SIGN_IN = 101;
    private static final int REQ_TUMBLR_SIGN_IN = 102;

    // managing tokens
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initData();
        initView();
    }

    private void initData() {

        // init google client
        // 여기서 Scope 를 지정하는것이 매우 중요!
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestScopes(new Scope(YouTubeScopes.YOUTUBE), new Scope(YouTubeScopes.YOUTUBE_READONLY), new Scope(YouTubeScopes.YOUTUBEPARTNER))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // init tumblr client
        mConsumer = new CommonsHttpOAuthConsumer(
                getString(R.string.tumblr_consumer_key),
                getString(R.string.tumblr_consumer_secret));

        mProvider = new CommonsHttpOAuthProvider(
                getString(R.string.tumblr_request_token_url),
                getString(R.string.tumblr_access_token_url),
                getString(R.string.tumblr_authorize_url));

        // init Preference
        mPref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    private void initView() {

        mFacebookLogin = (Button) findViewById(R.id.button_fb_login);
        mFacebookLogin.setOnClickListener(this);

        mGoogleLogin = (Button) findViewById(R.id.button_google_login);
        mGoogleLogin.setOnClickListener(this);

        mTumblrLogin = (Button) findViewById(R.id.button_tumblr_login);
        mTumblrLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_fb_login:
                getFacebookToken();
                break;

            case R.id.button_google_login:
                getGoogleToken();
                break;

            case R.id.button_tumblr_login:
                getTumblrToken();
                break;
        }
    }

    private void getFacebookToken() {

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_posts"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // set facebook preference
                mEditor.putString(getString(R.string.facebook_token), loginResult.getAccessToken().getToken());
                mEditor.commit();
                Log.d("CHECK_PREF", "Login Activity >>>>" + mPref.getString(getString(R.string.facebook_token), ""));

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, getString(R.string.facebook_login_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, getString(R.string.facebook_login_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGoogleToken() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQ_GOOGLE_SIGN_IN);
    }

    // request call oAuth logic
    private void getTumblrToken() {
        new GetTumblrTokenAsync().execute();
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
                Log.d("CHECK_TOKEN", "Login Activity >>>>> " + credential.getToken());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR_LOGIN", "Login Activity >>>>> fail to get credential token");
            } catch (GoogleAuthException e) {
                e.printStackTrace();
                Log.e("ERROR_LOGIN", "Login Activity >>>>> fail to get credential token");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    private class GetTumblrTokenAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // TODO - making Progress dial here
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                mAuthUrl = mProvider.retrieveRequestToken(mConsumer, getString(R.string.tumblr_callback_url));
                String token = Uri.parse(mAuthUrl).getQueryParameter("oauth_token");
                String verifier = Uri.parse(mAuthUrl).getQueryParameter("oauth_verifier");
                Log.d("CHECK_TOKEN", "Login Activity >>>>> check tumblr token init " + verifier);

                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(mAuthUrl));
                startActivityForResult(intent, REQ_TUMBLR_SIGN_IN);

//                // get authorization first
//                if(TextUtils.isEmpty(token)){
//                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(mAuthUrl));
//                    startActivityForResult(intent, REQ_TUMBLR_SIGN_IN);
//                }
//                // not first time get
//                else {
//                    tumblrSignInResult();
//                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR_TUMBLR", "Login Activity >>>>> fail to get access token");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // google login
        if (requestCode == REQ_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleSignInResult(result);
        }
        // tumblr login
        else if (requestCode == REQ_TUMBLR_SIGN_IN) {
            tumblrSignInResult();
        }
        // facebook login
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // google auth callback method
    private void googleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // have to call 'getToken' in working thread
            GoogleSignInAccount account = result.getSignInAccount();
            new GetGoogleTokenAsync().execute(account.getAccount());

        } else {
            Log.d("ERROR_LOGIN", "Login Activity >>>>> fail to get google Account");
            Toast.makeText(LoginActivity.this, getString(R.string.google_login_fail), Toast.LENGTH_SHORT).show();
        }
    }

    // tumblr auth callback method
    private void tumblrSignInResult() {

        String token = Uri.parse(mAuthUrl).getQueryParameter("oauth_token");
        String verifier = Uri.parse(mAuthUrl).getQueryParameter("oauth_verifier");
        Log.d("CHECK_TOKEN", "Login Activity >>>>> check tumblr token callback " + token);
        Log.d("CHECK_TOKEN", "Login Activity >>>>> check tumblr token callback " + verifier);

        // Authorization is success
        if (!TextUtils.isEmpty(token)) {

            mEditor.putString(getString(R.string.tumblr_token), token);
            mEditor.putString(getString(R.string.tumblr_token_secret), verifier);

            mEditor.commit();

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
        // Authorization is fail
        else {
            Log.d("ERROR_LOGIN", "Login Activity >>>>> fail to get tumblr Account");
            Toast.makeText(LoginActivity.this, getString(R.string.tumblr_login_fail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERROR_LOGIN", "Login Activity >>>>> " + connectionResult.getErrorMessage());
    }
}
