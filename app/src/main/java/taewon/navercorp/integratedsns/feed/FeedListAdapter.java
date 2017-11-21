package taewon.navercorp.integratedsns.feed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.Vector;

import io.realm.Realm;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.comment.CommentActivity;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;
import taewon.navercorp.integratedsns.model.favo.FavoMyPinData;
import taewon.navercorp.integratedsns.page.PageDetailActivity;
import taewon.navercorp.integratedsns.customview.GridImageView;
import taewon.navercorp.integratedsns.util.RealmDataConvertingHelper;
import taewon.navercorp.integratedsns.video.VideoActivity;

import static taewon.navercorp.integratedsns.R.layout.item_image_article;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_IMAGE;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_MULTI_IMAGE;
import static taewon.navercorp.integratedsns.util.AppController.CONTENTS_VIDEO;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

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
    private Realm mRealm;

    public FeedListAdapter(Context context, Vector<FavoFeedData> dataset, Realm realm) {

        mContext = context;
        mDataset = dataset;
        mRealm = realm;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // common components
        private TextView mUserName, mUploadTime, mDescription;
        private ImageView mProfile, mPicture, mPlatformType;
        private Button mComment, mShare, mMore;
        private FrameLayout mPageDetail;

        // video component
        private ImageButton mPlay;

        // multi image component
        private GridImageView mGridImageView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mMore = (Button) itemView.findViewById(R.id.button_more);
            mMore.setOnClickListener(this);
            mComment = (Button) itemView.findViewById(R.id.button_comment);
            mComment.setOnClickListener(this);
            mPageDetail = (FrameLayout) itemView.findViewById(R.id.layout_page_detail);
            mPageDetail.setOnClickListener(this);

            switch(viewType){
                case CONTENTS_IMAGE:
                    mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
                    mPicture.setOnClickListener(this);
                    break;

                case CONTENTS_MULTI_IMAGE:
                    mGridImageView = (GridImageView) itemView.findViewById(R.id.layout_gridImageView);
                    mGridImageView.setOnClickListener(this);

                    break;

                case CONTENTS_VIDEO:
                    mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
                    mPicture.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
                    mPlay = (ImageButton) itemView.findViewById(R.id.imageButton_play);
                    mPlay.setOnClickListener(this);
                    break;
            }
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            switch (v.getId()) {

                case R.id.imageButton_play:
                    loadVideo(position);
                    break;

                case R.id.button_comment:
                    loadComments(position);
                    break;

                case R.id.layout_page_detail:
                    loadPageDetail(position);
                    break;

                case R.id.imageView_picture:
//                    loadVideo(position);
                    break;

                case R.id.button_more:
                    loadPopupMenu(position);
                    break;

                case R.id.layout_gridImageView:
                    loadMultiImageDetail(position);
                    break;
            }
        }

        private void loadVideo(int position) {

            FavoFeedData videoData = mDataset.get(position);
            Intent intent = new Intent(mContext, VideoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("FEED_DATA", videoData);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Log.d("CHECK_URL", mDataset.get(position).getVideoUrl());

            mContext.startActivity(intent);
        }

        private void loadComments(int position) {

            String platformType = mDataset.get(position).getPlatformType();

            Intent intent = new Intent(mContext, CommentActivity.class);
            intent.putExtra("PLATFORM_TYPE", platformType);

            switch (platformType) {

                case PLATFORM_FACEBOOK:
                    intent.putExtra("ARTICLE_ID", mDataset.get(position).getFeedId());
                    break;

                case PLATFORM_YOUTUBE:
                    intent.putExtra("VIDEO_ID", mDataset.get(position).getFeedId());
                    break;
            }
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(0, 0);
        }

        private void loadPageDetail(int position) {

            Intent intent = new Intent(mContext, PageDetailActivity.class);
            String platformType = mDataset.get(position).getPlatformType();
            intent.putExtra("PLATFORM_TYPE", platformType);

            switch (platformType) {

                case PLATFORM_FACEBOOK:
                    intent.putExtra("PAGE_ID", mDataset.get(position).getPageId());
                    break;

                case PLATFORM_YOUTUBE:
                    intent.putExtra("CHANNEL_ID", mDataset.get(position).getPageId());
                    intent.putExtra("PROFILE_URL", mDataset.get(position).getProfileImage());
                    break;

                case PLATFORM_TWITCH:
                    intent.putExtra("USER_ID", mDataset.get(position).getPageId());
                    intent.putExtra("PROFILE_URL", mDataset.get(position).getProfileImage());
                    intent.putExtra("USER_NAME", mDataset.get(position).getUserName());
                    break;
            }
            mContext.startActivity(intent);
        }

        private void loadLink(int position) {

            Intent intent;
            String url = mDataset.get(position).getLink();
            if (url != null) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
            }
        }

        private void loadPopupMenu(final int position) {

            PopupMenu popupMenu = new PopupMenu(mContext, mMore);
            popupMenu.getMenuInflater().inflate(R.menu.feed_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getTitle().equals("Save Pin")) {

                        final FavoMyPinData myPin = RealmDataConvertingHelper.convertToRealmObject(mDataset.get(position));
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(myPin);
                            }
                        });
                    }
                    return true;
                }
            });
            popupMenu.show();
        }

        private void loadMultiImageDetail(int position){

            Intent intent = new Intent(mContext, MultiViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("FEED_DATA", mDataset.get(position));
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (mDataset.get(position).getContentsType()) {

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
                itemView = mLayoutInflater.inflate(item_image_article, parent, false);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        FavoFeedData data = mDataset.get(position);

        holder.mUploadTime.setText(data.getCreatedTime());
        holder.mUserName.setText(data.getUserName());
        holder.mDescription.setText(data.getDescription());
        Glide.with(mContext).load(data.getProfileImage()).apply(new RequestOptions().circleCropTransform()).into(holder.mProfile);

        switch(data.getContentsType()){
            case CONTENTS_IMAGE:
                Glide.with(mContext).load(data.getPicture())
                        .apply(new RequestOptions().override(holder.mPicture.getMaxWidth()))
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mPicture);
                break;

            case CONTENTS_MULTI_IMAGE:
                holder.mGridImageView.removeAllViews();
                holder.mGridImageView.initView(data.getSubAttatchments());
                break;

            case CONTENTS_VIDEO:
                Glide.with(mContext.getApplicationContext()).load(data.getPicture())
                        .apply(new RequestOptions().override(864, 486))
                        .apply(new RequestOptions().centerCrop())
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mPicture);
                break;
        }

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

            case PLATFORM_TWITCH:
                Glide.with(mContext).load(R.drawable.twitch_icon_small).into(holder.mPlatformType);
                break;
        }
    }

    @Override
    public int getItemCount() {

        return mDataset.size();
    }


}

