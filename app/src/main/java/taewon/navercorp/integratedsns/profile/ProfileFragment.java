package taewon.navercorp.integratedsns.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.pinterest.android.pdk.PDKClient;

import org.json.JSONException;
import org.json.JSONObject;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.FeedFragment;
import taewon.navercorp.integratedsns.profile.following.FollowingListFragment;
import taewon.navercorp.integratedsns.profile.pin.MyPinFragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author 김태원
 * @file ProfileFragment.java
 * @brief show user profile, subscription list and my pin
 * @date 2017.10.13
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String BOARD_FIELDS = "id,name";
    private static final String PIN_FIELDS = "created_at,creator,id,image, media,note,original_link";

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private BroadcastReceiver mTokenUpdateReceiver;

    private String mFacebookToken, mGoogleToken, mPinterestToken;
    private PDKClient mPinterestClient;

    private ImageView mProfile;
    private TextView mUserName, mId;
    private ImageButton mSetting;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private static boolean isInit;

    private static final int TAB_COUNT = 2;
    private static final int TAB_FOLLOWING = 0;
    private static final int TAB_MY_PIN = 1;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        isInit = true;
        return fragment;
    }

    @Override
    public void onDestroyView() {
        mViewPager.setAdapter(null);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initData();
        initView(view);
        setAction();

        return view;
    }

    private void initData() {

        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        mFacebookToken = mPref.getString(getString(R.string.facebook_token), "");
        mGoogleToken = mPref.getString(getString(R.string.google_token), "");
        mPinterestToken = mPref.getString(getString(R.string.pinterest_token), "");

        PDKClient.configureInstance(getContext(), getString(R.string.pinterest_app_id));
        mPinterestClient = PDKClient.getInstance();

        // init update token status receiver
        mTokenUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                setProfileInfo();
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTokenUpdateReceiver, new IntentFilter(getString(R.string.update_token_status)));

        setProfileInfo();
    }

    private void initView(View view) {

        mProfile = (ImageView) view.findViewById(R.id.imageView_profile);
        mUserName = (TextView) view.findViewById(R.id.textView_userName);
        mSetting = (ImageButton) view.findViewById(R.id.button_setting);
        mSetting.setOnClickListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
    }

    private void setProfileInfo() {

        if (!mFacebookToken.equals("")) {
            getFacebookUserInfo();
        } else if (!mGoogleToken.equals("")) {

        } else {

        }
    }

    private void setAction() {

        // set viewPager action
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);

        // set interaction between viewPager & tabLayout
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void getFacebookUserInfo() {

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            mUserName.setText(response.getJSONObject().getString("name"));
                            Glide.with(getContext().getApplicationContext())
                                    .load(response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url"))
                                    .transition(new DrawableTransitionOptions().crossFade())
                                    .apply(new RequestOptions().circleCropTransform())
                                    .into(mProfile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
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

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new FeedFragment();
            switch (position) {

                case TAB_FOLLOWING:
                    fragment = new FollowingListFragment();
                    break;

                case TAB_MY_PIN:
                    fragment = new MyPinFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}
