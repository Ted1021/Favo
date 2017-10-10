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

    String HEADER = "Authorization: Bearer ya29.GlvgBGFSDx_OVV4vuCvxnmyrAuRYzmbBhRl8wxFNY0XYPb711I5XLPxcpJq4Hrf6TnO0Fma9DcZpJHeiMpW0wt68aCPEzVvESfom0uXOaKPCPeymSwbiARlkwXrz";

    @Headers(HEADER)
    @GET("youtube/v3/subscriptions")
    Call<YoutubeFeedData> subscriptionList (@Query("part") String part, @Query("maxResults") int maxResults, @Query("mine") boolean mine);
}