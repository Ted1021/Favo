package taewon.navercorp.integratedsns.today;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardStackFragment extends Fragment implements View.OnClickListener {

    private FavoFeedData data;

    private GestureDetector mGestureDetector;

    // common components
    private TextView mUserName, mDescription;
    private ImageView mPicture;
    private Button mShare, mMore;

    private static String ARG_PARM1 = "FAVO_DATA";

    public static CardStackFragment newInstance(FavoFeedData data) {

        CardStackFragment fragment = new CardStackFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARM1, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CardStackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_stack, container, false);

        if (getArguments() != null) {
            data = (FavoFeedData) getArguments().getSerializable(ARG_PARM1);
        }

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {

        mUserName = (TextView) view.findViewById(R.id.textView_userName);
        mDescription = (TextView) view.findViewById(R.id.textView_description);
        mPicture = (ImageView) view.findViewById(R.id.imageView_picture);
    }

    private void initData() {

        mUserName.setText(data.getUserName());
        mDescription.setText(data.getDescription());

        Glide.with(getContext().getApplicationContext()).load(data.getPicture())
                .apply(new RequestOptions().override(864, 486))
                .apply(new RequestOptions().centerCrop())
//                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(mPicture);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


        }

    }


    private GestureDetector.SimpleOnGestureListener mOnSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    };

}
