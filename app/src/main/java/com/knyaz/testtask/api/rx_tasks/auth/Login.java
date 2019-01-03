package com.knyaz.testtask.api.rx_tasks.auth;

import com.knyaz.testtask.api.Api;
import com.knyaz.testtask.api.model.auth.AuthDataResponse;
import com.knyaz.testtask.api.rx_tasks.ApiTask;
import com.knyaz.testtask.api.utils.Config;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class Login extends ApiTask<AuthDataResponse> {
    private String email;
    private String password;

    public Login(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    @Override
    protected Observable<AuthDataResponse> getObservableTask() {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        return Config.USE_MOCK ? Api.getInst().auth().mockLogin() : Api.getInst().auth().login(map);
    }
}