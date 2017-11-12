package taewon.navercorp.integratedsns.profile.pin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoMyPinData;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPinFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private Realm mRealm;
    private RecyclerView mSaveFeedList;
    private MyPinListAdapter mAdapter;
    private RealmResults<FavoMyPinData> mDataset;

    private SwipeRefreshLayout mRefreshLayout;

    public MyPinFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_pin, container, false);

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
    }

    private void initView(View view){

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mSaveFeedList = (RecyclerView) view.findViewById(R.id.recyclerView_myFeed);
        mAdapter = new MyPinListAdapter(mDataset, true, getContext());
        mSaveFeedList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSaveFeedList.setLayoutManager(layoutManager);
    }

    @Override
    public void onRefresh() {
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }
}
