package taewon.navercorp.integratedsns.model.page;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tedkim on 2017. 10. 22..
 */

public class FacebookPageVideoData {

    @SerializedName("data")
    private List<Video> data = null;
    @SerializedName("paging")
    private Paging paging;

    public List<Video> getData() {
        return data;
    }

    public void setData(List<Video> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public class Video {

        @SerializedName("description")
        private String description;
        @SerializedName("picture")
        private String picture;
        @SerializedName("source")
        private String source;
        @SerializedName("length")
        private Float length;
        @SerializedName("created_time")
        private String created_time;
        @SerializedName("id")
        private String id;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Float getLength() {
            return length;
        }

        public void setLength(Float length) {
            this.length = length;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
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
