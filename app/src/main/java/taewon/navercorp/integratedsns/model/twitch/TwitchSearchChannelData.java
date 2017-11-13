package taewon.navercorp.integratedsns.model.twitch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 2017-11-13.
 */

public class TwitchSearchChannelData {

    @SerializedName("_total")
    private Integer total;
    @SerializedName("channels")
    private List<Channel> channels = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public class Channel {

        @SerializedName("mature")
        private Boolean mature;
        @SerializedName("status")
        private String status;
        @SerializedName("broadcaster_language")
        private String broadcasterLanguage;
        @SerializedName("display_name")
        private String displayName;
        @SerializedName("game")
        private String game;
        @SerializedName("language")
        private String language;
        @SerializedName("_id")
        private Integer id;
        @SerializedName("name")
        private String name;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("updated_at")
        private String updatedAt;
        @SerializedName("partner")
        private Boolean partner;
        @SerializedName("logo")
        private String logo;
        @SerializedName("video_banner")
        private String videoBanner;
        @SerializedName("profile_banner")
        private String profileBanner;
        @SerializedName("profile_banner_background_color")
        private String profileBannerBackgroundColor;
        @SerializedName("url")
        private String url;
        @SerializedName("views")
        private Integer views;
        @SerializedName("followers")
        private Integer followers;
        @SerializedName("broadcaster_type")
        private String broadcasterType;
        @SerializedName("description")
        @Expose
        private String description;

        public Boolean getMature() {
            return mature;
        }

        public void setMature(Boolean mature) {
            this.mature = mature;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBroadcasterLanguage() {
            return broadcasterLanguage;
        }

        public void setBroadcasterLanguage(String broadcasterLanguage) {
            this.broadcasterLanguage = broadcasterLanguage;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getGame() {
            return game;
        }

        public void setGame(String game) {
            this.game = game;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Boolean getPartner() {
            return partner;
        }

        public void setPartner(Boolean partner) {
            this.partner = partner;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getVideoBanner() {
            return videoBanner;
        }

        public void setVideoBanner(String videoBanner) {
            this.videoBanner = videoBanner;
        }

        public String getProfileBanner() {
            return profileBanner;
        }

        public void setProfileBanner(String profileBanner) {
            this.profileBanner = profileBanner;
        }

        public String getProfileBannerBackgroundColor() {
            return profileBannerBackgroundColor;
        }

        public void setProfileBannerBackgroundColor(String profileBannerBackgroundColor) {
            this.profileBannerBackgroundColor = profileBannerBackgroundColor;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getViews() {
            return views;
        }

        public void setViews(Integer views) {
            this.views = views;
        }

        public Integer getFollowers() {
            return followers;
        }

        public void setFollowers(Integer followers) {
            this.followers = followers;
        }

        public String getBroadcasterType() {
            return broadcasterType;
        }

        public void setBroadcasterType(String broadcasterType) {
            this.broadcasterType = broadcasterType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
