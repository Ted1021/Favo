package taewon.navercorp.integratedsns.facebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.FacebookFeedData;

/**
 * @author 김태원
 * @file FacebookListAdapter.java
 * @brief inflate facebook articles
 * @date 2017.09.29
 */

public class FacebookListAdapter extends RecyclerView.Adapter<FacebookListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FacebookFeedData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public FacebookListAdapter(Context context, ArrayList<FacebookFeedData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mUserName, mUploadTime, mDescription;
        private ImageView mProfile, mPicture;
        private Button mLike, mComment, mShare;

        public ViewHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
            mDescription = (TextView) itemView.findViewById(R.id.textView_description);

            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mPicture = (ImageView) itemView.findViewById(R.id.imageView_picture);
            mPicture.setOnClickListener(this);

            mLike = (Button) itemView.findViewById(R.id.button_like);
            mComment = (Button) itemView.findViewById(R.id.button_comment);
            mShare = (Button) itemView.findViewById(R.id.button_share);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            switch (v.getId()) {

                case R.id.imageView_picture:
                    loadVideo((mDataset.get(position).getVideo()));
                    break;
            }
        }
    }

    private void loadVideo(String videoUrl) {

        if(TextUtils.isEmpty(videoUrl)){
            return;
        }
        Uri uri = Uri.parse(videoUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/*");
        mContext.startActivity(intent);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_facebook_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FacebookFeedData data = mDataset.get(position);

        try {
            holder.mUserName.setText(data.getName());
            holder.mUploadTime.setText(data.getUpload_time());
            holder.mDescription.setText(data.getDescription());
            Glide.with(mContext).load(data.getPicture()).apply(new RequestOptions().override(holder.mPicture.getMaxWidth())).into(holder.mPicture);

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("ERROR_FACEBOOK", "Facebook List Adapter >>>>> fail to load json object " + position);
        }
    }

    @Override
    public int getItemCount() {

        return mDataset.size();
    }
}

