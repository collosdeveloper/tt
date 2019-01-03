package com.knyaz.testtask.base.mvp;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({State.CREATE, State.START, State.RESUME, State.PAUSE,
        State.STOP, State.DESTROY_VIEW, State.DESTROY})
public @interface LifecycleState {
}