package taewon.navercorp.integratedsns.splash;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.home.HomeActivity;
import taewon.navercorp.integratedsns.login.LoginActivity;
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_GIPHY;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

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
        FavoTokenManager favoTokenManager = FavoTokenManager.getInstance();

        Intent intent;
        // jump to login activity
        if (!favoTokenManager.isTokenVaild(PLATFORM_FACEBOOK) &&
                !favoTokenManager.isTokenVaild(PLATFORM_YOUTUBE) &&
                !favoTokenManager.isTokenVaild(PLATFORM_PINTEREST) &&
                !favoTokenManager.isTokenVaild(PLATFORM_TWITCH) &&
                !favoTokenManager.isTokenVaild(PLATFORM_GIPHY)) {

            intent = new Intent(SplashActivity.this, LoginActivity.class);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, mLogo, "logo");
            startActivity(intent, options.toBundle());
        }
        // jump to home activity
        else {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        SplashActivity.this.finish();
    }
}
