package com.knyaz.testtask.api.requests;

import com.knyaz.testtask.api.model.data.LoadVideosDataResponse;
import com.knyaz.testtask.api.model.data.VideoDetailDataResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiData {
    @GET("/videos%3Fpage=1&searchQuery=test")
    Observable<LoadVideosDataResponse> mockLoadVideos();

    @GET("/videos")
    Observable<LoadVideosDataResponse> loadVideos(@QueryMap Map<String, String> dataPartMap);

    @GET("/video/lXQ6n/")
    Observable<VideoDetailDataResponse> videoDetailsMock();

    @GET("/video/{videoId}/")
    Observable<VideoDetailDataResponse> videoDetails(@Path("videoId") String videoId);
}