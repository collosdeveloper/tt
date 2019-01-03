package com.knyaz.testtask.screen;

import android.text.TextUtils;
import com.knyaz.testtask.ApplicationLoader;
import com.knyaz.testtask.base.mvp.LifecyclePresenter;

public class MainActivityPresenter extends LifecyclePresenter<MainActivityView> {

    @Override
    public void onCreate() {
        super.onCreate();
        if (TextUtils.isEmpty(ApplicationLoader.getApplicationInstance().getPreferenceManager().getToken())) {
            getView().goToLoginScreen();
        } else {
            getView().goToLandingScreen();
        }
    }
}