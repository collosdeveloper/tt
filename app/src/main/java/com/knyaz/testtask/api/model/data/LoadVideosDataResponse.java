package com.knyaz.testtask.api.model.data;

import com.knyaz.testtask.api.model.BaseResponse;
import com.knyaz.testtask.api.model.pojos.VideoItem;

import java.util.List;

public class LoadVideosDataResponse extends BaseResponse {
    public List<VideoItem> results;
    public boolean isNew;

    @Override
    public String toString() {
        return "LoadVideosDataResponse{" +
                "results=" + results +
                ", status=" + status +
                '}';
    }
}