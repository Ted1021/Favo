package taewon.navercorp.integratedsns.profile;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import taewon.navercorp.integratedsns.R;

import static android.content.Context.MODE_PRIVATE;
import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

/**
 * @author 김태원
 * @file TumblrFragment.java
 * @brief show tumblr contents, search & add tumblr channels
 * @date 2017.10.13
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String BOARD_FIELDS = "id,name";
    private static final String PIN_FIELDS = "created_at,creator,id,image, media,note,original_link";

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private RecyclerView mRecyclerView;
    private PinterestListAdapter mAdapter;
    private ArrayList<PDKPin> mDataset = new ArrayList<>();

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mLayoutDisconnection;
    private Button mConnectTumblr;

    private PDKClient mPinterestClient;

    private ImageView mProfile;
    private TextView mUserName;
    private ImageButton mSetting;
    private ViewPager mViewPager;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initData();
        initView(view);

        return view;
    }

    private void initData() {

        mPref = getContext().getSharedPreferences(getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();

        PDKClient.configureInstance(getContext(), getString(R.string.pinterest_app_id));
        mPinterestClient = PDKClient.getInstance();
    }

    private void initView(View view) {

        mProfile = (ImageView) view.findViewById(R.id.imageView_profile);
        mUserName = (TextView) view.findViewById(R.id.textView_userName);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mSetting = (ImageButton) view.findViewById(R.id.button_setting);
        mSetting.setOnClickListener(this);
    }

    private void getFollowingBoards() {

        mPinterestClient.getMyFollowedBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);

                mDataset.clear();
                for (PDKBoard board : response.getBoardList()) {
                    Log.d("CHECK_BOARD", " >>>>> " + board.getName());
                    new GetFollowingPins().executeOnExecutor(THREAD_POOL_EXECUTOR, board.getUid());
                }
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
            }
        });
    }

    private class GetFollowingPins extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            mPinterestClient.getBoardPins(params[0], PIN_FIELDS, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    super.onSuccess(response);

                    mDataset.addAll(response.getPinList());
                    Collections.sort(mDataset, new Comparator<PDKPin>() {
                        @Override
                        public int compare(PDKPin o1, PDKPin o2) {

                            return o2.getCreatedAt().toString().compareToIgnoreCase(o1.getCreatedAt().toString());
                        }
                    });
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(PDKException exception) {
                    super.onFailure(exception);
                    exception.printStackTrace();
                }
            });

            return null;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_setting:

                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onRefresh() {

    }
}