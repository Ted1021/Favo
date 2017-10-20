package taewon.navercorp.integratedsns.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 2017-10-19.
 */

public class YoutubeCommentData {

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

            @SerializedName("videoId")
            private String videoId;
            @SerializedName("topLevelComment")
            private TopLevelComment topLevelComment;
            @SerializedName("canReply")
            private Boolean canReply;
            @SerializedName("totalReplyCount")
            private Integer totalReplyCount;
            @SerializedName("isPublic")
            private Boolean isPublic;

            public String getVideoId() {
                return videoId;
            }

            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }

            public TopLevelComment getTopLevelComment() {
                return topLevelComment;
            }

            public void setTopLevelComment(TopLevelComment topLevelComment) {
                this.topLevelComment = topLevelComment;
            }

            public Boolean getCanReply() {
                return canReply;
            }

            public void setCanReply(Boolean canReply) {
                this.canReply = canReply;
            }

            public Integer getTotalReplyCount() {
                return totalReplyCount;
            }

            public void setTotalReplyCount(Integer totalReplyCount) {
                this.totalReplyCount = totalReplyCount;
            }

            public Boolean getIsPublic() {
                return isPublic;
            }

            public void setIsPublic(Boolean isPublic) {
                this.isPublic = isPublic;
            }

        }

        public class TopLevelComment {

            @SerializedName("kind")
            private String kind;
            @SerializedName("etag")
            private String etag;
            @SerializedName("id")
            private String id;
            @SerializedName("snippet")
            private Author snippet;

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

            public Author getSnippet() {
                return snippet;
            }

            public void setSnippet(Author snippet) {
                this.snippet = snippet;
            }

            public class Author {

                @SerializedName("authorDisplayName")
                private String authorDisplayName;
                @SerializedName("authorProfileImageUrl")
                private String authorProfileImageUrl;
                @SerializedName("authorChannelUrl")
                private String authorChannelUrl;
                @SerializedName("authorChannelId")
                private AuthorChannelId authorChannelId;
                @SerializedName("videoId")
                private String videoId;
                @SerializedName("textDisplay")
                private String textDisplay;
                @SerializedName("textOriginal")
                private String textOriginal;
                @SerializedName("canRate")
                private Boolean canRate;
                @SerializedName("viewerRating")
                private String viewerRating;
                @SerializedName("likeCount")
                private Integer likeCount;
                @SerializedName("publishedAt")
                private String publishedAt;
                @SerializedName("updatedAt")
                private String updatedAt;

                public String getAuthorDisplayName() {
                    return authorDisplayName;
                }

                public void setAuthorDisplayName(String authorDisplayName) {
                    this.authorDisplayName = authorDisplayName;
                }

                public String getAuthorProfileImageUrl() {
                    return authorProfileImageUrl;
                }

                public void setAuthorProfileImageUrl(String authorProfileImageUrl) {
                    this.authorProfileImageUrl = authorProfileImageUrl;
                }

                public String getAuthorChannelUrl() {
                    return authorChannelUrl;
                }

                public void setAuthorChannelUrl(String authorChannelUrl) {
                    this.authorChannelUrl = authorChannelUrl;
                }

                public AuthorChannelId getAuthorChannelId() {
                    return authorChannelId;
                }

                public void setAuthorChannelId(AuthorChannelId authorChannelId) {
                    this.authorChannelId = authorChannelId;
                }

                public String getVideoId() {
                    return videoId;
                }

                public void setVideoId(String videoId) {
                    this.videoId = videoId;
                }

                public String getTextDisplay() {
                    return textDisplay;
                }

                public void setTextDisplay(String textDisplay) {
                    this.textDisplay = textDisplay;
                }

                public String getTextOriginal() {
                    return textOriginal;
                }

                public void setTextOriginal(String textOriginal) {
                    this.textOriginal = textOriginal;
                }

                public Boolean getCanRate() {
                    return canRate;
                }

                public void setCanRate(Boolean canRate) {
                    this.canRate = canRate;
                }

                public String getViewerRating() {
                    return viewerRating;
                }

                public void setViewerRating(String viewerRating) {
                    this.viewerRating = viewerRating;
                }

                public Integer getLikeCount() {
                    return likeCount;
                }

                public void setLikeCount(Integer likeCount) {
                    this.likeCount = likeCount;
                }

                public String getPublishedAt() {
                    return publishedAt;
                }

                public void setPublishedAt(String publishedAt) {
                    this.publishedAt = publishedAt;
                }

                public String getUpdatedAt() {
                    return updatedAt;
                }

                public void setUpdatedAt(String updatedAt) {
                    this.updatedAt = updatedAt;
                }

                public class AuthorChannelId {

                    @SerializedName("value")
                    @Expose
                    private String value;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                }
            }
        }
    }
}
