package com.knyaz.testtask.api.rx_tasks.data;

import com.knyaz.testtask.api.Api;
import com.knyaz.testtask.api.model.data.LoadVideosDataResponse;
import com.knyaz.testtask.api.rx_tasks.ApiTask;
import com.knyaz.testtask.api.utils.Config;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class LoadVideos extends ApiTask<LoadVideosDataResponse> {
    private Integer page;
    private String searchQuery;

    public LoadVideos(int page, String searchQuery) {
        super();
        this.page = page;
        this.searchQuery = searchQuery;
    }

    @Override
    protected Observable<LoadVideosDataResponse> getObservableTask() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page.toString());
        map.put("searchQuery", searchQuery);
        return Config.USE_MOCK ? Api.getInst().data().mockLoadVideos() : Api.getInst().data().loadVideos(map);
    }
}
