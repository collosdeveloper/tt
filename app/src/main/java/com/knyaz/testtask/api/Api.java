package com.knyaz.testtask.api;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.knyaz.testtask.ApplicationLoader;
import com.knyaz.testtask.BuildConfig;
import com.knyaz.testtask.api.requests.ApiAuth;
import com.knyaz.testtask.api.requests.ApiData;
import com.knyaz.testtask.api.utils.Config;
import com.knyaz.testtask.api.utils.RxErrorHandlingCallAdapterFactory;
import com.knyaz.testtask.utils.prefs.PreferenceManager;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private final String TAG = this.getClass().getSimpleName();

    private static final int CONNECTION_TIMEOUT = 200;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static Api sInstance;
    private Retrofit mRetrofit;
    private Gson mGson;
    private ServerSession mSession;

    private Api() {
    }

    public synchronized static Api getInst() {
        if (sInstance == null) {
            sInstance = new Api();
            sInstance.build();
        }
        return sInstance;
    }

    private void build() {
        OkHttpClient.Builder clientBuilder = getClientBuilderWithCookieAction();
        clientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

        OkHttpClient okHttpClient = clientBuilder.build();
        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .baseUrl(Config.getBaseUrl())
                .client(okHttpClient)
                .build();
        initSession();
    }

    public synchronized Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder().create();
        }
        return mGson;
    }

    public ApiAuth auth() {
        return mRetrofit.create(ApiAuth.class);
    }

    public ApiData data() {
        return mRetrofit.create(ApiData.class);
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public OkHttpClient.Builder getClientBuilderWithCookieAction() {
        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(chain -> {
            Request.Builder builder = chain.request().newBuilder()
                    .addHeader("Accept-Language", Locale.getDefault().getLanguage() + ";q=1")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded");
            if (!TextUtils.isEmpty(mSession.getToken())) {
                builder.addHeader(AUTHORIZATION_HEADER, mSession.getToken());
            }
            Request request = builder.build();
            return chain.proceed(request);
        });
        if (BuildConfig.DEBUG) {
            loggingAPI(clientBuilder);
        }
        return clientBuilder;
    }

    private void loggingAPI(OkHttpClient.Builder clientBuilder) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(interceptor);
    }

    private ServerSession createServerSession() {
        return createServerSession(null);
    }

    public void createSessionWithAuthData(String token) {
        mSession = createServerSession(token);
        saveSession();
    }

    private void saveSession() {
        PreferenceManager preferenceManager = ApplicationLoader.getApplicationInstance().getPreferenceManager();
        preferenceManager.saveSession(mSession);
    }

    private ServerSession createServerSession(String token) {
        ServerSession result;
        if (token == null) {
            result = new ServerSession();
        } else {
            result = new ServerSession(token);
        }
        return result;
    }

    private void initSession() {
        if (mSession == null) {
            this.mSession = ApplicationLoader.getApplicationInstance().getPreferenceManager().loadSession();
        }
        if (mSession == null) {
            this.mSession = createServerSession();
        }
    }

    public ServerSession getSession() {
        return mSession;
    }

    synchronized private void clearAuth() {
        mSession = null;
        ApplicationLoader.getApplicationInstance().getPreferenceManager().clearSession();
        initSession();
    }

    public void clearSession() {
        clearAuth();
    }
}