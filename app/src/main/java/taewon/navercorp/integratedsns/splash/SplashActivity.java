package taewon.navercorp.integratedsns.splash;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

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

    private ImageView mLogo;

    private static int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mLogo = (ImageView) findViewById(R.id.imageView_logo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkTokens();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void checkTokens() {

        SharedPreferences pref = getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);

        String facebookToken = pref.getString(getString(R.string.facebook_token), "");
        String googleToken = pref.getString(getString(R.string.google_token), "");
        String pinterestToken = pref.getString(getString(R.string.pinterest_token), "");

        Intent intent;

        if (facebookToken.equals("") && googleToken.equals("") && pinterestToken.equals("")) {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, mLogo, "logo");
            startActivity(intent, options.toBundle());
        } else {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        SplashActivity.this.finish();
    }
}
