package taewon.navercorp.integratedsns.interfaces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import taewon.navercorp.integratedsns.model.comment.YoutubePostCommentData;
import taewon.navercorp.integratedsns.model.comment.YoutubeCommentData;
import taewon.navercorp.integratedsns.model.feed.YoutubeSearchVideoData;
import taewon.navercorp.integratedsns.model.feed.YoutubeSubscriptionData;
import taewon.navercorp.integratedsns.model.page.YoutubeChannelInfoData;
import taewon.navercorp.integratedsns.model.page.YoutubeChannelPlaylistData;

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
                                                      @Query("mine") boolean mine);

    @GET("youtube/v3/search")
    Call<YoutubeSearchVideoData> getVideoList(@Header("Authorization") String auth,
                                              @Query("part") String part,
                                              @Query("maxResults") int maxResults,
                                              @Query("channelId") String channelId,
                                              @Query("order") String order,
                                              @Query("type") String type);

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
                                                         @Query("channelId") String channelId);

    @POST("youtube/v3/commentThreads")
    Call<Void> setComment(@Header("Authorization") String auth,
                          @Query("part") String part,
                          @Body YoutubePostCommentData comment
    );
}