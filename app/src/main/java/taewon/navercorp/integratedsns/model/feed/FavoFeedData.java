package taewon.navercorp.integratedsns.model.feed;

import com.pinterest.android.pdk.PDKPin;

import java.util.Date;

/**
 * Created by tedkim on 2017. 10. 15..
 */

public class FavoFeedData {

    private int platformType;
    private int contentsType;
    private Date pubDate;

    private FacebookFeedData.ArticleData facebookData;
    private YoutubeSearchVideoData.Item youtubeData;
    private PDKPin pinterestData;

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
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

    public FacebookFeedData.ArticleData getFacebookData() {
        return facebookData;
    }

    public void setFacebookData(FacebookFeedData.ArticleData facebookData) {
        this.facebookData = facebookData;
    }

    public YoutubeSearchVideoData.Item getYoutubeData() {
        return youtubeData;
    }

    public void setYoutubeData(YoutubeSearchVideoData.Item youtubeData) {
        this.youtubeData = youtubeData;
    }

    public PDKPin getPinterestData() {
        return pinterestData;
    }

    public void setPinterestData(PDKPin pinterestData) {
        this.pinterestData = pinterestData;
    }
}
