package com.asra.mobileapp.ui.guest;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.ActivityViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class GuestViewModel extends ActivityViewModel {


    @Inject
    public GuestViewModel(MemberUseCase memberUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(memberUseCase, appEngine, resourceFetcher);
    }
}
