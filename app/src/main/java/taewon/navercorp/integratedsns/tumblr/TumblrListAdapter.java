package taewon.navercorp.integratedsns.tumblr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

/**
 * @author 김태원
 * @file TumblrFragment.java
 * @brief show tumblr contents, search & add tumblr channels
 * @date 2017.10.13
 */

public class TumblrListAdapter extends RecyclerView.Adapter<TumblrListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList mDataset;
    private LayoutInflater mInflater;

    public TumblrListAdapter(Context context, ArrayList dataset) {

        mContext = context;
        mDataset = dataset;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.item_tumblr_list, parent, false);
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
