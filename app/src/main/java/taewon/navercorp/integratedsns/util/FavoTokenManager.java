package taewon.navercorp.integratedsns.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import taewon.navercorp.integratedsns.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by USER on 2017-11-14.
 */

public class FavoTokenManager {

    private static FavoTokenManager instance;
    private static HashMap<String, String> mFavoTokens;

    private Context mContext;
    private static SharedPreferences mPref;
    private static SharedPreferences.Editor mEditor;

    private FavoTokenManager() {
        mFavoTokens = new HashMap<>();
        mContext = AppController.getFavoContext();
        mPref = mContext.getSharedPreferences(mContext.getString(R.string.tokens), MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    public static FavoTokenManager getInstance() {

        if (instance == null) {
            return instance = new FavoTokenManager();
        }
        return instance;
    }

    public void createToken(String platformType, String token) {
        mEditor.putString(platformType, token);
        mEditor.commit();
        mFavoTokens.put(platformType, token);
    }

    public String getCurrentToken(String platformType) {
        return mFavoTokens.get(platformType);
    }

    public HashMap checkTokenStatus() {
        return mFavoTokens;
    }

    public void removeToken(String platFormType) {
        mEditor.remove(platFormType);
        mEditor.commit();
        mFavoTokens.remove(platFormType);
    }

    public void removeAllTokens() {
        mEditor.clear();
        mEditor.commit();
        mFavoTokens.clear();
    }
}
