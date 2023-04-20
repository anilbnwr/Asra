package com.asra.mobileapp.ui.guest.home;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class GuestHomeViewModel extends BaseViewModel {

    @Inject
    public GuestHomeViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
    }
}
