package com.asra.mobileapp.ui.dashboard.shop.product;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.CategoryHeader;
import com.asra.mobileapp.model.Product;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.ShopUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductViewModel extends ShoppeViewModel {

    public SingleLiveEvent<List<Product>> productsObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> productListErrorObservable = new SingleLiveEvent<>();



    private ShopUseCase shopUseCase;

    @Inject
    public ProductViewModel(ShopUseCase shopUseCase, CartUseCase cartUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.shopUseCase = shopUseCase;
    }

    public void getGearProductDetails(String category){
        showProgressBar();
        Disposable disposable = shopUseCase.getGearProducts(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onProductListFetched,
                        this::onFetchingProductsFailed);
        addDisposable(disposable);

    }

    private void onFetchingProductsFailed(Throwable throwable) {
        hideProgressBar();
        productListErrorObservable.postValue(throwable.getMessage());
    }

    private void onProductListFetched(List<Product> products) {
        hideProgressBar();
        productsObservable.postValue(products);
    }

    public void getRentalProductDetails(String category){
        showProgressBar();
        Disposable disposable = shopUseCase.getRentalProducts(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onProductListFetched,
                        this::onFetchingProductsFailed);
        addDisposable(disposable);

    }



    public void getProductList(String categoryHeader, String categoryId) {
        if(categoryHeader == CategoryHeader.TITLE_GEAR){
            getGearProductDetails(categoryId);
        }else if(categoryHeader == CategoryHeader.TITLE_RENTAL){
            getRentalProductDetails(categoryId);
        }
    }
}
