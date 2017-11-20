package taewon.navercorp.integratedsns.video;

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

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

/**
 * Created by tedkim on 2017. 11. 20..
 */

public class NextVideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public NextVideoListAdapter(Context context, ArrayList dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView mUserName, mDescription, mUploadTime;
        private ImageView mProfile;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_createdTime);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
        }
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder {

        // common ui components
        private ImageView mThumbnail;
        private TextView mTitle, mCreatedTime;
        private LinearLayout mVideoItem;

        // facebook ui components
        private ImageView mPlay;
        private TextView mRunTime;

        // youtube ui components
        private TextView mVideoCount;

        public BodyViewHolder(View itemView) {
            super(itemView);

            mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
            mThumbnail.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
            mTitle = (TextView) itemView.findViewById(R.id.textView_title);
            mCreatedTime = (TextView) itemView.findViewById(R.id.textView_createdTime);
            mVideoItem = (LinearLayout) itemView.findViewById(R.id.linearLayout_item);
//            mVideoItem.setOnClickListener(this);

        }
    }

    // header 여부 체크 메소드
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position))
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_HEADER) {
            View itemView = mLayoutInflater.inflate(R.layout.item_video_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else {
            View itemView = mLayoutInflater.inflate(R.layout.item_video_list, parent, false);
            return new BodyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    private void bindHeaderItem() {

    }

    private void bindBodyItem() {

    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }
}
