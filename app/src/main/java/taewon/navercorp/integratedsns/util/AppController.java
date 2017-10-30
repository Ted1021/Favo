package taewon.navercorp.integratedsns.util;

import android.app.Application;

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

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
