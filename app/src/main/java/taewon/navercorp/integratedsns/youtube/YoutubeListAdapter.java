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
import taewon.navercorp.integratedsns.model.YoutubeFeedData;

/**
 * @author 김태원
 * @file YoutubeListAdapter.java
 * @brief inflate user subscription list from youtube
 * @date 2017.10.07
 */

public class YoutubeListAdapter extends RecyclerView.Adapter<YoutubeListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<YoutubeFeedData.Item> mDataset;
    private LayoutInflater mLayoutInflater;

    public YoutubeListAdapter (Context context, ArrayList<YoutubeFeedData.Item> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mThumbnail;
        private TextView mListName, mDescription;
        private LinearLayout mItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
            mListName = (TextView) itemView.findViewById(R.id.textView_listName);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);
            mItemLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout_item);
            mItemLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = 0;
            switch(v.getId()){

                case R.id.linearLayout_item:

                    position = getLayoutPosition();
                    Toast.makeText(mContext, mDataset.get(position).getSnippet().getTitle(), Toast.LENGTH_SHORT).show();

                    break;
            }

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_youtube_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        YoutubeFeedData.Item.Snippet data = mDataset.get(position).getSnippet();

        Glide.with(mContext).load(data.getThumbnails().getHigh().getUrl()).into(holder.mThumbnail);
        holder.mListName.setText(data.getTitle());
//        holder.mDescription.setText(data.getDescription());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
