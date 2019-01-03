package com.knyaz.testtask.api.rx_tasks;

import io.reactivex.Observable;

public abstract class ApiTask<R> extends ObservableTask<R> {

    public ApiTask(boolean executeOnNewThread) {
        super(executeOnNewThread);
    }

    public ApiTask() {
        super(true);
    }

    @Override
    public Observable<R> getTask() {
        return super.getTask();
    }
}