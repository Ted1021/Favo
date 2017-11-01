package taewon.navercorp.integratedsns.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import taewon.navercorp.integratedsns.model.GiphyImageData;

/**
 * Created by USER on 2017-11-01.
 */

public interface GiphyService {

    // Trend
    @GET("/v1/gifs/trending")
    Call<GiphyImageData> getGiphyTrendImage(@Query("api_key") String apiKey,
                                            @Query("limit") int limit);

    // Search
    @GET("/v1/gifs/search")
    Call<GiphyImageData> getGiphySearchImage(@Query("api_key") String apiKey,
                                       @Query("limit") int limit,
                                       @Query("p") String target);
}
