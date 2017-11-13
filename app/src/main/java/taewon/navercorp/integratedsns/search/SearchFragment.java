package taewon.navercorp.integratedsns.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private EditText mSearch;
    private RecyclerView mSearchList;
    private SearchResultLayout mPageResult, mVideoResult, mPhotoResult;

    private ArrayList<FavoSearchResultData> mPageDataset = new ArrayList<>();
    private ArrayList<FavoSearchResultData> mVideoDataset = new ArrayList<>();
    private ArrayList<FavoSearchResultData> mPhotoDataset = new ArrayList<>();

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
            initData();
            isInit = false;
        }
        return view;
    }

    private void initView(View view) {

        mSearch = (EditText) view.findViewById(R.id.editText_search);
        mPageResult = (SearchResultLayout) view.findViewById(R.id.layout_resultPage);
        mVideoResult = (SearchResultLayout) view.findViewById(R.id.layout_resultVideo);
        mPhotoResult = (SearchResultLayout) view.findViewById(R.id.layout_resultPhoto);

        mPageResult.setView(RESULT_PAGE, getContext(), mPageDataset);
        mVideoResult.setView(RESULT_VIDEO, getContext(), mVideoDataset);
        mPhotoResult.setView(RESULT_PHOTO, getContext(), mPhotoDataset);
    }

    private void initData() {

        mPageDataset.add(new FavoSearchResultData());
        mPageDataset.add(new FavoSearchResultData());
        mPageDataset.add(new FavoSearchResultData());
        mPageDataset.add(new FavoSearchResultData());
        mPageDataset.add(new FavoSearchResultData());
        mPageDataset.add(new FavoSearchResultData());
        mPageResult.getAdapter().notifyDataSetChanged();

        mVideoDataset.add(new FavoSearchResultData());
        mVideoDataset.add(new FavoSearchResultData());
        mVideoDataset.add(new FavoSearchResultData());
        mVideoDataset.add(new FavoSearchResultData());
        mVideoDataset.add(new FavoSearchResultData());
        mVideoDataset.add(new FavoSearchResultData());
        mVideoResult.getAdapter().notifyDataSetChanged();

        mPhotoDataset.add(new FavoSearchResultData());
        mPhotoDataset.add(new FavoSearchResultData());
        mPhotoDataset.add(new FavoSearchResultData());
        mPhotoDataset.add(new FavoSearchResultData());
        mPhotoDataset.add(new FavoSearchResultData());
        mPhotoDataset.add(new FavoSearchResultData());
        mPhotoResult.getAdapter().notifyDataSetChanged();
    }
}
