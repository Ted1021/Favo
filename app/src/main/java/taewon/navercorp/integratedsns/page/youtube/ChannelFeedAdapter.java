package taewon.navercorp.integratedsns.page.youtube;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.comment.CommentActivity;
import taewon.navercorp.integratedsns.model.feed.YoutubeSearchVideoData;

/**
 * Created by tedkim on 2017. 10. 22..
 */

public class ChannelFeedAdapter extends RecyclerView.Adapter<ChannelFeedAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<YoutubeSearchVideoData.Item> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private String mProfileUrl;

    private SimpleDateFormat mDateConverter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    private static final int PLATFORM_YOUTUBE = 2;
    private static final int CONTENT_TYPE = 2;

    public ChannelFeedAdapter(Context context, ArrayList<YoutubeSearchVideoData.Item> dataset, String profileUrl) {

        mContext = context;
        mDataset = dataset;
        mProfileUrl = profileUrl;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // common components
        private TextView mUserName, mUploadTime, mDescription;
        private ImageView mProfile, mPicture, mPlatformType;
        private Button mLike, mComment;
        private LinearLayout mPageDetail;

        // video component
        private ImageButton mPlay;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);
            mDescription.setMaxLines(Integer.MAX_VALUE);

            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);

            mLike = (Button) itemView.findViewById(R.id.button_like);
            mLike.setOnClickListener(this);

            mComment = (Button) itemView.findViewById(R.id.button_comment);
            mComment.setOnClickListener(this);

            mPageDetail = (LinearLayout) itemView.findViewById(R.id.layout_page_detail);
            mPageDetail.setOnClickListener(this);

            mPicture.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
            mPlay = (ImageButton) itemView.findViewById(R.id.button_play);
            mPlay.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();

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

        private void loadVideo(int position) {

            String videoUrl = videoUrl = String.format("http://www.youtube.com/watch?v=%s", mDataset.get(position).getId().getVideoId());

            if (TextUtils.isEmpty(videoUrl)) {
                return;
            }

            Uri uri = Uri.parse(videoUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            mContext.startActivity(intent);
        }

        private void loadComments(int position) {

            int contentsType = CONTENT_TYPE;

            Intent intent = new Intent(mContext, CommentActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("VIDEO_CONTENT", mDataset.get(position));
            intent.putExtras(bundle);

            intent.putExtra("VIDEO_ID", mDataset.get(position).getId().getVideoId());
            intent.putExtra("CONTENT_TYPE", contentsType);
            intent.putExtra("PLATFORM_TYPE", PLATFORM_YOUTUBE);

            mContext.startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_video_article, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        YoutubeSearchVideoData.Item.Snippet data = mDataset.get(position).getSnippet();

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

        Glide.with(mContext).load(data.getThumbnails().getHigh().getUrl()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
        Glide.with(mContext).load(mProfileUrl).apply(new RequestOptions().circleCropTransform()).into(holder.mProfile);
        Glide.with(mContext).load(R.drawable.icon_youtube_small).into(holder.mPlatformType);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
