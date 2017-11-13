package taewon.navercorp.integratedsns.search;

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

public class SearchVideoListAdapter extends RecyclerView.Adapter<SearchVideoListAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<FavoSearchResultData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public SearchVideoListAdapter(Context context, ArrayList<FavoSearchResultData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // common components
        private TextView mUserName, mTitle;
        private ImageView mProfile, mPicture;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mTitle = (TextView) itemView.findViewById(R.id.textView_title);
            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_search_result_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FavoSearchResultData data = mDataset.get(position);

        holder.mUserName.setText(data.getUserName());
        holder.mTitle.setText(data.getDescription());

        Glide.with(mContext.getApplicationContext()).load(data.getPicture())
                .apply(new RequestOptions().override(864, 486))
                .apply(new RequestOptions().centerCrop())
//                .thumbnail(0.5f)
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

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
