package com.knyaz.testtask.utils;

import android.util.Pair;

public class AppDataInstance {
    private String email;
    private String password;

    public Pair getLoginData() {
        return new Pair(email, password);
    }

    public void saveLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}