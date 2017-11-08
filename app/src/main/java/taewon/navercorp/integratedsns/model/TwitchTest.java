package taewon.navercorp.integratedsns.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 2017-11-08.
 */

public class TwitchTest {

    @SerializedName("protocol")
    private String protocol;
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;
    @SerializedName("url")
    private String url;

    public String getProtocal() {
        return protocol;
    }

    public void setProtocal(String protocal) {
        this.protocol = protocal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
