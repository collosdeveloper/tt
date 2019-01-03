package com.knyaz.testtask;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.knyaz.testtask.utils.AppDataInstance;
import com.knyaz.testtask.utils.prefs.PreferenceManager;

/**
 * TestTaskLink : https://docs.google.com/document/d/1MrzdETm3NZVg6v_0YawzkUCizM7L7kpcKvKwhuPP-f8/edit#
 */
public class ApplicationLoader extends MultiDexApplication {
    private static ApplicationLoader sApplicationInstance;
    private AppDataInstance mAppDataInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplicationInstance = this;
    }

    public AppDataInstance getAppDataInstance() {
        if (mAppDataInstance == null) {
            mAppDataInstance = new AppDataInstance();
        }
        return mAppDataInstance;
    }

    public static Context getContext() {
        return sApplicationInstance.getApplicationContext();
    }

    public static ApplicationLoader getApplicationInstance() {
        return sApplicationInstance;
    }

    public PreferenceManager getPreferenceManager() {
        return PreferenceManager.getInstance(this);
    }
}