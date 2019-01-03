package com.knyaz.testtask.api.rx_tasks.data;

import com.knyaz.testtask.api.Api;
import com.knyaz.testtask.api.model.data.VideoDetailDataResponse;
import com.knyaz.testtask.api.rx_tasks.ApiTask;
import com.knyaz.testtask.api.utils.Config;

import io.reactivex.Observable;

public class VideoDetails extends ApiTask<VideoDetailDataResponse> {
    private String videoId;

    public VideoDetails(String videoId) {
        super();
        this.videoId = videoId;
    }

    @Override
    protected Observable<VideoDetailDataResponse> getObservableTask() {
        return Config.USE_MOCK ? Api.getInst().data().videoDetailsMock() : Api.getInst().data().videoDetails(videoId);
    }
}