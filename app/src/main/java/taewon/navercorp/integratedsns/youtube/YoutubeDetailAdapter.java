package taewon.navercorp.integratedsns.youtube;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.YoutubeSearchVideoData;

/**
 * @author 김태원
 * @file YoutubeDetailAdapter.java
 * @brief show videos from channel
 * @date 2017.10.11
 */

public class YoutubeDetailAdapter extends RecyclerView.Adapter<YoutubeDetailAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<YoutubeSearchVideoData.Item> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public YoutubeDetailAdapter(Context context, ArrayList dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mThumbnail;
        TextView mTitle, mUserName;
        LinearLayout mItem;

        public ViewHolder(View itemView) {
            super(itemView);

        mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
        mTitle = (TextView) itemView.findViewById(R.id.textView_title);
        mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
        mItem = (LinearLayout) itemView.findViewById(R.id.linearLayout_item);
    }

        @Override
        public void onClick(View v) {

            int position;
            switch (v.getId()) {

                case R.id.linearLayout_item:
                    position = getLayoutPosition();

                    String channelId = mDataset.get(position).getSnippet().getChannelId();
                    String channelTitle = mDataset.get(position).getSnippet().getChannelTitle();
                    Toast.makeText(mContext, channelId + " " + channelTitle, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_youtube_detail_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        YoutubeSearchVideoData.Item.Snippet data = mDataset.get(position).getSnippet();

        holder.mTitle.setText(data.getTitle());
        holder.mUserName.setText(data.getChannelTitle());
        Glide.with(mContext).load(data.getThumbnails().getHigh().getUrl()).into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
