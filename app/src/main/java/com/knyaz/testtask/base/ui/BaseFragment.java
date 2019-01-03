package com.knyaz.testtask.base.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.knyaz.testtask.R;
import com.knyaz.testtask.base.ui.interfaces.FragmentOperations;

public abstract class BaseFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FragmentOperations fragmentOperations;

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        fragmentOperations = (FragmentOperations) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.info_loading));
        progressDialog.setIndeterminate(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setClickable(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentOperations = null;
    }

    public void showLoading() {
        if(progressDialog != null) {
            progressDialog.show();
        }
    }

    public void hideLoading() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void addFragment(Fragment fragment, boolean addToBackStack) {
        fragmentOperations.addFragment(fragment, addToBackStack);
    }

    protected void switchFragment(Fragment fragment, boolean addToBackStack) {
        fragmentOperations.switchFragment(fragment, addToBackStack);
    }

    public void showError(String errorText) {
        if (!TextUtils.isEmpty(errorText)) {
            Snackbar.make(getParentView(), errorText, Snackbar.LENGTH_LONG).show();
        }
    }

    public abstract View getParentView();

    /**
     * TODO No reason handle rotation if data(video item collections, video info... etc) not save to ROM or DB.
     */
    public void setLockScreenOrientation(boolean lock) {
        getActivity().setRequestedOrientation(lock ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                : ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
}