package com.knyaz.testtask.utils;

import android.text.TextUtils;

import com.knyaz.testtask.base.ui.interfaces.LoadingView;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.knyaz.testtask.utils.LogUtils.LOGE;

public class RxUtils {
    public static final String TAG = RxUtils.class.getName();

    public static void safeDispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static Consumer<Object> getEmptyDataConsumer() {
        return o -> {
        };
    }

    public static Consumer<Throwable> getEmptyErrorConsumer() {
        return getEmptyErrorConsumer(null, null);
    }

    public static Consumer<Throwable> getEmptyErrorConsumer(String className) {
        return getEmptyErrorConsumer(className, null);
    }

    public static Consumer<Throwable> getEmptyErrorConsumer(String className, String methodName) {
        return throwable -> {
            String tag = TextUtils.isEmpty(className) ? TAG : "ClassName : " + className;
            String errorMsg = (TextUtils.isEmpty(methodName) ? "" : "MethodName : " + methodName + "\n") +
                    "Error : " +
                    throwable.toString();
            LOGE(tag, errorMsg);
        };
    }

    public static <T> ObservableTransformer<T, T> withLoading(LoadingView view) {
        return upstream -> upstream
                .doOnSubscribe(__ -> view.showLoading())
                .doOnEach(__ -> view.hideLoading());
    }
}