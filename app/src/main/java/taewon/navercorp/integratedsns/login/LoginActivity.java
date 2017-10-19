package taewon.navercorp.integratedsns.login;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import java.io.IOException;
import java.util.Arrays;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.home.HomeActivity;

/**
 * @author 김태원
 * @file LoginActivity.java
 * @brief Login service from multiple platforms
 * @date 2017.09.27
 */

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private Button mFacebookLogin, mGoogleLogin, mTumblrLogin;

    // Auth for facebook
    private CallbackManager mCallbackManager;

    // Auth for google (Youtube)
    private GoogleApiClient mGoogleApiClient;
    private static final String[] GOOGLE_SCOPES = {
            YouTubeScopes.YOUTUBE_READONLY,
            YouTubeScopes.YOUTUBE_READONLY,
            YouTubeScopes.YOUTUBEPARTNER};

    // Auth for pinterest
    private PDKClient mPinterestClient;
    private static final String[] PINTEREST_SCOPE = {
            PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC,
            PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC,
            PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS,
            PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS
    };

    // Auth Request Code
    private static final int REQ_FACEBOOK_SIGN_IN = 100;
    private static final int REQ_GOOGLE_SIGN_IN = 101;
    private static final int REQ_PINTEREST_SIGN_IN = 8772;

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

        // init Preference
        mPref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        // init google client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestScopes(new Scope(YouTubeScopes.YOUTUBE), new Scope(YouTubeScopes.YOUTUBE_READONLY), new Scope(YouTubeScopes.YOUTUBEPARTNER))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // init pinterest client
        mPinterestClient = PDKClient.configureInstance(this, getString(R.string.pinterest_app_id));
        mPinterestClient.onConnect(LoginActivity.this);
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
                getPinterestToken();
                break;
        }
    }

    // request facebook token
    private void getFacebookToken() {

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_posts", "user_likes, publish_actions"));
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

    // request google token
    private void getGoogleToken() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQ_GOOGLE_SIGN_IN);
    }

    // request pinterest token
    private void getPinterestToken() {

        mPinterestClient.login(this, Arrays.asList(PINTEREST_SCOPE), new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                mEditor.putString(getString(R.string.pinterest_token), response.getUser().getUid());
                mEditor.commit();
                Log.d("CHECK_TOKEN","Login activity >>>>> pinterest "+response.getUser().getUid());

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e("ERROR_LOGIN", exception.getDetailMessage());
            }
        });

    }

    private class GetGoogleTokenAsync extends AsyncTask<Account, Void, Void> {

        @Override
        protected Void doInBackground(Account... params) {

            // get credential info from google account
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(GOOGLE_SCOPES));
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERROR_LOGIN", "Login Activity >>>>> " + connectionResult.getErrorMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // google login activity result
        if (requestCode == REQ_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleSignInResult(result);
        }

        // pinterest login activity result
        else if (requestCode == REQ_PINTEREST_SIGN_IN) {
            mPinterestClient.onOauthResponse(requestCode, resultCode, data);
        }
        // facebook login activity result
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
