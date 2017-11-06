package taewon.navercorp.integratedsns.page;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.page.FavoPageVideoData;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * Created by tedkim on 2017. 10. 22..
 */

public class PageVideoAdapter extends RecyclerView.Adapter<PageVideoAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FavoPageVideoData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    SimpleDateFormat mDateConverter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    public PageVideoAdapter(Context context, ArrayList<FavoPageVideoData> dataset) {

        mContext = context;
        mDataset = dataset;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // common ui components
        private ImageView mThumbnail;
        private TextView mTitle, mCreatedTime;
        private LinearLayout mVideoItem;

        // facebook ui components
        private ImageView mPlay;
        private TextView mRunTime;

        // youtube ui components
        private TextView mVideoCount;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
            mThumbnail.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
            mTitle = (TextView) itemView.findViewById(R.id.textView_title);
            mCreatedTime = (TextView) itemView.findViewById(R.id.textView_createdTime);
            mVideoItem = (LinearLayout) itemView.findViewById(R.id.linearLayout_item);
            mVideoItem.setOnClickListener(this);

            if (viewType == PLATFORM_FACEBOOK) {
                mPlay = (ImageView) itemView.findViewById(R.id.imageView_play);
                mRunTime = (TextView) itemView.findViewById(R.id.textView_runTime);
            } else if (viewType == PLATFORM_YOUTUBE) {
                mVideoCount = (TextView) itemView.findViewById(R.id.textView_itemCount);
            }
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            switch (v.getId()) {

                case R.id.linearLayout_item:
                    loadVideo(position);
                    break;
            }
        }
    }

    private void loadVideo(int position) {

        if (mDataset.get(position).getPlatformType() == PLATFORM_FACEBOOK) {
            String videoUrl = mDataset.get(position).getVideoUrl();
            if (TextUtils.isEmpty(videoUrl)) {
                return;
            }

            Uri uri = Uri.parse(videoUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setDataAndType(uri, "video/*");

            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (mDataset.get(position).getPlatformType()) {

            case PLATFORM_FACEBOOK:
                return PLATFORM_FACEBOOK;

            case PLATFORM_YOUTUBE:
                return PLATFORM_YOUTUBE;
        }
        return -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (viewType) {
            case PLATFORM_FACEBOOK:
                itemView = mLayoutInflater.inflate(R.layout.item_video_list, parent, false);
                break;

            case PLATFORM_YOUTUBE:
                itemView = mLayoutInflater.inflate(R.layout.item_channel_playlist, parent, false);
                break;
        }
        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FavoPageVideoData data = mDataset.get(position);
        switch (data.getPlatformType()) {

            case PLATFORM_FACEBOOK:
                bindFacebookVideo(data, holder);
                break;

            case PLATFORM_YOUTUBE:
                bindYoutubePlaylist(data, holder);
                break;
        }
    }

    private void bindFacebookVideo(FavoPageVideoData data, ViewHolder holder) {

        String date = null;
        try {
            date = mFormat.format(mDateConverter.parse(data.getPubDate()));
            Log.d("CHECK_DATE", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int min = (int) (data.getRunTime() / 60);
        int sec = (int) (data.getRunTime() % 60);

        holder.mTitle.setText(data.getTitle());
        holder.mCreatedTime.setText(date);
        holder.mRunTime.setText(String.format("%d:%d", min, sec));

        Glide.with(mContext).load(data.getPicture())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mThumbnail);

    }

    private void bindYoutubePlaylist(FavoPageVideoData data, ViewHolder holder) {

        String date = null;
        try {
            date = mFormat.format(mDateConverter.parse(data.getPubDate()));
            Log.d("CHECK_DATE", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mTitle.setText(data.getTitle());
        holder.mCreatedTime.setText(date);
        holder.mVideoCount.setText(data.getVideoCount() + "");

        Glide.with(mContext).load(data.getPicture())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
