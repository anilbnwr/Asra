package com.asra.mobileapp.ui.admin.adminduties;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class AdminDutyViewModel extends BaseViewModel {


    @Inject
    public AdminDutyViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
    }
}
