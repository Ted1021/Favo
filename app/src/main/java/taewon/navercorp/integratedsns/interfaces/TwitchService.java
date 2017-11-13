package taewon.navercorp.integratedsns.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import taewon.navercorp.integratedsns.model.twitch.TwitchSearchChannelData;
import taewon.navercorp.integratedsns.model.twitch.TwitchStreamingData;
import taewon.navercorp.integratedsns.model.twitch.TwitchUserData;
import taewon.navercorp.integratedsns.model.twitch.TwitchFollowingData;
import taewon.navercorp.integratedsns.model.twitch.TwitchVideoData;

public interface TwitchService {

    @GET("kraken/oauth2/authorize")
    Call<ResponseBody> getTwitchAccessToken(@Query("client_id") String clientId,
                                            @Query("redirect_uri") String redirectUrl,
                                            @Query("response_type") String responseType,
                                            @Query("scope") String scope,
                                            @Query("state") String state);
    @GET("kraken/search/channels")
    Call<TwitchSearchChannelData> searchTwitchChannel(@Header("Accept") String accept,
                                                      @Header("Client-ID") String clientId,
                                                      @Query("query") String query,
                                                      @Query("limit") int limit);

    @GET("helix/users")
    Call<TwitchUserData> getTwitchUserInfo(@Header("Client-ID") String clientId,
                                           @Header("Authorization") String auth,
                                           @Query("id") String userId);

    @GET("helix/users/follows")
    Call<TwitchFollowingData> getTwitchFollowingInfo(@Header("Client-ID") String clientId,
                                                     @Query("from_id") String id,
                                                     @Query("first") int limit);

    @GET("helix/videos")
    Call<TwitchVideoData> getTwitchVideoInfo(@Header("Client-ID") String clientId,
                                             @Query("user_id") String userId,
                                             @Query("first") int limit);

    @GET("helix/streams")
    Call<TwitchStreamingData> getTwitchStreams(@Header("Client-ID") String clinetId,
                                               @Query("first") int count);
}
