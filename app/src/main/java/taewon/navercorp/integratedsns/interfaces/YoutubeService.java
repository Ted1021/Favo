package taewon.navercorp.integratedsns.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import taewon.navercorp.integratedsns.model.youtube.YoutubeChannelInfoData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeChannelPlaylistData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeCommentData;
import taewon.navercorp.integratedsns.model.youtube.YoutubePostCommentData;
import taewon.navercorp.integratedsns.model.youtube.YoutubePostSubscriptionData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSearchChannelData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeSubscriptionData;
import taewon.navercorp.integratedsns.model.youtube.YoutubeTrendVideoData;

/**
 * @author 김태원
 * @file YoutubeService.java
 * @brief retrofit interface for youtube
 * @date 2017.10.07
 */

public interface YoutubeService {

    @GET("youtube/v3/subscriptions")
    Call<YoutubeSubscriptionData> getSubscriptionList(@Header("Authorization") String auth,
                                                      @Query("part") String part,
                                                      @Query("maxResults") int maxResults,
                                                      @Query("mine") boolean mine,
                                                      @Query("forChannelId") String channelId);

    @POST("/youtube/v3/subscriptions")
    Call<ResponseBody> insertSubscription(@Header("Authorization") String auth,
                                          @Query("part") String part,
                                          @Body YoutubePostSubscriptionData channelId);

    @DELETE("/youtube/v3/subscriptions")
    Call<ResponseBody> deleteSubscription(@Header("Authorization") String auth,
                                          @Query("id") String channelId);

    @GET("youtube/v3/search")
    Call<YoutubeSearchVideoData> getVideoList(@Header("Authorization") String auth,
                                              @Query("part") String part,
                                              @Query("maxResults") int maxResults,
                                              @Query("channelId") String channelId,
                                              @Query("pageToken") String pageToken,
                                              @Query("eventType") String eventType,
                                              @Query("q") String query,
                                              @Query("order") String order,
                                              @Query("type") String type,
                                              @Query("chart") String chart,
                                              @Query("regionCode") String regionCode,
                                              @Query("relatedToVideoId") String relatedTovideoId);

    @GET("youtube/v3/videos")
    Call<YoutubeTrendVideoData> getTrendVideoList(@Header("Authorization") String auth,
                                                  @Query("part") String part,
                                                  @Query("maxResults") int maxResults,
                                                  @Query("chart") String chart,
                                                  @Query("regionCode") String regionCode);

    @GET("youtube/v3/search")
    Call<YoutubeSearchChannelData> searchChannelList (@Header("Authorization") String auth,
                                                      @Query("part") String part,
                                                      @Query("maxResults") int maxResults,
                                                      @Query("order") String order,
                                                      @Query("type") String type,
                                                      @Query("q") String query);

    @GET("youtube/v3/commentThreads")
    Call<YoutubeCommentData> getCommentList(@Header("Authorization") String auth,
                                            @Query("part") String part,
                                            @Query("maxResults") int maxResults,
                                            @Query("videoId") String videoId);

    @GET("youtube/v3/commentThreads")
    Call<YoutubeCommentData> getCommentListNext(@Header("Authorization") String auth,
                                                @Query("part") String part,
                                                @Query("pageToken") String pageToken,
                                                @Query("maxResults") int maxResults,
                                                @Query("videoId") String videoId);

    @GET("youtube/v3/channels")
    Call<YoutubeChannelInfoData> getChannelInfo(@Header("Authorization") String auth,
                                                @Query("part") String part,
                                                @Query("id") String channelId);

    @GET("youtube/v3/playlists")
    Call<YoutubeChannelPlaylistData> getChannelPlaylist (@Header("Authorization") String auth,
                                                         @Query("part") String part,
                                                         @Query("maxResults") int maxResults,
                                                         @Query("channelId") String channelId,
                                                         @Query("pageToken") String pageToken);

    @POST("youtube/v3/commentThreads")
    Call<Void> setComment(@Header("Authorization") String auth,
                          @Query("part") String part,
                          @Body YoutubePostCommentData comment
    );
}