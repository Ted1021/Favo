package taewon.navercorp.integratedsns.model;

/**
 * @author 김태원
 * @file FacebookFeedData.java
 * @brief Model class for facebook
 * @date 2017.09.29
 */

public class FacebookFeedData {

    private String name;
    private String upload_time;
    private String description;
    private String picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

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
}
