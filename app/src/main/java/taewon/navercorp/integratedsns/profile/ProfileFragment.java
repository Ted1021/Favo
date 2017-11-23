package taewon.navercorp.integratedsns.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import taewon.navercorp.integratedsns.profile.following.FollowingListFragment;
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * @author 김태원
 * @file ProfileFragment.java
 * @brief show user profile, subscription list and my pin
 * @date 2017.10.13
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Fragment[] mFragmentList = new Fragment[MAX_FRAGMENT];

    private FavoTokenManager mFavoTokenManager;
    private BroadcastReceiver mTokenUpdateReceiver;
    private PDKClient mPinterestClient;

    private ImageView mProfile;
    private TextView mUserName, mId, mTitle;
    private ImageButton mSetting;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AppBarLayout mAppbar;

    private static boolean isInit;

    // fragment index
    private static final int TAB_FACEBOOK = 0;
    private static final int TAB_YOUTUBE = 1;
    private static final int TAB_PINTEREST = 2;
    private static final int TAB_TWITCH = 3;
    private static final int MAX_FRAGMENT = 4;

    private static final String BOARD_FIELDS = "id,name";
    private static final String PIN_FIELDS = "created_at,creator,id,image, media,note,original_link";

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

        mFavoTokenManager = FavoTokenManager.getInstance();

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
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mAppbar = (AppBarLayout) view.findViewById(R.id.appBar);
        mTitle = (TextView) view.findViewById(R.id.textView_title);
        mSetting = (ImageButton) view.findViewById(R.id.button_setting);
        mSetting.setOnClickListener(this);
    }

    private void setProfileInfo() {

        if (mFavoTokenManager.isTokenVaild(PLATFORM_FACEBOOK)) {
            getFacebookUserInfo();
        } else if (mFavoTokenManager.isTokenVaild(PLATFORM_YOUTUBE)) {

        } else if(mFavoTokenManager.isTokenVaild(PLATFORM_PINTEREST)) {

        } else if(mFavoTokenManager.isTokenVaild(PLATFORM_TWITCH)){

        }
    }

    private void setAction() {

        // set viewPager action
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(4);

        // set interaction between viewPager & tabLayout
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());
                mTitle.setAlpha(percentage);
            }
        });
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
            Log.e("CHECK_POSITION", position+"");
            Fragment fragment=FollowingListFragment.newInstance(PLATFORM_FACEBOOK);
            switch (position) {

                case TAB_FACEBOOK:
                    fragment = FollowingListFragment.newInstance(PLATFORM_FACEBOOK);
                    break;

                case TAB_YOUTUBE:
                    fragment = FollowingListFragment.newInstance(PLATFORM_YOUTUBE);
                    break;

                case TAB_PINTEREST:
                    fragment = FollowingListFragment.newInstance(PLATFORM_PINTEREST);
                    break;

                case TAB_TWITCH:
                    fragment = FollowingListFragment.newInstance(PLATFORM_TWITCH);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return MAX_FRAGMENT;
        }
    }
}
