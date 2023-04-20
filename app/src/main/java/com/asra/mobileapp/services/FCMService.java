package com.asra.mobileapp.services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import javax.inject.Inject;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FCMService extends FirebaseMessagingService {


    @Inject
    public AppEngine appEngine;

    @Inject
    public MemberUseCase memberUseCase;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();

    }


    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Timber.d("PUSH - From: %s", remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0 && remoteMessage.getNotification() != null) {
            Timber.d("PUSH - Message data payload: %s", remoteMessage.getData());

            Timber.d("PUSH - Message Notification Body: %s", remoteMessage.getNotification().getBody());
            handleNow(remoteMessage.getNotification().getBody(), remoteMessage.getData());
        }


    }

    private void handleNow( String body, Map<String, String> data) {
        Timber.d("PUSH - ET_PN-TYPE: %s", data.get(PnConstants.DATA_TYPE));
        Timber.d("PUSH - EVENT ID: %s", data.get(PnConstants.DATA_EVENT_ID));
        Timber.d("PUSH - Event name: %s", data.get(PnConstants.DATA_EVENT_TITLE));
        Timber.d("PUSH - Push title: %s", data.get(PnConstants.DATA_PUSH_TITLE));
        Timber.d("PUSH - Push Banner: %s", data.get(PnConstants.DATA_PUSH_BANNER));

        String pnType = data.get(PnConstants.DATA_TYPE);
        if (TextUtils.isEmpty(pnType)) return;

        Bundle bundle = new Bundle();
        bundle.putString(PnConstants.DATA_TYPE, data.get(PnConstants.DATA_TYPE));
        bundle.putString(PnConstants.DATA_EVENT_ID, data.get(PnConstants.DATA_EVENT_ID));
        bundle.putString(PnConstants.DATA_EVENT_TITLE, data.get(PnConstants.DATA_EVENT_TITLE));
        bundle.putString(PnConstants.DATA_PUSH_TITLE, data.get(PnConstants.DATA_PUSH_TITLE));
        bundle.putString(PnConstants.DATA_WEB_URL, data.get(PnConstants.DATA_WEB_URL));
        bundle.putString(PnConstants.DATA_PUSH_BANNER, data.get(PnConstants.DATA_PUSH_BANNER));
        bundle.putString(PnConstants.DATA_PUSH_BODY, body);

        Intent intent = new Intent();
        intent.setAction(PnConstants.ACTION_NOTIFICATION);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    public void onNewToken(@NotNull String newToken) {
        super.onNewToken(newToken);
        Timber.i("PUSH - newToken - %s", newToken);

        appEngine.saveDeviceToken(newToken);

        memberUseCase.updateDeviceToken(newToken, "Android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Timber.i("Device Token updated"),
                        throwable -> Timber.e(throwable, "Device Token sync failed"));

    }

}
