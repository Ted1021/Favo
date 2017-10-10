package taewon.navercorp.integratedsns.youtube;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class YoutubeFragment extends Fragment {

    private RecyclerView mYoutubeList;
    private YoutubeListAdapter mAdapter;
    private ArrayList<YoutubeSubscriptionData.Item> mDataset = new ArrayList<>();

    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    private static final int MAX_COUNTS = 20;

    public YoutubeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_youtube, container, false);

        initView(view);
        setRecyclerView();
        getSubscriptionList();

        return view;
    }

    private void initView(View view) {

        mYoutubeList = (RecyclerView) view.findViewById(R.id.recyclerView_youtube);
    }

    private void setRecyclerView() {

        // set adapter
        mAdapter = new YoutubeListAdapter(getContext(), mDataset);
        mYoutubeList.setAdapter(mAdapter);

        // set layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mYoutubeList.setLayoutManager(layoutManager);
    }

    private void getSubscriptionList() {

        // get google credential access token
        SharedPreferences pref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        String accessToken = String.format("Bearer " + pref.getString(getString(R.string.google_token), null));

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

                    mDataset.addAll(response.body().getItems());
                    mAdapter.notifyDataSetChanged();

                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> fail to get json from youtube" + response.toString());
                    Toast.makeText(getContext(), "Fail to get json data from youtube", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<YoutubeSubscriptionData> call, Throwable t) {
                Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> fail to access youtube api server");
                Toast.makeText(getContext(), "Fail to access youtube server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
