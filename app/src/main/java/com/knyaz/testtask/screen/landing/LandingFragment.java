package com.knyaz.testtask.screen.landing;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.knyaz.testtask.R;
import com.knyaz.testtask.api.model.pojos.VideoItem;
import com.knyaz.testtask.base.mvp.LifecyclePresenter;
import com.knyaz.testtask.base.mvp.MVPFragment;
import com.knyaz.testtask.screen.login.LoginFragment;
import com.knyaz.testtask.screen.video_details.VideoDetailsFragment;
import com.knyaz.testtask.utils.PlatformUtils;

import java.util.ArrayList;
import java.util.List;

public class LandingFragment extends MVPFragment implements LandingView {
    private LandingPresenter mPresenter = new LandingPresenter();
    private LandingVideosAdapter mVideosAdapter;

    // UI
    private View mParentView;
    private RecyclerView mRViewVideos;
    private Button mBtnLogout;
    private Button mBtnSwitchVideoDisplayMode;
    private EditText mEdtSearchView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_landing, container, false);
    }

    @Override
    public void onPostViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onPostViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View view) {
        mParentView = view.findViewById(R.id.parent_view);
        mRViewVideos = view.findViewById(R.id.videos_rview);
        mRViewVideos.setItemAnimator(new DefaultItemAnimator());
        mBtnLogout = view.findViewById(R.id.btn_logout);
        mBtnLogout.setOnClickListener(view1 -> new MaterialDialog.Builder(getActivity())
                .title(R.string.action_logout)
                .content(R.string.exit_dialog_body)
                .positiveText(R.string.action_logout)
                .negativeText(R.string.action_cancel)
                .onPositive((dialog1, which) -> mPresenter.onLogoutClick())
                .show());
        mBtnSwitchVideoDisplayMode = view.findViewById(R.id.btn_switch_rv_mode);
        mBtnSwitchVideoDisplayMode.setOnClickListener(view12 -> mPresenter.switchRecyclerViewLayoutManager());
        mEdtSearchView = view.findViewById(R.id.edt_search);
        mEdtSearchView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_SEARCH || id == EditorInfo.IME_NULL) {
                mPresenter.performSearch(textView.getText().toString());
                return true;
            }
            return false;
        });
    }

    @Override
    public void setRVDisplayType(RecyclerViewDisplayType displayType) {
        switch (displayType) {
            case LIST:
                mRViewVideos.setLayoutManager(new LinearLayoutManager(mRViewVideos.getContext()));
                break;
            case GRID:
                mRViewVideos.setLayoutManager(new GridLayoutManager(mRViewVideos.getContext(), 2));
                break;
        }
        List<VideoItem> videos = null;
        if(mVideosAdapter != null) {
            videos = new ArrayList<>(mVideosAdapter.getVideoList());
        }
        mVideosAdapter = new LandingVideosAdapter(videoId -> addFragment(VideoDetailsFragment.newInstance(videoId), true));
        mVideosAdapter.setRecyclerViewLayoutManagerType(displayType);
        mRViewVideos.setAdapter(mVideosAdapter);
        mVideosAdapter.setOnLoadMoreListener(mRViewVideos, () -> mPresenter.loadVideos());
        if(videos != null) {
            mVideosAdapter.setVideos(videos);
        }
    }

    @Override
    public void setSearchError(String error) {
        showError(error);
    }

    @Override
    public void setVideos(List<VideoItem> videos) {
        mVideosAdapter.setVideos(videos);
    }

    @Override
    public void addVideos(List<VideoItem> videos) {
        mVideosAdapter.addVideos(videos);
    }

    @Override
    public void goToLoginScreen() {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switchFragment(new LoginFragment(), false);
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
        setLockScreenOrientation(true);
        super.onResume();
    }
}