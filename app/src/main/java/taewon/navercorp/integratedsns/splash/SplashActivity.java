package taewon.navercorp.integratedsns.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.home.HomeActivity;
import taewon.navercorp.integratedsns.login.LoginActivity;

/**
 * @author 김태원
 * @file SplashActivity.java
 * @brief Check service tokens
 * @date 2017.09.27
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkTokens();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void checkTokens(){

        SharedPreferences pref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);

        String facebook_token = pref.getString(getString(R.string.facebook_token),"");
        String google_token = pref.getString(getString(R.string.google_token),"");

        Intent intent;

        if(facebook_token.equals("") && google_token.equals("")){
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
        }

        startActivity(intent);
        SplashActivity.this.finish();
    }
}
