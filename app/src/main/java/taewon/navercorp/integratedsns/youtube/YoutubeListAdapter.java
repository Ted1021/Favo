package taewon.navercorp.integratedsns.youtube;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.YoutubeFeedData;

/**
 * @author 김태원
 * @file YoutubeListAdapter.java
 * @brief inflate user subscription list from youtube
 * @date 2017.10.07
 */

public class YoutubeListAdapter extends RecyclerView.Adapter<YoutubeListAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<YoutubeFeedData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public YoutubeListAdapter (Context context,ArrayList<YoutubeFeedData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mThumbnail;
        private TextView mListName, mSubscriptor;
        private LinearLayout mItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
            mListName = (TextView) itemView.findViewById(R.id.textView_listName);
            mSubscriptor = (TextView) itemView.findViewById(R.id.textView_subscriptor);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_youtube_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        YoutubeFeedData data = mDataset.get(position);



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onClick(View v) {

    }
}
