package taewon.navercorp.integratedsns.feed.FeedDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by USER on 2017-10-18.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList mDataset;
    private LayoutInflater mLayoutInflater;

    private int mContentType, mPlatformType;

    public CommentListAdapter(Context context, ArrayList dataset, int contentType, int platformType){
        mContext = context;
        mDataset = dataset;
        mContentType = contentType;
        mPlatformType = platformType;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Header & comments
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
