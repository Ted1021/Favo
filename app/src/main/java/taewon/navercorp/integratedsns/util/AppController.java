package taewon.navercorp.integratedsns.util;

import android.app.Application;

import com.bumptech.glide.Glide;

import io.realm.Realm;

/**
 * Created by USER on 2017-10-30.
 */

public class AppController extends Application {

    private static AppController instance;

    public static final int CONTENTS_IMAGE = 1;
    public static final int CONTENTS_VIDEO = 2;
    public static final int CONTENTS_MULTI_IMAGE = 3;

    public static final String PLATFORM_FACEBOOK = "facebook";
    public static final String PLATFORM_YOUTUBE = "youtube";
    public static final String PLATFORM_PINTEREST = "pinterest";
    public static final String PLATFORM_GIPHY = "giphy";
    public static final String PLATFORM_TWITCH = "twitch";

    public static final int RESULT_PAGE = 0;
    public static final int RESULT_VIDEO = 1;
    public static final int RESULT_PHOTO = 2;

    public static final String GIPHY_BASE_URL = "https://api.giphy.com/";
    public static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    public static final String TWITCH_BASE_URL = "https://api.twitch.tv/";
    public static final String TWITCH_REDIRECT_URL = "http://174.138.18.90/message";
    public static final String TWITCH_ACCEPT_CODE = "application/vnd.twitchtv.v5+json";

    @Override
    public void onCreate() {
        super.onCreate();

        // init application context
        instance = this;

        // init Realm
        Realm.init(this);
    }

    public static AppController getFavoContext(){
        return instance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }
}
