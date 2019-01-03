package com.knyaz.testtask.screen.landing;

import com.knyaz.testtask.api.model.pojos.VideoItem;
import com.knyaz.testtask.base.ui.interfaces.LoadingView;

import java.util.List;

public interface LandingView extends LoadingView {
    void setVideos(List<VideoItem> videos);

    void addVideos(List<VideoItem> videos);

    void setRVDisplayType(RecyclerViewDisplayType displayType);

    void setSearchError(String error);

    void goToLoginScreen();
}