package taewon.navercorp.integratedsns.today;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardStackFragment extends Fragment implements View.OnClickListener{

    private FavoFeedData data;

    // common components
    private TextView mUserName, mUploadTime, mDescription, mComment;
    private ImageView mProfile, mPicture, mPlatformType;
    private Button mShare, mMore;
    private FrameLayout mPageDetail;
    private LinearLayout mCommentDetail;

    private static String ARG_PARM1 = "FAVO_DATA";

    public static CardStackFragment newInstance(FavoFeedData data){

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

        if(getArguments() != null){
            data = (FavoFeedData) getArguments().getSerializable(ARG_PARM1);
        }

        initView(view);
        initData();

        return view;
    }

    private void initView(View view){

        mUserName = (TextView) view.findViewById(R.id.textView_userName);
        mUploadTime = (TextView) view.findViewById(R.id.textView_uploadTime);
        mDescription = (TextView) view.findViewById(R.id.textView_description);

        mProfile = (ImageView) view.findViewById(R.id.imageView_profile);
        mPlatformType = (ImageView) view.findViewById(R.id.imageView_platformType);
        mPicture = (ImageView) view.findViewById(R.id.imageView_picture);
//        mPicture.setOnClickListener(this);
        mComment = (TextView) view.findViewById(R.id.textView_comment);
        mComment.setOnClickListener(this);
        mPageDetail = (FrameLayout) view.findViewById(R.id.layout_page_detail);
        mPageDetail.setOnClickListener(this);
        mCommentDetail = (LinearLayout) view.findViewById(R.id.layout_comment);
        mCommentDetail.setOnClickListener(this);
    }

    private void initData(){

        mUploadTime.setText(data.getCreatedTime());
        mUserName.setText(data.getUserName());
        mDescription.setText(data.getDescription());
        mComment.setText(data.getCommentCount() + "");

        Glide.with(getContext().getApplicationContext()).load(data.getProfileImage())
                .apply(new RequestOptions().circleCropTransform())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(mProfile);

        Glide.with(getContext().getApplicationContext()).load(data.getPicture())
                .apply(new RequestOptions().override(864, 486))
                .apply(new RequestOptions().centerCrop())
//                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(mPicture);

        switch (data.getPlatformType()) {

            case PLATFORM_FACEBOOK:
                Glide.with(getContext().getApplicationContext()).load(R.drawable.icon_facebook_small).into(mPlatformType);
                break;

            case PLATFORM_YOUTUBE:
                Glide.with(getContext().getApplicationContext()).load(R.drawable.icon_youtube_small).into(mPlatformType);
                break;

            case PLATFORM_PINTEREST:
                Glide.with(getContext().getApplicationContext()).load(R.drawable.icon_pinterest_small).into(mPlatformType);
                break;

            case PLATFORM_TWITCH:
                Glide.with(getContext().getApplicationContext()).load(R.drawable.twitch_icon_small).into(mPlatformType);
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){


        }

    }
}
