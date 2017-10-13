package taewon.navercorp.integratedsns.settings;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.login.LoginActivity;

import static android.content.Context.MODE_PRIVATE;
import static taewon.navercorp.integratedsns.home.HomeActivity.mGoogleApiClient;

/**
 * @author 김태원
 * @file SettingsFragment.java
 * @brief Managing multiple tokens
 * @date 2017.09.28
 */

public class SettingsFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    // managing tokens
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private Button mFacebookLogout, mGoogleLogout, mInstaLogout;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initData();
        initView(view);

        return view;
    }

    private void initData() {

        // init preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    private void initView(View view) {

        mFacebookLogout = (Button) view.findViewById(R.id.button_fb_logout);
        mFacebookLogout.setOnClickListener(this);

        mGoogleLogout = (Button) view.findViewById(R.id.button_google_logout);
        mGoogleLogout.setOnClickListener(this);

        mInstaLogout = (Button) view.findViewById(R.id.button_tumblr_logout);
        mInstaLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_fb_logout:
                deleteFacebookToken();
                break;

            case R.id.button_google_logout:
                deleteGoogleToken();
                break;

            case R.id.button_tumblr_logout:
                deleteTumblrToken();
                break;
        }
        checkTokens();
    }

    private void deleteFacebookToken() {

        if (AccessToken.getCurrentAccessToken() != null) {
            // call expire facebook token
            LoginManager.getInstance().logOut();

            // delete facebook preference
            mEditor.putString(getString(R.string.facebook_token), "");
            mEditor.commit();

            Toast.makeText(getContext(), "disconnect facebook successfully!!", Toast.LENGTH_SHORT).show();
        }
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

                    Toast.makeText(getContext(), "disconnect google successfully!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteTumblrToken() {

        mEditor.putString(getString(R.string.tumblr_token), "");
        mEditor.commit();
    }

    // check remained token
    private void checkTokens() {

        String facebookToken = mPref.getString(getString(R.string.facebook_token), "");
        String googleToken = mPref.getString(getString(R.string.google_token), "");
        String tumblrToken = mPref.getString(getString(R.string.tumblr_token), "");

        if (facebookToken.equals("") && googleToken.equals("") && tumblrToken.equals("")) {

            // call Login Activity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERROR_LOGOUT", "Settings Fragment >>>>> " + connectionResult.getErrorMessage());
    }
}
