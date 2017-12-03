package taewon.navercorp.integratedsns.model.youtube;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tedkim on 2017. 11. 20..
 */


public class YoutubeTrendVideoData {

    @SerializedName("kind")
    private String kind;
    @SerializedName("etag")
    private String etag;
    @SerializedName("nextPageToken")
    private String nextPageToken;
    @SerializedName("pageInfo")
    private PageInfo pageInfo;
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

    public class Item {

        @SerializedName("kind")
        private String kind;
        @SerializedName("etag")
        private String etag;
        @SerializedName("id")
        private String id;
        @SerializedName("snippet")
        private Snippet snippet;

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

        public class Snippet {

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
            @SerializedName("categoryId")
            private String categoryId;
            @SerializedName("liveBroadcastContent")
            private String liveBroadcastContent;
            @SerializedName("defaultLanguage")
            private String defaultLanguage;
            @SerializedName("localized")
            private Localized localized;
            @SerializedName("defaultAudioLanguage")
            private String defaultAudioLanguage;

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

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getLiveBroadcastContent() {
                return liveBroadcastContent;
            }

            public void setLiveBroadcastContent(String liveBroadcastContent) {
                this.liveBroadcastContent = liveBroadcastContent;
            }

            public String getDefaultLanguage() {
                return defaultLanguage;
            }

            public void setDefaultLanguage(String defaultLanguage) {
                this.defaultLanguage = defaultLanguage;
            }

            public Localized getLocalized() {
                return localized;
            }

            public void setLocalized(Localized localized) {
                this.localized = localized;
            }

            public String getDefaultAudioLanguage() {
                return defaultAudioLanguage;
            }

            public void setDefaultAudioLanguage(String defaultAudioLanguage) {
                this.defaultAudioLanguage = defaultAudioLanguage;
            }

            public class Localized {

                @SerializedName("title")
                private String title;
                @SerializedName("description")
                private String description;

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

            }

            public class Thumbnails {

                @SerializedName("high")
                private High high;

                public High getHigh() {
                    return high;
                }

                public void setHigh(High high) {
                    this.high = high;
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

}
