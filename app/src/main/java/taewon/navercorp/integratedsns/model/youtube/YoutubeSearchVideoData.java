package taewon.navercorp.integratedsns.model.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by USER on 2017-10-11.
 */

@SuppressWarnings("serial")
public class YoutubeSearchVideoData implements Serializable{

    @SerializedName("kind")
    private String kind;
    @SerializedName("etag")
    private String etag;
    @SerializedName("nextPageToken")
    private String nextPageToken;
    @SerializedName("regionCode")
    private String regionCode;
    @SerializedName("pageInfo")
    private PageInfo pageInfo = new PageInfo();
    @SerializedName("items")
    private List<Item> items = null;

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

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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


    @SuppressWarnings("serial")
    public class PageInfo implements Serializable{

        @SerializedName("totalResults")
        @Expose
        private Integer totalResults;
        @SerializedName("resultsPerPage")
        @Expose
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

    @SuppressWarnings("serial")
    public class Item implements Serializable {

        @SerializedName("kind")
        private String kind;
        @SerializedName("etag")
        private String etag;
        @SerializedName("id")
        private Id id;
        @SerializedName("snippet")
        private Snippet snippet = new Snippet();

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

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }

        @SuppressWarnings("serial")
        public class Id implements Serializable{

            @SerializedName("kind")
            private String kind;
            @SerializedName("videoId")
            private String videoId;

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getVideoId() {
                return videoId;
            }

            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }

        }

        @SuppressWarnings("serial")
        public class Snippet implements Serializable{

            @SerializedName("publishedAt")
            private String publishedAt;
            @SerializedName("channelId")
            private String channelId;
            @SerializedName("title")
            private String title;
            @SerializedName("description")
            private String description;
            @SerializedName("thumbnails")
            private Thumbnails thumbnails;
            @SerializedName("channelTitle")
            private String channelTitle;
            @SerializedName("liveBroadcastContent")
            private String liveBroadcastContent;

            private String profileImage;

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

            public String getLiveBroadcastContent() {
                return liveBroadcastContent;
            }

            public void setLiveBroadcastContent(String liveBroadcastContent) {
                this.liveBroadcastContent = liveBroadcastContent;
            }

            public String getProfileImage() {
                return profileImage;
            }

            public void setProfileImage(String profileImage) {
                this.profileImage = profileImage;
            }

            @SuppressWarnings("serial")
            public class Thumbnails implements Serializable {

                @SerializedName("default")
                @Expose
                private Default _default;
                @SerializedName("medium")
                @Expose
                private Medium medium;
                @SerializedName("high")
                @Expose
                private High high;

                public Default getDefault() {
                    return _default;
                }

                public void setDefault(Default _default) {
                    this._default = _default;
                }

                public Medium getMedium() {
                    return medium;
                }

                public void setMedium(Medium medium) {
                    this.medium = medium;
                }

                public High getHigh() {
                    return high;
                }

                public void setHigh(High high) {
                    this.high = high;
                }

            }

            @SuppressWarnings("serial")
            public class High implements Serializable {

                @SerializedName("url")
                @Expose
                private String url;
                @SerializedName("width")
                @Expose
                private Integer width;
                @SerializedName("height")
                @Expose
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

            @SuppressWarnings("serial")
            public class Medium implements Serializable{

                @SerializedName("url")
                @Expose
                private String url;
                @SerializedName("width")
                @Expose
                private Integer width;
                @SerializedName("height")
                @Expose
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

            @SuppressWarnings("serial")
            public class Default implements Serializable {

                @SerializedName("url")
                @Expose
                private String url;
                @SerializedName("width")
                @Expose
                private Integer width;
                @SerializedName("height")
                @Expose
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
