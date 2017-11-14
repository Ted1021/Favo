package taewon.navercorp.integratedsns.live;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.VideoActivity;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;
import taewon.navercorp.integratedsns.page.PageDetailActivity;
import taewon.navercorp.integratedsns.util.TwitchWebViewActivity;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * Created by USER on 2017-11-14.
 */

public class LiveStreamingListAdapter extends RecyclerView.Adapter<LiveStreamingListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FavoFeedData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public LiveStreamingListAdapter(Context context, ArrayList<FavoFeedData> dataset) {
        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // common components
        private TextView mUserName, mUploadTime, mDescription, mComment;
        private ImageView mProfile, mPicture, mPlatformType;
        private Button mShare, mMore;
        private FrameLayout mPageDetail;
        private LinearLayout mCommentDetail;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);

            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
            mPicture.setOnClickListener(this);
            mComment = (TextView) itemView.findViewById(R.id.textView_comment);
            mComment.setOnClickListener(this);
            mMore = (Button) itemView.findViewById(R.id.button_more);
            mMore.setOnClickListener(this);
            mPageDetail = (FrameLayout) itemView.findViewById(R.id.layout_page_detail);
            mPageDetail.setOnClickListener(this);
            mCommentDetail = (LinearLayout) itemView.findViewById(R.id.layout_comment);
            mCommentDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        private void loadVideo(int position) {

            if (TextUtils.isEmpty(mDataset.get(position).getVideoUrl())) {
                return;
            }

            String videoUrl = mDataset.get(position).getVideoUrl();
            String platformType = mDataset.get(position).getPlatformType();
            Intent intent = null;
            switch (platformType) {

                case PLATFORM_FACEBOOK:
                    Uri uri = Uri.parse(videoUrl);
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setDataAndType(uri, "video/*");
                    break;

                case PLATFORM_YOUTUBE:
                    intent = new Intent(mContext, VideoActivity.class);
                    intent.putExtra("VIDEO_ID", videoUrl);
                    break;

                case PLATFORM_TWITCH:
                    String twitchUrl = String.format("http://player.twitch.tv?video=%s", mDataset.get(position).getVideoUrl());
                    intent = new Intent(mContext, TwitchWebViewActivity.class);
                    intent.putExtra("REQ_TYPE", "video");
                    intent.putExtra("REQ_URL", twitchUrl);
                    break;
            }
            mContext.startActivity(intent);
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
                    break;
            }
            mContext.startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
