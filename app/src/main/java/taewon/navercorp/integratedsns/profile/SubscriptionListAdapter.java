package taewon.navercorp.integratedsns.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

/**
 * Created by USER on 2017-10-16.
 */

public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.ViewHolder> {

    Context mContext;
    ArrayList mDataset;
    LayoutInflater mLayoutInflater;

    public SubscriptionListAdapter(Context context, ArrayList dataset){

        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mProfile;
        TextView mUserName;

        public ViewHolder(View itemView) {
            super(itemView);

            mProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
            mUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            mUserName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            switch (v.getId()) {

                case R.id.textView_userName:

                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_subscriptor, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
