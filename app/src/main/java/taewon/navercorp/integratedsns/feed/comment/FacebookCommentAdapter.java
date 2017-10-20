package taewon.navercorp.integratedsns.feed.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.comment.FacebookCommentData;

/**
 * Created by USER on 2017-10-18.
 */

public class FacebookCommentAdapter extends RecyclerView.Adapter<FacebookCommentAdapter.ViewHolder> {

    private Context mContext;
    private FacebookCommentData mFeedDetail;
    private ArrayList<FacebookCommentData.Comments.CommentData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    private int mContentType;

    // ViewHolder Type
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    // Contents Type
    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI = 3;

    // Platform Type
    private static final int PLATFORM_FACEBOOK = 1;
    private static final int PLATFORM_YOUTUBE = 2;
    private static final int PLATFORM_PINTEREST = 3;

    public FacebookCommentAdapter(Context context, FacebookCommentData feedDetail, ArrayList<FacebookCommentData.Comments.CommentData> dataset, int contentType) {

        mContext = context;
        mFeedDetail = feedDetail;
        mDataset = dataset;
        mContentType = contentType;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        int mViewType;

        // Header View components
        ImageView mArticleProfile, mPlatformType, mPicture;
        TextView mArticleUserName, mArticleUploadTime, mDescription;
        Button mLike, mComment, mShare;

        // Body View components
        ImageView mCommentProfile;
        TextView mCommentUserName, mCommentText, mUploadTime;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            mViewType = viewType;
            if (TYPE_HEADER == viewType) {
                initHeaderView(itemView);
            } else {
                initBodyView(itemView);
            }
        }

        private void initHeaderView(View itemView) {

            mArticleProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);

            mArticleUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mArticleUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);

            mLike = (Button) itemView.findViewById(R.id.button_like);
            mComment = (Button) itemView.findViewById(R.id.button_comment);
            mShare = (Button) itemView.findViewById(R.id.button_share);
        }

        private void initBodyView(View itemView) {

            mCommentProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mCommentUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mCommentText = (TextView) itemView.findViewById(R.id.textView_comment);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {

            View headerView = mLayoutInflater.inflate(R.layout.item_image_article, parent, false);
            return new ViewHolder(headerView, viewType);
        } else {

            View itemView = mLayoutInflater.inflate(R.layout.item_comment, parent, false);
            return new ViewHolder(itemView, viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder.mViewType == TYPE_HEADER) {
            bindHeaderViewItem(holder);
        } else {
            bindBodyViewItem(holder, position - 1);
        }
    }

    private void bindHeaderViewItem(ViewHolder holder) {

        holder.mArticleUserName.setText(mFeedDetail.getFrom().getName());
        holder.mArticleUploadTime.setText(mFeedDetail.getCreatedTime());
        holder.mDescription.setText(mFeedDetail.getMessage());

        Glide.with(mContext).load(mFeedDetail.getFullPicture()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
        Glide.with(mContext).load(mFeedDetail.getFrom().getPicture().getData().getUrl()).apply(new RequestOptions().circleCropTransform()).into(holder.mArticleProfile);
        Glide.with(mContext).load(R.drawable.icon_facebook_small).into(holder.mPlatformType);
    }

    private void bindBodyViewItem(ViewHolder holder, int position) {

        if (!mDataset.isEmpty()) {
            FacebookCommentData.Comments.CommentData data = mFeedDetail.getComments().getData().get(position);

            holder.mCommentUserName.setText(data.getFrom().getName());
            holder.mCommentText.setText(data.getMessage());
            holder.mUploadTime.setText(data.getUploadTime());

            Glide.with(mContext).load(data.getFrom().getPicture().getData().getUrl()).apply(new RequestOptions().circleCropTransform()).into(holder.mCommentProfile);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }
}
