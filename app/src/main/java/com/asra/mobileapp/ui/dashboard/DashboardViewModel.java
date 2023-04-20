package com.asra.mobileapp.ui.dashboard;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.ActivityViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class DashboardViewModel extends ActivityViewModel {

    public SingleLiveEvent<Boolean> switchToAdminObserver = new SingleLiveEvent<>();

    @Inject
    public DashboardViewModel(MemberUseCase memberUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(memberUseCase, appEngine, resourceFetcher);
    }
    public void onSwitchToAdmin(){

        if(appEngine.isUserAdmin()){
            exitActivityObservable.postValue(true);
        }else{
            switchToAdminObserver.postValue(true);
        }

    }

}
