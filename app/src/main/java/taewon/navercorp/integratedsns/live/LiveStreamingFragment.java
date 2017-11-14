package taewon.navercorp.integratedsns.live;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taewon.navercorp.integratedsns.R;


public class LiveStreamingFragment extends Fragment {

    private static boolean isInit;

    public static LiveStreamingFragment newInstance(){

        LiveStreamingFragment fragment = new LiveStreamingFragment();
        isInit = true;
        return fragment;
    }

    public LiveStreamingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_live_streaming, container, false);

        return view;
    }

}
