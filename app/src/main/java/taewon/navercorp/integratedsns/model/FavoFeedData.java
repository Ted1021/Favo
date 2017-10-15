package taewon.navercorp.integratedsns.model;

import com.pinterest.android.pdk.PDKPin;

import java.util.Date;

/**
 * Created by tedkim on 2017. 10. 15..
 */

public class FavoFeedData {

    private int flatformType;
    private int contentsType;
    private Date pubDate;

    private FacebookFeedData facebookData;
    private YoutubeSearchVideoData youtubeData;
    private PDKPin pinterestData;

    public int getFlatformType() {
        return flatformType;
    }

    public void setFlatformType(int flatformType) {
        this.flatformType = flatformType;
    }

    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public FacebookFeedData getFacebookData() {
        return facebookData;
    }

    public void setFacebookData(FacebookFeedData facebookData) {
        this.facebookData = facebookData;
    }

    public YoutubeSearchVideoData getYoutubeData() {
        return youtubeData;
    }

    public void setYoutubeData(YoutubeSearchVideoData youtubeData) {
        this.youtubeData = youtubeData;
    }

    public PDKPin getPinterestData() {
        return pinterestData;
    }

    public void setPinterestData(PDKPin pinterestData) {
        this.pinterestData = pinterestData;
    }
}
