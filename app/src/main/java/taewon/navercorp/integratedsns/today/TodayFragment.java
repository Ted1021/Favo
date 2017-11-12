package taewon.navercorp.integratedsns.today;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import taewon.navercorp.integratedsns.R;

import static android.content.Context.MODE_PRIVATE;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    // for managing tokens
    private SharedPreferences mPref;

    private ParallaxViewPager mViewPager;
    private PlatformAdapter mAdapter;

    private List<Integer> mPlatformList = new ArrayList<>();

    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        checkToken();
        initView(view);

        return view;
    }

    private void checkToken() {

        // preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);

        String facebookToken = mPref.getString(getString(R.string.facebook_token), "");
        String googleToken = mPref.getString(getString(R.string.google_token), "");
//        String pinterestToken = mPref.getString(getString(R.string.pinterest_token), "");
        String twitchToken = mPref.getString(getString(R.string.twitch_token), "");

        if (!facebookToken.equals("")) {
            mPlatformList.add(PLATFORM_FACEBOOK);
        }

        if (!googleToken.equals("")) {
            mPlatformList.add(PLATFORM_YOUTUBE);
        }

//        if (!pinterestToken.equals("")) {
//            mPlatformList.add(PLATFORM_PINTEREST);
//        }

        if (!twitchToken.equals("")) {
            mPlatformList.add(PLATFORM_TWITCH);
        }
    }

    private void initView(View view) {

        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10 * 11, getResources().getDisplayMetrics());

        mViewPager = (ParallaxViewPager) view.findViewById(R.id.viewPager_parent);
        mAdapter = new PlatformAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setPageMargin(-margin);
    }

    public class PlatformAdapter extends FragmentPagerAdapter {

        public PlatformAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlatformFragment.newInstance(mPlatformList.get(position));
        }

        @Override
        public int getCount() {
            return mPlatformList.size();
        }
    }
}
