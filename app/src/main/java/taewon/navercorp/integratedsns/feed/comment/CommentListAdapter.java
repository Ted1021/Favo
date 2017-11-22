package taewon.navercorp.integratedsns.feed.comment;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoCommentData;

/**
 * Created by USER on 2017-11-10.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<FavoCommentData> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    private SimpleDateFormat mStringFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public CommentListAdapter(Context context, ArrayList<FavoCommentData> dataset) {

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mCommentProfile;
        TextView mCommentUserName, mCommentText, mUploadTime;

        public ViewHolder(View itemView) {
            super(itemView);

            mCommentProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mCommentUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mCommentText = (TextView) itemView.findViewById(R.id.textView_comment);
            mUploadTime = (TextView) itemView.findViewById(R.id.textView_uploadTime);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FavoCommentData data = mDataset.get(position);

        holder.mCommentUserName.setText(data.getUserName());
        holder.mCommentText.setText(data.getMessage());

        try {
            String date = mStringFormat.format(mDateFormat.parse(data.getCreatedTime()));
            holder.mUploadTime.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Glide.with(mContext.getApplicationContext())
                .load(data.getProfileImage())
                .apply(new RequestOptions().circleCropTransform())
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mCommentProfile);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
