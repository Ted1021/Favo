package taewon.navercorp.integratedsns.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import taewon.navercorp.integratedsns.model.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.model.YoutubeSubscriptionData;

/**
 * @author 김태원
 * @file YoutubeService.java
 * @brief retrofit interface for youtube
 * @date 2017.10.07
 */

public interface YoutubeService {

    @GET("youtube/v3/subscriptions")
    Call<YoutubeSubscriptionData> subscriptionList (@Header("Authorization") String auth, @Query("part") String part, @Query("maxResults") int maxResults, @Query("mine") boolean mine);

    @GET("youtube/v3/search")
    Call<YoutubeSearchVideoData> searchVideoList (@Header("Authorization") String auth, @Query("part") String part, @Query("maxResults") int maxResults, @Query("channelId") String channelId);
}