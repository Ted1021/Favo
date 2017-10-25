package taewon.navercorp.integratedsns.profile.following;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.FollowingInfo;

/**
 * Created by USER on 2017-10-25.
 */

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FollowingInfo> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public FollowingListAdapter(Context context, ArrayList dataset) {

        mContext = context;
        mDataset = dataset;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mProfile;
        TextView mUserName;
        Button mFollowing;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mFollowing = (Button) itemView.findViewById(R.id.button_follow);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mProfile.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_following, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FollowingInfo data = mDataset.get(position);

        holder.mUserName.setText(data.getUserName());
        Glide.with(mContext).load(data.getProfile()).into(holder.mProfile);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
