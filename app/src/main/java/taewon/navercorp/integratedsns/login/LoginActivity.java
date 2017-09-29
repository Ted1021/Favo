package taewon.navercorp.integratedsns.login;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.home.HomeActivity;

/**
 * @author 김태원
 * @file LoginActivity.java
 * @brief Login service from multiple platforms
 * @date 2017.09.27
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private Button mFacebookLogin, mGoogleLogin, mInstaLogin;
    private CallbackManager mCallbackManager;

    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_SIGN_IN_REQ = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initGoogleSignInclient();
    }

    private void initView() {

        mFacebookLogin = (Button) findViewById(R.id.button_fb_login);
        mFacebookLogin.setOnClickListener(this);

        mGoogleLogin = (Button) findViewById(R.id.button_google_login);
        mGoogleLogin.setOnClickListener(this);

        mInstaLogin = (Button) findViewById(R.id.button_insta_login);
        mInstaLogin.setOnClickListener(this);
    }

    private void initGoogleSignInclient() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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

            case R.id.button_insta_login:
                getInstaToken();
                break;
        }
    }

    private void getFacebookToken() {

        mCallbackManager = CallbackManager.Factory.create();
        final SharedPreferences pref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                editor.putString(getString(R.string.facebook_token), loginResult.getAccessToken().getToken());
                editor.commit();
                Log.d("CHECK_PREF", "Login Activity >>>>" + pref.getString(getString(R.string.facebook_token), ""));

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
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQ);
    }

    private void getInstaToken() {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_REQ) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            // TODO - facebook callback manager 내부적으로 requestCode 를 어떻게 처리하는지 알아 볼 것
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // TODO - SharedPreference 호출 로직과 Activity 이동로직 정리 할 것 (중복)
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            final SharedPreferences pref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
            final SharedPreferences.Editor editor = pref.edit();

            if (acct.getId() != null) {
                editor.putString(getString(R.string.google_token), acct.getId());
                Log.d("CHECK_TOKEN", "Login Activity >>> " + acct.getId());

                editor.commit();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();

            } else {
                Log.d("LOGIN_ERROR", "Login Activity >>> fail to get google ID");
            }

        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.google_login_fail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("LOGIN_ERROR", "Login Activity >>>> " + connectionResult.getErrorMessage());
    }
}
