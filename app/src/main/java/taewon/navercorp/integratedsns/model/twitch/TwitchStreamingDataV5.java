package taewon.navercorp.integratedsns.model.twitch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 2017-11-13.
 */

public class TwitchStreamingDataV5 {

    @SerializedName("_total")
    private Integer total;
    @SerializedName("streams")
    private List<Stream> streams = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    public class Stream {

        @SerializedName("_id")
        private Integer id;
        @SerializedName("game")
        private String game;
        @SerializedName("broadcast_platform")
        private String broadcastPlatform;
        @SerializedName("community_id")
        private String communityId;
        @SerializedName("community_ids")
        private List<Object> communityIds = null;
        @SerializedName("viewers")
        private Integer viewers;
        @SerializedName("video_height")
        private Integer videoHeight;
        @SerializedName("average_fps")
        private Double averageFps;
        @SerializedName("delay")
        private Integer delay;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("is_playlist")
        private Boolean isPlaylist;
        @SerializedName("stream_type")
        private String streamType;
        @SerializedName("preview")
        private Preview preview;
        @SerializedName("channel")
        private Channel channel;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getGame() {
            return game;
        }

        public void setGame(String game) {
            this.game = game;
        }

        public String getBroadcastPlatform() {
            return broadcastPlatform;
        }

        public void setBroadcastPlatform(String broadcastPlatform) {
            this.broadcastPlatform = broadcastPlatform;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public List<Object> getCommunityIds() {
            return communityIds;
        }

        public void setCommunityIds(List<Object> communityIds) {
            this.communityIds = communityIds;
        }

        public Integer getViewers() {
            return viewers;
        }

        public void setViewers(Integer viewers) {
            this.viewers = viewers;
        }

        public Integer getVideoHeight() {
            return videoHeight;
        }

        public void setVideoHeight(Integer videoHeight) {
            this.videoHeight = videoHeight;
        }

        public Double getAverageFps() {
            return averageFps;
        }

        public void setAverageFps(Double averageFps) {
            this.averageFps = averageFps;
        }

        public Integer getDelay() {
            return delay;
        }

        public void setDelay(Integer delay) {
            this.delay = delay;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Boolean getIsPlaylist() {
            return isPlaylist;
        }

        public void setIsPlaylist(Boolean isPlaylist) {
            this.isPlaylist = isPlaylist;
        }

        public String getStreamType() {
            return streamType;
        }

        public void setStreamType(String streamType) {
            this.streamType = streamType;
        }

        public Preview getPreview() {
            return preview;
        }

        public void setPreview(Preview preview) {
            this.preview = preview;
        }

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public class Preview {

            @SerializedName("small")
            private String small;
            @SerializedName("medium")
            private String medium;
            @SerializedName("large")
            private String large;
            @SerializedName("template")
            private String template;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getTemplate() {
                return template;
            }

            public void setTemplate(String template) {
                this.template = template;
            }

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
}
