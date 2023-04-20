package com.asra.mobileapp.ui.dashboard.checkout.receipt;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.CheckoutData;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import javax.inject.Inject;

public class ReceiptViewModel extends ShoppeViewModel {

    public SingleLiveEvent<CheckoutData> transactionObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<UserDetails> userObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> cartTotalObservable = new SingleLiveEvent<>();
    private CheckoutData checkoutData;

    @Inject
    public ReceiptViewModel(CartUseCase cartUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
    }

    @Override
    public void loadCartList() {
        hideProgressBar();
    }

    public void setCheckoutData(CheckoutData checkoutData) {
        this.checkoutData = checkoutData;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();
        transactionObservable.postValue(checkoutData);
        userObservable.postValue(appEngine.getUserDetails());
        cartTotalObservable.postValue(
                StringUtils.formatAmount(checkoutData.cartListTotal));

    }
}
