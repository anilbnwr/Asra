package com.asra.mobileapp.logger;

import timber.log.Timber;

/**
 * Created by P76711 on 8/2/2018.
 */

public class DebugTimber extends Timber.DebugTree {

    @Override
    public void i(String message, Object... args) {
        super.i(message, args);
    }

    @Override
    public void d(String message, Object... args) {
        super.d(message, args);
    }

    @Override
    public void w(String message, Object... args) {
        super.w(message, args);
    }

    @Override
    public void e(String message, Object... args) {
        super.e(message, args);
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        super.e(t, message, args);
    }
}
