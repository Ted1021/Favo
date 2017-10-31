package taewon.navercorp.integratedsns.util;

import android.app.Application;

import com.campmobile.android.bandsdk.BandManager;
import com.campmobile.android.bandsdk.BandManagerFactory;

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

    private static final String BAND_CLIENT_ID = "219449230";
    private static final String BAND_CLIENT_SECRET = "LRGkEU_Pm8ixZk7Y9348Z2X2ylve2L5M";

    @Override
    public void onCreate() {
        super.onCreate();

        // init Realm
        Realm.init(this);

        // init Band sdk
        BandManager bandManager = BandManagerFactory.getSingleton();
        bandManager.init(AppController.this, BAND_CLIENT_ID, BAND_CLIENT_SECRET, true);
    }
}
