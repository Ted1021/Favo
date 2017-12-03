package taewon.navercorp.integratedsns.model.youtube;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tedkim on 2017. 11. 23..
 */

public class YoutubePostSubscriptionData {

    @SerializedName("snippet")
    private Snippet snippet = new Snippet();

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public class Snippet {

        @SerializedName("resourceId")
        private PageInfo resource = new PageInfo();

        public PageInfo getResource() {
            return resource;
        }

        public void setResource(PageInfo resource) {
            this.resource = resource;
        }

        public class PageInfo {

            @SerializedName("channelId")
            private String channelId;

            public String getChannelId() {
                return channelId;
            }

            public void setChannelId(String channelId) {
                this.channelId = channelId;
            }
        }

    }
}
