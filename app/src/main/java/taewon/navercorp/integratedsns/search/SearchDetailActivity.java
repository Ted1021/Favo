package taewon.navercorp.integratedsns.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import taewon.navercorp.integratedsns.util.FavoTokenManager;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.RESULT_PAGE;
import static taewon.navercorp.integratedsns.util.AppController.RESULT_PHOTO;
import static taewon.navercorp.integratedsns.util.AppController.RESULT_VIDEO;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_ACCEPT_CODE;
import static taewon.navercorp.integratedsns.util.AppController.TWITCH_BASE_URL;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

public class SearchDetailActivity extends AppCompatActivity {

    private FavoTokenManager mFavoTokenManager;

    private RecyclerView mSearchDetailList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<FavoSearchResultData> mDataset = new ArrayList<>();

    private int mResultType;
    private String mQuery;

    private static final int MAX_COUNT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);

        initData();
        initView();
        getSearchResult();
    }

    private void initData() {

        mFavoTokenManager = FavoTokenManager.getInstance();

        mResultType = getIntent().getIntExtra("RESULT_TYPE", 0);
        mQuery = getIntent().getStringExtra("QUERY");
    }

    private void getSearchResult() {

        mDataset.clear();
        mAdapter.notifyDataSetChanged();

        switch (mResultType) {

            case RESULT_PAGE:
                loadPageSearchResult();
                break;

            case RESULT_VIDEO:
                loadVideoSearchResult();
                break;

            case RESULT_PHOTO:
                loadPhotoSearchResult();
                break;
        }
    }

    private void initView() {
        switch (mResultType) {

            case RESULT_PAGE:
                mAdapter = new SearchDetailListAdapter(this, mDataset, RESULT_PAGE);
                mLayoutManager = new LinearLayoutManager(this);
                break;

            case RESULT_VIDEO:
                mAdapter = new SearchDetailListAdapter(this, mDataset, RESULT_VIDEO);
                mLayoutManager = new LinearLayoutManager(this);
                break;

            case RESULT_PHOTO:
                mAdapter = new SearchDetailListAdapter(this, mDataset, RESULT_PHOTO);
                mLayoutManager = new GridLayoutManager(this, 2);
                break;
        }
        mSearchDetailList = (RecyclerView) findViewById(R.id.recyclerView_searchDetail);
        mSearchDetailList.setAdapter(mAdapter);
        mSearchDetailList.setLayoutManager(mLayoutManager);
    }

    private void loadPageSearchResult() {

        searchFacebookPage();
        getYoutubeChannel();
        getTwitchChannel();
    }

    private void loadVideoSearchResult() {

        getYoutubeVideo();
        getTwitchVideo();
    }

    private void loadPhotoSearchResult() {

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

                                    mDataset.add(data);
                                }
                                mAdapter.notifyDataSetChanged();

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
        parameters.putString("limit", MAX_COUNT + "");
        parameters.putString("fields", "name,about,picture.height(1024){url},cover.height(1024){source},fan_count,description");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getYoutubeChannel() {

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchChannelData> call = service.searchChannelList(accessToken, "Snippet", MAX_COUNT, "viewCount", "channel", mQuery);
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

                        mDataset.add(data);
                    }
                    mAdapter.notifyDataSetChanged();

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
        Call<TwitchSearchChannelData> call = service.searchTwitchChannel(TWITCH_ACCEPT_CODE, getString(R.string.twitch_client_id), mQuery, MAX_COUNT);
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

                        mDataset.add(data);
                    }
                    mAdapter.notifyDataSetChanged();

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

        String accessToken = String.format("Bearer " + mFavoTokenManager.getCurrentToken(PLATFORM_YOUTUBE));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchVideoData> call = service.getVideoList(accessToken, "snippet", MAX_COUNT, null, null, mQuery, "viewCount", "video", null, "KR");
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

                        mDataset.add(data);
                    }
                    mAdapter.notifyDataSetChanged();
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwitchService service = retrofit.create(TwitchService.class);
        Call<TwitchStreamingDataV5> call = service.searchTwitchStreams(TWITCH_ACCEPT_CODE, getString(R.string.twitch_client_id), mQuery, MAX_COUNT);
        call.enqueue(new Callback<TwitchStreamingDataV5>() {
            @Override
            public void onResponse(Call<TwitchStreamingDataV5> call, Response<TwitchStreamingDataV5> response) {
                Log.d("CHECK_SEARCH", " >>>>>>>>>>>> in");
                if (response.isSuccessful()) {
                    TwitchStreamingDataV5 result = response.body();

                    for (TwitchStreamingDataV5.Stream item : result.getStreams()) {

                        FavoSearchResultData data = new FavoSearchResultData();

                        data.setPlatformType(PLATFORM_TWITCH);
                        data.setUserName(item.getChannel().getName());
                        data.setDescription(item.getChannel().getStatus());
                        data.setPicture(item.getPreview().getLarge());

                        data.setFeedId(item.getChannel().getName());
                        data.setPageId(item.getChannel().getId() + "");
                        data.setVideoUrl(item.getChannel().getName());

                        mDataset.add(data);
                    }
                    mAdapter.notifyDataSetChanged();

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
