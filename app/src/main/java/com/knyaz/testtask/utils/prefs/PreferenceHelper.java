package com.knyaz.testtask.utils.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class PreferenceHelper {
    private SharedPreferences mPrefs;

    protected PreferenceHelper(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    protected String getStringValue(String key, boolean useDecryption) {
        String value = mPrefs.getString(key, null);
        if (useDecryption && value != null) {
            return SecurityHelper.decodeString(value, false);
        } else {
            return value;
        }
    }

    protected String getStringValue(String key, boolean useDecryption, String defaultValue) {
        String value = getStringValue(key, useDecryption);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    protected long getLongValue(String key, boolean useDecryption) {
        String stringValue = getStringValue(key, useDecryption);
        if (stringValue == null) {
            return 0L;
        } else {
            return Long.valueOf(stringValue);
        }
    }

    protected int getIntValue(String key, boolean useDecryption) {
        String stringValue = getStringValue(key, useDecryption);
        if (stringValue == null) {
            return 0;
        } else {
            return Integer.valueOf(stringValue);
        }
    }

    protected boolean getBooleanValue(String key, boolean useDecryption) {
        return getBooleanValue(key, useDecryption, false);
    }

    protected boolean getBooleanValue(String key, boolean useDecryption, boolean defaultValue) {
        String stringValue = getStringValue(key, useDecryption);
        if (stringValue == null) {
            return defaultValue;
        } else {
            return Boolean.valueOf(stringValue);
        }
    }

    protected <U> void storeValue(String key, U value, boolean useEncryption) {
        String resultValue;
        if (useEncryption) {
            resultValue = SecurityHelper.encodeString(String.valueOf(value));
        } else {
            resultValue = String.valueOf(value);
        }
        mPrefs.edit().putString(key, resultValue).apply();
    }

    protected <U> void removeValue(String key) {
        mPrefs.edit().remove(key).apply();
    }

    protected boolean containsValue(String key){
        return mPrefs.contains(key);
    }

    protected void migrateToNewStyleEncryption(String... prefsKeys) {
        SharedPreferences.Editor editor = mPrefs.edit();
        for (String key : prefsKeys) {
            String oldEncodedValue = mPrefs.getString(key, null);
            if (!TextUtils.isEmpty(oldEncodedValue)) {
                String value = SecurityHelper.decodeString(oldEncodedValue, true);
                String newStyleEncodedValue = SecurityHelper.encodeString(value);
                editor.remove(key).putString(key, newStyleEncodedValue);
            }
        }
        editor.apply();
    }
}