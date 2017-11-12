package taewon.navercorp.integratedsns.search;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoSearchResultData;
import taewon.navercorp.integratedsns.search.pagenchannel.SearchPageListAdapter;
import taewon.navercorp.integratedsns.search.photo.SearchPhotoListAdapter;
import taewon.navercorp.integratedsns.search.video.SearchVideoListAdapter;

/**
 * Created by tedkim on 2017. 11. 12..
 */

public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder> {

    private Context mContext;
    private FavoSearchResultData mDataset = new FavoSearchResultData();
    private LayoutInflater mLayoutInflater;

    private static final int RESULT_PAGE = 0;
    private static final int RESULT_VIDEO = 1;
    private static final int RESULT_PHOTO = 2;

    public SearchCategoryAdapter(Context context, FavoSearchResultData dataset) {
        mContext = context;
        mDataset = dataset;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerView mSearchResultList;
        private Button mSeeAll;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            mSearchResultList = (RecyclerView) itemView.findViewById(R.id.recyclerView_searchResult);
            mSeeAll = (Button) itemView.findViewById(R.id.button_more);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.button_more:

                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case RESULT_PAGE:
                return RESULT_PAGE;

            case RESULT_VIDEO:
                return RESULT_VIDEO;

            case RESULT_PHOTO:
                return RESULT_PHOTO;
        }
        return -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        switch (viewType) {
            case RESULT_PAGE:
                itemView = mLayoutInflater.inflate(R.layout.item_search_list_page, parent, false);
                return new ViewHolder(itemView, viewType);

            case RESULT_VIDEO:
                itemView = mLayoutInflater.inflate(R.layout.item_search_list_video, parent, false);
                return new ViewHolder(itemView, viewType);

            case RESULT_PHOTO:
                itemView = mLayoutInflater.inflate(R.layout.item_search_list_photo, parent, false);
                return new ViewHolder(itemView, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RecyclerView.Adapter adapter=null;
        RecyclerView.LayoutManager layoutManager=null;

        switch(position){
            case RESULT_PAGE:
                adapter = new SearchPageListAdapter(mContext, mDataset.getPageSearchResultDataset());
                layoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.HORIZONTAL, false);
                break;

            case RESULT_VIDEO:
                adapter = new SearchVideoListAdapter(mContext, mDataset.getVideoSearchResultDataset());
                layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                break;

            case RESULT_PHOTO:
                adapter = new SearchPhotoListAdapter(mContext, mDataset.getPhotoSearchResultDataset());
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
        }

        holder.mSearchResultList.setAdapter(adapter);
        holder.mSearchResultList.setLayoutManager(layoutManager);

    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
