package taewon.navercorp.integratedsns.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        // common components
        private TextView mUserName, mAbout, mComment;
        private ImageView mProfile, mPlatformType;
        private FrameLayout mPageDetail;
        private LinearLayout mCommentDetail;
        private View mLine;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mAbout = (TextView) itemView.findViewById(R.id.textView_about);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mComment = (TextView) itemView.findViewById(R.id.textView_comment);
            mPageDetail = (FrameLayout) itemView.findViewById(R.id.layout_page_detail);
            mCommentDetail = (LinearLayout) itemView.findViewById(R.id.layout_comment);
            mLine = itemView.findViewById(R.id.line);
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
        Glide.with(mContext.getApplicationContext()).load(data.getProfileImage())
                .apply(new RequestOptions().circleCropTransform())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mProfile);

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
