package com.asra.mobileapp.ui.dashboard;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class ShoppeViewModel extends BaseViewModel {

    public SingleLiveEvent<List<CartItem>> cartListObservable = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> cartItemCountObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> cartUpdatedObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> cartUpdateErrorObservable = new SingleLiveEvent<>();

    protected CartUseCase cartUseCase;

    public ShoppeViewModel(CartUseCase cartUseCase,
                           AppEngine appEngine,
                           ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.cartUseCase = cartUseCase;
    }

    public boolean shouldSyncCart(){
        List<CartItem> cachedCartList = cartListObservable.getValue();
        return ListUtils.isEmpty(cachedCartList);
    }

    protected void loadCartList(boolean ignoreCache) {
        List<CartItem> cachedCartList = appEngine.getCartItems();
        if(ignoreCache || ListUtils.isEmpty(cachedCartList)) {
            Disposable disposable = cartUseCase.getCartList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(cartItems -> {
                        if(ListUtils.isEmpty(cartItems)) {
                            appEngine.setCartItems(new ArrayList<>());
                            throw new ETApiException(getConfigString(MessageProvider.cart_empty_message));
                        }
                    })
                    .doOnError(throwable -> appEngine.setCartItems(new ArrayList<>()))
                    .subscribe(this::onCartListFetched,
                            this::onFetchingCartListFailed);
            addDisposable(disposable);
        }else{
            onCartListFetched(cachedCartList);
        }
    }
    public void loadCartList() {
        loadCartList(shouldSyncCart());
    }

    protected void onFetchingCartListFailed(Throwable throwable) {

    }

    protected void onCartListFetched(List<CartItem> cartItems) {

        if(ListUtils.isEmpty(cartItems)){
            onFetchingCartListFailed(new ETApiException(
                    getConfigString(MessageProvider.cart_empty_message)));
        }else{
            cartListObservable.postValue(cartItems);
        }
        appEngine.setCartItems(cartItems);
        cartItemCountObservable.postValue(cartItems.size());

    }

    public void updateCartCountBy(int count) {
        loadCartList(true);

    }

    public void updateMenu() {
        postCartCount();
        postLoggedInStatus();
    }

    protected void postCartCount(){
        cartItemCountObservable.postValue(appEngine.getCartCount());
    }

    public SingleLiveEvent<Integer> getCartItemCountObservable(){
        return cartItemCountObservable;
    }


    public void onCartUpdatedSuccessfully(String message){
        hideProgressBar();
        cartUpdatedObservable.postValue(message);
        loadCartList(true);
    }

    public void onCartUpdateError(String message){
        hideProgressBar();
        cartUpdateErrorObservable.postValue(message);
    }

}
