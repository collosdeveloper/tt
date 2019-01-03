package com.knyaz.testtask.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.knyaz.testtask.base.ui.BaseActivity;

public class MVPActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPresenter() != null) {
            getPresenter().onAttach(this.getApplicationContext());
            getPresenter().attachToView(this, savedInstanceState);
            getPresenter().onCreate();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getPresenter() != null) {
            getPresenter().onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getPresenter() != null) {
            getPresenter().onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getPresenter() != null) {
            getPresenter().onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().onDestroyView();
            getPresenter().onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getPresenter() != null) {
            getPresenter().onSaveInstanceState(outState);
        }
    }

    public LifecyclePresenter getPresenter() {
        return null;
    }
}