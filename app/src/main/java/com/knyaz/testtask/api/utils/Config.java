package com.knyaz.testtask.api.utils;

public class Config {
    public static final boolean USE_MOCK = true;

    private static final String SERVER_URL = "http://demo5151808.mockable.io";

    public static String getBaseUrl() {
        return SERVER_URL;
    }
}