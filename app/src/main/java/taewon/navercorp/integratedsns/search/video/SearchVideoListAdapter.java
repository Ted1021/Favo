package taewon.navercorp.integratedsns.search.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoVideoSearchResultData;

/**
 * Created by tedkim on 2017. 11. 12..
 */

public class SearchVideoListAdapter extends RecyclerView.Adapter<SearchVideoListAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<FavoVideoSearchResultData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public SearchVideoListAdapter(Context context, ArrayList<FavoVideoSearchResultData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // common components
        private TextView mUserName, mUploadTime, mComment;
        private ImageView mProfile, mPicture, mPlatformType;
        private FrameLayout mPageDetail;
        private LinearLayout mCommentDetail;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);

            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
            mComment = (TextView) itemView.findViewById(R.id.textView_comment);
            mPageDetail = (FrameLayout) itemView.findViewById(R.id.layout_page_detail);
            mCommentDetail = (LinearLayout) itemView.findViewById(R.id.layout_comment);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_search_result_video, parent, false);
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
