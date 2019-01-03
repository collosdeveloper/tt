package com.knyaz.testtask.base.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.knyaz.testtask.base.ui.BaseFragment;

public abstract class MVPFragment extends BaseFragment {
    public abstract LifecyclePresenter getPresenter();

    public void onPostViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onAttachToContext(Context context) {
        super.onAttachToContext(context);
        if (getPresenter() != null) {
            getPresenter().onAttach(context);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPresenter() != null) {
            getPresenter().onCreate();
        }
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onPostViewCreated(view, savedInstanceState);
        if (getPresenter() != null) {
            getPresenter().attachToView(this, savedInstanceState);
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
    public void onDestroyView() {
        super.onDestroyView();
        if (getPresenter() != null) {
            getPresenter().onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
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
}