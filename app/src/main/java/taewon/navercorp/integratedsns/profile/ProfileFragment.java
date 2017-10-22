package taewon.navercorp.integratedsns.profile;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author 김태원
 * @file TumblrFragment.java
 * @brief show tumblr contents, search & add tumblr channels
 * @date 2017.10.13
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String BOARD_FIELDS = "id,name";
    private static final String PIN_FIELDS = "created_at,creator,id,image, media,note,original_link";

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private String mFacebookToken, mGoogleToken, mPinterestToken;

    private RecyclerView mRecyclerView;
    private PinterestListAdapter mAdapter;
    private ArrayList<PDKPin> mDataset = new ArrayList<>();

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mLayoutDisconnection;

    private PDKClient mPinterestClient;

    private ImageView mProfile;
    private TextView mUserName, mId;
    private ImageButton mSetting;
    private ViewPager mViewPager;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initData() {

        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        mFacebookToken = mPref.getString(getString(R.string.facebook_token), "");
        Log.d("CHECK_TOKEN", "Setting Activity >>>>> init " + mFacebookToken);
        mGoogleToken = mPref.getString(getString(R.string.google_token), "");
        Log.d("CHECK_TOKEN", "Setting Activity >>>>> init " + mGoogleToken);
        mPinterestToken = mPref.getString(getString(R.string.pinterest_token), "");
        Log.d("CHECK_TOKEN", "Setting Activity >>>>> init " + mPinterestToken);

        PDKClient.configureInstance(getContext(), getString(R.string.pinterest_app_id));
        mPinterestClient = PDKClient.getInstance();

        if (!mFacebookToken.equals("")) {
            getFacebookUserInfo();
        } else if (!mGoogleToken.equals("")){

        } else {

        }
    }

    private void initView(View view) {

        mProfile = (ImageView) view.findViewById(R.id.imageView_profile);
        mUserName = (TextView) view.findViewById(R.id.textView_userName);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mSetting = (ImageButton) view.findViewById(R.id.button_setting);
        mSetting.setOnClickListener(this);
    }

    private void getFollowingBoards() {

        mPinterestClient.getMyFollowedBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);

                mDataset.clear();
                for (PDKBoard board : response.getBoardList()) {
                    Log.d("CHECK_BOARD", " >>>>> " + board.getName());

                }
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
            }
        });
    }

    private void getFacebookUserInfo(){

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            mUserName.setText(response.getJSONObject().getString("name"));
                            Glide.with(getContext())
                                    .load(response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url"))
                                    .apply(new RequestOptions().circleCropTransform())
                                    .into(mProfile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture.height(2048)");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_setting:

                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onRefresh() {

    }
}
