package taewon.navercorp.integratedsns.model.twitch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 2017-11-09.
 */

public class TwitchFollowingData {

    @SerializedName("data")
    private List<FollowingInfo> data = null;
    @SerializedName("pagination")
    private Pagination pagination;

    public List<FollowingInfo> getData() {
        return data;
    }

    public void setData(List<FollowingInfo> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public class FollowingInfo {

        @SerializedName("from_id")
        private String fromId;
        @SerializedName("to_id")
        private String toId;
        @SerializedName("followed_at")
        private String followedAt;

        public String getFromId() {
            return fromId;
        }

        public void setFromId(String fromId) {
            this.fromId = fromId;
        }

        public String getToId() {
            return toId;
        }

        public void setToId(String toId) {
            this.toId = toId;
        }

        public String getFollowedAt() {
            return followedAt;
        }

        public void setFollowedAt(String followedAt) {
            this.followedAt = followedAt;
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
