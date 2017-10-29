package taewon.navercorp.integratedsns.page.facebook;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.FeedListAdapter;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.feed.FacebookFeedData;
import taewon.navercorp.integratedsns.model.feed.FavoFeedData;
import taewon.navercorp.integratedsns.model.feed.YoutubeSearchVideoData;

import static android.content.Context.MODE_PRIVATE;

public class PageFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences mPref;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mPageFeedList;

    private String mPageId;
    private int mPlatformType;
    private String mProfileImage;

    private Vector<FavoFeedData> mDataset = new Vector<>();
    private FeedListAdapter mAdapter;

    SimpleDateFormat mDateConverter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    private static final int MAX_COUNTS = 25;

    private static final String ARG_PARAM1 = "PAGE_ID";
    private static final String ARG_PARAM2 = "PLATFORM_TYPE";
    private static final String ARG_PARAM3 = "PROFILE_IMAGE";

    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI = 3;

    private static final int PLATFORM_FACEBOOK = 1;
    private static final int PLATFORM_YOUTUBE = 2;
    private static final int PLATFORM_PINTEREST = 3;

    public static PageFeedFragment newInstance(String param1, int param2, String param3) {

        PageFeedFragment fragment = new PageFeedFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPageId = getArguments().getString(ARG_PARAM1);
            mPlatformType = getArguments().getInt(ARG_PARAM2);
            mProfileImage = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page_feed, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initData() {

        // init preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);

        switch (mPlatformType) {

            case PLATFORM_FACEBOOK:
                getFacebookPageFeed();
                break;

            case PLATFORM_YOUTUBE:
                getYoutubeChannelFeed();
                break;
        }
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);

        mPageFeedList = (RecyclerView) view.findViewById(R.id.recyclerView_feed);
        mAdapter = new FeedListAdapter(getContext(), mDataset);
        mPageFeedList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mPageFeedList.setLayoutManager(layoutManager);
    }

    private void getFacebookPageFeed() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String path = String.format("/%s/feed", mPageId);
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                path,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response.getError() == null) {
                            try {

                                FacebookFeedData result = new Gson().fromJson(response.getJSONObject().toString(), FacebookFeedData.class);
                                FacebookFeedData.ArticleData article;
                                for (int i = 0; i < result.getData().size(); i++) {

                                    article = result.getData().get(i);
                                    FavoFeedData data = new FavoFeedData();

                                    data.setPlatformType(PLATFORM_FACEBOOK);
                                    if (!(article.getSource() == null)) {
                                        data.setContentsType(CONTENTS_VIDEO);
                                        data.setVideoUrl(article.getSource());
                                    } else {
                                        data.setContentsType(CONTENTS_IMAGE);
                                    }
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    Date date = format.parse(article.getCreatedTime());
                                    data.setPubDate(date);

                                    data.setPageId(article.getFrom().getId());
                                    data.setFeedId(article.getId());
                                    data.setProfileImage(article.getFrom().getPicture().getProfileData().getUrl());
                                    data.setUserName(article.getFrom().getName());
                                    data.setCreatedTime(mFormat.format(date));
                                    data.setPicture(article.getFullPicture());
                                    data.setLink(article.getLink());
                                    data.setDescription(article.getMessage());
                                    data.setLikeCount(article.getLikes().getSummary().getTotalCount());
                                    data.setCommentCount(article.getComments().getSummary().getTotalCount());

                                    mDataset.add(data);
                                }
                                mAdapter.notifyDataSetChanged();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            mRefreshLayout.setRefreshing(false);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "link,created_time,from{name, picture.height(1024){url}},message,description,full_picture,id,likes.limit(0).summary(true),comments.limit(0).summary(true),source");
        parameters.putString("limit", "25");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getYoutubeChannelFeed() {

        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), ""));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSearchVideoData> call = service.getVideoList(accessToken, "snippet", MAX_COUNTS, mPageId, "date", "video");
        call.enqueue(new Callback<YoutubeSearchVideoData>() {
            @Override
            public void onResponse(Call<YoutubeSearchVideoData> call, Response<YoutubeSearchVideoData> response) {
                if (response.isSuccessful()) {

                    for (YoutubeSearchVideoData.Item item : response.body().getItems()) {

                        FavoFeedData data = new FavoFeedData();
                        Date date = null;

                        data.setPlatformType(PLATFORM_YOUTUBE);
                        data.setContentsType(CONTENTS_VIDEO);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        try {
                            date = format.parse(item.getSnippet().getPublishedAt());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        data.setPubDate(date);

                        data.setPageId(item.getSnippet().getChannelId());
                        data.setFeedId(item.getId().getVideoId());
                        data.setProfileImage(mProfileImage);
                        data.setUserName(item.getSnippet().getChannelTitle());
                        data.setCreatedTime(mFormat.format(date));
                        data.setPicture(item.getSnippet().getThumbnails().getHigh().getUrl());
                        data.setVideoUrl(item.getId().getVideoId());
                        data.setDescription(item.getSnippet().getTitle());

                        mDataset.add(data);
                    }
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);

                } else {
                    mRefreshLayout.setRefreshing(false);
                    Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to get json for video");
                }
            }

            @Override
            public void onFailure(Call<YoutubeSearchVideoData> call, Throwable t) {
                mRefreshLayout.setRefreshing(false);
                t.printStackTrace();
                Log.e("ERROR_YOUTUBE", "YoutubeDetailActivity >>>>> Fail to access youtube api server");
            }
        });
    }

    @Override
    public void onRefresh() {

        mDataset.clear();
        initData();
    }
}
