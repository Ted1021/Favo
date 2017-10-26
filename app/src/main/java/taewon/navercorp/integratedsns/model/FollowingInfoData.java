package taewon.navercorp.integratedsns.model;

/**
 * Created by USER on 2017-10-25.
 */

public class FollowingInfoData {

    private String _id;
    private String userName;
    private String profile;
    private int platformType;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }
}
