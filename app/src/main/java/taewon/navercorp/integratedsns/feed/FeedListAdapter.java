package taewon.navercorp.integratedsns.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pinterest.android.pdk.PDKPin;

import java.util.Vector;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.FacebookFeedData;
import taewon.navercorp.integratedsns.model.FavoFeedData;
import taewon.navercorp.integratedsns.model.YoutubeSearchVideoData;

/**
 * @author 김태원
 * @file FacebookListAdapter.java
 * @brief inflate facebook articles
 * @date 2017.09.29
 */

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private Context mContext;
    private Vector<FavoFeedData> mDataset = new Vector<>();
    private LayoutInflater mLayoutInflater;

    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI = 3;

    private static final int PLATFORM_FACEBOOK = 1;
    private static final int PLATFORM_YOUTUBE = 2;
    private static final int PLATFORM_PINTEREST = 3;

    public FeedListAdapter(Context context, Vector<FavoFeedData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // common components
        private TextView mUserName, mUploadTime, mDescription;
        private ImageView mProfile, mPicture, mPlatformType;
        private Button mLike, mComment, mShare;

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
            mComment = (Button) itemView.findViewById(R.id.button_comment);
            mShare = (Button) itemView.findViewById(R.id.button_share);

            if (viewType == CONTENTS_VIDEO) {

                mPicture.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
                mPlay = (ImageButton) itemView.findViewById(R.id.button_play);
                mPlay.setOnClickListener(this);

            } else if (viewType == CONTENTS_MULTI) {

            }
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            String videoUrl = null;
            int platformType = 0;
            switch (v.getId()) {

                case R.id.button_play:

                    if (mDataset.get(position).getPlatformType() == PLATFORM_FACEBOOK) {
                        videoUrl = mDataset.get(position).getFacebookData().getSource();
                        platformType = PLATFORM_FACEBOOK;
                    } else {
                        videoUrl = "http://www.youtube.com/watch?v=" + mDataset.get(position).getYoutubeData().getId().getVideoId();
                        platformType = PLATFORM_YOUTUBE;
                    }
                    break;
            }
            loadVideo(videoUrl, platformType);
        }
    }

    private void loadVideo(String videoUrl, int platformType) {

        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }

        Uri uri = Uri.parse(videoUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if(platformType == PLATFORM_FACEBOOK){
            intent.setDataAndType(uri, "video/*");
        }

        mContext.startActivity(intent);
    }

    @Override
    public int getItemViewType(int position) {

        switch (mDataset.get(position).getContentsType()) {

            case CONTENTS_IMAGE:
                return CONTENTS_IMAGE;

            case CONTENTS_VIDEO:
                return CONTENTS_VIDEO;

            case CONTENTS_MULTI:
                return CONTENTS_MULTI;
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

            case CONTENTS_MULTI:
                itemView = mLayoutInflater.inflate(R.layout.item_multi_image_article, parent, false);
                return new ViewHolder(itemView, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int platformType = mDataset.get(position).getPlatformType();
        switch (platformType) {

            case PLATFORM_FACEBOOK:
                bindFacebookItem(holder, position);
                break;

            case PLATFORM_YOUTUBE:
                bindYoutubeItem(holder, position);
                break;

            case PLATFORM_PINTEREST:
                bindPinterestItem(holder, position);
                break;
        }
    }

    private void bindFacebookItem(ViewHolder holder, int position) {

        FacebookFeedData.ArticleData data = mDataset.get(position).getFacebookData();

        try {
            holder.mUserName.setText(data.getFrom().getName());
            holder.mUploadTime.setText(data.getCreatedTime());
            holder.mDescription.setText(data.getMessage());


            Glide.with(mContext).load(data.getFullPicture()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
            Glide.with(mContext).load(data.getFrom().getPicture().getProfileData().getUrl()).into(holder.mProfile);
            Glide.with(mContext).load(R.drawable.icon_facebook_small).into(holder.mPlatformType);

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("ERROR_FACEBOOK", "Facebook List Adapter >>>>> fail to load json object " + position);
        }
    }

    private void bindYoutubeItem(ViewHolder holder, int position) {

        YoutubeSearchVideoData.Item.Snippet data = mDataset.get(position).getYoutubeData().getSnippet();
        YoutubeSearchVideoData.Item.Id id = mDataset.get(position).getYoutubeData().getId();

        holder.mUserName.setText(data.getChannelTitle());
        holder.mUploadTime.setText(data.getPublishedAt());
        holder.mDescription.setText(data.getDescription());

        Glide.with(mContext).load(data.getThumbnails().getHigh().getUrl()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth()).placeholder(R.drawable.image_loading)).into(holder.mPicture);
        Glide.with(mContext).load(R.drawable.icon_profile).into(holder.mProfile);
        Glide.with(mContext).load(R.drawable.icon_youtube_small).into(holder.mPlatformType);
        Log.d("CHECK_VIDEO_URL", "http://www.youtube.com/watch?v=" + id.getVideoId());
    }

    private void bindPinterestItem(ViewHolder holder, int position) {

        PDKPin data = mDataset.get(position).getPinterestData();

        holder.mUserName.setText(data.getUid());
        holder.mUploadTime.setText(data.getCreatedAt().toString());
        holder.mDescription.setText(data.getNote());
        Glide.with(mContext).load(data.getImageUrl()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth()).placeholder(R.drawable.image_loading)).into(holder.mPicture);
        Glide.with(mContext).load(R.drawable.icon_profile).into(holder.mProfile);
        Glide.with(mContext).load(R.drawable.icon_pinterest_small).into(holder.mPlatformType);
    }

    @Override
    public int getItemCount() {

        return mDataset.size();
    }
}

