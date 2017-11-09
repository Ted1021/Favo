package taewon.navercorp.integratedsns.model.feed.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import taewon.navercorp.integratedsns.model.feed.FavoFeedData;

/**
 * @author 김태원
 * @file FacebookFeedData.java
 * @brief Model class for facebook
 * @date 2017.09.29
 */

public class FacebookFeedData extends FavoFeedData {

    @SerializedName("data")
    private List<ArticleData> data = new ArrayList<>();
    @SerializedName("paging")
    private Paging paging;

    public List<ArticleData> getData() {
        return data;
    }

    public void setData(List<ArticleData> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public class ArticleData {

        @SerializedName("link")
        private String link;
        @SerializedName("created_time")
        private String createdTime;
        @SerializedName("message")
        private String message;
        @SerializedName("full_picture")
        private String fullPicture;
        @SerializedName("from")
        private From from = new From();
        @SerializedName("source")
        private String source;
        @SerializedName("attachments")
        private Attachments attachments = new Attachments();
        @SerializedName("id")
        private String id;
        @SerializedName("comments")
        private Comments comments = new Comments();
        @SerializedName("likes")
        private Likes likes = new Likes();

        private int contentsType;

        public int getContentsType() {
            return contentsType;
        }

        public void setContentsType(int contentsType) {
            this.contentsType = contentsType;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getFullPicture() {
            return fullPicture;
        }

        public void setFullPicture(String fullPicture) {
            this.fullPicture = fullPicture;
        }

        public From getFrom() {
            return from;
        }

        public void setFrom(From from) {
            this.from = from;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Attachments getAttachments() {
            return attachments;
        }

        public void setAttachments(Attachments attachments) {
            this.attachments = attachments;
        }

        public Comments getComments() {
            return comments;
        }

        public void setComments(Comments comments) {
            this.comments = comments;
        }

        public Likes getLikes() {
            return likes;
        }

        public void setLikes(Likes likes) {
            this.likes = likes;
        }

        public class Likes {

            @SerializedName("summary")
            private Summary summary;

            public class Summary {

                @SerializedName("total_count")
                private int totalCount;

                public int getTotalCount() {
                    return totalCount;
                }

                public void setTotalCount(int totalCount) {
                    this.totalCount = totalCount;
                }
            }

            public Summary getSummary() {
                return summary;
            }

            public void setSummary(Summary summary) {
                this.summary = summary;
            }
        }

        public class From {

            @SerializedName("name")
            private String name;
            @SerializedName("picture")
            private Picture picture;
            @SerializedName("id")
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Picture getPicture() {
                return picture;
            }

            public void setPicture(Picture picture) {
                this.picture = picture;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public class Picture {

            @SerializedName("data")
            private ProfileData profileData;

            public ProfileData getProfileData() {
                return profileData;
            }

            public void setProfileData(ProfileData profileData) {
                this.profileData = profileData;
            }

            public class ProfileData {

                @SerializedName("url")
                @Expose
                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

            }
        }

        public class Attachments {

            @SerializedName("data")
            private List<PhotoSet> data = null;

            public List<PhotoSet> getData() {
                return data;
            }

            public void setData(List<PhotoSet> data) {
                this.data = data;
            }

            public class PhotoSet {

                @SerializedName("subattachments")
                private Subattachments subattachments;

                public Subattachments getSubattachments() {
                    return subattachments;
                }

                public void setSubattachments(Subattachments subattachments) {
                    this.subattachments = subattachments;
                }

                public class Subattachments {

                    @SerializedName("data")
                    private List<Photo> data = null;

                    public List<Photo> getData() {
                        return data;
                    }

                    public void setData(List<Photo> data) {
                        this.data = data;
                    }

                    public class Photo {

                        @SerializedName("media")
                        private Media media;

                        public Media getMedia() {
                            return media;
                        }

                        public void setMedia(Media media) {
                            this.media = media;
                        }

                        public class Media {

                            @SerializedName("image")
                            private Image image;

                            public Image getImage() {
                                return image;
                            }

                            public void setImage(Image image) {
                                this.image = image;
                            }

                            public class Image {

                                @SerializedName("height")
                                private Integer height;
                                @SerializedName("src")
                                private String src;
                                @SerializedName("width")
                                private Integer width;

                                public Integer getHeight() {
                                    return height;
                                }

                                public void setHeight(Integer height) {
                                    this.height = height;
                                }

                                public String getSrc() {
                                    return src;
                                }

                                public void setSrc(String src) {
                                    this.src = src;
                                }

                                public Integer getWidth() {
                                    return width;
                                }

                                public void setWidth(Integer width) {
                                    this.width = width;
                                }
                            }

                        }
                    }
                }
            }
        }

        public class Comments {

            @SerializedName("data")
            private List<CommentData> data = null;
            @SerializedName("paging")
            private Paging paging;
            @SerializedName("summary")
            private Summary summary;

            public List<CommentData> getData() {
                return data;
            }

            public void setData(List<CommentData> data) {
                this.data = data;
            }

            public Paging getPaging() {
                return paging;
            }

            public void setPaging(Paging paging) {
                this.paging = paging;
            }

            public Summary getSummary() {
                return summary;
            }

            public void setSummary(Summary summary) {
                this.summary = summary;
            }

            public class Summary {

                @SerializedName("total_count")
                private int totalCount;

                public int getTotalCount() {
                    return totalCount;
                }

                public void setTotalCount(int totalCount) {
                    this.totalCount = totalCount;
                }
            }

            public class CommentData {

                @SerializedName("from")
                @Expose
                private CommentUser from;
                @SerializedName("message")
                @Expose
                private String message;
                @SerializedName("id")
                @Expose
                private String id;

                public CommentUser getFrom() {
                    return from;
                }

                public void setFrom(CommentUser from) {
                    this.from = from;
                }

                public String getMessage() {
                    return message;
                }

                public void setMessage(String message) {
                    this.message = message;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public class CommentUser {

                    @SerializedName("name")
                    private String name;
                    @SerializedName("picture")
                    private UserProfile picture;
                    @SerializedName("id")
                    private String id;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public UserProfile getPicture() {
                        return picture;
                    }

                    public void setPicture(UserProfile picture) {
                        this.picture = picture;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                }

                public class UserProfile {

                    @SerializedName("data")
                    private UserProfileUrl data;

                    public UserProfileUrl getData() {
                        return data;
                    }

                    public void setData(UserProfileUrl data) {
                        this.data = data;
                    }

                    public class UserProfileUrl {

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
        }
    }

    public class Paging {

        @SerializedName("cursors")
        private Cursors cursors;
        @SerializedName("next")
        private String next;

        public Cursors getCursors() {
            return cursors;
        }

        public void setCursors(Cursors cursors) {
            this.cursors = cursors;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public class Cursors {

            @SerializedName("before")
            private String before;
            @SerializedName("after")
            private String after;

            public String getBefore() {
                return before;
            }

            public void setBefore(String before) {
                this.before = before;
            }

            public String getAfter() {
                return after;
            }

            public void setAfter(String after) {
                this.after = after;
            }

        }
    }
}
