package com.asra.mobileapp.ui.dashboard.cart;

import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.common.dialog.EmergencyContactDialog;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CartViewModel extends ShoppeViewModel {

    SingleLiveEvent<String> cartListErrorObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> cartTotalObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> cartDeleteSuccessObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> cartDeleteErrorObservable = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> hasOutOfStockObservable = new SingleLiveEvent<>();

    SingleLiveEvent<Boolean> isGuestObservable = new SingleLiveEvent<>();

    SingleLiveEvent<Boolean> proceedToPaymentObservable = new SingleLiveEvent<>();
    SingleLiveEvent<UserDetails> emergencyContactRequiredObservable = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> loginRequiredObservable = new SingleLiveEvent<>();

    private double cartTotal = 0;
    private boolean hasOutOfStockItems;
    private List<CartItem> cartDataSet;
    private MemberUseCase memberUseCase;

    @Inject
    public CartViewModel(CartUseCase cartUseCase, MemberUseCase memberUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();
        loadCartList();
        isGuestObservable.postValue(appEngine.isUserLoggedIn());
    }

    @Override
    public boolean shouldSyncCart() {
        return true;
    }

    @Override
    protected void loadCartList(boolean ignoreCache) {
        showProgressBar(getConfigString(MessageProvider.msg_syncing_cart));
        super.loadCartList(ignoreCache);
    }

    @Override
    protected void onFetchingCartListFailed(Throwable throwable) {
        super.onFetchingCartListFailed(throwable);
        hideProgressBar();

        String message = throwable.getMessage();
        if (StringUtils.isEmpty(message)) {
            message = getConfigString(MessageProvider.cart_empty_message);
            Timber.e("Empty message from throwable - Cart List");
        }
        cartListErrorObservable.postValue(message);
    }

    @Override
    protected void onCartListFetched(List<CartItem> cartItems) {
        super.onCartListFetched(cartItems);
        hideProgressBar();

        this.cartDataSet = cartItems;
        updateTotal();
    }

    protected void updateTotal() {

        cartTotal = 0;
        hasOutOfStockItems = false;
        for (CartItem item : cartDataSet) {
            cartTotal += AppUtils.getDouble(item.price) * (Integer.valueOf(item.quantity));

            if (item.isOutOfStock()) {
                hasOutOfStockItems = true;
            }
        }

        cartTotalObservable.postValue(StringUtils.formatAmount(cartTotal));
        hasOutOfStockObservable.postValue(hasOutOfStockItems);

    }

    public void deleteFromCart(CartItem cart) {

        showProgressBar(getConfigString(MessageProvider.msg_deleting_from_cart));
        Disposable disposable = cartUseCase.deleteCartItem(cart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCartDeleted,
                        this::onCartDeleteFailed);
        addDisposable(disposable);
    }

    private void onCartDeleteFailed(Throwable throwable) {
        hideProgressBar();
        cartDeleteErrorObservable.postValue(throwable.getMessage());
    }

    private void onCartDeleted() {
        hideProgressBar();
        cartDeleteSuccessObservable.postValue(getConfigString(MessageProvider.msg_cart_deleted));
        loadCartList(true);
    }

    public void proceedToPayment() {
        if (appEngine.isUserLoggedIn()) {
            validateCartBeforePay();
        } else {
            loginRequiredObservable.postValue(true);
        }
    }

    private void validateCartBeforePay() {
        showProgressBar("");
        Disposable disposable = cartUseCase.validateBeforePay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(etResponse -> {
                            hideProgressBar();
                            proceedToPaymentObservable.postValue(true);
                            //emergencyContactRequiredObservable.postValue(appEngine.getUserDetails());
                        },
                        throwable -> {
                            hideProgressBar();
                            if (throwable instanceof ETApiException) {
                                ETApiException exception = (ETApiException) throwable;
                                if (exception.errorCode == ETApiException.ApiErrorCode.EMERGENCY_CONTACT) {
                                    emergencyContactRequiredObservable.postValue(appEngine.getUserDetails());
                                } else if (exception.errorCode == ETApiException.ApiErrorCode.BILLING_ADDRESS) {
                                    proceedToPaymentObservable.postValue(true);
                                }else{
                                    showErrorMessage(throwable.getMessage());
                                }
                            } else {
                                showErrorMessage(throwable.getMessage());
                            }
                        });
        addDisposable(disposable);
    }

    public void saveEmergencyContact(EmergencyContactDialog.EmergencyContact emergencyContact) {
        showProgressBar();
        Disposable disposable = memberUseCase.updateEmergencyContact(emergencyContact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            hideProgressBar();
                            proceedToPayment();
                        },
                        throwable -> {
                            hideProgressBar();
                            showErrorMessage(throwable.getMessage());
                        });
        addDisposable(disposable);
    }
}
