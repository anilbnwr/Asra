package com.asra.mobileapp.ui.base;

import com.asra.mobileapp.BuildConfig;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.AppVersionStatus;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ActivityViewModel extends BaseViewModel {


    public SingleLiveEvent<AppVersionStatus> mandatoryAppUpdateObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<AppVersionStatus> optionAppUpdateObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<User> loginObservable = new SingleLiveEvent<>();

    protected MemberUseCase memberUseCase;

    public ActivityViewModel(MemberUseCase memberUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();

        checkForAppUpdate();
    }

    private void checkForAppUpdate() {
        Disposable disposable = memberUseCase.checkForUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::appUpdateAvailable,
                        this::checkForAppUpdateFailed);
        addDisposable(disposable);
    }

    private void checkForAppUpdateFailed(Throwable throwable) {
        Timber.e(throwable, "Checking App Update failed");
    }

    public void appUpdateAvailable(AppVersionStatus appVersionStatus) {
        appEngine.setAppVersionStatus(appVersionStatus);

        if (BuildConfig.VERSION_CODE < appEngine.getVersionCode()) {
            String updateMessage;
            if (appVersionStatus.shouldForceUpdate()) {
                updateMessage = getConfigString(MessageProvider.app_update_mandatory_message);
                updateMessage = String.format(updateMessage, appVersionStatus.versionName,
                        getString(R.string.app_name));
                appVersionStatus.displayMessage = updateMessage;
                mandatoryAppUpdateObservable.postValue(appVersionStatus);
            } else {
                updateMessage = getConfigString(MessageProvider.app_update_optional_message);
                updateMessage = String.format(updateMessage, appVersionStatus.versionName);
                appVersionStatus.displayMessage = updateMessage;
                optionAppUpdateObservable.postValue(appVersionStatus);
            }
        }else{
            Timber.i("Latest version has been installed");
        }
    }

    public void launchDashboard() {
        if(appEngine.isUserLoggedIn()){
            if(appEngine.isUserAdmin()){
                adminLoggedInObservable.postValue(true);
            }else{
                userLoggedInObservable.postValue(true);
            }
        }else{
            guestUserObservable.postValue(true);
        }
    }
}
