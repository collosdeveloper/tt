package com.knyaz.testtask.screen.video_details;

import com.knyaz.testtask.api.model.pojos.VideoItem;
import com.knyaz.testtask.base.ui.interfaces.LoadingView;

public interface VideoDetailsView extends LoadingView {
    void setVideoDetails(VideoItem videoDetails);
}