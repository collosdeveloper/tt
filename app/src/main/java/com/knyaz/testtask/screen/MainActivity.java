package com.knyaz.testtask.screen;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.knyaz.testtask.R;
import com.knyaz.testtask.base.mvp.LifecyclePresenter;
import com.knyaz.testtask.base.mvp.MVPActivity;
import com.knyaz.testtask.screen.landing.LandingFragment;
import com.knyaz.testtask.screen.login.LoginFragment;

public class MainActivity extends MVPActivity implements MainActivityView {
    private MainActivityPresenter mPresenter = new MainActivityPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void goToLoginScreen() {
        switchFragment(new LoginFragment(), false);
    }

    @Override
    public void goToLandingScreen() {
        switchFragment(new LandingFragment(), false);
    }

    @Override
    public LifecyclePresenter getPresenter() {
        return mPresenter;
    }
}