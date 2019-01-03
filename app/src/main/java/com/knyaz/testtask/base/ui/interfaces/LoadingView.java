package com.knyaz.testtask.base.ui.interfaces;

public interface LoadingView {
    void showLoading();

    void hideLoading();

    void showError(String error);
}