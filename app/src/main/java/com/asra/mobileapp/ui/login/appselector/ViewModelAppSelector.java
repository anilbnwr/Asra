package com.asra.mobileapp.ui.login.appselector;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class ViewModelAppSelector extends BaseViewModel {


    private AppEngine appEngine;

    public SingleLiveEvent<Boolean> loginRequired = new SingleLiveEvent<>();
    @Inject
    ViewModelAppSelector(AppEngine appEngine,
                         ResourceFetcher resourceFetcher){
        super(appEngine, resourceFetcher);

        this.appEngine = appEngine;
    }

    public void onMotoAppSelected(){
        appEngine.setThemeMotoApp();
        loginRequired.postValue(true);
    }

    public void onEvAppSelected(){
        appEngine.setThemeEvApp();
        loginRequired.postValue(true);
    }
}
