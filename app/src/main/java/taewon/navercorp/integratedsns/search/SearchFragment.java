package taewon.navercorp.integratedsns.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoPageSearchResultData;
import taewon.navercorp.integratedsns.model.favo.FavoPhotoSearchResultData;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;
import taewon.navercorp.integratedsns.model.favo.FavoVideoSearchResultData;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private EditText mSearch;
    private RecyclerView mSearchList;
    private SearchCategoryAdapter mAdapter;
    private FavoSearchResultData mDataset = new FavoSearchResultData();
    private ArrayList<FavoPageSearchResultData> page1 = new ArrayList<>();
    private ArrayList<FavoVideoSearchResultData> video1 = new ArrayList<>();
    private ArrayList<FavoPhotoSearchResultData> photo1 = new ArrayList<>();

    private static boolean isInit;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        isInit = true;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initView(view);

        mDataset = new FavoSearchResultData();

        page1.add(new FavoPageSearchResultData());
        page1.add(new FavoPageSearchResultData());
        page1.add(new FavoPageSearchResultData());

        video1.add(new FavoVideoSearchResultData());
        video1.add(new FavoVideoSearchResultData());
        video1.add(new FavoVideoSearchResultData());

        photo1.add(new FavoPhotoSearchResultData());
        photo1.add(new FavoPhotoSearchResultData());
        photo1.add(new FavoPhotoSearchResultData());

        mDataset.setPageSearchResultDataset(page1);
        mDataset.setVideoSearchResultDataset(video1);
        mDataset.setPhotoSearchResultDataset(photo1);

        mAdapter.notifyDataSetChanged();
        return view;
    }

    private void initView(View view){

        mSearch = (EditText) view.findViewById(R.id.editText_search);

        mSearchList = (RecyclerView) view.findViewById(R.id.recyclerView_searchList);
        mAdapter = new SearchCategoryAdapter(getContext(), mDataset);
        mSearchList.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSearchList.setLayoutManager(layoutManager);
    }

    private void checkToken(){

    }

}
