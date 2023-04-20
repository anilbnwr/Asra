package com.asra.mobileapp.ui.general.settings;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.NotificationType;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SettingsViewModel extends BaseViewModel {

    public SingleLiveEvent<List<NotificationType>> notificationTypesObservable =
            new SingleLiveEvent<>();

    public SingleLiveEvent<String> notificationTypesErrorObservable =
            new SingleLiveEvent<>();
    private MemberUseCase memberUseCase;
    private boolean hasChanged = false;

    List<NotificationType> notificationTypeList = null;

    @Inject
    public SettingsViewModel(MemberUseCase memberUseCase,
                             AppEngine appEngine,
                             ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();
        getNotificationTypes();
    }

    public void getNotificationTypes(){
        showProgressBar(getConfigString(MessageProvider.reading_your_preferences));

        Disposable disposable = memberUseCase.getNotificationTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNotificationsFetched,
                        this::onNotificationFailed);
        addDisposable(disposable);

    }

    private void onNotificationFailed(Throwable throwable) {

        hideProgressBar();
        notificationTypesErrorObservable.postValue(throwable.getMessage());
    }

    private void onNotificationsFetched(List<NotificationType> notificationTypes) {
        hideProgressBar();
        if (ListUtils.isEmpty(notificationTypes)) {
            onNotificationFailed(new ETApiException(getConfigString(MessageProvider.error_no_data)));
        }else{
            this.notificationTypeList = notificationTypes;
            notificationTypesObservable.postValue(notificationTypes);
        }
    }

    public void onPrefChanged(NotificationType type, boolean enabled) {
        type.setEnabled(enabled);

        hasChanged = true;
        syncSettings();
    }


    public void syncSettings() {
        if(hasChanged){
            Disposable disposable = memberUseCase.updateNotificationSettings(notificationTypeList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onNotificationsUpdated,
                            this::onNotificationUpdateFailed);
            addDisposable(disposable);
        }
    }

    private void onNotificationUpdateFailed(Throwable throwable) {
        Timber.e("Updating Notification failed %s", throwable.getMessage());
    }

    private void onNotificationsUpdated() {
        Timber.i("Notifications Updated");
    }
}
