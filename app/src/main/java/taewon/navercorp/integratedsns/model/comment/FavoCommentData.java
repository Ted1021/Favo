package taewon.navercorp.integratedsns.model.comment;

/**
 * Created by tedkim on 2017. 10. 29..
 */

public class FavoCommentData {

    protected String profileImage;
    protected String UserName;
    protected String CreatedTime;
    protected String message;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
