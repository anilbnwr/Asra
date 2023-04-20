package com.asra.mobileapp.ui.general.settings;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class TermsViewModel extends BaseViewModel {

    @Inject
    public TermsViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
    }
}
