package taewon.navercorp.integratedsns.feed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.pinterest.android.pdk.PDKPin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.comment.CommentActivity;
import taewon.navercorp.integratedsns.model.feed.FacebookFeedData;
import taewon.navercorp.integratedsns.model.feed.FavoFeedData;
import taewon.navercorp.integratedsns.model.feed.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.subscription.facebook.PageDetailActivity;
import taewon.navercorp.integratedsns.subscription.youtube.ChannelDetailActivity;

/**
 * @author 김태원
 * @file FacebookListAdapter.java
 * @brief inflate facebook articles
 * @date 2017.09.29
 */

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private Activity mActivity;
    private Vector<FavoFeedData> mDataset = new Vector<>();
    private LayoutInflater mLayoutInflater;
    SimpleDateFormat mDateConverter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI = 3;

    private static final int PLATFORM_FACEBOOK = 1;
    private static final int PLATFORM_YOUTUBE = 2;
    private static final int PLATFORM_PINTEREST = 3;

    public FeedListAdapter(Activity activity, Vector<FavoFeedData> dataset) {

        mActivity = activity;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // common components
        private TextView mUserName, mUploadTime, mDescription;
        private ImageView mProfile, mPicture, mPlatformType;
        private Button mLike, mComment, mShare;
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

            int position = getLayoutPosition();
            int platformType = mDataset.get(position).getPlatformType();
            int contentsType = mDataset.get(position).getContentsType();

            switch (v.getId()) {

                case R.id.button_play:
                    loadVideo(position, platformType);
                    break;

                case R.id.button_comment:
                    loadComments(position, platformType, contentsType);
                    break;

                case R.id.layout_page_detail:
                    loadPageDetail(position, platformType);
                    break;

                case R.id.imageView_picture:
                    loadLink(position, platformType);
                    break;
            }
        }
    }

    private void loadVideo(int position, int platformType) {

        String videoUrl = null;
        Intent intent = null;
        if (platformType == PLATFORM_FACEBOOK) {

            videoUrl = mDataset.get(position).getFacebookData().getSource();
            if (TextUtils.isEmpty(videoUrl)) {
                return;
            }

            Uri uri = Uri.parse(videoUrl);

            intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setDataAndType(uri, "video/*");
            mActivity.startActivity(intent);

        } else {
            videoUrl = mDataset.get(position).getYoutubeData().getId().getVideoId();
            intent = new Intent(mActivity, VideoActivity.class);
            intent.putExtra("VIDEO_ID", videoUrl);
            mActivity.startActivity(intent);
        }
    }

    private void loadComments(int position, int platformType, int contentsType) {

        Intent intent = new Intent(mActivity, CommentActivity.class);
        intent.putExtra("PLATFORM_TYPE", platformType);
        intent.putExtra("CONTENTS_TYPE", contentsType);

        switch (platformType) {

            case PLATFORM_FACEBOOK:
                intent.putExtra("ARTICLE_ID", mDataset.get(position).getFacebookData().getId());
                Log.d("CHECK_FACEBOOK_ID", mDataset.get(position).getFacebookData().getId());
                break;

            case PLATFORM_YOUTUBE:

                Bundle bundle = new Bundle();
                bundle.putSerializable("VIDEO_CONTENT", mDataset.get(position).getYoutubeData());
                intent.putExtras(bundle);

                intent.putExtra("VIDEO_ID", mDataset.get(position).getYoutubeData().getId().getVideoId());
                break;

            case PLATFORM_PINTEREST:

                break;
        }
        mActivity.startActivity(intent);
    }

    private void loadPageDetail(int position, int platformType) {

        Intent intent;
        switch (platformType) {

            case PLATFORM_FACEBOOK:

                intent = new Intent(mActivity, PageDetailActivity.class);
//                intent.putExtra("CONTENT_TYPE", mDataset.get(position).getContentsType());
                intent.putExtra("PAGE_ID", mDataset.get(position).getFacebookData().getFrom().getId());
                mActivity.startActivity(intent);

                break;

            case PLATFORM_YOUTUBE:

                intent = new Intent(mActivity, ChannelDetailActivity.class);
                intent.putExtra("CHANNEL_ID", mDataset.get(position).getYoutubeData().getSnippet().getChannelId());
                intent.putExtra("PROFILE_URL", mDataset.get(position).getYoutubeData().getSnippet().getProfileImage());
                mActivity.startActivity(intent);
                break;

            case PLATFORM_PINTEREST:

                break;
        }
    }

    private void loadLink(int position, int platformType) {

        Intent intent = null;
        String url;
        switch (platformType) {

            case PLATFORM_FACEBOOK:

                if (mDataset.get(position).getFacebookData().getLink() != null) {

                    url = mDataset.get(position).getFacebookData().getLink();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mActivity.startActivity(intent);
                }
                break;

            case PLATFORM_PINTEREST:

                if (mDataset.get(position).getPinterestData().getLink() != null) {

                    url = mDataset.get(position).getPinterestData().getLink();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mActivity.startActivity(intent);
                }
                break;
        }
    }

