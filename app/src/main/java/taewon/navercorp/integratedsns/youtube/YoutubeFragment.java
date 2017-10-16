package taewon.navercorp.integratedsns.youtube;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.YoutubeSubscriptionData;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author 김태원
 * @file YoutubeFragment.java
 * @brief show youtube contents, search & add youtube channels
 * @date 2017.10.07
 */

public class YoutubeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private RecyclerView mYoutubeList;
    private YoutubeListAdapter mAdapter;
    private ArrayList<YoutubeSubscriptionData.Item> mDataset = new ArrayList<>();

    private OnRequestYoutubeTokenListener mCallback;
    private YoutubeHandler mHandler;

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mLayoutDisconnection;
    private Button mConnectYoutube;

    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    private static final int MAX_COUNTS = 10;

    private static final int REQ_REFRESH = 100;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRequestYoutubeTokenListener) context;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR_CALLBACK", "Youtube Fragment >>>>> check callback logic");
        }
    }

    public YoutubeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_youtube, container, false);

        initData();
        initView(view);
        checkToken();

        return view;
    }

    private void initData() {

        // get preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        mHandler = new YoutubeHandler();
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        // view for disconnection
        mLayoutDisconnection = (RelativeLayout) view.findViewById(R.id.layout_disconnection);
        mConnectYoutube = (Button) view.findViewById(R.id.button_connect_youtube);
        mConnectYoutube.setOnClickListener(this);

        // set recyclerView
        mYoutubeList = (RecyclerView) view.findViewById(R.id.recyclerView_youtube);
        mAdapter = new YoutubeListAdapter(getContext(), mDataset);
        mYoutubeList.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mYoutubeList.setLayoutManager(layoutManager);
    }

    // check youtube token state
    private void checkToken() {

        String youtubeToken = mPref.getString(getString(R.string.google_token), "");
        if (!youtubeToken.equals("")) {
            mLayoutDisconnection.setVisibility(View.GONE);
            getSubscriptionList();

        } else {
            mLayoutDisconnection.setVisibility(View.VISIBLE);
        }
    }

    private void getSubscriptionList() {

        // get google credential access token
        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), null));

        // set retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // get 'subscriptions' from youtube data api v3
        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSubscriptionData> call = service.subscriptionList(accessToken, "snippet", MAX_COUNTS, true);
        call.enqueue(new Callback<YoutubeSubscriptionData>() {
            @Override
            public void onResponse(Call<YoutubeSubscriptionData> call, Response<YoutubeSubscriptionData> response) {
                if (response.isSuccessful()) {

                    mDataset.clear();
                    mDataset.addAll(response.body().getItems());
                    mAdapter.notifyDataSetChanged();


                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> Token is expired" + response.toString());

                    // TODO - Google Token Refresh 로직이 구현되기 전까지의 임시방편...
                    mEditor.putString(getString(R.string.google_token), "");
                    mEditor.commit();
                    checkToken();
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<YoutubeSubscriptionData> call, Throwable t) {
                Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> fail to access youtube api server");
                Toast.makeText(getContext(), "Fail to access youtube server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_connect_youtube:
                mCallback.onRequestYoutubeToken(mHandler);
                break;
        }
    }

    @Override
    public void onRefresh() {
        getSubscriptionList();
    }

    private class YoutubeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == REQ_REFRESH){
                getSubscriptionList();
                checkToken();
            }
        }
    }
}
