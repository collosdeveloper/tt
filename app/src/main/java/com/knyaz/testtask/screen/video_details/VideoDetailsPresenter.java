package com.knyaz.testtask.screen.video_details;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.knyaz.testtask.api.rx_tasks.data.VideoDetails;
import com.knyaz.testtask.api.utils.ErrorResponse;
import com.knyaz.testtask.base.mvp.LifecyclePresenter;
import com.knyaz.testtask.base.mvp.State;

import io.reactivex.disposables.Disposable;

public class VideoDetailsPresenter extends LifecyclePresenter<VideoDetailsView> {
    private String mVideoId;

    void setVideoId(String videoId) {
        this.mVideoId = videoId;
    }

    @Override
    public void attachToView(VideoDetailsView view, @Nullable Bundle savedInstanceState) {
        super.attachToView(view, savedInstanceState);
        loadVideoDetails();
    }

    public void loadVideoDetails() {
        Disposable disposable = new VideoDetails(mVideoId).getTask()
                .doOnSubscribe(d -> getView().showLoading())
                .doOnEach(n -> getView().hideLoading())
                .subscribe(videoDetailDataResponse -> getView().setVideoDetails(videoDetailDataResponse.video),
                        error -> getView().showError(ErrorResponse.getServerMessage(error)));
        monitor(disposable, State.DESTROY_VIEW);
    }
}