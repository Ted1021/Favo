package taewon.navercorp.integratedsns.tumblr;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author 김태원
 * @file TumblrFragment.java
 * @brief show tumblr contents, search & add tumblr channels
 * @date 2017.10.13
 */
public class TumblrFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private RecyclerView mRecyclerView;
    private TumblrListAdapter mAdapter;
    private ArrayList mDataset = new ArrayList<>();

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mLayoutDisconnection;
    private Button mConnectTumblr;

    public TumblrFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tumblr, container, false);

        initData();
        initView(view);

        return view;
    }

    private void initData(){

        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    private void initView(View view){

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mLayoutDisconnection = (RelativeLayout) view.findViewById(R.id.layout_disconnection);
        mConnectTumblr = (Button) view.findViewById(R.id.button_connect_tumblr);
        mConnectTumblr.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_tumblr);
        mAdapter = new TumblrListAdapter(getContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.button_connect_tumblr:

                break;
        }
    }

    @Override
    public void onRefresh() {

    }
}
