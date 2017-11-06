package taewon.navercorp.integratedsns.profile.pin;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.feed.FavoMyPinData;

import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_IMAGE;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_MULTI_IMAGE;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_VIDEO;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * Created by USER on 2017-10-30.
 */

public class MyPinListAdapter extends RealmRecyclerViewAdapter<FavoMyPinData, MyPinListAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public MyPinListAdapter(@Nullable OrderedRealmCollection<FavoMyPinData> data, boolean autoUpdate, Context context) {
        super(data, autoUpdate);

        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // common components
        private TextView mUserName, mUploadTime, mDescription;
        private ImageView mProfile, mPicture, mPlatformType;
        private Button mLike, mComment, mShare, mMore;
        private LinearLayout mPageDetail;

        // video component
        private ImageButton mPlay;

        // multi image component

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);

            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);

            mLike = (Button) itemView.findViewById(R.id.button_like);
            mLike.setOnClickListener(this);

            mComment = (Button) itemView.findViewById(R.id.button_comment);
            mComment.setOnClickListener(this);

            mMore = (Button) itemView.findViewById(R.id.button_more);
            mMore.setOnClickListener(this);

            mPageDetail = (LinearLayout) itemView.findViewById(R.id.layout_page_detail);
            mPageDetail.setOnClickListener(this);

            if (viewType == CONTENTS_VIDEO) {

                mPicture.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
                mPlay = (ImageButton) itemView.findViewById(R.id.button_play);
                mPlay.setOnClickListener(this);
            } else {
                mPicture.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (getItem(position).getContentsType()) {

            case CONTENTS_IMAGE:
                return CONTENTS_IMAGE;

            case CONTENTS_VIDEO:
                return CONTENTS_VIDEO;

            case CONTENTS_MULTI_IMAGE:
                return CONTENTS_MULTI_IMAGE;
        }

        return -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        switch (viewType) {
            case CONTENTS_IMAGE:
                itemView = mLayoutInflater.inflate(R.layout.item_image_article, parent, false);
                return new ViewHolder(itemView, viewType);

            case CONTENTS_VIDEO:
                itemView = mLayoutInflater.inflate(R.layout.item_video_article, parent, false);
                return new ViewHolder(itemView, viewType);

            case CONTENTS_MULTI_IMAGE:
                itemView = mLayoutInflater.inflate(R.layout.item_multi_image_article, parent, false);
                return new ViewHolder(itemView, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FavoMyPinData data = getItem(position);

        holder.mUploadTime.setText(data.getCreatedTime());
        holder.mUserName.setText(data.getUserName());
        holder.mDescription.setText(data.getDescription());
        holder.mLike.setText(data.getLikeCount() + "");
        holder.mComment.setText(data.getCommentCount() + "");

        Glide.with(mContext).load(data.getPicture()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
        Glide.with(mContext).load(data.getProfileImage()).apply(new RequestOptions().circleCropTransform()).into(holder.mProfile);

        switch (data.getPlatformType()) {

            case PLATFORM_FACEBOOK:
                Glide.with(mContext).load(R.drawable.icon_facebook_small).into(holder.mPlatformType);
                break;

            case PLATFORM_YOUTUBE:
                Glide.with(mContext).load(R.drawable.icon_youtube_small).into(holder.mPlatformType);
                break;

            case PLATFORM_PINTEREST:
                Glide.with(mContext).load(R.drawable.icon_pinterest_small).into(holder.mPlatformType);
                break;
        }
    }
}
