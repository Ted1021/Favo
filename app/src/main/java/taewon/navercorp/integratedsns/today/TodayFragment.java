package taewon.navercorp.integratedsns.today;


import android.graphics.Typeface;
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
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    private Typeface mTypeface;

    // for managing tokens
    private FavoTokenManager mFavoTokenManager;

    private ParallaxViewPager mViewPager;
    private PlatformAdapter mAdapter;
    private List<String> mPlatformList = new ArrayList<>();

    private static boolean isInit;

    public static TodayFragment newInstance() {

        TodayFragment fragment = new TodayFragment();
        isInit = true;
        return fragment;
    }
    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        if(isInit){
            checkToken();
            isInit = false;
        }
        initView(view);
        return view;
    }

    private void checkToken() {

        // preference
        mFavoTokenManager = FavoTokenManager.getInstance();

        if (mFavoTokenManager.isTokenVaild(PLATFORM_FACEBOOK)) {
            mPlatformList.add(PLATFORM_FACEBOOK);
        }

        if (mFavoTokenManager.isTokenVaild(PLATFORM_YOUTUBE)) {
            mPlatformList.add(PLATFORM_YOUTUBE);
        }

        if (mFavoTokenManager.isTokenVaild(PLATFORM_TWITCH)) {
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
