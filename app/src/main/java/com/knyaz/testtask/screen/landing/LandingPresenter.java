package com.knyaz.testtask.screen.landing;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.knyaz.testtask.R;
import com.knyaz.testtask.api.Api;
import com.knyaz.testtask.api.rx_tasks.data.LoadVideos;
import com.knyaz.testtask.api.utils.ErrorResponse;
import com.knyaz.testtask.base.mvp.LifecyclePresenter;
import com.knyaz.testtask.base.mvp.State;
import com.knyaz.testtask.utils.UserFieldsValidator;

import io.reactivex.disposables.Disposable;

public class LandingPresenter extends LifecyclePresenter<LandingView> {
    private UserFieldsValidator mValidator;
    private RecyclerViewDisplayType mLastRecyclerViewDisplayType = RecyclerViewDisplayType.LIST;
    private int mPage = 1;
    private String mSearchQuery = "";

    public LandingPresenter() {
        mValidator = UserFieldsValidator.getInstance();
    }

    @Override
    public void attachToView(LandingView view, @Nullable Bundle savedInstanceState) {
        super.attachToView(view, savedInstanceState);
        view.setRVDisplayType(mLastRecyclerViewDisplayType);
        loadVideos();
    }

    public void loadVideos() {
        Disposable disposable = new LoadVideos(mPage, mSearchQuery).getTask()
                .map(loadVideosDataResponse -> {
                    loadVideosDataResponse.isNew = mPage == 1;
                    return loadVideosDataResponse;
                })
                .doOnNext(list -> mPage++)
                .doOnSubscribe(d -> getView().showLoading())
                .doOnEach(n -> getView().hideLoading())
                .subscribe(loadVideosDataResponse -> {
                    if(loadVideosDataResponse.isNew) {
                        getView().setVideos(loadVideosDataResponse.results);
                    } else {
                        getView().addVideos(loadVideosDataResponse.results);
                    }
                    }, error -> getView().showError(ErrorResponse.getServerMessage(error)));
        monitor(disposable, State.DESTROY_VIEW);
    }

    public void onLogoutClick() {
        Api.getInst().clearSession();
        getView().goToLoginScreen();
    }

    public void switchRecyclerViewLayoutManager() {
        switch (mLastRecyclerViewDisplayType) {
            case LIST:
                mLastRecyclerViewDisplayType = RecyclerViewDisplayType.GRID;
                break;
            case GRID:
                mLastRecyclerViewDisplayType = RecyclerViewDisplayType.LIST;
                break;
        }
        getView().setRVDisplayType(mLastRecyclerViewDisplayType);
    }

    public void performSearch(String searchQuery) {
        if (!mValidator.validateNotEmpty(searchQuery)) {
            getView().setSearchError(getResources().getString(R.string.error_empty_search));
        } else {
            if(!mSearchQuery.equals(searchQuery)) {
                toFirstPage();
            }
            loadVideos();
        }
    }

    public void toFirstPage() {
        mPage = 1;
    }
}