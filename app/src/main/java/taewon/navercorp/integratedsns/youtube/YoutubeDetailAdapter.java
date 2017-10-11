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

/**
 * Created by USER on 2017-10-11.
 */

public class YoutubeDetailAdapter extends RecyclerView.Adapter<YoutubeDetailAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList mDataset;
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
            switch(v.getId()){

                case R.id.linearLayout_item:
                    position = getLayoutPosition();
                    // TODO - call video
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

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
