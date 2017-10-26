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
import taewon.navercorp.integratedsns.model.comment.YoutubeCommentData;
import taewon.navercorp.integratedsns.model.feed.YoutubeSearchVideoData;

/**
 * Created by USER on 2017-10-20.
 */

public class YoutubeCommentAdapter extends RecyclerView.Adapter<YoutubeCommentAdapter.ViewHolder> {

    private Context mContext;
    private YoutubeSearchVideoData.Item mVideoData;
    private ArrayList<YoutubeCommentData.Item> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    // ViewHolder Type
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    public YoutubeCommentAdapter(Context context, YoutubeSearchVideoData.Item videoData, ArrayList<YoutubeCommentData.Item> dataset) {

        mContext = context;
        mVideoData = videoData;
        mDataset = dataset;
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

            View headerView = mLayoutInflater.inflate(R.layout.item_video_article, parent, false);
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

        holder.mArticleUserName.setText(mVideoData.getSnippet().getChannelTitle());
        holder.mArticleUploadTime.setText(mVideoData.getSnippet().getPublishedAt());
        holder.mDescription.setText(mVideoData.getSnippet().getTitle());

        Glide.with(mContext).load(mVideoData.getSnippet().getThumbnails().getHigh().getUrl()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
        Glide.with(mContext).load(mVideoData.getSnippet().getProfileImage()).apply(new RequestOptions().circleCropTransform()).into(holder.mArticleProfile);
        Glide.with(mContext).load(R.drawable.icon_youtube_small).into(holder.mPlatformType);
    }

    private void bindBodyViewItem(ViewHolder holder, int position) {

        YoutubeCommentData.Item.TopLevelComment.Author data = mDataset.get(position).getSnippet().getTopLevelComment().getSnippet();

        holder.mCommentUserName.setText(data.getAuthorDisplayName());
        holder.mCommentText.setText(data.getTextOriginal());
        holder.mUploadTime.setText(data.getPublishedAt());

        Glide.with(mContext).load(data.getAuthorProfileImageUrl()).apply(new RequestOptions().circleCropTransform()).into(holder.mCommentProfile);
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }
}
