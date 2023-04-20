package com.asra.mobileapp.ui.main;


import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel

    @Inject
    public MainViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher){
        super(appEngine, resourceFetcher);

    }
}
