package com.knyaz.testtask.api.requests;

import com.knyaz.testtask.api.model.auth.AuthDataResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiAuth {
    @POST("/login%3Femail=test@gmail.com&password=testpassword")
    Observable<AuthDataResponse> mockLogin();

    @FormUrlEncoded
    @POST("/login")
    Observable<AuthDataResponse> login(@FieldMap final Map<String, String> dataPartMap);
}