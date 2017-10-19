package taewon.navercorp.integratedsns.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 2017-10-18.
 */

public class FacebookFeedDetailData {

    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("full_picture")
    @Expose
    private String fullPicture;
    @SerializedName("from")
    @Expose
    private From from;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("comments")
    @Expose
    private Comments comments;
    @SerializedName("id")
    @Expose
    private String id;

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

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public class From {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("picture")
        @Expose
        private Picture picture;
        @SerializedName("id")
        @Expose
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

        public class Picture {

            @SerializedName("data")
            @Expose
            private Data data;

            public Data getData() {
                return data;
            }

            public void setData(Data data) {
                this.data = data;
            }

            public class Data {

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
    }

    public class Comments {

        @SerializedName("data")
        @Expose
        private List<CommentData> data = null;
        @SerializedName("paging")
        @Expose
        private Paging paging;

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

        public class CommentData {

            @SerializedName("from")
            private CommentFrom from;
            @SerializedName("message")
            private String message;
            @SerializedName("created_time")
            private String uploadTime;
            @SerializedName("id")
            private String id;

            public CommentFrom getFrom() {
                return from;
            }

            public void setFrom(CommentFrom from) {
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

            public String getUploadTime() {
                return uploadTime;
            }

            public void setUploadTime(String uploadTime) {
                this.uploadTime = uploadTime;
            }

            public class CommentFrom {

                @SerializedName("name")
                private String name;
                @SerializedName("picture")
                private CommentPicture picture;
                @SerializedName("id")
                private String id;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public CommentPicture getPicture() {
                    return picture;
                }

                public void setPicture(CommentPicture picture) {
                    this.picture = picture;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public class CommentPicture {

                    @SerializedName("data")
                    @Expose
                    private Data_ data;

                    public Data_ getData() {
                        return data;
                    }

                    public void setData(Data_ data) {
                        this.data = data;
                    }

                    public class Data_ {

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
            }
        }

        public class Paging {

            @SerializedName("cursors")
            @Expose
            private Cursors cursors;

            public Cursors getCursors() {
                return cursors;
            }

            public void setCursors(Cursors cursors) {
                this.cursors = cursors;
            }

            public class Cursors {

                @SerializedName("before")
                @Expose
                private String before;
                @SerializedName("after")
                @Expose
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
}