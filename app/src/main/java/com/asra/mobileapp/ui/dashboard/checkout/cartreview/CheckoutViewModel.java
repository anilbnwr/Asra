package com.asra.mobileapp.ui.dashboard.checkout.cartreview;

import android.text.TextUtils;

import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.CheckoutData;
import com.asra.mobileapp.model.Coupon;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CheckoutViewModel extends ShoppeViewModel {


    public SingleLiveEvent<String> totalPriceObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> couponDiscountObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> subTotalObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> hasOutOfStockObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<CheckoutData> paymentObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Coupon> couponObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> walletObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> couponErrorObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<UserDetails> currentUserObservable = new SingleLiveEvent<>();

    private double cartTotal = 0;
    private double appliedCouponAmount = 0;
    private Coupon coupon = new Coupon();
    private List<CartItem> cartItems;

    private boolean hasOutOfStockItems = false;

    @Inject
    public CheckoutViewModel(CartUseCase cartUseCase,
                             AppEngine appEngine,
                             ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
    }


    @Override
    public void onViewStarted() {
        super.onViewStarted();
        currentUserObservable.postValue(appEngine.getUserDetails());
    }

    @Override
    public void loadCartList() {
        showProgressBar(getConfigString(MessageProvider.msg_reading_cart_summary));
        super.loadCartList();
    }

    public void applyCoupon(String couponCode) {
        coupon.couponCode = couponCode;
        showProgressBar(getConfigString(MessageProvider.checkout_applying_coupon_code));

        Disposable disposable = cartUseCase.applyCoupon(couponCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCouponApplied,
                        this::onCouponValidationFailed);
        addDisposable(disposable);
    }

    private void onCouponValidationFailed(Throwable throwable) {
        hideProgressBar();
        couponErrorObservable.postValue(throwable.getMessage());
    }

    private void onCouponApplied(String couponBalance) {
        hideProgressBar();
        coupon.couponBalance = AppUtils.getDouble(couponBalance);
        computeTotalPrices();
        postCouponStatus();
    }

    public void onCouponDelete() {
        coupon.couponCode = "";
        coupon.couponBalance = 0;
        appliedCouponAmount = 0;
        postCouponStatus();
        computeTotalPrices();
    }

    @Override
    protected void onCartListFetched(List<CartItem> cartItems) {
        super.onCartListFetched(cartItems);

        this.cartItems = cartItems;
        computeTotalPrices();
        walletObservable.postValue(StringUtils.formatAmount(
                appEngine.getUserDetails().walletAmount));
        hideProgressBar();
    }

    @Override
    protected void onFetchingCartListFailed(Throwable throwable) {
        super.onFetchingCartListFailed(throwable);
        hideProgressBar();
    }

    private void computeTotalPrices() {
        double subTotal = 0;
        hasOutOfStockItems = false;
        for(CartItem item : cartItems){
            double itemTotal = Integer.valueOf(item.quantity) * AppUtils.getDouble(item.price);
            if (!TextUtils.isEmpty(item.feeAmount)) {

                double feeAmount = AppUtils.getDouble(item.feeAmount);
                if (feeAmount > 0) {
                    itemTotal += feeAmount;
                }
                subTotal += itemTotal;
            }

            if(item.isOutOfStock())
                hasOutOfStockItems = true;
        }
        double tempTotal = subTotal - coupon.couponBalance;
        if(tempTotal <= 0){
            cartTotal = 0;
            appliedCouponAmount = subTotal;
        }else {
            cartTotal = tempTotal;
            appliedCouponAmount = subTotal - tempTotal;
        }

        subTotalObservable.postValue(StringUtils.formatAmount(subTotal));
        totalPriceObservable.postValue(StringUtils.formatAmount(cartTotal));
        hasOutOfStockObservable.postValue(hasOutOfStockItems);

        postCouponStatus();
    }

    private void postCouponStatus() {
        couponObservable.postValue(coupon);
        if(coupon.couponBalance == 0){
            couponDiscountObservable.postValue("");
        }else{
            couponDiscountObservable.postValue(StringUtils.formatAmount(appliedCouponAmount));
        }
    }

    public void proceedToPayment() {
        if(hasOutOfStockItems){
            hasOutOfStockObservable.postValue(true);
        }else if (!appEngine.getUserDetails().hasValidBillingAddress()){
            showErrorMessage("Please provide your valid billing address.");
        }else{

            CheckoutData checkoutData = new CheckoutData();
            checkoutData.dueAmount = cartTotal ;
            checkoutData.cartListTotal = cartTotal + appliedCouponAmount;

            checkoutData.couponBalance = coupon.couponBalance;
            checkoutData.couponCode = appliedCouponAmount > 0 ? coupon.couponCode : "";
            checkoutData.appliedCouponDiscount = appliedCouponAmount;

            checkoutData.walletEnabled = appEngine.isWalletEnabled();
            checkoutData.walletBalance = appEngine.getWalletBalance();

            checkoutData.cartCount = cartItems.size();
            paymentObservable.postValue(checkoutData);
        }
    }
}
