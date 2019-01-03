package com.knyaz.testtask.api.model.pojos;

import android.text.format.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class CommentItem implements Serializable {
    public int id;
    public String text;
    public int ts;
    public String author;

    public Date getDate() {
        return new Date((ts * DateUtils.SECOND_IN_MILLIS));
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", ts=" + ts +
                ", author='" + author + '\'' +
                '}';
    }
}