package com.asra.mobileapp.ui.splash;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.ActivityViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class SplashViewModel extends ActivityViewModel {


    public SingleLiveEvent<Boolean> loginRequiredObserver = new SingleLiveEvent();
    public SingleLiveEvent<Boolean> regularUserLoggedObserver = new SingleLiveEvent();
    public SingleLiveEvent<Boolean> adminLoggedObserver = new SingleLiveEvent();

    @Inject
    public SplashViewModel(MemberUseCase memberUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(memberUseCase, appEngine, resourceFetcher);
    }


    public void onSplashFinished() {

        if (appEngine.isUserLoggedIn()) {
            if (appEngine.isUserAdmin()) {
                adminLoggedObserver.postValue(true);
            } else if (appEngine.isUserCoach()) {
                regularUserLoggedObserver.postValue(true);
            } else {
                regularUserLoggedObserver.postValue(true);
            }
        } else {
            loginRequiredObserver.postValue(true);
        }
    }
}
