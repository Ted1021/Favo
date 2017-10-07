package taewon.navercorp.integratedsns.youtube;


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
import taewon.navercorp.integratedsns.model.YoutubeFeedData;

/**
 * @author 김태원
 * @file YoutubeFragment.java
 * @brief show youtube contents, search & add youtube channels
 * @date 2017.10.07
 */

public class YoutubeFragment extends Fragment {

    private RecyclerView mYoutubeList;
    private YoutubeListAdapter mAdapter;
    private ArrayList<YoutubeFeedData> mDataset = new ArrayList<>();

    private static final String YOUTUBE_URL = "https://www.googleapis.com/youtube/v3";
    private static final String YOUTUBE_KEY = "AIzaSyA6VQVXg4TwfzrmZGGFdiehVhlQhsIfETQ";

    public YoutubeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_youtube, container, false);

        initView(view);
        setRecyclerView();
        getYoutubeFeed();

        return view;
    }

    private void initView(View view) {

        mYoutubeList = (RecyclerView) view.findViewById(R.id.recyclerView_youtube);
    }

    private void setRecyclerView() {

        mAdapter = new YoutubeListAdapter(getContext(), mDataset);
        mYoutubeList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mYoutubeList.setLayoutManager(layoutManager);
    }

    private void getYoutubeFeed() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUTUBE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeService service = retrofit.create(YoutubeService.class);
        Call<YoutubeFeedData> call = service.subscriptionList("snippet%2CcontentDetails", true, YOUTUBE_KEY);
        call.enqueue(new Callback<YoutubeFeedData>() {
            @Override
            public void onResponse(Call<YoutubeFeedData> call, Response<YoutubeFeedData> response) {
                if (response.isSuccessful()) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> fail to get json");
                    Toast.makeText(getContext(), "Fail to get json data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<YoutubeFeedData> call, Throwable t) {
                Log.e("ERROR_YOUTUBE", "YoutubeFragment >>>>> fail to access youtube api server");
                Toast.makeText(getContext(), "Fail to access youtube server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
