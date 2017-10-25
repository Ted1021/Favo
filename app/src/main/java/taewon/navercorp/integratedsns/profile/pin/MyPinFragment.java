package taewon.navercorp.integratedsns.profile.pin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taewon.navercorp.integratedsns.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPinFragment extends Fragment {


    public MyPinFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_pin, container, false);
        return view;
    }

}
