package taewon.navercorp.integratedsns.search;


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

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.facebook.FacebookPageInfoData;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements EditText.OnEditorActionListener {

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

        if (isInit) {
//            initData();
            isInit = false;
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

        mPageResult.getAdapter().notifyDataSetChanged();
        mPageResult.getLayoutManager().scrollToPosition(mPageDataset.size() - 1);

        mVideoResult.getAdapter().notifyDataSetChanged();

        mPhotoResult.getAdapter().notifyDataSetChanged();
        mPhotoResult.getLayoutManager().scrollToPosition(mPhotoDataset.size() - 1);
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

                    Log.d("CHECK_SEARCH", " >>>>> keyboard");
                    mQuery = mSearch.getText().toString();

                    loadPageSearchResult();
                    loadVideoSearchResult();
                    loadPhotoSearchResult();

                    return true;
            }
        }
        return false;
    }

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
                                Log.d("CHECK_SEARCH", " >>>>> search");
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
        parameters.putString("limit", "6");
        parameters.putString("fields", "name,about,picture.height(1024){url},cover.height(1024){source},fan_count,description");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getYoutubeChannel() {

    }

    private void getYoutubeVideo() {


    }

    private void getTwitchVideo() {


    }

}
