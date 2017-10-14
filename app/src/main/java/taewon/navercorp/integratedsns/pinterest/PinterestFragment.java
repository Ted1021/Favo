package taewon.navercorp.integratedsns.pinterest;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author 김태원
 * @file TumblrFragment.java
 * @brief show tumblr contents, search & add tumblr channels
 * @date 2017.10.13
 */
public class PinterestFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String BOARD_FIELDS = "id,name";
    private static final String PIN_FIELDS = "created_at,creator,id,image, media, note,original_link";

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private RecyclerView mRecyclerView;
    private PinterestListAdapter mAdapter;
    private ArrayList<PDKPin> mDataset = new ArrayList<>();

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mLayoutDisconnection;
    private Button mConnectTumblr;

    private PDKClient mPinterestClient;

    public PinterestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pinterest, container, false);

        initData();
        initView(view);
        checkToken();

        return view;
    }

    private void initData() {

        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        PDKClient.configureInstance(getContext(), getString(R.string.pinterest_app_id));
        mPinterestClient = PDKClient.getInstance();
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mLayoutDisconnection = (RelativeLayout) view.findViewById(R.id.layout_disconnection);
        mConnectTumblr = (Button) view.findViewById(R.id.button_connect_tumblr);
        mConnectTumblr.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_tumblr);
        mAdapter = new PinterestListAdapter(getContext(), mDataset);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void checkToken() {

        String pinterestToken = mPref.getString(getString(R.string.pinterest_token), "");
        if (!pinterestToken.equals("")) {
            mLayoutDisconnection.setVisibility(View.GONE);
            getFollowingBoards();

        } else {
            mLayoutDisconnection.setVisibility(View.VISIBLE);
        }
    }

    private void getFollowingBoards() {

        mPinterestClient.getMyFollowedBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);

                for (PDKBoard board : response.getBoardList()) {

                    Log.d("CHECK_DATA", "Pinterest Fragment >>>>> " + board.getUid() + " " + board.getName());
//                    new GetFollowingPins().execute(board.getUid());

                    mPinterestClient.getBoardPins(board.getUid(), PIN_FIELDS, new PDKCallback() {
                        @Override
                        public void onSuccess(PDKResponse response) {
                            super.onSuccess(response);

                            Log.d("CHECK_PIN", "Pinterest Fragment >>>>> " +response.getData());
                            for (PDKPin pin : response.getPinList()) {
                                Log.d("CHECK_PIN", "Pinterest Fragment >>>>> " + pin.getUser().getFirstName());
                                Log.d("CHECK_PIN", "Pinterest Fragment >>>>> " + pin.getImageUrl());
                                Log.d("CHECK_PIN", "Pinterest Fragment >>>>> " + pin.getNote());
                            }
                        }

                        @Override
                        public void onFailure(PDKException exception) {
                            super.onFailure(exception);
                            exception.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
            }
        });
    }

    private class GetFollowingPins extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            mPinterestClient.getBoardPins(params[0], PIN_FIELDS, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    super.onSuccess(response);

                    for (PDKPin pin : response.getPinList()) {
                        Log.d("CHECK_PIN", "Pinterest Fragment >>>>> " + pin.getUser().getFirstName());
                        Log.d("CHECK_PIN", "Pinterest Fragment >>>>> " + pin.getImageUrl());
                        Log.d("CHECK_PIN", "Pinterest Fragment >>>>> " + pin.getNote());
                    }
                }

                @Override
                public void onFailure(PDKException exception) {
                    super.onFailure(exception);
                    exception.printStackTrace();
                }
            });

            return null;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_connect_tumblr:

                break;
        }
    }

    @Override
    public void onRefresh() {

    }
}
