package taewon.navercorp.integratedsns.feed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Vector;

import io.realm.Realm;
import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.comment.CommentActivity;
import taewon.navercorp.integratedsns.model.feed.FavoFeedData;
import taewon.navercorp.integratedsns.model.feed.FavoMyPinData;
import taewon.navercorp.integratedsns.page.facebook.PageDetailActivity;
import taewon.navercorp.integratedsns.util.RealmDataConvertingHelper;

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

    private SimpleDateFormat mDateConverter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI = 3;

    private static final int PLATFORM_FACEBOOK = 1;
    private static final int PLATFORM_YOUTUBE = 2;
    private static final int PLATFORM_PINTEREST = 3;

    public FeedListAdapter(Context context, Vector<FavoFeedData> dataset, Realm realm) {

        mContext = context;
        mDataset = dataset;
        mRealm = realm;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // common components
        private TextView mUserName, mUploadTime, mDescription, mComment;
        private ImageView mProfile, mPicture, mPlatformType;
        private Button mShare, mMore;
        private FrameLayout mPageDetail;
        private LinearLayout mCommentDetail;

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

//            mLike = (Button) itemView.findViewById(R.id.button_like);
//            mLike.setOnClickListener(this);

            mComment = (TextView) itemView.findViewById(R.id.textView_comment);
            mComment.setOnClickListener(this);

            mMore = (Button) itemView.findViewById(R.id.button_more);
            mMore.setOnClickListener(this);

            mPageDetail = (FrameLayout) itemView.findViewById(R.id.layout_page_detail);
            mPageDetail.setOnClickListener(this);

            mCommentDetail = (LinearLayout) itemView.findViewById(R.id.layout_comment);
            mCommentDetail.setOnClickListener(this);

            if (viewType == CONTENTS_VIDEO) {

//                mPicture.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
//                mPlay = (ImageButton) itemView.findViewById(R.id.button_play);
//                mPlay.setOnClickListener(this);

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

                case R.id.layout_comment:
                    loadComments(position, platformType, contentsType);
                    break;

                case R.id.layout_page_detail:
                    loadPageDetail(position, platformType);
                    break;

                case R.id.imageView_picture:
                    loadLink(position);
                    break;

                case R.id.button_more:
                    loadPopupMenu(position);
                    break;
            }
        }

        private void loadVideo(int position, int platformType) {

            String videoUrl = null;
            Intent intent = null;
            if (platformType == PLATFORM_FACEBOOK) {

                videoUrl = mDataset.get(position).getVideoUrl();
                if (TextUtils.isEmpty(videoUrl)) {
                    return;
                }

                Uri uri = Uri.parse(videoUrl);

                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setDataAndType(uri, "video/*");
                mContext.startActivity(intent);

            } else {
                videoUrl = mDataset.get(position).getVideoUrl();
                intent = new Intent(mContext, VideoActivity.class);
                intent.putExtra("VIDEO_ID", videoUrl);
                mContext.startActivity(intent);
            }
        }

        private void loadComments(int position, int platformType, int contentsType) {

            Intent intent = new Intent(mContext, CommentActivity.class);
            intent.putExtra("PLATFORM_TYPE", platformType);
            intent.putExtra("CONTENTS_TYPE", contentsType);

            switch (platformType) {

                case PLATFORM_FACEBOOK:
                    intent.putExtra("ARTICLE_ID", mDataset.get(position).getFeedId());
                    break;

                case PLATFORM_YOUTUBE:

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("VIDEO_CONTENT", mDataset.get(position));
                    intent.putExtras(bundle);

                    intent.putExtra("VIDEO_ID", mDataset.get(position).getFeedId());
                    break;

                case PLATFORM_PINTEREST:

                    break;
            }
            mContext.startActivity(intent);
        }

        private void loadPageDetail(int position, int platformType) {

            Intent intent = new Intent(mContext, PageDetailActivity.class);
            intent.putExtra("PLATFORM_TYPE", platformType);

            switch (platformType) {

                case PLATFORM_FACEBOOK:
                    intent.putExtra("PAGE_ID", mDataset.get(position).getPageId());
                    break;

                case PLATFORM_YOUTUBE:
                    intent.putExtra("CHANNEL_ID", mDataset.get(position).getPageId());
                    intent.putExtra("PROFILE_URL", mDataset.get(position).getProfileImage());
                    break;

                case PLATFORM_PINTEREST:

                    break;
            }
            mContext.startActivity(intent);

        }

        private void loadLink(int position) {

            Intent intent = null;
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

                    if (item.getTitle().equals("Save")) {

                        final FavoMyPinData myPin = RealmDataConvertingHelper.convertToRealmObject(mDataset.get(position));
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(myPin);
                            }
                        });

                    } else {

                    }

                    return true;
                }
            });
            popupMenu.show();
        }
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
                itemView = mLayoutInflater.inflate(R.layout.item_image_article_test, parent, false);
                return new ViewHolder(itemView, viewType);

            case CONTENTS_VIDEO:
//                itemView = mLayoutInflater.inflate(R.layout.item_video_article, parent, false);
                itemView = mLayoutInflater.inflate(R.layout.item_image_article_test, parent, false);
                return new ViewHolder(itemView, viewType);

            case CONTENTS_MULTI:
                itemView = mLayoutInflater.inflate(R.layout.item_multi_image_article_test, parent, false);
                return new ViewHolder(itemView, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FavoFeedData data = mDataset.get(position);

        holder.mUploadTime.setText(data.getCreatedTime());
        holder.mUserName.setText(data.getUserName());
        holder.mDescription.setText(data.getDescription());
//        holder.mLike.setText(data.getLikeCount() + "");
        holder.mComment.setText(data.getCommentCount() + "");

        Glide.with(mContext).load(data.getPicture()).into(holder.mPicture);
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

    @Override
    public int getItemCount() {

        return mDataset.size();
    }
}

