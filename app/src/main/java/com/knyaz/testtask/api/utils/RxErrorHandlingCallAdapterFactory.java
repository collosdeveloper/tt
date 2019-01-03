package com.knyaz.testtask.api.utils;


import com.google.gson.stream.MalformedJsonException;
import com.knyaz.testtask.api.model.BaseResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.HttpException;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.knyaz.testtask.utils.LogUtils.LOGE;

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private static final String TAG = RxErrorHandlingCallAdapterFactory.class.getSimpleName();
    private final RxJava2CallAdapterFactory mOriginal;

    private RxErrorHandlingCallAdapterFactory() {
        mOriginal = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(mOriginal.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper<R> implements CallAdapter<R, Observable<R>> {
        private final CallAdapter<R, ?> mWrapped;

        public RxCallAdapterWrapper(CallAdapter<R, ?> wrapped) {
            this.mWrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return mWrapped.responseType();
        }


        @SuppressWarnings("unchecked")
        @Override
        public Observable<R> adapt(Call<R> call) {
            return ((Observable) mWrapped.adapt(call))
                    .map(o -> {
                        BaseResponse baseResponse = (BaseResponse) o;
                        if (baseResponse.isError()) {
                            throw ErrorResponse.serverError(baseResponse.getErrorMessage());
                        } else {
                            return o;
                        }
                    })
                    .onErrorResumeNext(throwable -> {
                        return Observable.error(asRetrofitException((Throwable) throwable));
                    });
        }

        private ErrorResponse asRetrofitException(Throwable throwable) {
            if (throwable instanceof ErrorResponse) {
                return (ErrorResponse) throwable;
            }
            // We had non-200 http error
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                return ErrorResponse.httpError(response.raw().request().url().toString(), response);
            }
            if (throwable instanceof MalformedJsonException) {
                return ErrorResponse.parseError((MalformedJsonException) throwable);
            }
            // A network error happened
            if (throwable instanceof IOException) {
                return ErrorResponse.networkError((IOException) throwable);
            }
            // We don't know what happened. We need to simply convert to an unknown error
            LOGE(TAG, "Unexpected : " + throwable.toString());
            return ErrorResponse.unexpectedError(throwable);
        }
    }
}