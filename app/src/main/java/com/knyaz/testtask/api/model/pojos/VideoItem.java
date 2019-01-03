package com.knyaz.testtask.api.model.pojos;

import android.text.format.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class VideoItem implements Serializable {
    public String id;
    public String url;
    public String name;
    public String author;
    public String description;
    public String thumb_url;
    public long ts;
    public List<CommentItem> comments;

    public Date getDate() {
        return new Date((ts * DateUtils.SECOND_IN_MILLIS));
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", thumb_url='" + thumb_url + '\'' +
                ", ts=" + ts +
                ", comments=" + comments +
                '}';
    }
}