package taewon.navercorp.integratedsns.subscription.facebook;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.comment.CommentActivity;
import taewon.navercorp.integratedsns.model.feed.FacebookFeedData;

/**
 * Created by tedkim on 2017. 10. 21..
 */

public class PageFeedAdapter extends RecyclerView.Adapter<PageFeedAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FacebookFeedData.ArticleData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    private static final int CONTENTS_IMAGE = 1;
    private static final int CONTENTS_VIDEO = 2;
    private static final int CONTENTS_MULTI = 3;

    public PageFeedAdapter(Context context, ArrayList<FacebookFeedData.ArticleData> dataset) {

        mContext = context;
        mDataset = dataset;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            mShare = (Button) itemView.findViewById(R.id.button_share);
            mShare.setOnClickListener(this);

            mPageDetail = (LinearLayout) itemView.findViewById(R.id.layout_page_detail);
            mPageDetail.setOnClickListener(this);

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
//            int contentsType = mDataset.get(position).getContentsType();

            switch (v.getId()) {

                case R.id.button_play:
                    loadVideo(position);
                    break;

                case R.id.button_comment:
                    loadComments(position);
                    break;

                case R.id.layout_page_detail:
                    break;
            }
        }
    }

    private void loadVideo(int position) {

        String videoUrl = mDataset.get(position).getSource();

        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }

        Uri uri = Uri.parse(videoUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setDataAndType(uri, "video/*");

        mContext.startActivity(intent);
    }

    private void loadComments(int position) {

        Intent intent = new Intent(mContext, CommentActivity.class);
//        intent.putExtra("CONTENTS_TYPE", contentsType);
        intent.putExtra("ARTICLE_ID", mDataset.get(position).getId());
        mContext.startActivity(intent);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_image_article, parent, false);
        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FacebookFeedData.ArticleData data = mDataset.get(position);

        try {
            holder.mUserName.setText(data.getFrom().getName());
            holder.mUploadTime.setText(data.getCreatedTime());
            holder.mDescription.setText(data.getMessage());


            Glide.with(mContext).load(data.getFullPicture()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
            Glide.with(mContext).load(data.getFrom().getPicture().getProfileData().getUrl()).apply(new RequestOptions().circleCropTransform()).into(holder.mProfile);
            Glide.with(mContext).load(R.drawable.icon_facebook_small).into(holder.mPlatformType);

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("ERROR_FACEBOOK", "Facebook List Adapter >>>>> fail to load json object " + position);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
