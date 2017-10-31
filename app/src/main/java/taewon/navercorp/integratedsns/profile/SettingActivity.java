package taewon.navercorp.integratedsns.profile;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.campmobile.android.bandsdk.BandManager;
import com.campmobile.android.bandsdk.BandManagerFactory;
import com.campmobile.android.bandsdk.api.ApiCallbacks;
import com.campmobile.android.bandsdk.api.LoginCallbacks;
import com.campmobile.android.bandsdk.entity.AccessToken;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import java.io.IOException;
import java.util.Arrays;

import taewon.navercorp.integratedsns.R;

public class SettingActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Auth for facebook
    private CallbackManager mCallbackManager;

    // Auth for google (Youtube)
    private GoogleApiClient mGoogleApiClient;
    private static final String[] GOOGLE_SCOPES = {
            YouTubeScopes.YOUTUBE,
            YouTubeScopes.YOUTUBE_READONLY,
            YouTubeScopes.YOUTUBEPARTNER,
            YouTubeScopes.YOUTUBE_FORCE_SSL,
            YouTubeScopes.YOUTUBEPARTNER_CHANNEL_AUDIT
    };

    // Auth for pinterest
    private PDKClient mPinterestClient;
    private static final String[] PINTEREST_SCOPE = {
            PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC,
            PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC,
            PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS,
            PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS
    };

    // Auth for Band
    private BandManager mBandManager;
    LoginCallbacks<AccessToken> mBandLoginApiCallbacks;

    // managing tokens
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    // UI Components
    private Switch mFacebookSwitch, mYoutubeSwitch, mPinterestSwitch, mBandSwitch;
    private String mFacebookToken, mGoogleToken, mPinterestToken, mBandToken;

    // Auth Request Code
    private static final int REQ_FACEBOOK_SIGN_IN = 100;
    private static final int REQ_GOOGLE_SIGN_IN = 101;
    private static final int REQ_PINTEREST_SIGN_IN = 8772;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initData();
        initView();
        setAction();
    }

    private void initData() {

        // init preference of tokens
        mPref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        mFacebookToken = mPref.getString(getString(R.string.facebook_token), "");
        mGoogleToken = mPref.getString(getString(R.string.google_token), "");
        mPinterestToken = mPref.getString(getString(R.string.pinterest_token), "");
        mBandToken = mPref.getString(getString(R.string.band_token), "");

        // init google client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestScopes(new Scope(YouTubeScopes.YOUTUBE),
                        new Scope(YouTubeScopes.YOUTUBE_READONLY),
                        new Scope(YouTubeScopes.YOUTUBEPARTNER),
                        new Scope(YouTubeScopes.YOUTUBE_FORCE_SSL),
                        new Scope(YouTubeScopes.YOUTUBEPARTNER_CHANNEL_AUDIT)
                )
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(SettingActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // init pinterest client
        mPinterestClient = PDKClient.configureInstance(this, getString(R.string.pinterest_app_id));
        mPinterestClient.onConnect(SettingActivity.this);

        // init band client
        mBandManager = BandManagerFactory.getSingleton();
    }

    private void initView() {

        mFacebookSwitch = (Switch) findViewById(R.id.switch_facebook);
        mYoutubeSwitch = (Switch) findViewById(R.id.switch_youtube);
        mPinterestSwitch = (Switch) findViewById(R.id.switch_pinterest);
        mBandSwitch = (Switch) findViewById(R.id.switch_band);

        checkTokens();
    }

    private void setAction() {

        mFacebookSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mFacebookToken.equals("")) {
                        getFacebookToken();
                    }
                } else {
                    if (!mFacebookToken.equals("")) {
                        deleteFacebookToken();
                    }
                }
            }
        });

        mYoutubeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mGoogleToken.equals("")) {
                        getGoogleToken();
                    }
                } else {
                    if (!mGoogleToken.equals("")) {
                        deleteGoogleToken();
                    }
                }
            }
        });

        mPinterestSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mPinterestToken.equals("")) {
                        getPinterestToken();
                    }
                } else {
                    if (!mPinterestToken.equals("")) {
                        deletePinterestToken();
                    }
                }
            }
        });

        mBandSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mBandToken.equals("")) {
                        getBandToken();
                    }
                } else {
                    if (!mBandToken.equals("")) {
                        deleteBandToken();
                    }
                }
            }
        });
    }

    // send status of tokens to "FeedFragment"
    private void sendTokenStatus() {
        Intent intent = new Intent(getString(R.string.update_token_status));
        LocalBroadcastManager.getInstance(SettingActivity.this).sendBroadcast(intent);
    }

    // check remained token
    private void checkTokens() {

        if (!mFacebookToken.equals("")) {
            mFacebookSwitch.setChecked(true);
        } else {
            mFacebookSwitch.setChecked(false);
        }

        if (!mGoogleToken.equals("")) {
            mYoutubeSwitch.setChecked(true);
        } else {
            mYoutubeSwitch.setChecked(false);
        }

        if (!mPinterestToken.equals("")) {
            mPinterestSwitch.setChecked(true);
        } else {
            mPinterestSwitch.setChecked(false);
        }

        if (!mBandToken.equals("")) {
            mBandSwitch.setChecked(true);
        } else {
            mBandSwitch.setChecked(false);
        }
    }

    // request facebook token
    private void getFacebookToken() {

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_posts", "user_likes"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // set facebook preference
                mEditor.putString(getString(R.string.facebook_token), loginResult.getAccessToken().getToken());
                mEditor.commit();

                Toast.makeText(SettingActivity.this, "Connect to facebook", Toast.LENGTH_SHORT).show();
                Log.d("CHECK_PREF", "Setting Activity >>>>" + mPref.getString(getString(R.string.facebook_token), ""));
                sendTokenStatus();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SettingActivity.this, getString(R.string.facebook_login_fail), Toast.LENGTH_SHORT).show();
                mFacebookSwitch.setChecked(false);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SettingActivity.this, getString(R.string.facebook_login_fail), Toast.LENGTH_SHORT).show();
                mFacebookSwitch.setChecked(false);
            }
        });
    }

    private void deleteFacebookToken() {

        if (com.facebook.AccessToken.getCurrentAccessToken() != null) {
            // call expire facebook token
            LoginManager.getInstance().logOut();

            // delete facebook preference
            mEditor.putString(getString(R.string.facebook_token), "");
            mEditor.commit();
            Toast.makeText(SettingActivity.this, "disconnect facebook successfully!!", Toast.LENGTH_SHORT).show();
            sendTokenStatus();
        }
    }

    // request google token
    private void getGoogleToken() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQ_GOOGLE_SIGN_IN);
    }

    private void deleteGoogleToken() {

        if (!mPref.getString(getString(R.string.google_token), "").equals("")) {
            // call expire google token
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {

                    // delete google token
                    mEditor.putString(getString(R.string.google_token), "");
                    mEditor.commit();
                    Toast.makeText(SettingActivity.this, "disconnect google successfully!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // request pinterest token
    private void getPinterestToken() {

        mPinterestClient.login(this, Arrays.asList(PINTEREST_SCOPE), new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                mEditor.putString(getString(R.string.pinterest_token), response.getUser().getUid());
                mEditor.commit();
                Log.d("CHECK_TOKEN", "Setting Activity >>>>> pinterest " + response.getUser().getUid());
                sendTokenStatus();
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e("ERROR_LOGIN", exception.getDetailMessage());
                mPinterestSwitch.setChecked(false);
            }
        });
    }

    private void deletePinterestToken() {

        mPinterestClient.logout();
        mEditor.putString(getString(R.string.pinterest_token), "");
        mEditor.commit();
        Toast.makeText(SettingActivity.this, "disconnect pinterest successfully!!", Toast.LENGTH_SHORT).show();
        sendTokenStatus();
    }

    private void getBandToken() {

        mBandLoginApiCallbacks = new LoginCallbacks<AccessToken>() {
            @Override
            public void onResponse(AccessToken response) {
                mEditor.putString(getString(R.string.band_token), response.getAccessToken());
                mEditor.commit();
                Log.d("CHECK_TOKEN", mPref.getString(getString(R.string.band_token), ""));
                Toast.makeText(SettingActivity.this, "Login succeeded.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.e("ERROR_LOGIN", "Error band login");
            }
        };
        mBandManager.login(SettingActivity.this, mBandLoginApiCallbacks);
    }

    private void deleteBandToken() {
        mBandManager.logout(new ApiCallbacks<Void>() {
            @Override
            public void onResponse(Void response) {
                Toast.makeText(SettingActivity.this, "Logout succeeded.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError error) {
                Log.e("ERROR_TOKEN", "Fail to band logout");
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
                Log.d("CHECK_TOKEN", "Setting Activity >>>>> " + credential.getToken());
                sendTokenStatus();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR_LOGIN", "Setting Activity >>>>> fail to get credential token");
            } catch (GoogleAuthException e) {
                e.printStackTrace();
                Log.e("ERROR_LOGIN", "Setting Activity >>>>> fail to get credential token");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    // google auth callback method
    private void googleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // have to call 'getToken' in working thread
            GoogleSignInAccount account = result.getSignInAccount();
            new GetGoogleTokenAsync().execute(account.getAccount());

        } else {
            Log.d("ERROR_LOGIN", "Setting Activity >>>>> fail to get google Account");
            Toast.makeText(SettingActivity.this, getString(R.string.google_login_fail), Toast.LENGTH_SHORT).show();
            mYoutubeSwitch.setChecked(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
