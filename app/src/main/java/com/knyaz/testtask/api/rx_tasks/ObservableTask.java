package com.knyaz.testtask.api.rx_tasks;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class ObservableTask<R> {
    private boolean executeOnNewThread = true;

    public ObservableTask(final boolean executeOnNewThread) {
        this.executeOnNewThread = executeOnNewThread;
    }

    protected abstract Observable<R> getObservableTask();

    public Observable<R> getTask() {
        Observable<R> observable;
        if (executeOnNewThread) {
            observable = Observable.defer(this::getObservableTask);
        } else {
            observable = getObservableTask();
        }
        observable = observable.observeOn(getObserveScheduler())
                .subscribeOn(getSubscribeScheduler())
                .unsubscribeOn(getSubscribeScheduler());
        return observable;
    }

    public Scheduler getObserveScheduler() {
        return AndroidSchedulers.mainThread();
    }

    public Scheduler getSubscribeScheduler() {
        return executeOnNewThread ? Schedulers.newThread() : Schedulers.io();
    }
}