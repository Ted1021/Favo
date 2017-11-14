package taewon.navercorp.integratedsns.model.favo;

/**
 * Created by USER on 2017-10-25.
 */

public class FavoFollowingInfoData {

    private String _id;
    private String userName;
    private String profile;
    private String platformType;

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

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }
}
