package taewon.navercorp.integratedsns.model.facebook;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tedkim on 2017. 10. 21..
 */

public class FacebookPageInfoData {

    @SerializedName("id")
    private String id;

    @SerializedName("picture")
    private Picture picture;

    @SerializedName("fan_count")
    private String fan_count;

    @SerializedName("cover")
    private Cover cover;

    @SerializedName("description")
    private String description;

    @SerializedName("name")
    private String name;

    @SerializedName("about")
    private String about;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getFan_count() {
        return fan_count;
    }

    public void setFan_count(String fan_count) {
        this.fan_count = fan_count;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public class Cover {

        @SerializedName("id")
        private String id;

        @SerializedName("source")
        private String source;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
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
