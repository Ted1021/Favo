package taewon.navercorp.integratedsns.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.R;

/**
 * Created by USER on 2017-11-13.
 */

public class SearchResultLayout extends LinearLayout implements View.OnClickListener {

    private TextView mTitle;
    private Button mMore;
    private RecyclerView mResultList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int mResultType;

    private static final int RESULT_PAGE = 0;
    private static final int RESULT_VIDEO = 1;
    private static final int RESULT_PHOTO = 2;

    public SearchResultLayout(Context context) {
        super(context);
        initView();
    }

    public SearchResultLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchResultLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SearchResultLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_search_list_page, this, true);

        mTitle = (TextView) view.findViewById(R.id.textView_title);
        mMore = (Button) view.findViewById(R.id.button_more);
        mMore.setOnClickListener(this);
        mResultList = (RecyclerView) view.findViewById(R.id.recyclerView_searchResult);
    }

    public void setView(int resultType, Context context, ArrayList dataset){

        mResultType = resultType;

        switch(mResultType){
            case RESULT_PAGE:
                mTitle.setText("Page & Channel");
                mAdapter = new SearchPageListAdapter(context, dataset);
                mLayoutManager = new GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, true);
                mLayoutManager.scrollToPosition(dataset.size()-1);
                break;

            case RESULT_VIDEO:
                mTitle.setText("Video");
                mAdapter = new SearchVideoListAdapter(context, dataset);
                mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                break;

            case RESULT_PHOTO:
                mTitle.setText("Photo");
                mAdapter = new SearchPhotoListAdapter(context, dataset);
                mLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, true);
                break;
        }

        mResultList.setAdapter(mAdapter);
        mResultList.setLayoutManager(mLayoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mResultList);
    }

    public RecyclerView.Adapter getAdapter(){
        return mAdapter;
    }
    public RecyclerView.LayoutManager getLayoutManager(){
        return mLayoutManager;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button_more:
                Intent intent = new Intent(this.getContext().getString(R.string.search_detail_request));
                intent.putExtra("RESULT_TYPE",mResultType);
                LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
                break;
        }
    }
}
