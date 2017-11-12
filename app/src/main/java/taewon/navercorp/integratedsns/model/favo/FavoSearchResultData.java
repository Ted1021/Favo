package taewon.navercorp.integratedsns.model.favo;

import java.util.ArrayList;

/**
 * Created by tedkim on 2017. 11. 12..
 */

public class FavoSearchResultData {

    public FavoSearchResultData() {
        pageSearchResultDataset = new ArrayList<>();
        videoSearchResultDataset = new ArrayList<>();
        photoSearchResultDataset = new ArrayList<>();
    }

    private ArrayList<FavoPageSearchResultData> pageSearchResultDataset;
    private ArrayList<FavoVideoSearchResultData> videoSearchResultDataset;
    private ArrayList<FavoPhotoSearchResultData> photoSearchResultDataset;

    public ArrayList<FavoPageSearchResultData> getPageSearchResultDataset() {
        return pageSearchResultDataset;
    }

    public void setPageSearchResultDataset(ArrayList<FavoPageSearchResultData> pageSearchResultDataset) {
        this.pageSearchResultDataset = pageSearchResultDataset;
    }

    public ArrayList<FavoVideoSearchResultData> getVideoSearchResultDataset() {
        return videoSearchResultDataset;
    }

    public void setVideoSearchResultDataset(ArrayList<FavoVideoSearchResultData> videoSearchResultDataset) {
        this.videoSearchResultDataset = videoSearchResultDataset;
    }

    public ArrayList<FavoPhotoSearchResultData> getPhotoSearchResultDataset() {
        return photoSearchResultDataset;
    }

    public void setPhotoSearchResultDataset(ArrayList<FavoPhotoSearchResultData> photoSearchResultDataset) {
        this.photoSearchResultDataset = photoSearchResultDataset;
    }
}