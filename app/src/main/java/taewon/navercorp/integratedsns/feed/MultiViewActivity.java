package taewon.navercorp.integratedsns.feed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import taewon.navercorp.integratedsns.R;
import taewon.navercorp.integratedsns.model.favo.FavoFeedData;

public class MultiViewActivity extends AppCompatActivity {

    RecyclerView mImageList;
    MultiViewListAdapter mAdapter;
    FavoFeedData mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_view);

        initData();
        initView();
    }

    private void initData(){

        mDataset = (FavoFeedData) getIntent().getSerializableExtra("FEED_DATA");
    }

    private void initView(){

        mImageList = (RecyclerView) findViewById(R.id.recyclerView_imageList);
        mAdapter = new MultiViewListAdapter(MultiViewActivity.this, mDataset);
        mImageList.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MultiViewActivity.this);
        mImageList.setLayoutManager(layoutManager);
    }
}
