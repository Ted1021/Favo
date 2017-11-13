package taewon.navercorp.integratedsns.search;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.TwitchService;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.facebook.FacebookPageInfoData;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;
import taewon.navercorp.integratedsns.model.twitch.TwitchSearchChannelData;
import taewon.navercorp.integratedsns.model.twitch.TwitchStreamingDataV5;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSearchChannelData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSearchVideoData;

import static android.content.Context.MODE_PRIVATE;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_ACCEPT_CODE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_BASE_URL;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements EditText.OnEditorActionListener {

    private SharedPreferences mPref;

    private EditText mSearch;
    private RecyclerView mSearchList;
    private SearchResultLayout mPageResult, mVideoResult, mPhotoResult;

    private ArrayList<FavoSearchResultData> mPageDataset = new ArrayList<>();
    private ArrayList<FavoSearchResultData> mVideoDataset = new ArrayList<>();
    private ArrayList<FavoSearchResultData> mPhotoDataset = new ArrayList<>();

    private String mQuery;

    private static boolean isInit;
    private static final int RESULT_PAGE = 0;
    private static final int RESULT_VIDEO = 1;
    private static final int RESULT_PHOTO = 2;
    private static final int MAX_PAGE_COUNT = 6;
    private static final int MAX_VIDEO_COUNT = 3;
    private static final int MAX_PHOTO_COUNT = 4;


    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        isInit = true;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initView(view);
        initData();

        Log.d("CHECK_TOKEN", mPref.getString(getString(R.string.twitch_token), ""));
        if (isInit) {
        }
        return view;
    }

    private void initView(View view) {
        mPageResult = (SearchResultLayout) view.findViewById(R.id.layout_resultPage);
        mVideoResult = (SearchResultLayout) view.findViewById(R.id.layout_resultVideo);
        mPhotoResult = (SearchResultLayout) view.findViewById(R.id.layout_resultPhoto);

        mPageResult.setView(RESULT_PAGE, getContext(), mPageDataset);
        mVideoResult.setView(RESULT_VIDEO, getContext(), mVideoDataset);
        mPhotoResult.setView(RESULT_PHOTO, getContext(), mPhotoDataset);

        mSearch = (EditText) view.findViewById(R.id.editText_search);
        mSearch.setOnEditorActionListener(this);
    }

    private void initData() {

        // init preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (event == null) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        } else if (event.getAction() == KeyEvent.ACTION_UP) {

            switch (actionId) {
                case EditorInfo.IME_ACTION_UNSPECIFIED:
                case EditorInfo.IME_ACTION_DONE:
                case EditorInfo.IME_ACTION_SEARCH:
                case EditorInfo.IME_ACTION_GO:
                case EditorInfo.IME_ACTION_NEXT:

                    mQuery = mSearch.getText().toString();

                    loadPageSearchResult();
                    loadVideoSearchResult();
                    loadPhotoSearchResult();

                    return true;
            }
        }
        return false;
    }

    // TODO - have to implement checking token logic
    private void loadPageSearchResult() {

        mPageDataset.clear();
        mPageResult.getAdapter().notifyDataSetChanged();

        searchFacebookPage();
        getYoutubeChannel();
        getTwitchChannel();
    }

    private void loadVideoSearchResult() {

        mVideoDataset.clear();
        mVideoResult.getAdapter().notifyDataSetChanged();

        getYoutubeVideo();
        getTwitchVideo();
    }

    private void loadPhotoSearchResult() {

        mPhotoDataset.clear();
        mPhotoResult.getAdapter().notifyDataSetChanged();

    }

    private void searchFacebookPage() {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/search",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() == null) {
                            try {
                                JSONArray result = response.getJSONObject().getJSONArray("data");
                                for (int i = 0; i < result.length(); i++) {

                                    FacebookPageInfoData temp = new Gson().fromJson(result.get(i).toString(), FacebookPageInfoData.class);
                                    FavoSearchResultData data = new FavoSearchResultData();

                                    data.setPlatformType(PLATFORM_FACEBOOK);
                                    data.setPageId(temp.getId());
                                    data.setProfileImage(temp.getPicture().getData().getUrl());
                                    data.setUserName(temp.getName());
                                    data.setDescription(temp.getAbout());

                                    mPageDataset.add(data);
                                }
                                mPageResult.getAdapter().notifyDataSetChanged();
                                mPageResult.getLayoutManager().scrollToPosition(mPageDataset.size() - 1);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("ERROR_SEARCH", response.toString());
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("q", mQuery);
        parameters.putString("type", "page");
        parameters.putString("limit", MAX_PAGE_COUNT + "");
        parameters.putString("fields", "name,about,picture.height(1024){url},cover.height(1024){source},fan_count,description");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getYoutubeChannel() {

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchChannelData> call = service.searchChannelList(accessToken, "Snippet", MAX_PAGE_COUNT, "viewCount", "channel", mQuery);
        call.enqueue(new Callback<YoutubeSearchChannelData>() {
            @Override
            public void onResponse(Call<YoutubeSearchChannelData> call, Response<YoutubeSearchChannelData> response) {
                if (response.isSuccessful()) {
                    YoutubeSearchChannelData result = response.body();
                    for (YoutubeSearchChannelData.Item item : result.getItems()) {
                        FavoSearchResultData data = new FavoSearchResultData();

                        data.setPlatformType(PLATFORM_YOUTUBE);
                        data.setPageId(item.getSnippet().getChannelId());
                        data.setProfileImage(item.getSnippet().getThumbnails().getHigh().getUrl());
                        data.setUserName(item.getSnippet().getChannelTitle());
                        data.setDescription(item.getSnippet().getDescription());

                        mPageDataset.add(data);
                    }

                    mPageResult.getAdapter().notifyDataSetChanged();
                    mPageResult.getLayoutManager().scrollToPosition(mPageDataset.size() - 1);
                } else {
                    Log.e("ERROR_SEARCH", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<YoutubeSearchChannelData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getTwitchChannel() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwitchService service = retrofit.create(TwitchService.class);
        Call<TwitchSearchChannelData> call = service.searchTwitchChannel(TWITCH_ACCEPT_CODE, getString(R.string.twitch_client_id), mQuery, MAX_PAGE_COUNT);
        call.enqueue(new Callback<TwitchSearchChannelData>() {
            @Override
            public void onResponse(Call<TwitchSearchChannelData> call, Response<TwitchSearchChannelData> response) {
                if (response.isSuccessful()) {
                    TwitchSearchChannelData result = response.body();
                    for (TwitchSearchChannelData.Channel item : result.getChannels()) {

                        FavoSearchResultData data = new FavoSearchResultData();

                        data.setPlatformType(PLATFORM_TWITCH);
                        data.setPageId(item.getId().toString());
                        data.setProfileImage(item.getLogo());
                        data.setUserName(item.getName());
                        data.setDescription(item.getDescription());

                        mPageDataset.add(data);
                    }
                    mPageResult.getAdapter().notifyDataSetChanged();
                    mPageResult.getLayoutManager().scrollToPosition(mPageDataset.size() - 1);

                } else {
                    Log.e("ERROR_SEARCH", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<TwitchSearchChannelData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getYoutubeVideo() {

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchVideoData> call = service.getVideoList(accessToken, "snippet", MAX_VIDEO_COUNT, null, mQuery, "viewCount", "video", null, "KR");
        call.enqueue(new Callback<YoutubeSearchVideoData>() {
            @Override
            public void onResponse(Call<YoutubeSearchVideoData> call, Response<YoutubeSearchVideoData> response) {
                if (response.isSuccessful()) {
                    YoutubeSearchVideoData result = response.body();
                    for (YoutubeSearchVideoData.Item item : result.getItems()) {

                        FavoSearchResultData data = new FavoSearchResultData();

                        data.setPlatformType(PLATFORM_YOUTUBE);
                        data.setUserName(item.getSnippet().getChannelTitle());
                        data.setDescription(item.getSnippet().getTitle());
                        data.setPicture(item.getSnippet().getThumbnails().getHigh().getUrl());

                        data.setFeedId(item.getId().getVideoId());
                        data.setPageId(item.getSnippet().getChannelId());
                        data.setVideoUrl(item.getId().getVideoId());

                        mVideoDataset.add(data);
                    }
                    mVideoResult.getAdapter().notifyDataSetChanged();

                } else {
                    Log.e("ERROR_SEARCH", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<YoutubeSearchVideoData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getTwitchVideo() {
        Log.d("CHECK_SEARCH", " >>>>>>>>>>>> in 12 121 ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("CHECK_SEARCH", " >>>>>>>>>>>> in 12 112312312312321 ");
        TwitchService service = retrofit.create(TwitchService.class);
        Call<TwitchStreamingDataV5> call = service.searchTwitchStreams(TWITCH_ACCEPT_CODE, getString(R.string.twitch_client_id), mQuery, MAX_VIDEO_COUNT);
        Log.d("CHECK_SEARCH", " >>>>>>>>>>>> in 12 112312312312321123123123123123123123 ");
        call.enqueue(new Callback<TwitchStreamingDataV5>() {
            @Override
            public void onResponse(Call<TwitchStreamingDataV5> call, Response<TwitchStreamingDataV5> response) {
                Log.d("CHECK_SEARCH", " >>>>>>>>>>>> in");
                if(response.isSuccessful()){
                    TwitchStreamingDataV5 result = response.body();

                    for(TwitchStreamingDataV5.Stream item : result.getStreams()){

                        FavoSearchResultData data = new FavoSearchResultData();

                        data.setPlatformType(PLATFORM_TWITCH);
                        data.setUserName(item.getChannel().getName());
                        data.setDescription(item.getChannel().getStatus());
                        data.setPicture(item.getPreview().getLarge());

                        data.setFeedId(item.getChannel().getName());
                        data.setPageId(item.getChannel().getId()+"");
                        data.setVideoUrl(item.getChannel().getName());

                        mVideoDataset.add(data);
                    }
                    mVideoResult.getAdapter().notifyDataSetChanged();

                } else {
                    Log.e("ERROR_SEARCH", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<TwitchStreamingDataV5> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
