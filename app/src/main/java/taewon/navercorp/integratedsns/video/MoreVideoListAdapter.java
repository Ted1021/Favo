package taewon.navercorp.integratedsns.video;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * Created by tedkim on 2017. 11. 20..
 */

public class MoreVideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<FavoFeedData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public MoreVideoListAdapter(Context context, ArrayList<FavoFeedData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mUserName, mDescription, mUploadTime;
        private ImageView mProfile, mPlatformType, mThumbnail;
        private FrameLayout mPageDetail;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
            mPageDetail = (FrameLayout) itemView.findViewById(R.id.layout_page_detail);
            mPageDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.layout_page_detail:

                    break;
            }
        }
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // common ui components
        private ImageView mThumbnail;
        private TextView mTitle, mUserName;
        private LinearLayout mVideoItem;

        // facebook ui components
        private ImageView mPlay;

        // youtube ui components
        private TextView mVideoCount;

        public BodyViewHolder(View itemView) {
            super(itemView);

            mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
            mThumbnail.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
            mTitle = (TextView) itemView.findViewById(R.id.textView_title);
            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mVideoItem = (LinearLayout) itemView.findViewById(R.id.linearLayout_item);
            mVideoItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (v.getId()) {

                case R.id.imageButton_play:

                    break;

                case R.id.imageView_profile:

                    break;
            }
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position))
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View itemView = mLayoutInflater.inflate(R.layout.item_video_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else {
            View itemView = mLayoutInflater.inflate(R.layout.item_video_body, parent, false);
            return new BodyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == 0) {
            bindHeaderItem((HeaderViewHolder) holder);
        } else {
            if (mDataset.size() > 1) {
                bindBodyItem((BodyViewHolder) holder, position);
            }
        }
    }

    private void bindHeaderItem(MoreVideoListAdapter.HeaderViewHolder holder) {

        FavoFeedData data = mDataset.get(0);

        holder.mUserName.setText(data.getUserName());
        holder.mDescription.setText(data.getDescription());
        holder.mUploadTime.setText(data.getCreatedTime());
        Glide.with(mContext).load(data.getProfileImage()).apply(new RequestOptions().circleCropTransform()).into(holder.mProfile);
        switch (data.getPlatformType()) {

            case PLATFORM_FACEBOOK:
                Glide.with(mContext).load(R.drawable.icon_facebook_small).into(holder.mPlatformType);
                break;

            case PLATFORM_YOUTUBE:
                Glide.with(mContext).load(R.drawable.icon_youtube_small).into(holder.mPlatformType);
                break;

            case PLATFORM_TWITCH:
                Glide.with(mContext).load(R.drawable.twitch_icon_small).into(holder.mPlatformType);
                break;
        }
    }

    private void bindBodyItem(final MoreVideoListAdapter.BodyViewHolder holder, final int position) {

        FavoFeedData data = mDataset.get(position);

        holder.mUserName.setText(data.getUserName());
        holder.mTitle.setText(data.getDescription());
        Glide.with(mContext.getApplicationContext()).load(data.getPicture())
                .apply(new RequestOptions().override(864, 486))
                .apply(new RequestOptions().centerCrop())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
