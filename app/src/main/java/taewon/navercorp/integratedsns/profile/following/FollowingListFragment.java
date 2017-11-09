package taewon.navercorp.integratedsns.profile.following;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.interfaces.YoutubeService;
import taewon.navercorp.integratedsns.model.FollowingInfoData;
import taewon.navercorp.integratedsns.model.feed.YoutubeSubscriptionData;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;
import static taewon.navercorp.integratedsns.util.AppController.YOUTUBE_BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private PDKClient mPinterestClient;

    private BroadcastReceiver mTokenUpdateReceiver;

    private Spinner mSpinner;
    private ParallaxRecyclerView mFollowingList;
    private FollowingListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mLayoutDisconnection;

    private ArrayList<FollowingInfoData> mDataset = new ArrayList<>();

    private String mFacebookToken;
    private String mGoogleToken;
    private String mPinterestToken;

    private int mCurrentPlatform = 1;

    private static final String BOARD_FIELDS = "id,name, created_at, creator, image, url";
    private static final String PIN_FIELDS = "created_at,creator,id,image, media,note,original_link";
    private static final int MAX_COUNTS = 10;

    public FollowingListFragment() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // destroy broadcast receiver along with fragment life cycle
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTokenUpdateReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_following_list, container, false);

        initData();
        initView(view);

        return view;
    }

    private void initData() {

        // init update token status receiver
        mTokenUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setFollowingList(mCurrentPlatform);
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTokenUpdateReceiver, new IntentFilter(getString(R.string.update_token_status)));

        // init preference
        mPref = getContext().getSharedPreferences(getString(R.string.tokens), Context.MODE_PRIVATE);
        mEditor = mPref.edit();

        // init pinterest client
        PDKClient.configureInstance(getContext(), getString(R.string.pinterest_app_id));
        mPinterestClient = PDKClient.getInstance();
    }

    private void initView(View view) {

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPlatform = position + 1;
                setFollowingList(mCurrentPlatform);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // view for disconnection
        mLayoutDisconnection = (RelativeLayout) view.findViewById(R.id.layout_disconnection);

        mFollowingList = (ParallaxRecyclerView) view.findViewById(R.id.recyclerView_following);
        mFollowingList.setHasFixedSize(true);

        mAdapter = new FollowingListAdapter(getContext(), mDataset);
        mFollowingList.setAdapter(mAdapter);

//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFollowingList.setLayoutManager(layoutManager);
    }

    private void setFollowingList(int position) {

        mFacebookToken = mPref.getString(getString(R.string.facebook_token), "");
        mGoogleToken = mPref.getString(getString(R.string.google_token), "");
        mPinterestToken = mPref.getString(getString(R.string.pinterest_token), "");

        mLayoutDisconnection.setVisibility(View.VISIBLE);
        switch (position) {

            case PLATFORM_FACEBOOK:
                if (!mFacebookToken.equals("")) {
                    mLayoutDisconnection.setVisibility(View.GONE);
                    getFacebookUserPages();
                }
                break;

            case PLATFORM_YOUTUBE:
                if (!mGoogleToken.equals("")) {
                    mLayoutDisconnection.setVisibility(View.GONE);
                    getYoutubeSubscriptionList();
                }
                break;

            case PLATFORM_PINTEREST:
                if (!mPinterestToken.equals("")) {
                    mLayoutDisconnection.setVisibility(View.GONE);
                    getPinterestFollowingBoards();
                }
                break;
        }
    }

    // Facebook API Call
    private void getFacebookUserPages() {

        mRefreshLayout.setRefreshing(true);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/likes",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() == null) {

                            try {

                                JSONArray results = response.getJSONObject().getJSONArray("data");
                                JSONObject pageInfo;

                                for (int i = 0; i < results.length(); i++) {
                                    pageInfo = results.getJSONObject(i);
                                    FollowingInfoData data = new FollowingInfoData();

                                    data.setUserName(pageInfo.getString("name"));
                                    data.setProfile(pageInfo.getJSONObject("picture").getJSONObject("data").getString("url"));
                                    data.set_id(pageInfo.getString("id"));
                                    data.setPlatformType(PLATFORM_FACEBOOK);

                                    mDataset.add(data);
                                }
                                mAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e(getClass().getName(), " >>>>> getFacebookUserPages() " + response.getError().getErrorMessage());
                        }
                        mRefreshLayout.setRefreshing(false);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, picture.height(1024){url},id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    // Youtube API Call
    private void getYoutubeSubscriptionList() {

        mRefreshLayout.setRefreshing(true);

        // get google credential access token
        String accessToken = String.format("Bearer " + mPref.getString(getString(R.string.google_token), null));

        // set retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // get 'subscriptions' from youtube data api v3
        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeSubscriptionData> call = service.getSubscriptionList(accessToken, "snippet", MAX_COUNTS, true);
        call.enqueue(new Callback<YoutubeSubscriptionData>() {
            @Override
            public void onResponse(Call<YoutubeSubscriptionData> call, Response<YoutubeSubscriptionData> response) {
                if (response.isSuccessful()) {

                    for (YoutubeSubscriptionData.Item item : response.body().getItems()) {
                        FollowingInfoData data = new FollowingInfoData();

                        data.setUserName(item.getSnippet().getTitle());
                        data.setProfile(item.getSnippet().getThumbnails().getHigh().getUrl());
                        data.set_id(item.getSnippet().getResourceId().getChannelId());
                        data.setPlatformType(PLATFORM_YOUTUBE);

                        mDataset.add(data);
                    }
                    mAdapter.notifyDataSetChanged();

                } else {
                    Log.e(getClass().getName(), " >>>>> Token is expired" + response.toString());

                    mEditor.putString(getString(R.string.google_token), "");
                    mEditor.commit();
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<YoutubeSubscriptionData> call, Throwable t) {
                Log.e(getClass().getName(), " >>>>> fail to access youtube api server");
                Toast.makeText(getContext(), "Fail to access youtube server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Pinterest API Call
    private void getPinterestFollowingBoards() {

        mRefreshLayout.setRefreshing(true);
        mPinterestClient.getMyFollowedBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);

                for (PDKBoard board : response.getBoardList()) {

                    FollowingInfoData data = new FollowingInfoData();

                    data.setUserName(board.getName());
                    data.setProfile(board.getImageUrl());

                    mDataset.add(data);
                }
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {

        mDataset.clear();
        mAdapter.notifyDataSetChanged();
        setFollowingList(mCurrentPlatform);
    }
}
