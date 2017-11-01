package taewon.navercorp.integratedsns.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 2017-11-01.
 */

public class GiphyImageData {

    @SerializedName("data")
    public List<Info> data = null;
    @SerializedName("pagination")
    public Pagination pagination;
    @SerializedName("meta")
    public Meta meta;

    public List<Info> getData() {
        return data;
    }

    public void setData(List<Info> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public class Info {

        @SerializedName("type")
        public String type;
        @SerializedName("id")
        public String id;
        @SerializedName("slug")
        public String slug;
        @SerializedName("url")
        public String url;
        @SerializedName("username")
        public String username;
        @SerializedName("source")
        public String source;
        @SerializedName("rating")
        public String rating;
        @SerializedName("content_url")
        public String contentUrl;
        @SerializedName("source_tld")
        public String sourceTld;
        @SerializedName("source_post_url")
        public String sourcePostUrl;
        @SerializedName("is_indexable")
        public Integer isIndexable;
        @SerializedName("import_datetime")
        public String importDatetime;
        @SerializedName("trending_datetime")
        public String trendingDatetime;
        @SerializedName("user")
        public User user;
        @SerializedName("images")
        public Images images;
        @SerializedName("title")
        public String title;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getSourceTld() {
            return sourceTld;
        }

        public void setSourceTld(String sourceTld) {
            this.sourceTld = sourceTld;
        }

        public String getSourcePostUrl() {
            return sourcePostUrl;
        }

        public void setSourcePostUrl(String sourcePostUrl) {
            this.sourcePostUrl = sourcePostUrl;
        }

        public Integer getIsIndexable() {
            return isIndexable;
        }

        public void setIsIndexable(Integer isIndexable) {
            this.isIndexable = isIndexable;
        }

        public String getImportDatetime() {
            return importDatetime;
        }

        public void setImportDatetime(String importDatetime) {
            this.importDatetime = importDatetime;
        }

        public String getTrendingDatetime() {
            return trendingDatetime;
        }

        public void setTrendingDatetime(String trendingDatetime) {
            this.trendingDatetime = trendingDatetime;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public class User {

            @SerializedName("avatar_url")
            public String avatarUrl;
            @SerializedName("banner_url")
            public String bannerUrl;
            @SerializedName("profile_url")
            public String profileUrl;
            @SerializedName("username")
            public String username;
            @SerializedName("display_name")
            public String displayName;
            @SerializedName("twitter")
            public String twitter;
            @SerializedName("is_verified")
            public Boolean isVerified;

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public String getBannerUrl() {
                return bannerUrl;
            }

            public void setBannerUrl(String bannerUrl) {
                this.bannerUrl = bannerUrl;
            }

            public String getProfileUrl() {
                return profileUrl;
            }

            public void setProfileUrl(String profileUrl) {
                this.profileUrl = profileUrl;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getDisplayName() {
                return displayName;
            }

            public void setDisplayName(String displayName) {
                this.displayName = displayName;
            }

            public String getTwitter() {
                return twitter;
            }

            public void setTwitter(String twitter) {
                this.twitter = twitter;
            }

            public Boolean getVerified() {
                return isVerified;
            }

            public void setVerified(Boolean verified) {
                isVerified = verified;
            }
        }

        public class Images {

            @SerializedName("original")
            public Original original;
            @SerializedName("fixed_height")
            public FixedHeight fixedHeight;

            public Original getOriginal() {
                return original;
            }

            public void setOriginal(Original original) {
                this.original = original;
            }

            public FixedHeight getFixedHeight() {
                return fixedHeight;
            }

            public void setFixedHeight(FixedHeight fixedHeight) {
                this.fixedHeight = fixedHeight;
            }

            public class FixedHeight {

                @SerializedName("url")
                public String url;
                @SerializedName("width")
                public String width;
                @SerializedName("height")
                public String height;
                @SerializedName("size")
                public String size;
                @SerializedName("mp4")
                public String mp4;
                @SerializedName("mp4_size")
                public String mp4Size;
                @SerializedName("webp")
                public String webp;
                @SerializedName("webp_size")
                public String webpSize;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getWidth() {
                    return width;
                }

                public void setWidth(String width) {
                    this.width = width;
                }

                public String getHeight() {
                    return height;
                }

                public void setHeight(String height) {
                    this.height = height;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getMp4() {
                    return mp4;
                }

                public void setMp4(String mp4) {
                    this.mp4 = mp4;
                }

                public String getMp4Size() {
                    return mp4Size;
                }

                public void setMp4Size(String mp4Size) {
                    this.mp4Size = mp4Size;
                }

                public String getWebp() {
                    return webp;
                }

                public void setWebp(String webp) {
                    this.webp = webp;
                }

                public String getWebpSize() {
                    return webpSize;
                }

                public void setWebpSize(String webpSize) {
                    this.webpSize = webpSize;
                }
            }

            public class Original {

                @SerializedName("url")
                public String url;
                @SerializedName("width")
                public String width;
                @SerializedName("height")
                public String height;
                @SerializedName("size")
                public String size;
                @SerializedName("frames")
                public String frames;
                @SerializedName("mp4")
                public String mp4;
                @SerializedName("mp4_size")
                public String mp4Size;
                @SerializedName("webp")
                public String webp;
                @SerializedName("webp_size")
                public String webpSize;
                @SerializedName("hash")
                public String hash;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getWidth() {
                    return width;
                }

                public void setWidth(String width) {
                    this.width = width;
                }

                public String getHeight() {
                    return height;
                }

                public void setHeight(String height) {
                    this.height = height;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getFrames() {
                    return frames;
                }

                public void setFrames(String frames) {
                    this.frames = frames;
                }

                public String getMp4() {
                    return mp4;
                }

                public void setMp4(String mp4) {
                    this.mp4 = mp4;
                }

                public String getMp4Size() {
                    return mp4Size;
                }

                public void setMp4Size(String mp4Size) {
                    this.mp4Size = mp4Size;
                }

                public String getWebp() {
                    return webp;
                }

                public void setWebp(String webp) {
                    this.webp = webp;
                }

                public String getWebpSize() {
                    return webpSize;
                }

                public void setWebpSize(String webpSize) {
                    this.webpSize = webpSize;
                }

                public String getHash() {
                    return hash;
                }

                public void setHash(String hash) {
                    this.hash = hash;
                }
            }

        }
    }

    public class Pagination {

        @SerializedName("count")
        public Integer count;
        @SerializedName("offset")
        public Integer offset;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }
    }

    public class Meta {

        @SerializedName("status")
        public Integer status;
        @SerializedName("msg")
        public String msg;
        @SerializedName("response_id")
        public String responseId;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getResponseId() {
            return responseId;
        }

        public void setResponseId(String responseId) {
            this.responseId = responseId;
        }
    }
}
