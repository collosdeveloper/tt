package com.knyaz.testtask.api.model;

import android.text.Html;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class Meta {
    private final String[] possibleErrorKeys = {
            "message",
            "errors",
            "general"
    };

    public Meta(int code, String redirect, HashMap<String, String[]> description) {
        this.code = code;
        this.redirect = redirect;
        this.description = description;
    }

    public Meta() {
    }

    @Expose
    private int code;

    @Expose
    private String redirect;

    @Expose
    private HashMap<String, String[]> description;

    public int getCode() {
        return code;
    }

    public HashMap<String, String[]> getDescription() {
        return description;
    }

    public String getFirstMessage() {
        String firstKey = null;
        if (description != null && !description.isEmpty()) {
            for (String key : possibleErrorKeys) {
                if (description.containsKey(key)) {
                    firstKey = key;
                    break;
                }
            }
            if (firstKey == null) {
                firstKey = description.keySet().iterator().next();
            }
            return Html.fromHtml(description.get(firstKey)[0]).toString();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "code=" + code +
                ", redirect='" + redirect + '\'' +
                ", description=" + description +
                '}';
    }
}