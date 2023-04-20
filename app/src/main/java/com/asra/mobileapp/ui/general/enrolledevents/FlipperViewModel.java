package com.asra.mobileapp.ui.general.enrolledevents;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class FlipperViewModel extends BaseViewModel {

    @Inject
    public FlipperViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
    }
}
