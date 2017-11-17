package taewon.navercorp.integratedsns.model.facebook;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 2017-10-18.
 */

public class FacebookCommentData {

    @SerializedName("data")
    private List<Comment> data = null;
    @SerializedName("paging")
    private Paging paging;

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
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

    public class Comment {

        @SerializedName("created_time")
        private String createdTime;
        @SerializedName("message")
        private String message;
        @SerializedName("from")
        private From from;
        @SerializedName("id")
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

        public From getFrom() {
            return from;
        }

        public void setFrom(From from) {
            this.from = from;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

            public class Picture {

                @SerializedName("data")
                private Data data;

                public Data getData() {
                    return data;
                }

                public void setData(Data data) {
                    this.data = data;
                }

                public class Data {

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
