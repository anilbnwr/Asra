package com.asra.mobileapp.ui.base;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.analytics.FirebaseAnalyticsHelper;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.model.ProgressData;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.ViewModel;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BaseViewModel extends ViewModel {

    protected Gson gson = new Gson();
    protected FirebaseAnalyticsHelper analyticsHelper;

    public SingleLiveEvent<ProgressData> progressDialogObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> switchModuleObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> switchAppObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> errorMessageObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> errorAlertMessageObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> successMessageObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> errorEmptyMessageObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> logoutObservable = new SingleLiveEvent();

    public SingleLiveEvent<Boolean> exitActivityObservable = new SingleLiveEvent();
    public SingleLiveEvent<Boolean> exitScreenObservable = new SingleLiveEvent();
    public SingleLiveEvent<Boolean> adminLoggedInObservable = new SingleLiveEvent();
    public SingleLiveEvent<Boolean> userLoggedInObservable = new SingleLiveEvent();
    public SingleLiveEvent<Boolean> coachLoggedInObservable = new SingleLiveEvent();
    public SingleLiveEvent<Boolean> guestUserObservable = new SingleLiveEvent();
    public SingleLiveEvent<String> webPageObservable = new SingleLiveEvent();

    public SingleLiveEvent<String> successToastObservable = new SingleLiveEvent();
    public SingleLiveEvent<String> errorToastObservable = new SingleLiveEvent();

    public SingleLiveEvent<Boolean> loginRequestedObservable = new SingleLiveEvent();


    public BaseViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher) {
        this.resourceFetcher = resourceFetcher;
        this.mCompositeDisposable = new CompositeDisposable();
        this.appEngine = appEngine;

        analyticsHelper = FirebaseAnalyticsHelper.getInstance(ETApplication.getInstance());

    }

    private CompositeDisposable mCompositeDisposable;

    public ResourceFetcher resourceFetcher;
    public AppEngine appEngine;

    public void onViewStarted() {

        postAppTheme();
        postLoggedInStatus();

    }



    public void postAppTheme() {

    }

    protected void postLoggedInStatus() {
        if (appEngine.isUserLoggedIn()) {
            if (appEngine.hasAdminPrevilege()) {
                adminLoggedInObservable.postValue(true);
            } else {
                userLoggedInObservable.postValue(true);
            }
            guestUserObservable.postValue(false);
        } else {
            guestUserObservable.postValue(true);
        }
    }

    public void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public void showProgressBar(String text) {
        ProgressData progressData = new ProgressData();
        progressData.progressText = text;
        progressData.show = true;
        progressDialogObservable.postValue(progressData);
    }

    public void showProgressBar() {
        ProgressData progressData = new ProgressData();
        progressData.progressText = "";
        progressData.show = true;
        progressDialogObservable.postValue(progressData);
    }

    public void hideProgressBar() {
        progressDialogObservable.postValue(null);
    }

    public String getString(int resourceId) {
        return resourceFetcher.getString(resourceId);
    }

    public String getConfigString(String key) {
        return resourceFetcher.getConfigString(key);
    }

    public void switchModule() {
        switchModuleObservable.postValue(appEngine.isUserAdmin());
    }

    public void showErrorMessage(String errorMessage) {
        errorMessageObservable.postValue(errorMessage);
    }

    public void showEmptyMessage(String errorMessage) {
        errorEmptyMessageObservable.postValue(errorMessage);
    }

    public void onLogout() {
        showProgressBar(MessageProvider.msg_logging_out);

        Disposable disposable = Completable.complete()
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    appEngine.logout();
                    hideProgressBar();
                    logoutObservable.postValue(true);
                });
        addDisposable(disposable);

    }



    public void onSwitchApp() {
        showProgressBar();
        Disposable disposable = Completable.fromAction(() -> appEngine.switchApp())
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    hideProgressBar();
                    switchAppObservable.postValue(true);
                    postAppTheme();
                });
        addDisposable(disposable);
    }


    public void openWebPage(String url){
        webPageObservable.postValue(url);
    }
    public void invalidate() {

    }

    public List<String> getGeneralSkills() {

        if(ListUtils.isNotEmpty(appEngine.generalSkills)){
            return appEngine.generalSkills;
        }else {
            String levelList = getConfigString(MessageProvider.skill_levels);
            List<String> skillLevelList = gson.fromJson(levelList, new TypeToken<List<String>>() {
            }.getType());
            return skillLevelList;
        }

    }
}
