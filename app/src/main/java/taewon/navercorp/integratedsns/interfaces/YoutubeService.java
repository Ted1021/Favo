package taewon.navercorp.integratedsns.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import taewon.navercorp.integratedsns.model.YoutubeFeedData;

/**
 * @author 김태원
 * @file YoutubeService.java
 * @brief retrofit interface for youtube
 * @date 2017.10.07
 */

// ?part=snippet%2CcontentDetails&mine=true&key={YOUR_API_KEY}

public interface YoutubeService {

    @Headers("Authorization: Bearer ya29.GlvfBHJrFKD9oUovMrx5Yq_Zyz1RKU2viI35bbRoT81TbjGWGv0vI_lkIUeWCWZDjkRDPRHU0DNzYxgwwiciu1ZVbR1oFFgNVHTsVW-QzExjOwVy7CFGWs3JFFEX")
    @GET("youtube/v3/subscriptions")
    Call<YoutubeFeedData> subscriptionList (@Query("part") String part, @Query("maxResults") int maxResults, @Query("mine") boolean mine);
}