package taewon.navercorp.integratedsns.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import taewon.navercorp.integratedsns.model.YoutubeSubscriptionData;

/**
 * @author 김태원
 * @file YoutubeService.java
 * @brief retrofit interface for youtube
 * @date 2017.10.07
 */

// ?part=snippet%2CcontentDetails&mine=true&key={YOUR_API_KEY}

public interface YoutubeService {

    @GET("youtube/v3/subscriptions")
    Call<YoutubeSubscriptionData> subscriptionList (@Header("Authorization") String Auth, @Query("part") String part, @Query("maxResults") int maxResults, @Query("mine") boolean mine);
}