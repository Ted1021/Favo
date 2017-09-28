package taewon.navercorp.integratedsns.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import taewon.navercorp.integratedsns.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button mFacebookLogout, mGoogleLogout, mInstaLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    private void initView() {

        mFacebookLogout = (Button) findViewById(R.id.button_fb_logout);
        mFacebookLogout.setOnClickListener(this);

        mGoogleLogout = (Button) findViewById(R.id.button_google_logout);
        mGoogleLogout.setOnClickListener(this);

        mInstaLogout = (Button) findViewById(R.id.button_insta_logout);
        mInstaLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_fb_logout:
                deleteFacebookToken();
                HomeActivity.this.finish();
                break;

            case R.id.button_google_logout:

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
}
