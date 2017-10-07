package taewon.navercorp.integratedsns.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
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

    @GET("/subscriptions")
    Call<YoutubeFeedData> subscriptionList (@Query("part") String part, @Query("mine") boolean mine, @Query("key") String key);
}