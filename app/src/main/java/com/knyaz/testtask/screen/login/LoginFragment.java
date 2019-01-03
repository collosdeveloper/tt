package com.knyaz.testtask.screen.login;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.knyaz.testtask.ApplicationLoader;
import com.knyaz.testtask.R;
import com.knyaz.testtask.base.mvp.LifecyclePresenter;
import com.knyaz.testtask.base.mvp.MVPFragment;
import com.knyaz.testtask.screen.landing.LandingFragment;
import com.knyaz.testtask.utils.PlatformUtils;
import com.knyaz.testtask.utils.SimpleTextWatcher;

public class LoginFragment extends MVPFragment implements LoginView {
    private LoginPresenter mPresenter = new LoginPresenter();

    // UI
    private View mParentView;
    private TextInputLayout mEmailInputLayout;
    private TextInputLayout mPasswordInputLayout;
    private EditText mEdtEmailView;
    private EditText mEdtPasswordView;
    private Button mBtnSignIn;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        ApplicationLoader.getApplicationInstance().getAppDataInstance().saveLoginData(mEdtEmailView.getText().toString(),
                mEdtPasswordView.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onPostViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onPostViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View view) {
        mParentView = view.findViewById(R.id.parent_view);
        mEmailInputLayout = view.findViewById(R.id.input_layout_email);
        mPasswordInputLayout = view.findViewById(R.id.input_layout_password);
        mEdtEmailView = view.findViewById(R.id.edt_input_email);
        mEdtPasswordView = view.findViewById(R.id.edt_input_password);
        mBtnSignIn = view.findViewById(R.id.btn_sign_in);
        mBtnSignIn.setOnClickListener(v -> signIn());
        initLoginInput();
        initPasswordInput();
    }

    private void initLoginInput() {
        mEdtEmailView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.checkEmail(s.toString());
            }
        });
    }

    private void initPasswordInput() {
        mEdtPasswordView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.checkPassword(s.toString());
            }
        });
        mEdtPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                signIn();
                return true;
            }
            return false;
        });
    }

    private void signIn() {
        PlatformUtils.Keyboard.hideKeyboard(getActivity());
        String login = mEdtEmailView.getText().toString();
        String password = mEdtPasswordView.getText().toString();
        mPresenter.onSignInClicked(login, password);
    }

    @Override
    public void setSaveInstanceData(Pair data) {
        String email = (String) data.first;
        String password = (String) data.second;
        if (!TextUtils.isEmpty(email)) {
            mEdtEmailView.setText(email);
        }
        if (!TextUtils.isEmpty(password)) {
            mEdtPasswordView.setText(password);
        }
    }

    @Override
    public void setEmailError(String error) {
        showInputLayoutError(mEmailInputLayout, error);
    }

    @Override
    public void setPasswordError(String error) {
        showInputLayoutError(mPasswordInputLayout, error);
    }

    @Override
    public void setSignInBtnEnabled(boolean isEnabled) {
        mBtnSignIn.setEnabled(isEnabled);
    }

    private void showInputLayoutError(TextInputLayout inputLayout, String error) {
        inputLayout.setError(error);
        if (!TextUtils.isEmpty(error)) {
            inputLayout.requestFocus();
        }
    }

    @Override
    public void goToLandingScreen() {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switchFragment(new LandingFragment(), false);
    }

    @Override
    public View getParentView() {
        return mParentView;
    }

    @Override
    public LifecyclePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onStop() {
        super.onStop();
        PlatformUtils.Keyboard.hideKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        setLockScreenOrientation(false);
        super.onResume();
    }
}