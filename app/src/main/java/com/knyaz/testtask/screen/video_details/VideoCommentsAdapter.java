package com.knyaz.testtask.screen.video_details;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knyaz.testtask.R;
import com.knyaz.testtask.api.model.pojos.CommentItem;
import com.knyaz.testtask.utils.DateConvertingUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoCommentsAdapter extends RecyclerView.Adapter<VideoCommentsAdapter.CommentItemViewHolder> {
    private List<CommentItem> mCommentsList = new ArrayList<>();

    public void setComments(List<CommentItem> comments) {
        this.mCommentsList.addAll(comments);
        notifyDataSetChanged();
    }

    @Override
    public VideoCommentsAdapter.CommentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoCommentsAdapter.CommentItemViewHolder(View.inflate(parent.getContext(),
                R.layout.item_video_comment,
                null));
    }

    @Override
    public void onBindViewHolder(VideoCommentsAdapter.CommentItemViewHolder holder, int position) {
        holder.bindData(mCommentsList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

    public class CommentItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtAuthorName, mTxtAuthorComment, mTxtCommentDate;

        CommentItemViewHolder(View view) {
            super(view);
            mTxtAuthorName = itemView.findViewById(R.id.txt_author_name);
            mTxtAuthorComment = itemView.findViewById(R.id.txt_comment);
            mTxtCommentDate = itemView.findViewById(R.id.txt_comment_date);
        }

        void bindData(CommentItem comment) {
            mTxtAuthorName.setText(comment.author);
            mTxtAuthorComment.setText(comment.text);
            mTxtCommentDate.setText(DateConvertingUtils.formatDateToVideoItemStyle(comment.getDate(),
                    mTxtCommentDate.getContext()));
        }
    }
}