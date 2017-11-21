package taewon.navercorp.integratedsns.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;
import taewon.navercorp.integratedsns.util.Photo;

/**
 * Created by USER on 2017-11-21.
 */

public class MultiViewListAdapter extends RecyclerView.Adapter<MultiViewListAdapter.ViewHolder> {

    private Context mContext;
    private FavoFeedData mDataset = new FavoFeedData();
    private LayoutInflater mInflater;

    public MultiViewListAdapter(Context context, FavoFeedData dataset) {

        mContext = context;
        mDataset = dataset;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage, mProfile;
        private TextView mUserName, mUploadTime;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);

            mImage = (ImageView) itemView.findViewById(R.id.imageView_picture);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_multi_image_detail, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo data = mDataset.getSubAttatchments().get(position);

        holder.mUploadTime.setText(mDataset.getCreatedTime());
        holder.mUserName.setText(mDataset.getUserName());

        Glide.with(mContext).load(mDataset.getProfileImage()).apply(new RequestOptions().circleCropTransform()).into(holder.mProfile);
        Glide.with(mContext).load(data.getSrc())
                .apply(new RequestOptions().override(data.getWidth(), data.getHeight()))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        } else {
            return mDataset.getSubAttatchments().size();
        }
    }
}
