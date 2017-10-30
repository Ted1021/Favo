package taewon.navercorp.integratedsns.model.page;

/**
 * Created by tedkim on 2017. 10. 29..
 */

public class FavoPageInfoData {

    private String profileImage;
    private String PageName;
    private String coverImage;
    private String description;
    private String subscriptionCount;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPageName() {
        return PageName;
    }

    public void setPageName(String pageName) {
        PageName = pageName;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(String subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }
}
