package com.knyaz.testtask.utils;

import android.text.TextUtils;

public class UserFieldsValidator {
    private static UserFieldsValidator sInstance;

    public static UserFieldsValidator getInstance() {
        if (sInstance == null) {
            sInstance = new UserFieldsValidator();
        }
        return sInstance;
    }

    private UserFieldsValidator() {
    }

    public boolean validateNotEmpty(String text) {
        return !TextUtils.isEmpty(text);
    }

    public boolean validateEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validatePasswordLength(String password) {
        return password.length() >= 6;
    }

    public boolean validateNoSpaces(String line) {
        return !line.contains(" ");
    }
}