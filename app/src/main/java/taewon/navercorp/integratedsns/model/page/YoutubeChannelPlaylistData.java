package taewon.navercorp.integratedsns.model.page;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tedkim on 2017. 10. 22..
 */

public class YoutubeChannelPlaylistData {

    @SerializedName("kind")
    private String kind;
    @SerializedName("etag")
    private String etag;
    @SerializedName("nextPageToken")
    private String nextPageToken;
    @SerializedName("pageInfo")
    private PageInfo pageInfo;
    @SerializedName("items")
    private List<Item> items = new ArrayList<Item>();

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public class Item {

        @SerializedName("kind")
        private String kind;
        @SerializedName("etag")
        private String etag;
        @SerializedName("id")
        private String id;
        @SerializedName("snippet")
        private Snippet snippet;
        @SerializedName("contentDetails")
        private ContentDetails contentDetails;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getEtag() {
            return etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }

        public ContentDetails getContentDetails() {
            return contentDetails;
        }

        public void setContentDetails(ContentDetails contentDetails) {
            this.contentDetails = contentDetails;
        }

        public class ContentDetails {

            @SerializedName("itemCount")
            private Integer itemCount;

            public Integer getItemCount() {
                return itemCount;
            }

            public void setItemCount(Integer itemCount) {
                this.itemCount = itemCount;
            }

        }

        public class Snippet {

            @SerializedName("publishedAt")
            @Expose
            private String publishedAt;
            @SerializedName("channelId")
            @Expose
            private String channelId;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("thumbnails")
            @Expose
            private Thumbnails thumbnails;
            @SerializedName("channelTitle")
            @Expose
            private String channelTitle;

            public String getPublishedAt() {
                return publishedAt;
            }

            public void setPublishedAt(String publishedAt) {
                this.publishedAt = publishedAt;
            }

            public String getChannelId() {
                return channelId;
            }

            public void setChannelId(String channelId) {
                this.channelId = channelId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public Thumbnails getThumbnails() {
                return thumbnails;
            }

            public void setThumbnails(Thumbnails thumbnails) {
                this.thumbnails = thumbnails;
            }

            public String getChannelTitle() {
                return channelTitle;
            }

            public void setChannelTitle(String channelTitle) {
                this.channelTitle = channelTitle;
            }

            public class Thumbnails {

                @SerializedName("high")
                private High high;
                @SerializedName("standard")
                private Standard standard;

                public High getHigh() {
                    return high;
                }

                public void setHigh(High high) {
                    this.high = high;
                }

                public Standard getStandard() {
                    return standard;
                }

                public void setStandard(Standard standard) {
                    this.standard = standard;
                }

                public class Standard {

                    @SerializedName("url")
                    private String url;
                    @SerializedName("width")
                    private Integer width;
                    @SerializedName("height")
                    private Integer height;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public Integer getWidth() {
                        return width;
                    }

                    public void setWidth(Integer width) {
                        this.width = width;
                    }

                    public Integer getHeight() {
                        return height;
                    }

                    public void setHeight(Integer height) {
                        this.height = height;
                    }

                }

                public class High {

                    @SerializedName("url")
                    private String url;
                    @SerializedName("width")
                    private Integer width;
                    @SerializedName("height")
                    private Integer height;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public Integer getWidth() {
                        return width;
                    }

                    public void setWidth(Integer width) {
                        this.width = width;
                    }

                    public Integer getHeight() {
                        return height;
                    }

                    public void setHeight(Integer height) {
                        this.height = height;
                    }

                }

            }

        }

    }

    public class PageInfo {

        @SerializedName("totalResults")
        private Integer totalResults;
        @SerializedName("resultsPerPage")
        private Integer resultsPerPage;

        public Integer getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(Integer totalResults) {
            this.totalResults = totalResults;
        }

        public Integer getResultsPerPage() {
            return resultsPerPage;
        }

        public void setResultsPerPage(Integer resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
        }

    }


}
