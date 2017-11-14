package taewon.navercorp.integratedsns.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;
import taewon.navercorp.integratedsns.page.PageDetailActivity;

import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_FACEBOOK;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_PINTEREST;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_TWITCH;
import static taewon.navercorp.integratedsns.util.AppController.PLATFORM_YOUTUBE;

/**
 * Created by tedkim on 2017. 11. 12..
 */

public class SearchPageListAdapter extends RecyclerView.Adapter<SearchPageListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FavoSearchResultData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public SearchPageListAdapter(Context context, ArrayList<FavoSearchResultData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // common components
        private TextView mUserName, mAbout;
        private ImageView mProfile, mPlatformType;
        private LinearLayout mPageDetail;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mAbout = (TextView) itemView.findViewById(R.id.textView_about);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mPageDetail = (LinearLayout) itemView.findViewById(R.id.layout_pageDetail);
            mPageDetail.setOnClickListener(this);
        }

        private void loadPageDetail(int position) {

            Intent intent = new Intent(mContext, PageDetailActivity.class);
            String platformType = mDataset.get(position).getPlatformType();
            intent.putExtra("PLATFORM_TYPE", platformType);

            switch (platformType) {

                case PLATFORM_FACEBOOK:
                    intent.putExtra("PAGE_ID", mDataset.get(position).getPageId());
                    break;

                case PLATFORM_YOUTUBE:
                    intent.putExtra("CHANNEL_ID", mDataset.get(position).getPageId());
                    intent.putExtra("PROFILE_URL", mDataset.get(position).getProfileImage());
                    break;

                case PLATFORM_TWITCH:
                    intent.putExtra("USER_ID", mDataset.get(position).getPageId());
                    intent.putExtra("PROFILE_URL", mDataset.get(position).getProfileImage());
                    break;
            }
            mContext.startActivity(intent);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            switch (v.getId()) {
                case R.id.layout_pageDetail:
                    loadPageDetail(position);
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_search_result_page, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FavoSearchResultData data = mDataset.get(position);

        holder.mUserName.setText(data.getUserName());
        holder.mAbout.setText(data.getDescription());

        if (data.getProfileImage() != null) {
            Glide.with(mContext.getApplicationContext()).load(data.getProfileImage())
                    .apply(new RequestOptions().circleCropTransform())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(holder.mProfile);
        }

        switch (data.getPlatformType()) {

            case PLATFORM_FACEBOOK:
                Glide.with(mContext).load(R.drawable.icon_facebook_small).into(holder.mPlatformType);
                break;

            case PLATFORM_YOUTUBE:
                Glide.with(mContext).load(R.drawable.icon_youtube_small).into(holder.mPlatformType);
                break;

            case PLATFORM_PINTEREST:
                Glide.with(mContext).load(R.drawable.icon_pinterest_small).into(holder.mPlatformType);
                break;

            case PLATFORM_TWITCH:
                Glide.with(mContext).load(R.drawable.twitch_icon_small).into(holder.mPlatformType);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
