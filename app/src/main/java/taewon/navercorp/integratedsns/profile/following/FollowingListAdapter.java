package taewon.navercorp.integratedsns.profile.following;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.FollowingInfoData;
import taewon.navercorp.integratedsns.subscription.facebook.PageDetailActivity;
import taewon.navercorp.integratedsns.subscription.youtube.ChannelDetailActivity;

/**
 * Created by USER on 2017-10-25.
 */

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FollowingInfoData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    private static final int PLATFORM_FACEBOOK = 1;
    private static final int PLATFORM_YOUTUBE = 2;
    private static final int PLATFORM_PINTEREST = 3;

    public FollowingListAdapter(Context context, ArrayList dataset) {

        mContext = context;
        mDataset = dataset;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mProfile;
        TextView mUserName;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUserName.setOnClickListener(this);

            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mProfile.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();

            switch (v.getId()) {
                case R.id.textView_userName:
                    loadPageDetail(position);
                    break;
            }
        }

        private void loadPageDetail(int position){

            Intent intent;
            int platformType = mDataset.get(position).getPlatformType();
            switch (platformType) {

                case PLATFORM_FACEBOOK:

                    intent = new Intent(mContext, PageDetailActivity.class);
//                intent.putExtra("CONTENT_TYPE", mDataset.get(position).getContentsType());
                    intent.putExtra("PAGE_ID", mDataset.get(position).get_id());
                    mContext.startActivity(intent);

                    break;

                case PLATFORM_YOUTUBE:

                    intent = new Intent(mContext, ChannelDetailActivity.class);
                    intent.putExtra("CHANNEL_ID", mDataset.get(position).get_id());
                    intent.putExtra("PROFILE_URL", mDataset.get(position).getProfile());
                    mContext.startActivity(intent);
                    break;

                case PLATFORM_PINTEREST:

                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_following, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FollowingInfoData data = mDataset.get(position);

        holder.mUserName.setText(data.getUserName());
        Glide.with(mContext).load(data.getProfile()).into(holder.mProfile);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
