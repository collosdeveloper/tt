package com.knyaz.testtask.base.mvp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.knyaz.testtask.utils.RxUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class LifecyclePresenter<T> {
    protected String TAG = this.getClass().getSimpleName();

    private SparseArray<CompositeDisposable> aliveDisposables = new SparseArray<>();
    private T view;
    private Context context;
    private Resources resources;

    private boolean paused = true;
    private boolean stopped = true;
    private boolean viewDestroyed = true;
    private boolean destroyed = true;

    @CallSuper
    public void attachToView(T view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        viewDestroyed = false;
    }

    @CallSuper
    public void onAttach(Context context) {
        this.context = context;
        this.resources = context.getResources();
    }

    @CallSuper
    public void onCreate() {
        validateLifecycleDisposables(State.CREATE);
        destroyed = false;
    }

    @CallSuper
    public void onStart() {
        validateLifecycleDisposables(State.START);
        stopped = false;
    }

    @CallSuper
    public void onResume() {
        validateLifecycleDisposables(State.RESUME);
        paused = false;
    }

    @CallSuper
    public void onPause() {
        validateLifecycleDisposables(State.PAUSE);
        paused = true;
    }

    @CallSuper
    public void onStop() {
        validateLifecycleDisposables(State.STOP);
        stopped = true;
    }

    @CallSuper
    public void onDestroyView() {
        validateLifecycleDisposables(State.DESTROY_VIEW);
        viewDestroyed = true;
        view = null;
    }

    @CallSuper
    public void onDestroy() {
        validateLifecycleDisposables(State.DESTROY);
        destroyed = true;
    }

    public void onSaveInstanceState(Bundle state) {
    }

    public T getView() {
        return view;
    }

    protected boolean isDestroyed() {
        return destroyed;
    }

    protected boolean isStopped() {
        return stopped;
    }

    protected boolean isPaused() {
        return paused;
    }

    protected boolean isCreated() {
        return !destroyed;
    }

    protected boolean isStarted() {
        return !stopped;
    }

    protected boolean isResumed() {
        return !paused;
    }

    protected boolean isViewDestroyed() {
        return viewDestroyed;
    }

    private void validateLifecycleDisposables(@LifecycleState int state) {
        CompositeDisposable disposables = aliveDisposables.get(state);
        if (disposables != null) {
            RxUtils.safeDispose(disposables);
            disposables.clear();
            aliveDisposables.remove(state);
        }
    }

    private void addDisposableToStateMap(Disposable disposable, @LifecycleState int unsubscribeOn) {
        CompositeDisposable disposables = aliveDisposables.get(unsubscribeOn);
        if (disposables == null) {
            disposables = new CompositeDisposable();
            aliveDisposables.put(unsubscribeOn, disposables);
        }
        disposables.add(disposable);
    }

    public Context getContext() {
        return context;
    }

    public Resources getResources() {
        return resources;
    }

    protected void monitor(Disposable disposable, @LifecycleState int unsubscribeOn) {
        switch (unsubscribeOn) {
            case State.CREATE:
                if (isCreated()) RxUtils.safeDispose(disposable);
                break;
            case State.START:
                if (isStarted()) RxUtils.safeDispose(disposable);
                break;
            case State.RESUME:
                if (isResumed()) RxUtils.safeDispose(disposable);
                break;
            case State.PAUSE:
                if (isPaused()) RxUtils.safeDispose(disposable);
                break;
            case State.STOP:
                if (isStopped()) RxUtils.safeDispose(disposable);
                break;
            case State.DESTROY_VIEW:
                if (isViewDestroyed()) RxUtils.safeDispose(disposable);
                break;
            case State.DESTROY:
                if (isDestroyed()) RxUtils.safeDispose(disposable);
                break;
        }
        addDisposableToStateMap(disposable, unsubscribeOn);
    }
}