//    private void loadYoutubeVideo(final String videoId) {
//
//        android.app.FragmentManager fragmentManager = mActivity.getFragmentManager();
//        YouTubePlayerFragment playerFragment = (YouTubePlayerFragment) fragmentManager.findFragmentByTag("youtube_video");
//        if (playerFragment == null) {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            playerFragment = YouTubePlayerFragment.newInstance();
//            fragmentTransaction.add(R.id.layout_container, playerFragment, "youtube_video").addToBackStack(null).commit();
//        }
//
//        playerFragment.initialize(mActivity.getString(R.string.google_api_key), new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                youTubePlayer.cueVideo(videoId);
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                Log.e(getClass().getName(), "Error while initializing YouTubePlayer");
//            }
//        });
//    }

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

        String date = null;
        try {
            date = mFormat.format(mDateConverter.parse(data.getCreatedTime()));
            Log.d("CHECK_DATE", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mUploadTime.setText(date);
        holder.mUserName.setText(data.getFrom().getName());
        holder.mDescription.setText(data.getMessage());
        holder.mLike.setText(data.getLikes().getSummary().getTotalCount() + " 개");
        holder.mComment.setText(data.getComments().getSummary().getTotalCount() + " 개");

        Glide.with(mActivity).load(data.getFullPicture()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
        Glide.with(mActivity).load(data.getFrom().getPicture().getProfileData().getUrl()).apply(new RequestOptions().circleCropTransform()).into(holder.mProfile);
        Glide.with(mActivity).load(R.drawable.icon_facebook_small).into(holder.mPlatformType);
    }

    private void bindYoutubeItem(ViewHolder holder, int position) {

        YoutubeSearchVideoData.Item.Snippet data = mDataset.get(position).getYoutubeData().getSnippet();
        YoutubeSearchVideoData.Item.Id id = mDataset.get(position).getYoutubeData().getId();

        String date = null;
        try {
            date = mFormat.format(mDateConverter.parse(data.getPublishedAt()));
            Log.d("CHECK_DATE", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mUploadTime.setText(date);
        holder.mUserName.setText(data.getChannelTitle());
        holder.mDescription.setText(data.getTitle());

        Glide.with(mActivity).load(data.getThumbnails().getHigh().getUrl()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
        Glide.with(mActivity).load(data.getProfileImage()).apply(new RequestOptions().circleCropTransform()).into(holder.mProfile);
        Glide.with(mActivity).load(R.drawable.icon_youtube_small).into(holder.mPlatformType);
    }

    private void bindPinterestItem(ViewHolder holder, int position) {

        PDKPin data = mDataset.get(position).getPinterestData();

        String date = mFormat.format(data.getCreatedAt());
        holder.mUserName.setText(data.getUid());
        holder.mUploadTime.setText(date);
        holder.mDescription.setText(data.getNote());

        Glide.with(mActivity).load(data.getImageUrl()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
        Glide.with(mActivity).load(data.getImageUrl()).apply(new RequestOptions().circleCropTransform()).into(holder.mProfile);
        Glide.with(mActivity).load(R.drawable.icon_pinterest_small).into(holder.mPlatformType);
    }

    @Override
    public int getItemCount() {

        return mDataset.size();
    }
}

