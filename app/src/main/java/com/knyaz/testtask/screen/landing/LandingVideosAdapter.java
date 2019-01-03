package com.knyaz.testtask.screen.landing;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.knyaz.testtask.R;
import com.knyaz.testtask.api.model.pojos.VideoItem;
import com.knyaz.testtask.utils.DateConvertingUtils;

import java.util.ArrayList;
import java.util.List;

public class LandingVideosAdapter extends RecyclerView.Adapter<LandingVideosAdapter.VideoItemViewHolder> {
    private static final int LIMIT = 4;
    private RecyclerViewDisplayType mRecyclerViewDisplayType = RecyclerViewDisplayType.LIST;
    private VideoItemClickListener mVideoItemClickListener;
    private List<VideoItem> mVideoList = new ArrayList<>();
    private boolean mIsLoading;
    private int mLastVisibleItem, mTotalItemCount;

    public LandingVideosAdapter(VideoItemClickListener videoItemClickListener) {
        this.mVideoItemClickListener = videoItemClickListener;
    }

    public void setVideos(List<VideoItem> videoList) {
        this.mVideoList.clear();
        addVideos(videoList);
    }

    public void addVideos(List<VideoItem> videoList) {
        this.mVideoList.addAll(videoList);
        notifyDataSetChanged();
        setLoaded();
    }

    public void setRecyclerViewLayoutManagerType(RecyclerViewDisplayType recyclerViewDisplayType) {
        mRecyclerViewDisplayType = recyclerViewDisplayType;
    }

    @Override
    public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoItemViewHolder(View.inflate(parent.getContext(),
                mRecyclerViewDisplayType.equals(RecyclerViewDisplayType.LIST) ? R.layout.item_landing_video : R.layout.item_landing_video_grid,
                null));
    }

    @Override
    public void onBindViewHolder(VideoItemViewHolder holder, int position) {
        holder.bindData(mVideoList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public List<VideoItem> getVideoList() {
        return mVideoList;
    }

    public class VideoItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgViewHolder;
        private TextView mTxtVideoName, mTxtVideoDescription, mTxtVideoDate;

        VideoItemViewHolder(View view) {
            super(view);
            mImgViewHolder = itemView.findViewById(R.id.img_video_thumb);
            mTxtVideoName = itemView.findViewById(R.id.txt_video_name);
            mTxtVideoDate = itemView.findViewById(R.id.txt_video_date);
            mTxtVideoDescription = itemView.findViewById(R.id.txt_video_description);
        }

        void bindData(VideoItem videoItem) {
            Glide.with(mImgViewHolder)
                    .load(videoItem.thumb_url)
                    .into(mImgViewHolder);
            mTxtVideoName.setText(videoItem.name);
            mTxtVideoDate.setText(DateConvertingUtils.formatDateToVideoItemStyle(videoItem.getDate(),
                    mTxtVideoDate.getContext()));
            String description = videoItem.description;
            if(mTxtVideoDescription != null && !TextUtils.isEmpty(description)) {
                mTxtVideoDescription.setVisibility(View.VISIBLE);
                mTxtVideoDescription.setText(description);
            }
            itemView.setOnClickListener(view -> {
                if(mVideoItemClickListener != null) {
                    mVideoItemClickListener.onItemClick(videoItem.id);
                }
            });
        }
    }

    public void setOnLoadMoreListener(RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(linearLayoutManager == null) {
                    return;
                }
                if(getItemCount() != 0) {
                    mTotalItemCount = linearLayoutManager.getItemCount();
                    mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!mIsLoading && mTotalItemCount <= (mLastVisibleItem + LIMIT)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        mIsLoading = true;
                    }
                }
            }
        });
    }

    public void setLoaded() {
        mIsLoading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface VideoItemClickListener {
        void onItemClick(String videoId);
    }
}