package taewon.navercorp.integratedsns.profile;

import android.accounts.Account;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.TwitchService;
import taewon.navercorp.integratedsns.util.FavoTokenManager;
import taewon.navercorp.integratedsns.util.TwitchLoginActivity;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_GIPHY;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_BASE_URL;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_REDIRECT_URL;

public class SettingActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, Switch.OnCheckedChangeListener {

    // Auth for facebook
    private CallbackManager mCallbackManager;

    private ImageButton mBack;

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

    // managing tokens
    private FavoTokenManager mFavoTokenManager;

    // UI Components
    private Switch mFacebookSwitch, mYoutubeSwitch, mPinterestSwitch, mTwitchSwitch, mGiphySwitch;

    // Auth Request Code
    private static final int REQ_FACEBOOK_SIGN_IN = FacebookSdk.getCallbackRequestCodeOffset() + 0;
    private static final int REQ_GOOGLE_SIGN_IN = 101;
    private static final int REQ_PINTEREST_SIGN_IN = 8772;
    private static final int REQ_TWITCH_SIGN_IN = 102;
    private static final int REQ_GIPHY_SIGN_IN = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initData();
        initView();
    }

    private void initData() {

        // init token manager
        mFavoTokenManager = FavoTokenManager.getInstance();

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
    }

    private void initView() {

        mFacebookSwitch = (Switch) findViewById(R.id.switch_facebook);
        mFacebookSwitch.setOnCheckedChangeListener(this);

        mYoutubeSwitch = (Switch) findViewById(R.id.switch_youtube);
        mYoutubeSwitch.setOnCheckedChangeListener(this);

        mPinterestSwitch = (Switch) findViewById(R.id.switch_pinterest);
        mPinterestSwitch.setOnCheckedChangeListener(this);

        mTwitchSwitch = (Switch) findViewById(R.id.switch_twitch);
        mTwitchSwitch.setOnCheckedChangeListener(this);

        mGiphySwitch = (Switch) findViewById(R.id.switch_giphy);
        mGiphySwitch.setOnCheckedChangeListener(this);

        mBack = (ImageButton) findViewById(R.id.button_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });

        checkTokens();
    }

    // send status of tokens to "FeedFragment"
    private void sendTokenStatus() {
        Intent intent = new Intent(getString(R.string.update_token_status));
        LocalBroadcastManager.getInstance(SettingActivity.this).sendBroadcast(intent);
    }

    // check remained token
    private void checkTokens() {

        if (mFavoTokenManager.isTokenVaild(PLATFORM_FACEBOOK)) {
            mFacebookSwitch.setChecked(true);
        } else {
            mFacebookSwitch.setChecked(false);
        }

        if (mFavoTokenManager.isTokenVaild(PLATFORM_YOUTUBE)) {
            mYoutubeSwitch.setChecked(true);
        } else {
            mYoutubeSwitch.setChecked(false);
        }

        if (mFavoTokenManager.isTokenVaild(PLATFORM_PINTEREST)) {
            mPinterestSwitch.setChecked(true);
        } else {
            mPinterestSwitch.setChecked(false);
        }

        if (mFavoTokenManager.isTokenVaild(PLATFORM_TWITCH)) {
            mTwitchSwitch.setChecked(true);
        } else {
            mTwitchSwitch.setChecked(false);
        }

        if (mFavoTokenManager.isTokenVaild(PLATFORM_GIPHY)) {
            mGiphySwitch.setChecked(true);
        } else {
            mGiphySwitch.setChecked(false);
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
                mFavoTokenManager.createToken(PLATFORM_FACEBOOK, loginResult.getAccessToken().getToken());
                Toast.makeText(SettingActivity.this, getString(R.string.login_success) + " Facebook", Toast.LENGTH_SHORT).show();
                sendTokenStatus();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SettingActivity.this, getString(R.string.login_fail) + " Facebook", Toast.LENGTH_SHORT).show();
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

            // delete facebook preference
            mFavoTokenManager.removeToken(PLATFORM_FACEBOOK);

            // call expire facebook token
            LoginManager.getInstance().logOut();

            Toast.makeText(SettingActivity.this, getString(R.string.logout_success) + " Facebook", Toast.LENGTH_SHORT).show();
            sendTokenStatus();
        }
    }

    // request google token
    private void getGoogleToken() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQ_GOOGLE_SIGN_IN);
    }

    private void deleteGoogleToken() {

        if (mFavoTokenManager.isTokenVaild(PLATFORM_YOUTUBE)) {
            // call revoke google token
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {

                    // delete google token
                    mFavoTokenManager.removeToken(PLATFORM_YOUTUBE);
                    Toast.makeText(SettingActivity.this, getString(R.string.logout_success) + " Youtube", Toast.LENGTH_SHORT).show();
                    sendTokenStatus();
                }
            });
        }
    }

    // request pinterest token
    private void getPinterestToken() {

        mPinterestClient.login(this, Arrays.asList(PINTEREST_SCOPE), new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {

                mFavoTokenManager.createToken(PLATFORM_PINTEREST, response.getUser().getUid());
                Toast.makeText(SettingActivity.this, getString(R.string.login_success) + " Pinterest", Toast.LENGTH_SHORT).show();
                sendTokenStatus();
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e("ERROR_LOGIN", exception.getDetailMessage());
                mPinterestSwitch.setChecked(false);
                Toast.makeText(SettingActivity.this, getString(R.string.login_fail) + " Pinterest", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void deletePinterestToken() {

        mFavoTokenManager.removeToken(PLATFORM_PINTEREST);
        mPinterestClient.logout();

        Toast.makeText(SettingActivity.this, getString(R.string.logout_success) + " Pinterest", Toast.LENGTH_SHORT).show();
        sendTokenStatus();
    }

    // request twitch token
    private void getTwitchToken() {

        // set retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwitchService service = retrofit.create(TwitchService.class);
        Call<ResponseBody> call = service.getTwitchAccessToken(
                getString(R.string.twitch_client_id),
                TWITCH_REDIRECT_URL,
                "token",
                "user:edit",
                getString(R.string.twitch_client_id));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    String requestUrl = response.raw().request().url().toString();
                    Intent intent = new Intent(SettingActivity.this, TwitchLoginActivity.class);
                    intent.putExtra("REQ_TYPE", "login");
                    intent.putExtra("REQ_URL", requestUrl);
                    startActivityForResult(intent, REQ_TWITCH_SIGN_IN);
                } else {
                    Toast.makeText(SettingActivity.this, getString(R.string.login_fail) + " Twitch", Toast.LENGTH_SHORT).show();
                    mTwitchSwitch.setChecked(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(SettingActivity.this, getString(R.string.login_fail) + " Twitch", Toast.LENGTH_SHORT).show();
                Log.d("CHECK_URL ", t.toString());
                mTwitchSwitch.setChecked(false);
            }
        });
    }

    private void deleteTwitchToken() {

        // set retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwitchService service = retrofit.create(TwitchService.class);
        Call<ResponseBody> call = service.deleteTwitchAccessToken(
                getString(R.string.twitch_client_id),
                mFavoTokenManager.getCurrentToken(getString(R.string.twitch_token)));
        Log.d("CHECK_TOKEN", mFavoTokenManager.getCurrentToken(getString(R.string.twitch_token)));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SettingActivity.this, getString(R.string.logout_success) + " Twitch", Toast.LENGTH_SHORT).show();
                    mFavoTokenManager.removeToken(PLATFORM_TWITCH);
                    mTwitchSwitch.setChecked(false);
                    sendTokenStatus();
                } else {
                    Toast.makeText(SettingActivity.this, getString(R.string.logout_fail) + " Twitch", Toast.LENGTH_SHORT).show();
                    mTwitchSwitch.setChecked(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SettingActivity.this, getString(R.string.logout_fail) + " Twitch", Toast.LENGTH_SHORT).show();
                mTwitchSwitch.setChecked(true);
                t.printStackTrace();
            }
        });
    }

    private void getGiphyToken() {
        mFavoTokenManager.createToken(PLATFORM_GIPHY, getString(R.string.giphy_token));
        Toast.makeText(SettingActivity.this, getString(R.string.login_success) + " Giphy", Toast.LENGTH_SHORT).show();
    }

    private void deleteGiphyToken() {
        mFavoTokenManager.removeToken(PLATFORM_GIPHY);
        Toast.makeText(SettingActivity.this, getString(R.string.logout_success) + " Giphy", Toast.LENGTH_SHORT).show();

    }

    private class GetGoogleTokenAsync extends AsyncTask<Account, Void, Void> {

        @Override
        protected Void doInBackground(Account... params) {

            // get credential info from google account
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(GOOGLE_SCOPES));
            credential.setSelectedAccount(params[0]);

            // set google preference
            try {
                mFavoTokenManager.createToken(PLATFORM_YOUTUBE, credential.getToken());
                sendTokenStatus();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(SettingActivity.this, getString(R.string.login_success) + " Youtube", Toast.LENGTH_SHORT).show();
        }
    }

    // google auth callback method
    private void googleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // have to call 'getToken' in working thread
            GoogleSignInAccount account = result.getSignInAccount();
            new GetGoogleTokenAsync().execute(account.getAccount());

        } else {
            Toast.makeText(SettingActivity.this, getString(R.string.login_fail) + " Youtube", Toast.LENGTH_SHORT).show();
            mYoutubeSwitch.setChecked(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // twitch auth callback method
    private void twitchSignInResult(String callbackResult) {

        int startPoint = callbackResult.indexOf("=") + 1;
        int endPoint = callbackResult.indexOf("&");

        String token = callbackResult.substring(startPoint, endPoint);

        if (!token.equals("")) {
            Toast.makeText(SettingActivity.this, getString(R.string.login_success) + " Twitch", Toast.LENGTH_SHORT).show();
            mFavoTokenManager.createToken(PLATFORM_TWITCH, token);
            mTwitchSwitch.setChecked(true);
            sendTokenStatus();
        } else {
            Toast.makeText(SettingActivity.this, getString(R.string.login_fail) + " Twitch", Toast.LENGTH_SHORT).show();
            mTwitchSwitch.setChecked(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // facebook login activity result
            if (requestCode == REQ_FACEBOOK_SIGN_IN) {
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
            }
            // google login activity result
            else if (requestCode == REQ_GOOGLE_SIGN_IN) {

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                googleSignInResult(result);
            }
            // pinterest login activity result
            else if (requestCode == REQ_PINTEREST_SIGN_IN) {
                mPinterestClient.onOauthResponse(requestCode, resultCode, data);
            }
            // giphy login activity result
            else if (requestCode == REQ_GIPHY_SIGN_IN) {

            }
            // twitch login activity result
            else if (requestCode == REQ_TWITCH_SIGN_IN) {
                String callbackResult = data.getStringExtra("CALLBACK");
                twitchSignInResult(callbackResult);
            } else {

            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {

            case R.id.switch_facebook:

                if (isChecked && !mFavoTokenManager.isTokenVaild(PLATFORM_FACEBOOK)) {
                    getFacebookToken();
                } else if (!isChecked && mFavoTokenManager.isTokenVaild(PLATFORM_FACEBOOK)) {
                    deleteFacebookToken();
                }
                break;

            case R.id.switch_youtube:

                if (isChecked && !mFavoTokenManager.isTokenVaild(PLATFORM_YOUTUBE)) {
                    getGoogleToken();
                } else if (!isChecked && mFavoTokenManager.isTokenVaild(PLATFORM_YOUTUBE)) {
                    deleteGoogleToken();
                }
                break;

            case R.id.switch_pinterest:

                if (isChecked && !mFavoTokenManager.isTokenVaild(PLATFORM_PINTEREST)) {
                    getPinterestToken();
                } else if (!isChecked && mFavoTokenManager.isTokenVaild(PLATFORM_PINTEREST)) {
                    deletePinterestToken();
                }
                break;

            case R.id.switch_giphy:
                if (isChecked && !mFavoTokenManager.isTokenVaild(PLATFORM_GIPHY)) {
                    getGiphyToken();
                } else if (!isChecked && mFavoTokenManager.isTokenVaild(PLATFORM_GIPHY)) {
                    deleteGiphyToken();
                }
                break;

            case R.id.switch_twitch:

                if (isChecked && !mFavoTokenManager.isTokenVaild(PLATFORM_TWITCH)) {
                    getTwitchToken();
                } else if (!isChecked && mFavoTokenManager.isTokenVaild(PLATFORM_TWITCH)) {
                    deleteTwitchToken();
                }
                break;
        }
    }
}
