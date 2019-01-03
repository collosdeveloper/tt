package com.knyaz.testtask.screen.login;

import android.util.Pair;

import com.knyaz.testtask.base.ui.interfaces.LoadingView;

public interface LoginView extends LoadingView {
    void setSaveInstanceData(Pair data);

    void setEmailError(String error);

    void setPasswordError(String error);

    void setSignInBtnEnabled(boolean isEnabled);

    void goToLandingScreen();
}