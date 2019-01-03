package com.knyaz.testtask.utils.prefs;

import android.content.Context;
import android.text.TextUtils;

import com.knyaz.testtask.api.Api;
import com.knyaz.testtask.api.ServerSession;

public class PreferenceManager extends PreferenceHelper {
    public final String TAG = this.getClass().getSimpleName();

    private static PreferenceManager sInstance;

    // AuthData
    private static final String TOKEN = "token";
    private static final String SESSION = "session";

    public static PreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceManager(context.getApplicationContext());
        }
        return sInstance;
    }

    protected PreferenceManager(Context context) {
        super(context);
    }

    public void saveToken(String token) {
        storeValue(TOKEN, token, true);
    }

    public String getToken() {
        return getStringValue(TOKEN, true);
    }

    public void saveSession(ServerSession session) {
        storeValue(SESSION, Api.getInst().getGson().toJson(session), true);
    }

    public ServerSession loadSession() {
        String stringValue = getStringValue(SESSION, true);
        if (TextUtils.isEmpty(stringValue)) {
            return null;
        } else {
            return Api.getInst().getGson().fromJson(stringValue, ServerSession.class);
        }
    }

    public void clearSession() {
        removeValue(SESSION);
        removeValue(TOKEN);
    }
}