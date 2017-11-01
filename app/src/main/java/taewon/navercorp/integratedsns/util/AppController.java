package taewon.navercorp.integratedsns.util;

import android.app.Application;

import com.campmobile.android.bandsdk.BandManager;
import com.campmobile.android.bandsdk.BandManagerFactory;

import io.realm.Realm;
import taewon.navercorp.integratedsns.R;

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

    private static final String GIPHY_BASE_URL = "api.giphy.com";
    private static final String YOUTUBE_BASE_URL = "";

    @Override
    public void onCreate() {
        super.onCreate();

        // init Realm
        Realm.init(this);

        // init Band sdk
        BandManager bandManager = BandManagerFactory.getSingleton();
        bandManager.init(AppController.this, getString(R.string.band_client_id), getString(R.string.band_client_secret), true);


    }
}
