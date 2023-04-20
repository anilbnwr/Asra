package com.asra.mobileapp.ui.dashboard.checkout.payment;

import android.text.TextUtils;

import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.cardform.view.CardForm;
import com.asra.mobileapp.BuildConfig;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.BrainTreePaymentInfo;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.CheckoutData;
import com.asra.mobileapp.network.retrofit.request.BrainTreePaymentRequest;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.ui.dashboard.checkout.CheckoutConstants;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PaymentViewModel extends ShoppeViewModel {


    public SingleLiveEvent<Boolean> canApplyWallet = new SingleLiveEvent<>();
    public SingleLiveEvent<String> walletAmount = new SingleLiveEvent<>();
    public SingleLiveEvent<String> cartModifiedErrorObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> enableWalletPaymentObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> outOfStockObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> paymentRequiredObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> hasAppliedFromCouponObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> subTotalObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> totalPayableObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> discountFromCouponObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> appliedFromWalletObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<CheckoutData> paymentCompleteObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> paymentErrorClearTabsObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> paymentErrorObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<DropInRequest> dropInRequestObservable = new SingleLiveEvent<>();


    private CheckoutData checkoutData;
    private int payWith = CheckoutConstants.PAY_WITH_PAYPAL;

    @Inject
    public PaymentViewModel(CartUseCase cartUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();

        canApplyWallet.postValue(shouldApplyFromWallet());
        walletAmount.postValue(StringUtils.formatAmount(appEngine.getWalletBalance()));


        postCartSummary();
        enableWalletPaymentObservable.postValue(shouldEnableWalletPayment());
    }

    private boolean shouldApplyFromWallet(){
        return  checkoutData.cartListTotal - checkoutData.appliedCouponDiscount > 0
                && checkoutData.walletBalance > 0
                && checkoutData.walletBalance <= checkoutData.cartListTotal - checkoutData.appliedCouponDiscount
                &&checkoutData.walletEnabled;
    }
    private boolean shouldEnableWalletPayment(){
        return  checkoutData.cartListTotal - checkoutData.appliedCouponDiscount > 0
                && checkoutData.walletBalance > 0
                && checkoutData.walletBalance > checkoutData.cartListTotal - checkoutData.appliedCouponDiscount
                &&checkoutData.walletEnabled;
    }

    private void postCartSummary(){

        checkoutData.dueAmount = checkoutData.cartListTotal - checkoutData.appliedCouponDiscount;
        double walletDiscount = checkoutData.walletBalance;
        if(checkoutData.walletBalance >  checkoutData.dueAmount){
            walletDiscount = checkoutData.dueAmount;
        }

        if(shouldApplyFromWallet()){
            checkoutData.dueAmount = checkoutData.dueAmount - walletDiscount;
        }
        String subTotal = StringUtils.formatAmount(checkoutData.cartListTotal);

        String discountFromCoupon = StringUtils.formatAmount(checkoutData.appliedCouponDiscount);

        String appliedFromWallet = StringUtils.formatAmount(walletDiscount);

        boolean paymentRequired = checkoutData.dueAmount >0;

        boolean hasCouponApplied = checkoutData.appliedCouponDiscount > 0;

        String totalPayable = StringUtils.formatAmount(checkoutData.dueAmount);

        hasAppliedFromCouponObservable.postValue(hasCouponApplied);
        paymentRequiredObservable.postValue(paymentRequired);

        subTotalObservable.postValue(subTotal);
        discountFromCouponObservable.postValue(discountFromCoupon);
        appliedFromWalletObservable.postValue(appliedFromWallet);
        totalPayableObservable.postValue(totalPayable);


    }


    public void setCheckoutData(CheckoutData checkoutData) {
        this.checkoutData = checkoutData;
    }

    private double getTotalDiscounts(){
        double totalDiscount = checkoutData.appliedCouponDiscount;
        double walletDiscount = 0d;
        if(shouldApplyFromWallet()){
            walletDiscount = checkoutData.walletBalance;
            if(checkoutData.walletBalance >  checkoutData.dueAmount){
                walletDiscount = checkoutData.dueAmount;
            }
            totalDiscount += walletDiscount;
        }
        return totalDiscount;
    }

    private String getPaymentType() {
        boolean paymentWalletChecked = payWith == CheckoutConstants.PAY_WITH_WALLET;
        boolean paymentPaypalChecked = payWith == CheckoutConstants.PAY_WITH_PAYPAL;

        String paymentType = "" ;

        Timber.d("Applied Discount - %s", checkoutData.appliedCouponDiscount);

        Timber.d("shouldApplyFromWallet() - %s || paymentWallet.isChecked - %s",
                shouldApplyFromWallet(), paymentWalletChecked);
        Timber.d("paymentRequired() -%s || paymentPaypal.isChecked() - %s",
                paymentRequired(), paymentWalletChecked);



        if(checkoutData.appliedCouponDiscount > 0){
            paymentType = CheckoutConstants.PAY_COUPON + "-";
        }
        if(shouldApplyFromWallet() || paymentWalletChecked){
            paymentType +=  CheckoutConstants.PAY_WALLET + "-";
        }

        if(paymentRequired() && paymentPaypalChecked){
            paymentType += CheckoutConstants.PAY_PAYPAL;
        }

        if(!TextUtils.isEmpty(paymentType)){
            if(paymentType.lastIndexOf("-") == paymentType.length() - 1){
                paymentType = paymentType.substring(0, paymentType.length() - 1);
            }
        }else{
            paymentType = CheckoutConstants.PAY_WALLET;
        }

        Timber.i("Payment Mode - %s", paymentType);

        return paymentType;
    }

    private boolean paymentRequired() {
        return checkoutData.dueAmount > 0;
    }
    public void onPlaceOrder(int paywith) {
        this.payWith = paywith;
        showProgressBar(getConfigString(MessageProvider.msg_checkout_place_order));
        reviewCartList();
    }

    private void reviewCartList() {
        Disposable disposable = cartUseCase.getCartList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(cartItems -> {
                    if(ListUtils.isEmpty(cartItems))
                        throw new ETApiException(getConfigString(MessageProvider.cart_empty_message));
                })
                .subscribe(this::onCartListReview,
                        this::onFetchingCartListFailed);
        addDisposable(disposable);


    }

    private void onCartListReview(List<CartItem> cartItems) {
        super.onCartListFetched(cartItems);
        boolean hasOutOfStock = false;
        double tempTotal = 0;
        for (CartItem item : cartItems) {
            double itemTotal = Integer.valueOf(item.quantity) * AppUtils.getDouble(item.price);
            if (!TextUtils.isEmpty(item.feeAmount)) {

                double feeAmount = AppUtils.getDouble(item.feeAmount);
                if (feeAmount > 0) {
                    itemTotal += feeAmount;
                }

                if(item.isOutOfStock())
                    hasOutOfStock = true;
                tempTotal += itemTotal;
            }
        }

        if(hasOutOfStock){
            outOfStockObservable.postValue(hasOutOfStock);
        }else if(tempTotal != checkoutData.cartListTotal
                || cartItems.size() != checkoutData.cartCount){
            checkoutData.cartListTotal = tempTotal;
            checkoutData.walletEnabled = appEngine.isWalletEnabled();
            checkoutData.dueAmount = tempTotal - checkoutData.appliedCouponDiscount;

            postCartSummary();
            cartModifiedErrorObservable.postValue(
                    getConfigString(MessageProvider.msg_cart_changed_review_required));
        }else{
            initiatePayment();
        }
    }

    private void initiatePayment() {

        if(paymentRequired()){
            if(payWith == CheckoutConstants.PAY_WITH_WALLET){
                completeTransaction();
            }else if (payWith == CheckoutConstants.PAY_WITH_CREDIT){
                payWithCreditCard();
            }else{
                initiatePaypalPayment();
            }
        }else{
            completeTransaction();
        }
    }

    private void initiatePaypalPayment() {
        Disposable disposable = cartUseCase.getCheckoutToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBrainTreeTokenFetched,
                        this::onBrainTreeTokenFailed);
        addDisposable(disposable);
    }

    private void onBrainTreeTokenFailed(Throwable throwable) {
        hideProgressBar();
        showErrorMessage(getConfigString(MessageProvider.error_paypal_payment_not_available));

    }

    private void onBrainTreeTokenFetched(String brainTreeToken) {


        DropInRequest dropInRequest = new DropInRequest().clientToken(brainTreeToken);
        dropInRequest.amount(String.valueOf(checkoutData.dueAmount));
        dropInRequest.requestThreeDSecureVerification(true);
        dropInRequest.vaultManager(true);
        dropInRequest.cardholderNameStatus(CardForm.FIELD_REQUIRED);
        dropInRequestObservable.postValue(dropInRequest);
        hideProgressBar();
    }

    private void payWithCreditCard() {
        showErrorMessage(getConfigString(MessageProvider.payment_credit_not_supported));
    }

    private void completeTransaction() {
        showProgressBar(getConfigString(MessageProvider.msg_checkout_place_order));
        Disposable disposable = cartUseCase.placeOrder(checkoutData.couponCode, getPaymentType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTransactionCompleted,
                        this::onTransactionFailed);
        addDisposable(disposable);
    }

    private void onTransactionFailed(Throwable throwable) {
        hideProgressBar();
        paymentErrorObservable.postValue(throwable.getMessage());
    }

    private void onTransactionCompleted(String referenceNumber) {
        resetCart();
        checkoutData.orderNumber = referenceNumber;
        paymentCompleteObservable.postValue(checkoutData);
        Timber.i("Payment Completed , Ref - %s ", referenceNumber);
        analyticsHelper.logCheckoutComplete(""+getTotalDiscounts(),
                checkoutData.dueAmount+"", referenceNumber);
    }

    @Override
    protected void onFetchingCartListFailed(Throwable throwable) {
        super.onFetchingCartListFailed(throwable);
        hideProgressBar();
        showErrorMessage(throwable.getMessage());
    }

    public void completeDropInPayment(String paymentNonce) {
        showProgressBar(getConfigString(MessageProvider.msg_checkout_place_order));
        BrainTreePaymentRequest request = new BrainTreePaymentRequest();
        request.amount = checkoutData.dueAmount + "";
        request.mode = BuildConfig.CHECKOUT_MODE;
        request.brainTreeNonce = paymentNonce;
        request.userId = appEngine.getUserId();
        request.coupon = checkoutData.couponCode;
        request.paymentType = getPaymentType();

        Disposable disposable = cartUseCase.placeOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBrainTreeTransactionCompleted,
                        this::onTransactionFailed);
        addDisposable(disposable);
    }

    private void onBrainTreeTransactionCompleted(
            BrainTreePaymentInfo brainTreePaymentInfo) {

        analyticsHelper.logPlaceOrderError(appEngine.getUserId(),
                brainTreePaymentInfo.paymentStatus,
                brainTreePaymentInfo.cartStatus,
                brainTreePaymentInfo.message);

        if(AppUtils.isTrue(brainTreePaymentInfo.paymentStatus)){
            resetCart();
            checkoutData.orderNumber = brainTreePaymentInfo.orderId;
            paymentCompleteObservable.postValue(checkoutData);
        }else{
            hideProgressBar();
            if(CheckoutConstants.CODE_CART_CLEARED.equalsIgnoreCase(
                    brainTreePaymentInfo.cartStatus)){
                paymentErrorClearTabsObservable.postValue(brainTreePaymentInfo.message);
            }else{
                paymentErrorObservable.postValue(brainTreePaymentInfo.message);
            }
        }
    }

    private void resetCart() {
        Disposable disposable = cartUseCase.resetCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Timber.i("Cart Has been Reset");
                    hideProgressBar();
                }, throwable -> hideProgressBar());
        addDisposable(disposable);
    }
}
