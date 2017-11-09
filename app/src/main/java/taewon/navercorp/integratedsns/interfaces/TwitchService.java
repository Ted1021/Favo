package taewon.navercorp.integratedsns.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TwitchService {

    @GET("kraken/oauth2/authorize")
    Call<ResponseBody> getTwitchAccessToken(@Query("client_id") String clientId,
                                            @Query("redirect_uri") String redirectUrl,
                                            @Query("response_type") String responseType,
                                            @Query("scope") String scope,
                                            @Query("state") String state);
}
