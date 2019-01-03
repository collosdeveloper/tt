package com.knyaz.testtask.api.model;

import android.text.TextUtils;

public class BaseResponse {
    private String error_message;
    private String error_code;

    public int status;

    public String getErrorMessage() {
        return error_message;
    }

    public boolean isError() {
        return !TextUtils.isEmpty(error_code);
    }
}