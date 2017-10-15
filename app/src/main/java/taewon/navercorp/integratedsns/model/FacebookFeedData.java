package taewon.navercorp.integratedsns.model;

/**
 * @author 김태원
 * @file FacebookFeedData.java
 * @brief Model class for facebook
 * @date 2017.09.29
 */

public class FacebookFeedData {

    private String name;
    private String profileImage;
    private String uploadTime;
    private String description;
    private String picture;
    private String video;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
