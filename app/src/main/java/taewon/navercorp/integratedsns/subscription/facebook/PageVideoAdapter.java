package taewon.navercorp.integratedsns.subscription.facebook;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.page.FacebookPageVideoData;

/**
 * Created by tedkim on 2017. 10. 22..
 */

public class PageVideoAdapter extends RecyclerView.Adapter<PageVideoAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FacebookPageVideoData.Video> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public PageVideoAdapter(Context context, ArrayList<FacebookPageVideoData.Video> dataset) {

        mContext = context;
        mDataset = dataset;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mThumbnail, mPlay;
        private TextView mTitle, mCreatedTime, mRunTime;
        private LinearLayout mVideoItem;

        public ViewHolder(View itemView) {
            super(itemView);

            mPlay = (ImageView) itemView.findViewById(R.id.imageView_play);
            mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
            mThumbnail.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);

            mTitle = (TextView) itemView.findViewById(R.id.textView_title);
            mCreatedTime = (TextView) itemView.findViewById(R.id.textView_createdTime);
            mRunTime = (TextView) itemView.findViewById(R.id.textView_runTime);

            mVideoItem = (LinearLayout) itemView.findViewById(R.id.linearLayout_item);
            mVideoItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch(v.getId()){

                case R.id.linearLayout_item:
                     // TODO - load video
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_video_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FacebookPageVideoData.Video data = mDataset.get(position);

        holder.mTitle.setText(data.getDescription());
        holder.mCreatedTime.setText(data.getCreated_time());
        holder.mRunTime.setText(data.getLength().toString());

        Glide.with(mContext).load(data.getPicture()).into(holder.mThumbnail);
        holder.mThumbnail.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
