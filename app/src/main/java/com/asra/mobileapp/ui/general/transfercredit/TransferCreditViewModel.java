package com.asra.mobileapp.ui.general.transfercredit;

import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.CartItem;
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

public class TransferCreditViewModel extends ShoppeViewModel {

    public SingleLiveEvent<String> transferSuccessObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> balanceCreditObservable = new SingleLiveEvent<>();

    private MemberUseCase memberUseCase;

    @Inject
    public TransferCreditViewModel(MemberUseCase memberUseCase,
                                   AppEngine appEngine,
                                   ResourceFetcher resourceFetcher, CartUseCase cartUseCase) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();

        balanceCreditObservable.postValue(StringUtils.formatAmount(appEngine.getWalletBalance()));

    }

    public void onTransferCredit(String email, String amount) {
        showProgressBar();

        Disposable disposable = memberUseCase.transferCredit(AppUtils.getDouble(amount), email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            loadCartList(true);
                        },
                        throwable -> {
                            hideProgressBar();
                            showErrorMessage(throwable.getMessage());
                        });
        addDisposable(disposable);
    }

    @Override
    protected void onCartListFetched(List<CartItem> cartItems) {
        hideProgressBar();
        super.onCartListFetched(cartItems);
        transferSuccessObservable.postValue("Transaction was successful.");
        balanceCreditObservable.postValue(StringUtils.formatAmount(appEngine.getWalletBalance()));

    }

    @Override
    protected void onFetchingCartListFailed(Throwable throwable) {
        super.onFetchingCartListFailed(throwable);

        hideProgressBar();
        transferSuccessObservable.postValue("Transaction was successful.");
        balanceCreditObservable.postValue(StringUtils.formatAmount(appEngine.getWalletBalance()));


    }
}
