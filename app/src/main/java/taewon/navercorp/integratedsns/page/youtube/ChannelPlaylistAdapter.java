package taewon.navercorp.integratedsns.page.youtube;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.page.YoutubeChannelPlaylistData;

/**
 * Created by tedkim on 2017. 10. 22..
 */

public class ChannelPlaylistAdapter extends RecyclerView.Adapter<ChannelPlaylistAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<YoutubeChannelPlaylistData.Item> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    SimpleDateFormat mDateConverter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    public ChannelPlaylistAdapter(Context context, ArrayList<YoutubeChannelPlaylistData.Item> dataset){

        mContext = context;
        mDataset = dataset;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mThumbnail;
        private TextView mTitle, mCreatedTime, mItemCount;
        private LinearLayout mVideoItem;

        public ViewHolder(View itemView) {
            super(itemView);

            mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
//            mThumbnail.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);

            mTitle = (TextView) itemView.findViewById(R.id.textView_title);
            mCreatedTime = (TextView) itemView.findViewById(R.id.textView_createdTime);
            mItemCount = (TextView) itemView.findViewById(R.id.textView_itemCount);

            mVideoItem = (LinearLayout) itemView.findViewById(R.id.linearLayout_item);
            mVideoItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_channel_playlist, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        YoutubeChannelPlaylistData.Item data = mDataset.get(position);

        String date = null;
        try {
            date = mFormat.format(mDateConverter.parse(data.getSnippet().getPublishedAt()));
            Log.d("CHECK_DATE", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mTitle.setText(data.getSnippet().getTitle());
        holder.mCreatedTime.setText(date);
        holder.mItemCount.setText(data.getContentDetails().getItemCount()+"");

        Glide.with(mContext).load(data.getSnippet().getThumbnails().getHigh().getUrl()).into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
