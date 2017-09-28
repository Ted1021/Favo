package taewon.navercorp.integratedsns.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import taewon.navercorp.integratedsns.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button mFacebookLogout, mGoogleLogout, mInstaLogout;

    // TODO - API Client 를 매 Activity 마다 호출해 주어야 하는가? 한곳에서 선언하고 외부에서 가져올 방법 고려해 보기
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initGoogleSignInclient();
    }

    private void initView() {

        mFacebookLogout = (Button) findViewById(R.id.button_fb_logout);
        mFacebookLogout.setOnClickListener(this);

        mGoogleLogout = (Button) findViewById(R.id.button_google_logout);
        mGoogleLogout.setOnClickListener(this);

        mInstaLogout = (Button) findViewById(R.id.button_insta_logout);
        mInstaLogout.setOnClickListener(this);
    }
    private void initGoogleSignInclient(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(HomeActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_fb_logout:
                deleteFacebookToken();
                HomeActivity.this.finish();
                break;

            case R.id.button_google_logout:
                deleteGoogleToken();
                break;

            case R.id.button_insta_logout:

                break;
        }
    }

    private void deleteFacebookToken() {

        SharedPreferences pref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();

            editor.putString(getString(R.string.facebook_token), "");
            editor.commit();
        }
        Toast.makeText(HomeActivity.this, "logout facebook successfully!!", Toast.LENGTH_SHORT).show();
        Log.d("CHECK_TOKEN", "HomeActivity >>>>> " + pref.getString(getString(R.string.facebook_token), ""));
    }

    private void deleteGoogleToken() {
        SharedPreferences pref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

                editor.putString(getString(R.string.google_token), "");
                editor.commit();

                HomeActivity.this.finish();
            }
        });

        Toast.makeText(HomeActivity.this, "logout google successfully!!", Toast.LENGTH_SHORT).show();
        Log.d("CHECK_TOKEN", "HomeActivity >>>>> " + pref.getString(getString(R.string.google_token), ""));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
