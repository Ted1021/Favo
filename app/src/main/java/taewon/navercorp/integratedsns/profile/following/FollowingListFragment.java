package taewon.navercorp.integratedsns.profile.following;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taewon.navercorp.integratedsns.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingListFragment extends Fragment {


    public FollowingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following_list, container, false);

        return view;
    }

    private void initView(View view){


    }

}
