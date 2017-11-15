package taewon.navercorp.integratedsns.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.feed.VideoActivity;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;
import taewon.navercorp.integratedsns.util.TwitchWebViewActivity;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * Created by tedkim on 2017. 11. 12..
 */

public class SearchVideoListAdapter extends RecyclerView.Adapter<SearchVideoListAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<FavoSearchResultData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public SearchVideoListAdapter(Context context, ArrayList<FavoSearchResultData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // common components
        private TextView mUserName, mTitle;
        private ImageView mProfile, mPicture;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mTitle = (TextView) itemView.findViewById(R.id.textView_title);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
            mPicture.setOnClickListener(this);
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
                    String twitchUrl = String.format("http://player.twitch.tv?channel=%s", mDataset.get(position).getUserName());
                    intent = new Intent(mContext, TwitchWebViewActivity.class);
                    intent.putExtra("REQ_TYPE", "video");
                    intent.putExtra("REQ_URL", twitchUrl);
                    break;
            }
            mContext.startActivity(intent);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            switch(v.getId()){

                case R.id.imageView_picture:
                    loadVideo(position);
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_search_result_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FavoSearchResultData data = mDataset.get(position);

        holder.mUserName.setText(data.getUserName());
        holder.mTitle.setText(data.getDescription());

        Glide.with(mContext.getApplicationContext()).load(data.getPicture())
                .apply(new RequestOptions().override(864, 486))
                .apply(new RequestOptions().centerCrop())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mPicture);

        switch (data.getPlatformType()) {

            case PLATFORM_FACEBOOK:
                Glide.with(mContext.getApplicationContext()).load(R.drawable.icon_facebook_small)
                        .apply(new RequestOptions().centerCrop())
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mProfile);
                break;

            case PLATFORM_YOUTUBE:
                Glide.with(mContext.getApplicationContext()).load(R.drawable.icon_youtube_small)
                        .apply(new RequestOptions().centerCrop())
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mProfile);
                break;

            case PLATFORM_PINTEREST:
                Glide.with(mContext.getApplicationContext()).load(R.drawable.icon_pinterest_small)
                        .apply(new RequestOptions().centerCrop())
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mProfile);
                break;

            case PLATFORM_TWITCH:
                Glide.with(mContext.getApplicationContext()).load(R.drawable.twitch_icon_small)
                        .apply(new RequestOptions().centerCrop())
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mProfile);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
