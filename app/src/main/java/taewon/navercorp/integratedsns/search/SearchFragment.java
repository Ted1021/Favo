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
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.facebook.FacebookPageInfoData;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSearchChannelData;

import static android.content.Context.MODE_PRIVATE;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
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
    private static final int MAX_COUNT = 6;

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
        parameters.putString("limit", MAX_COUNT + "");
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

    private void getYoutubeVideo() {


    }

    private void getTwitchVideo() {


    }

}
