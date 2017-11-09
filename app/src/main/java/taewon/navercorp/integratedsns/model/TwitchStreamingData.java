package taewon.navercorp.integratedsns.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 2017-11-09.
 */

public class TwitchStreamingData {

    @SerializedName("data")
    private List<StreamInfo> data = null;
    @SerializedName("pagination")
    private Pagination pagination;

    public List<StreamInfo> getData() {
        return data;
    }

    public void setData(List<StreamInfo> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public class StreamInfo {

        @SerializedName("id")
        private String id;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("game_id")
        private String gameId;
        @SerializedName("type")
        private String type;
        @SerializedName("title")
        private String title;
        @SerializedName("viewer_count")
        private Integer viewerCount;
        @SerializedName("started_at")
        private String startedAt;
        @SerializedName("language")
        private String language;
        @SerializedName("community_ids")
        private List<String> communityIds = null;
        @SerializedName("thumbnail_url")
        private String thumbnailUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getViewerCount() {
            return viewerCount;
        }

        public void setViewerCount(Integer viewerCount) {
            this.viewerCount = viewerCount;
        }

        public String getStartedAt() {
            return startedAt;
        }

        public void setStartedAt(String startedAt) {
            this.startedAt = startedAt;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public List<String> getCommunityIds() {
            return communityIds;
        }

        public void setCommunityIds(List<String> communityIds) {
            this.communityIds = communityIds;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }
    }

    public class Pagination {

        @SerializedName("cursor")
        private String cursor;

        public String getCursor() {
            return cursor;
        }

        public void setCursor(String cursor) {
            this.cursor = cursor;
        }
    }
}
