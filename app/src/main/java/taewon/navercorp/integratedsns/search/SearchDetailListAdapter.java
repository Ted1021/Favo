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
import static taewon.navercorp.integratedsns.util.AppController.RESULT_PAGE;
import static taewon.navercorp.integratedsns.util.AppController.RESULT_PHOTO;
import static taewon.navercorp.integratedsns.util.AppController.RESULT_VIDEO;

/**
 * Created by USER on 2017-11-14.
 */

public class SearchDetailListAdapter extends RecyclerView.Adapter<SearchDetailListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FavoSearchResultData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private int mResultType;

    public SearchDetailListAdapter(Context context, ArrayList<FavoSearchResultData> dataset, int resultType) {
        mContext = context;
        mDataset = dataset;
        mResultType = resultType;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // common components
        private TextView mUserName, mTitle, mDescription, mAbout;
        private ImageView mProfile, mPicture, mPlatformType;
        private FrameLayout mVideoPageDetail;
        private LinearLayout mPageDetail;

        public ViewHolder(View itemView) {
            super(itemView);

          switch(mResultType){
              case RESULT_PAGE:
                  createPageViewHolder(itemView);
                  break;

              case RESULT_VIDEO:
                  createVideoViewHolder(itemView);
                  break;

              case RESULT_PHOTO:
                  createPhotoViewHolder(itemView);
                  break;
          }
        }

        @Override
        public void onClick(View v) {

        }

        private void createPageViewHolder(View itemView) {
            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mAbout = (TextView) itemView.findViewById(R.id.textView_about);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPlatformType = (ImageView) itemView.findViewById(R.id.imageView_platformType);
            mPageDetail = (LinearLayout) itemView.findViewById(R.id.layout_pageDetail);
            mPageDetail.setOnClickListener(this);
        }

        private void createVideoViewHolder(View itemView) {
            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mTitle = (TextView) itemView.findViewById(R.id.textView_title);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
            mPicture.setOnClickListener(this);
        }

        private void createPhotoViewHolder(View itemView) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (mResultType) {
            case RESULT_PAGE:
                itemView = mLayoutInflater.inflate(R.layout.item_search_result_page, parent, false);
                break;

            case RESULT_VIDEO:
                itemView = mLayoutInflater.inflate(R.layout.item_search_result_video, parent, false);
                break;

            case RESULT_PHOTO:
                itemView = mLayoutInflater.inflate(R.layout.item_search_result_photo, parent, false);
                break;
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (mResultType) {

            case RESULT_PAGE:
                bindPageResult(holder, position);
                break;

            case RESULT_VIDEO:
                bindVideoResult(holder, position);
                break;

            case RESULT_PHOTO:
                bindPhotoResult(holder, position);
                break;
        }
    }

    private void bindPageResult(ViewHolder holder, int position) {
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

    private void bindVideoResult(ViewHolder holder, int position) {
        FavoSearchResultData data = mDataset.get(position);

        holder.mTitle.setText(data.getDescription());
        holder.mUserName.setText(data.getUserName());

        Glide.with(mContext.getApplicationContext()).load(data.getPicture())
                .apply(new RequestOptions().override(864, 486))
                .apply(new RequestOptions().centerCrop())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mPicture);

        switch (data.getPlatformType()) {

            case PLATFORM_FACEBOOK:
                Glide.with(mContext.getApplicationContext()).load(R.drawable.icon_facebook_small)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mProfile);
                break;

            case PLATFORM_YOUTUBE:
                Glide.with(mContext.getApplicationContext()).load(R.drawable.icon_youtube_small)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mProfile);
                break;

            case PLATFORM_PINTEREST:
                Glide.with(mContext.getApplicationContext()).load(R.drawable.icon_pinterest_small)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mProfile);
                break;

            case PLATFORM_TWITCH:
                Glide.with(mContext.getApplicationContext()).load(R.drawable.twitch_icon_small)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(holder.mProfile);
                break;
        }
    }

    private void bindPhotoResult(ViewHolder holder, int position) {
        FavoSearchResultData data = mDataset.get(position);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
