package com.asra.mobileapp;

import android.app.Application;

import com.asra.mobileapp.analytics.FirebaseAnalyticsHelper;
import com.asra.mobileapp.common.Constants;
import com.asra.mobileapp.common.FabricUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.dagger.component.DaggerAppComponent;
import com.asra.mobileapp.logger.DebugTimber;
import com.asra.mobileapp.logger.ProdTimber;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import timber.log.Timber;

public class ETApplication extends Application implements HasAndroidInjector {


    private static ETApplication application;

    @Inject
    DispatchingAndroidInjector<Object> activityDispatchingInjector;


    private int selectedApp = Constants.APP_EVOLVE;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initializeComponent();

        MessageProvider.createInstance();

        FirebaseApp.initializeApp(this);
        FirebaseAnalyticsHelper.getInstance(this);
        FabricUtils.createInstance(this);

        UiUtils.createNotificationChannel(this);
        //Plant the Timber tree
        if(BuildConfig.TIMBER_LOG_ENABLED) {
            Timber.plant(new DebugTimber());
        }else{
            Timber.plant(new ProdTimber());
        }



        debugPrintFCMToken();

    }

    private void debugPrintFCMToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Timber.w(task.getException(), "getInstanceId failed");
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    Timber.d("PUSH - FCM Token - %s", token);
                });
    }




    public static ETApplication getInstance(){
        return application;
    }
    private void initializeComponent() {
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }




    public int getSelectedApp() {
        return selectedApp;
    }

    public void setSelectedApp(int selectedApp) {
        this.selectedApp = selectedApp;
    }

    public boolean isEvApp() {
        return selectedApp == Constants.APP_EVOLVE;
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return activityDispatchingInjector;
    }
}
