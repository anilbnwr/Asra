package com.asra.mobileapp.logger;

import com.asra.mobileapp.BuildConfig;
import com.asra.mobileapp.common.FabricUtils;

import timber.log.Timber;


public class ProdTimber extends Timber.DebugTree {

    @Override
    public void e(String message, Object... args) {
        super.e(message, args);
        FabricUtils.logMessage(String.format(message, args));
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        super.e(t, message, args);
        FabricUtils.logMessage(String.format(message, args));

    }

    @Override
    public void w(String message, Object... args) {
        super.w(message, args);
        FabricUtils.logMessage(String.format(message, args));
    }

    @Override
    public void d(String message, Object... args) {
        if(BuildConfig.TIMBER_LOG_ENABLED) {
            super.d(message, args);
        }

        FabricUtils.logMessage(String.format(message, args));
    }
}
