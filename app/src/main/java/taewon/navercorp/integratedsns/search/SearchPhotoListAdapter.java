package taewon.navercorp.integratedsns.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;

/**
 * Created by tedkim on 2017. 11. 12..
 */

public class SearchPhotoListAdapter extends RecyclerView.Adapter<SearchPhotoListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FavoSearchResultData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public SearchPhotoListAdapter(Context context, ArrayList<FavoSearchResultData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPicture;
        private TextView mDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.item_search_result_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
