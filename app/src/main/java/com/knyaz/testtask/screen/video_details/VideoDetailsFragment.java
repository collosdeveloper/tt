package com.knyaz.testtask.screen.video_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.knyaz.testtask.R;
import com.knyaz.testtask.api.model.pojos.VideoItem;
import com.knyaz.testtask.base.mvp.LifecyclePresenter;
import com.knyaz.testtask.base.mvp.MVPFragment;
import com.knyaz.testtask.utils.DateConvertingUtils;
import com.knyaz.testtask.utils.PlatformUtils;

import cn.jzvd.JZVideoPlayerStandard;

public class VideoDetailsFragment extends MVPFragment implements VideoDetailsView {
    private VideoDetailsPresenter mPresenter = new VideoDetailsPresenter();
    private final static String VIDEO_ID_KEY = "video_id";
    // UI
    private View mParentView;
    private JZVideoPlayerStandard mVideoView;
    private TextView mTxtVideoName, mTxtAuthorName, mTxtPostedDate, mTxtDescription;
    private RecyclerView mRVComments;

    public static VideoDetailsFragment newInstance(String videoId) {
        VideoDetailsFragment videoDetailsFragment = new VideoDetailsFragment();
        Bundle args = new Bundle();
        args.putString(VIDEO_ID_KEY, videoId);
        videoDetailsFragment.setArguments(args);
        return videoDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData(getArguments());
    }

    private void getBundleData(Bundle bundle) {
        if (bundle != null) {
            mPresenter.setVideoId(bundle.getString(VIDEO_ID_KEY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_detail, container, false);
    }

    @Override
    public void onPostViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onPostViewCreated(view, savedInstanceState);
        PlatformUtils.Keyboard.hideKeyboard(getActivity());
        initUI(view);
    }

    private void initUI(View view) {
        mParentView = view.findViewById(R.id.parent_view);
        mVideoView = view.findViewById(R.id.view_video);
        mTxtVideoName = view.findViewById(R.id.txt_video_name);
        mTxtAuthorName = view.findViewById(R.id.txt_author_name);
        mTxtPostedDate = view.findViewById(R.id.txt_posted_date);
        mTxtDescription = view.findViewById(R.id.txt_description);
        mRVComments = view.findViewById(R.id.comments_rview);
        mRVComments.setLayoutManager(new LinearLayoutManager(mRVComments.getContext()));
    }

    @Override
    public void setVideoDetails(VideoItem videoDetails) {
        mVideoView.setUp(videoDetails.url,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                videoDetails.name);
        mVideoView.startVideo();
        Glide.with(mVideoView.thumbImageView)
                .load(videoDetails.thumb_url)
                .into(mVideoView.thumbImageView);
        mTxtVideoName.setText(String.format(getContext().getString(R.string.formatted_video_name), videoDetails.name));
        mTxtAuthorName.setText(String.format(getContext().getString(R.string.formatted_author_name), videoDetails.author));
        mTxtPostedDate.setText(String.format(getContext().getString(R.string.formatted_posted_date), DateConvertingUtils.getStringVideoPostedDate(videoDetails.getDate())));
        mTxtDescription.setText(videoDetails.description);
        getView().findViewById(R.id.txt_view_comments_title).setVisibility(View.VISIBLE);
        VideoCommentsAdapter adapter = new VideoCommentsAdapter();
        mRVComments.setAdapter(adapter);
        adapter.setComments(videoDetails.comments);
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.releaseAllVideos();
    }

    @Override
    public View getParentView() {
        return mParentView;
    }

    @Override
    public LifecyclePresenter getPresenter() {
        return mPresenter;
    }
}