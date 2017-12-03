package taewon.navercorp.integratedsns.profile.following;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoFollowingInfoData;
import taewon.navercorp.integratedsns.page.PageDetailActivity;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * Created by USER on 2017-10-25.
 */

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FavoFollowingInfoData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

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
            String platformType = mDataset.get(position).getPlatformType();

            Intent intent = new Intent(mContext, PageDetailActivity.class);
            intent.putExtra("PLATFORM_TYPE", platformType);

            switch (platformType) {

                case PLATFORM_FACEBOOK:
                    intent.putExtra("PAGE_ID", mDataset.get(position).get_id());
                    break;

                case PLATFORM_YOUTUBE:
                    intent.putExtra("CHANNEL_ID", mDataset.get(position).get_id());
                    intent.putExtra("PROFILE_URL", mDataset.get(position).getProfile());
                    break;

                case PLATFORM_PINTEREST:

                    break;
            }
            mContext.startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_following, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FavoFollowingInfoData data = mDataset.get(position);

        holder.mUserName.setText(data.getUserName());
        Animation fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        holder.mUserName.startAnimation(fadeInAnimation);

        Glide.with(mContext.getApplicationContext()).load(data.getProfile())
                .apply(new RequestOptions().placeholder(new ColorDrawable(Color.BLACK)))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mProfile);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
