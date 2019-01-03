package com.knyaz.testtask.screen.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.knyaz.testtask.ApplicationLoader;
import com.knyaz.testtask.R;
import com.knyaz.testtask.api.Api;
import com.knyaz.testtask.api.model.auth.AuthDataResponse;
import com.knyaz.testtask.api.rx_tasks.auth.Login;
import com.knyaz.testtask.api.utils.ErrorResponse;
import com.knyaz.testtask.base.mvp.LifecyclePresenter;
import com.knyaz.testtask.base.mvp.State;
import com.knyaz.testtask.utils.UserFieldsValidator;
import com.knyaz.testtask.utils.prefs.PreferenceManager;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends LifecyclePresenter<LoginView> {
    private UserFieldsValidator mValidator;
    private boolean mIsEmailValid, mIsPasswordValid;

    public LoginPresenter() {
        mValidator = UserFieldsValidator.getInstance();
    }

    @Override
    public void attachToView(LoginView view, @Nullable Bundle savedInstanceState) {
        super.attachToView(view, savedInstanceState);
        getView().setSaveInstanceData(ApplicationLoader.getApplicationInstance().getAppDataInstance().getLoginData());
    }

    public void checkEmail(String email) {
        mIsEmailValid = validateEmail(email);
        getView().setSignInBtnEnabled(validateAllData());
    }

    public void checkPassword(String password) {
        mIsPasswordValid = validatePassword(password);
        getView().setSignInBtnEnabled(validateAllData());
    }

    private boolean validateAllData() {
        return mIsEmailValid && mIsPasswordValid;
    }

    public void onSignInClicked(String email, String password) {
        if (validateEmail(email) & validatePassword(password)) {
            Disposable disposable = new Login(email, password).getTask()
                    .doOnNext(this::storeDataToPref)
                    .doOnSubscribe(d -> getView().showLoading())
                    .doOnEach(n -> getView().hideLoading())
                    .subscribe(authDataResponse -> getView().goToLandingScreen(),
                            error -> getView().showError(ErrorResponse.getServerMessage(error)));
            monitor(disposable, State.DESTROY_VIEW);
        }
    }

    private void storeDataToPref(AuthDataResponse authData) {
        PreferenceManager preferenceManager = ApplicationLoader.getApplicationInstance().getPreferenceManager();
        preferenceManager.saveToken(authData.token);
        Api.getInst().createSessionWithAuthData(authData.token);
    }

    private boolean validateEmail(String email) {
        getView().setEmailError(null);
        if (!mValidator.validateNotEmpty(email)) {
            getView().setEmailError(getResources().getString(R.string.error_empty_email));
            return false;
        }
        if (!mValidator.validateEmail(email)) {
            getView().setEmailError(getResources().getString(R.string.error_invalid_email));
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        getView().setPasswordError(null);
        if (!mValidator.validateNotEmpty(password)) {
            getView().setPasswordError(getResources().getString(R.string.error_empty_password));
            return false;
        }
        if (!mValidator.validatePasswordLength(password)) {
            getView().setPasswordError(getResources().getString(R.string.error_short_password));
            return false;
        }
        if (!mValidator.validateNoSpaces(password)) {
            getView().setPasswordError(getResources().getString(R.string.error_incorrect_password));
            return false;
        }
        return true;
    }
}