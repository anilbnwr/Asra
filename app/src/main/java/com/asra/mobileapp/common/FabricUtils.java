package com.asra.mobileapp.common;

import android.content.Context;

import com.google.firebase.crashlytics.FirebaseCrashlytics;


public class FabricUtils {

    private FabricUtils(Context context){
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
    }

    public static void createInstance(Context context){
        new FabricUtils(context.getApplicationContext());
    }

    public static void logUserId(String userId, String userName){

        FirebaseCrashlytics.getInstance().setCustomKey("user_name", userName);
        FirebaseCrashlytics.getInstance().setUserId(userId);


    }

    public static void logMessage(String message){
        FirebaseCrashlytics.getInstance().log(message);
    }

    public static void logException(Throwable exception){
        FirebaseCrashlytics.getInstance().recordException(exception);
    }


}
