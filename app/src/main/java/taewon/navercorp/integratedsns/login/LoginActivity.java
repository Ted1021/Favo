package taewon.navercorp.integratedsns.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.Arrays;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.home.HomeActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mFacebookLogin, mGoogleLogin, mInstaLogin;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView(){

        mFacebookLogin = (Button) findViewById(R.id.button_fb_login);
        mFacebookLogin.setOnClickListener(this);

        mGoogleLogin = (Button) findViewById(R.id.button_google_login);
        mGoogleLogin.setOnClickListener(this);

        mInstaLogin = (Button) findViewById(R.id.button_insta_login);
        mInstaLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

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

    private void getFacebookToken(){

        mCallbackManager = CallbackManager.Factory.create();
        final SharedPreferences pref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                editor.putString(getString(R.string.facebook_token), loginResult.getAccessToken().getToken());
                editor.commit();
                Log.d("CHECK_PREF", "Login Activity >>>>"+pref.getString(getString(R.string.facebook_token),""));

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

    private void getGoogleToken(){


    }

    private void getInstaToken(){


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
