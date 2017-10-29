package taewon.navercorp.integratedsns.page.facebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.page.FacebookPageVideoData;

/**
 * Created by tedkim on 2017. 10. 22..
 */

public class PageVideoAdapter extends RecyclerView.Adapter<PageVideoAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<FacebookPageVideoData.Video> mDataset = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    SimpleDateFormat mDateConverter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    public PageVideoAdapter(Context context, ArrayList<FacebookPageVideoData.Video> dataset) {

        mContext = context;
        mDataset = dataset;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mThumbnail, mPlay;
        private TextView mTitle, mCreatedTime, mRunTime;
        private LinearLayout mVideoItem;

        public ViewHolder(View itemView) {
            super(itemView);

            mPlay = (ImageView) itemView.findViewById(R.id.imageView_play);
            mThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);
            mThumbnail.setColorFilter(Color.parseColor("#8e8e8e"), PorterDuff.Mode.MULTIPLY);

            mTitle = (TextView) itemView.findViewById(R.id.textView_title);
            mCreatedTime = (TextView) itemView.findViewById(R.id.textView_createdTime);
            mRunTime = (TextView) itemView.findViewById(R.id.textView_runTime);

            mVideoItem = (LinearLayout) itemView.findViewById(R.id.linearLayout_item);
            mVideoItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            switch (v.getId()) {

                case R.id.linearLayout_item:
                    loadVideo(position);
                    break;
            }
        }
    }

    private void loadVideo(int position) {

        String videoUrl = mDataset.get(position).getSource();

        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }

        Uri uri = Uri.parse(videoUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setDataAndType(uri, "video/*");

        mContext.startActivity(intent);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_video_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FacebookPageVideoData.Video data = mDataset.get(position);

        String date = null;
        try {
            date = mFormat.format(mDateConverter.parse(data.getCreated_time()));
            Log.d("CHECK_DATE", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int min = (int) (data.getLength()/60);
        int sec = (int) (data.getLength()%60);

        holder.mTitle.setText(data.getDescription());
        holder.mCreatedTime.setText(date);
        holder.mRunTime.setText(String.format("%d:%d",min,sec));

        Glide.with(mContext).load(data.getPicture()).into(holder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
