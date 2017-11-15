package taewon.navercorp.integratedsns.today;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.GiphyService;
import taewon.navercorp.integratedsns.interfaces.TwitchService;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;
import taewon.navercorp.integratedsns.model.giphy.GiphyImageData;
import taewon.navercorp.integratedsns.model.twitch.TwitchStreamingData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static taewon.navercorp.integratedsns.R.id.viewPager;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_VIDEO;
import static taewon.navercorp.integratedsns.util.AppController.GIPHY_BASE_URL;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_BASE_URL;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlatformFragment extends Fragment {

    // for managing tokens
    private FavoTokenManager mFavoTokenManager;

    private VerticalCardStackViewPager mViewPager;
    private CardStackAdapter mAdapter;
    private String mPlatformType;
    private ArrayList<FavoFeedData> mDataset = new ArrayList<>();

    private TextView mPlatformName;

    private SimpleDateFormat mStringFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static boolean isInit;

    private static String ARG_PARAM1 = "PLATFORM_TYPE";
    private static int MAX_COUNTS = 10;

    public static PlatformFragment newInstance(String platformType) {

        PlatformFragment fragment = new PlatformFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, platformType);
        fragment.setArguments(args);
        isInit = true;
        return fragment;
    }

    public PlatformFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_platform, container, false);


        if (getArguments() != null) {
            mPlatformType = getArguments().getString(ARG_PARAM1);
        }

        initView(view);
        if (isInit) {
            initData(mPlatformType);
        }


        return view;
    }

    private void initView(View view) {

        mViewPager = (VerticalCardStackViewPager) view.findViewById(viewPager);
        mViewPager.setOffscreenPageLimit(MAX_COUNTS);
        mViewPager.setClipToPadding(false);

        mPlatformName = (TextView) view.findViewById(R.id.textView_platform);
    }

    private void initData(String platformType) {

        // preference
        mFavoTokenManager = FavoTokenManager.getInstance();

        switch (platformType) {

            case PLATFORM_FACEBOOK:
                getGiphyTrendsGifs();
                mPlatformName.setText("GIPHY");
                break;

            case PLATFORM_YOUTUBE:
                getYoutubeTrendVideos();
                mPlatformName.setText("YOUTUBE");

                break;

            case PLATFORM_TWITCH:
                getTwitchTopStreaming();
                mPlatformName.setText("TWITCH");

                break;
        }
    }

    private void getYoutubeTrendVideos() {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchVideoData> call = service.getVideoList(accessToken, "snippet", MAX_COUNTS, null, null, "date", null,  "video", "mostPopular", "KR");
        call.enqueue(new Callback<YoutubeSearchVideoData>() {
            @Override
            public void onResponse(Call<YoutubeSearchVideoData> call, Response<YoutubeSearchVideoData> response) {
                if (response.isSuccessful()) {

                    for (YoutubeSearchVideoData.Item item : response.body().getItems()) {

                        FavoFeedData data = new FavoFeedData();
                        Date date = null;

                        data.setPlatformType(PLATFORM_YOUTUBE);
                        data.setContentsType(CONTENTS_VIDEO);

                        try {
                            date = mDateFormat.parse(item.getSnippet().getPublishedAt());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        data.setPubDate(date);

                        data.setPageId(item.getSnippet().getChannelId());
                        data.setFeedId(item.getId().getVideoId());
//                        data.setProfileImage(profileUrl);
                        data.setUserName(item.getSnippet().getChannelTitle());
                        data.setCreatedTime(mStringFormat.format(date));
                        data.setPicture(item.getSnippet().getThumbnails().getHigh().getUrl());
                        data.setVideoUrl(item.getId().getVideoId());
                        data.setDescription(item.getSnippet().getTitle());

                        mDataset.add(data);
                    }

                    mAdapter = new CardStackAdapter(getChildFragmentManager());
                    mViewPager.setAdapter(mAdapter);

                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to get json for video");
                }
            }

            @Override
            public void onFailure(Call<YoutubeSearchVideoData> call, Throwable t) {
                t.printStackTrace();
                Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to access youtube api server");
            }
        });
    }

    private void getGiphyTrendsGifs() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GIPHY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GiphyService service = retrofit.create(GiphyService.class);
        Call<GiphyImageData> call = service.getGiphyTrendImage(getString(R.string.giphy_api_key), MAX_COUNTS);
        call.enqueue(new Callback<GiphyImageData>() {
            @Override
            public void onResponse(Call<GiphyImageData> call, Response<GiphyImageData> response) {

                if (response.isSuccessful()) {
                    for (GiphyImageData.Info result : response.body().getData()) {

                        Date date = null;
                        FavoFeedData data = new FavoFeedData();

                        data.setPlatformType(PLATFORM_YOUTUBE);
                        data.setContentsType(CONTENTS_VIDEO);

                        try {
                            date = mDateFormat.parse(result.getTrendingDatetime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        data.setPubDate(date);

                        data.setPageId(result.getId());
                        data.setFeedId(result.getId());
//                    data.setProfileImage(result.get);
//                        data.setUserName(result.getUser().getDisplayName());
//                    data.setCreatedTime(mStringFormat.format(date));
                        data.setPicture(result.getImages().getOriginal().getUrl());
                        data.setDescription(result.getTitle());

                        mDataset.add(data);
                    }
                    mAdapter = new CardStackAdapter(getChildFragmentManager());
                    mViewPager.setAdapter(mAdapter);
                } else {

                    Log.e("ERROR_GIPHY", "fail to connect");
                }
            }

            @Override
            public void onFailure(Call<GiphyImageData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getTwitchTopStreaming() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwitchService service = retrofit.create(TwitchService.class);
        Call<TwitchStreamingData> call = service.getTwitchStreams(getString(R.string.twitch_client_id), MAX_COUNTS);
        call.enqueue(new Callback<TwitchStreamingData>() {
            @Override
            public void onResponse(Call<TwitchStreamingData> call, Response<TwitchStreamingData> response) {
                if (response.isSuccessful()) {

                    for (TwitchStreamingData.StreamInfo result : response.body().getData()) {

                        FavoFeedData data = new FavoFeedData();

                        try {
                            data.setPubDate(mDateFormat.parse(result.getStartedAt()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        data.setFeedId(result.getId());
                        data.setPlatformType(PLATFORM_TWITCH);
                        data.setContentsType(CONTENTS_VIDEO);
                        data.setPageId(result.getUserId());
//                        data.setProfileImage(profileUrl);
//                        data.setUserName(userName);
                        data.setCreatedTime(mStringFormat.format(data.getPubDate()));
                        int position = result.getThumbnailUrl().indexOf("{width}");
                        String thumbnail = result.getThumbnailUrl().substring(0, position) + "1280x720.jpg";
                        data.setPicture(thumbnail);
                        data.setVideoUrl(result.getId());
                        data.setDescription(result.getTitle());

                        mDataset.add(data);
                    }

                    mAdapter = new CardStackAdapter(getChildFragmentManager());
                    mViewPager.setAdapter(mAdapter);

                } else {
                    Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to login // " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<TwitchStreamingData> call, Throwable t) {
                Log.e("ERROR_TWITCH", "Feed Fragment >>>>> Fail to login ");
                t.printStackTrace();
            }
        });
    }

    private class CardStackAdapter extends FragmentPagerAdapter {

        public CardStackAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CardStackFragment.newInstance(mDataset.get(position));
        }

        @Override
        public int getCount() {
            return MAX_COUNTS;
        }
    }
}
