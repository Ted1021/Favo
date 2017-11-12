package taewon.navercorp.integratedsns.util;

import android.app.Application;

import com.bumptech.glide.Glide;

import io.realm.Realm;

/**
 * Created by USER on 2017-10-30.
 */

public class AppController extends Application {

    public static final int CONTENTS_IMAGE = 1;
    public static final int CONTENTS_VIDEO = 2;
    public static final int CONTENTS_MULTI_IMAGE = 3;

    public static final int PLATFORM_FACEBOOK = 1;
    public static final int PLATFORM_YOUTUBE = 2;
    public static final int PLATFORM_PINTEREST = 3;
    public static final int PLATFORM_GIPHY = 4;
    public static final int PLATFORM_TWITCH = 5;

    public static final String GIPHY_BASE_URL = "https://api.giphy.com/";
    public static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/";
    public static final String TWITCH_BASE_URL = "https://api.twitch.tv/";
    public static final String TWITCH_REDIRECT_URL = "http://174.138.18.90/message";

    @Override
    public void onCreate() {
        super.onCreate();

        // init Realm
        Realm.init(this);
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
