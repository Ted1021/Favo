package taewon.navercorp.integratedsns.model.youtube;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tedkim on 2017. 10. 22..
 */

public class YoutubeChannelInfoData {

    @SerializedName("kind")
    private String kind;
    @SerializedName("etag")
    private String etag;
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
        @SerializedName("brandingSettings")
        private BrandingSettings brandingSettings;

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

        public BrandingSettings getBrandingSettings() {
            return brandingSettings;
        }

        public void setBrandingSettings(BrandingSettings brandingSettings) {
            this.brandingSettings = brandingSettings;
        }

        public class Snippet {

            @SerializedName("title")
            private String title;
            @SerializedName("description")
            private String description;
            @SerializedName("customUrl")
            private String customUrl;
            @SerializedName("publishedAt")
            private String publishedAt;
            @SerializedName("thumbnails")
            private Thumbnails thumbnails;
            @SerializedName("country")
            private String country;

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

            public String getCustomUrl() {
                return customUrl;
            }

            public void setCustomUrl(String customUrl) {
                this.customUrl = customUrl;
            }

            public String getPublishedAt() {
                return publishedAt;
            }

            public void setPublishedAt(String publishedAt) {
                this.publishedAt = publishedAt;
            }

            public Thumbnails getThumbnails() {
                return thumbnails;
            }

            public void setThumbnails(Thumbnails thumbnails) {
                this.thumbnails = thumbnails;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public class Thumbnails {

                @SerializedName("default")
                private Default _default;

                @SerializedName("high")
                private High high;

                public Default get_default() {
                    return _default;
                }

                public void set_default(Default _default) {
                    this._default = _default;
                }

                public High getHigh() {
                    return high;
                }

                public void setHigh(High high) {
                    this.high = high;
                }

                public class Default {

                    @SerializedName("url")
                    private String url;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                }

                public class High {

                    @SerializedName("url")
                    private String url;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                }

            }

        }

        public class BrandingSettings {

            @SerializedName("image")
            private Image image;

            public Image getImage() {
                return image;
            }

            public void setImage(Image image) {
                this.image = image;
            }

            public class Image {

                @SerializedName("bannerMobileImageUrl")
                private String bannerMobileImageUrl;

                @SerializedName("bannerMobileExtraHdImageUrl")
                private String bannerMobileExtraHdImageUrl;

                public String getBannerMobileImageUrl() {
                    return bannerMobileImageUrl;
                }

                public void setBannerMobileImageUrl(String bannerMobileImageUrl) {
                    this.bannerMobileImageUrl = bannerMobileImageUrl;
                }

                public String getBannerMobileExtraHdImageUrl() {
                    return bannerMobileExtraHdImageUrl;
                }

                public void setBannerMobileExtraHdImageUrl(String bannerMobileExtraHdImageUrl) {
                    this.bannerMobileExtraHdImageUrl = bannerMobileExtraHdImageUrl;
                }
            }

        }

    }

}

