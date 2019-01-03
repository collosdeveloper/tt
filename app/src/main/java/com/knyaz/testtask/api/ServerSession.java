package com.knyaz.testtask.api;

import com.google.gson.annotations.Expose;

public final class ServerSession {
    @Expose
    private String token;

    public ServerSession() {
    }

    public ServerSession(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}