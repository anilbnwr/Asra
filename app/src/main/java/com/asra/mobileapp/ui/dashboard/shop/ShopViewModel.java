package com.asra.mobileapp.ui.dashboard.shop;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class ShopViewModel extends ShoppeViewModel {


    @Inject
    public ShopViewModel(CartUseCase cartUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();
        hideProgressBar();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        hideProgressBar();
    }
}
