package taewon.navercorp.integratedsns.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taewon.navercorp.integratedsns.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static boolean isInit;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        isInit = true;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

}
