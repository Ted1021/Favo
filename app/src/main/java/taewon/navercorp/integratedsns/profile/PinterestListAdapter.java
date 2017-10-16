package taewon.navercorp.integratedsns.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pinterest.android.pdk.PDKPin;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

/**
 * @author 김태원
 * @file TumblrFragment.java
 * @brief show tumblr contents, search & add tumblr channels
 * @date 2017.10.13
 */

public class PinterestListAdapter extends RecyclerView.Adapter<PinterestListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PDKPin> mDataset = new ArrayList<>();
    private LayoutInflater mInflater;

    public PinterestListAdapter(Context context, ArrayList dataset) {

        mContext = context;
        mDataset = dataset;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mUserName, mUploadTime, mDescription;
        private ImageView mProfile, mPicture;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);


            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.item_pinterest_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PDKPin pin = mDataset.get(position);

        holder.mUserName.setText(pin.getUid());
        holder.mUploadTime.setText(pin.getCreatedAt().toString());
        holder.mDescription.setText(pin.getNote());
        Glide.with(mContext).load(pin.getImageUrl()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
