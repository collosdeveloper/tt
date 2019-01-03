package com.knyaz.testtask.api.model.data;

import com.knyaz.testtask.api.model.BaseResponse;
import com.knyaz.testtask.api.model.pojos.VideoItem;

public class VideoDetailDataResponse extends BaseResponse {
    public VideoItem video;

    @Override
    public String toString() {
        return "VideoDetailDataResponse{" +
                "video=" + video +
                ", status=" + status +
                '}';
    }
}