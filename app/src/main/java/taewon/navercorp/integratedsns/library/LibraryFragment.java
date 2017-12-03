package taewon.navercorp.integratedsns.library;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoMyPinData;
import taewon.navercorp.integratedsns.search.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private Realm mRealm;
    private RecyclerView mSaveFeedList;
    private LibraryListAdapter mAdapter;
    private RealmResults<FavoMyPinData> mDataset;
    private ImageButton mSearch;
    private SwipeRefreshLayout mRefreshLayout;

    private static boolean isInit;

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        isInit = true;
        return fragment;
    }

    public LibraryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        if(isInit == true){
        }
        initData();
        initView(view);

        return view;
    }

    @Override
    public void onDestroyView() {
        mRealm.close();
        super.onDestroyView();
    }

    private void initData(){

        // init Realm Instance
        mRealm = Realm.getDefaultInstance();

        // init myPin data
        RealmQuery<FavoMyPinData> query = mRealm.where(FavoMyPinData.class);
        mDataset = query.findAll();
        if(mDataset.size() == 0){

        }
    }

    private void initView(View view){

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mSaveFeedList = (RecyclerView) view.findViewById(R.id.recyclerView_myFeed);
        mAdapter = new LibraryListAdapter(mDataset, true, getContext());
        mSaveFeedList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSaveFeedList.setLayoutManager(layoutManager);

        mSearch = (ImageButton) view.findViewById(R.id.button_search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }
}
