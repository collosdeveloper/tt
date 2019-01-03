package com.knyaz.testtask.base.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.knyaz.testtask.R;
import com.knyaz.testtask.base.ui.interfaces.BackPressable;
import com.knyaz.testtask.base.ui.interfaces.FragmentOperations;

public class BaseActivity extends AppCompatActivity
        implements FragmentOperations {

    protected FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void addFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    @Override
    public void switchFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    @Override
    public Fragment findFragment(Class fragmentClass) {
        return fragmentManager.findFragmentByTag(fragmentClass.getSimpleName());
    }

    @Override
    public Fragment getSecondaryFragment() {
        return fragmentManager.findFragmentById(R.id.container);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSecondaryFragment();
        if (currentFragment != null && currentFragment instanceof BackPressable) {
            if (!((BackPressable) currentFragment).onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}