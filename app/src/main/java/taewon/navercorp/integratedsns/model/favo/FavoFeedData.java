package taewon.navercorp.integratedsns.model.favo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tedkim on 2017. 10. 15..
 */

@SuppressWarnings("serial")
public class FavoFeedData implements Serializable{

    private String feedId;
    private int platformType;
    private int contentsType;
    private Date pubDate;
    private String pageId;
    private String profileImage;
    private String userName;
    private String createdTime;
    private String picture;
    private String link;
    private String videoUrl;
    private String description;
    private String[] subattachments;
    private int likeCount;
    private int commentCount;
    private String nextCursor, beforeCursor;

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String[] getSubattatchments() {
        return subattachments;
    }

    public void setSubattatchments(String[] subattatchments) {
        this.subattachments = subattatchments;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String[] getSubattachments() {
        return subattachments;
    }

    public void setSubattachments(String[] subattachments) {
        this.subattachments = subattachments;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getBeforeCursor() {
        return beforeCursor;
    }

    public void setBeforeCursor(String beforeCursor) {
        this.beforeCursor = beforeCursor;
    }
}
