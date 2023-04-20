package com.asra.mobileapp.ui.admin;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.ActivityViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class AdminViewModel extends ActivityViewModel {

    @Inject
    public AdminViewModel(MemberUseCase memberUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(memberUseCase, appEngine, resourceFetcher);
    }





}
