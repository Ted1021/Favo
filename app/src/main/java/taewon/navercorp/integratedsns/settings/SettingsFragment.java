package taewon.navercorp.integratedsns.settings;


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

import static android.content.Context.MODE_PRIVATE;
import static taewon.navercorp.integratedsns.home.HomeActivity.mGoogleApiClient;

public class SettingsFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button mFacebookLogout, mGoogleLogout, mInstaLogout;

    private static final String GOOGLE_CLIENT = "google_api_client";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {

        mFacebookLogout = (Button) view.findViewById(R.id.button_fb_logout);
        mFacebookLogout.setOnClickListener(this);

        mGoogleLogout = (Button) view.findViewById(R.id.button_google_logout);
        mGoogleLogout.setOnClickListener(this);

        mInstaLogout = (Button) view.findViewById(R.id.button_insta_logout);
        mInstaLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_fb_logout:
                deleteFacebookToken();
                getActivity().finish();
                break;

            case R.id.button_google_logout:
                deleteGoogleToken();
                getActivity().finish();
                break;

            case R.id.button_insta_logout:

                break;
        }
    }

    // TODO - SharedPreference 로직 유틸로 분기하기
    private void deleteFacebookToken() {

        SharedPreferences pref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();

            editor.putString(getString(R.string.facebook_token), "");
            editor.commit();
        }
        Toast.makeText(getContext(), "logout facebook successfully!!", Toast.LENGTH_SHORT).show();
        Log.d("CHECK_TOKEN", "Settings Fragment >>>>> " + pref.getString(getString(R.string.facebook_token), ""));
    }

    private void deleteGoogleToken() {
        SharedPreferences pref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

                editor.putString(getString(R.string.google_token), "");
                editor.commit();
            }
        });

        Toast.makeText(getContext(), "logout google successfully!!", Toast.LENGTH_SHORT).show();
        Log.d("CHECK_TOKEN", "Settings Fragment >>>>> " + pref.getString(getString(R.string.google_token), ""));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
