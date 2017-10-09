package taewon.navercorp.integratedsns.facebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

/**
 * @author 김태원
 * @file FacebookListAdapter.java
 * @brief inflate facebook articles
 * @date 2017.09.29
 */

public class FacebookListAdapter extends RecyclerView.Adapter<FacebookListAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<JSONObject> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public FacebookListAdapter(Context context, ArrayList<JSONObject> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

            mLike = (Button) itemView.findViewById(R.id.button_like);
            mComment = (Button) itemView.findViewById(R.id.button_comment);
            mShare = (Button) itemView.findViewById(R.id.button_share);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_facebook_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        JSONObject data = mDataset.get(position);
        try {
            holder.mUserName.setText(data.getString("name"));
            holder.mUploadTime.setText(data.getString("created_time"));
            holder.mDescription.setText(data.getString("description"));
            Glide.with(mContext).load(data.getString("full_picture")).into(holder.mPicture);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR_FACEBOOK", "fail to load json object");
        }
    }

    @Override
    public int getItemCount() {

        return mDataset.size();
    }
}
