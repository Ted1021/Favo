package taewon.navercorp.integratedsns.model.twitch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 2017-11-09.
 */

public class TwitchUserData {

    @SerializedName("data")
    public List<UserInfo> data = null;

    public List<UserInfo> getData() {
        return data;
    }

    public void setData(List<UserInfo> data) {
        this.data = data;
    }

    public class UserInfo {

        @SerializedName("id")
        public String id;
        @SerializedName("login")
        public String login;
        @SerializedName("display_name")
        public String displayName;
        @SerializedName("type")
        public String type;
        @SerializedName("broadcaster_type")
        public String broadcasterType;
        @SerializedName("description")
        public String description;
        @SerializedName("profile_image_url")
        public String profileImageUrl;
        @SerializedName("offline_image_url")
        public String offlineImageUrl;
        @SerializedName("view_count")
        public Integer viewCount;
        @SerializedName("email")
        public String email;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public String getOfflineImageUrl() {
            return offlineImageUrl;
        }

        public void setOfflineImageUrl(String offlineImageUrl) {
            this.offlineImageUrl = offlineImageUrl;
        }

        public Integer getViewCount() {
            return viewCount;
        }

        public void setViewCount(Integer viewCount) {
            this.viewCount = viewCount;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
