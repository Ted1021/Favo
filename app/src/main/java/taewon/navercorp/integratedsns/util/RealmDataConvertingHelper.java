package taewon.navercorp.integratedsns.util;

import com.google.gson.Gson;

import taewon.navercorp.integratedsns.model.feed.FavoFeedData;
import taewon.navercorp.integratedsns.model.feed.FavoMyPinData;

/**
 * Created by USER on 2017-10-30.
 */

public class RealmDataConvertingHelper {

    public static FavoMyPinData convertToRealmObject(FavoFeedData target){

        Gson gson = new Gson();
        String json = gson.toJson(target);
        return gson.fromJson(json, FavoMyPinData.class);
    }
}
