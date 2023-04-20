package com.asra.mobileapp.ui.dashboard.shop;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Category;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.ShopUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TabbedProductsViewModel extends ShoppeViewModel {

    private ShopUseCase shopUseCase;

    public SingleLiveEvent<List<Category>> categoryListObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> categoryListErrorObservable = new SingleLiveEvent<>();

    private List<Category> gearCategoryList;
    private List<Category> rentalCategoryList;

    @Inject
    TabbedProductsViewModel(ShopUseCase shopUseCase, CartUseCase cartUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.shopUseCase = shopUseCase;
    }

    public void getGearCategories() {

        if(ListUtils.isEmpty(gearCategoryList)) {
            showProgressBar();
            Disposable disposable = shopUseCase.getGearCategoryList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onGearCategoryListFetched,
                            this::onFetchingCategoryListFailed);
            addDisposable(disposable);
        }else{
            onGearCategoryListFetched(gearCategoryList);
        }
    }


    public void getRentalCategories() {

        if(ListUtils.isEmpty(rentalCategoryList)) {
            showProgressBar();
            Disposable disposable = shopUseCase.getRentalCategoryList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onRentalCategoryListFetched,
                            this::onFetchingCategoryListFailed);
            addDisposable(disposable);
        }else{
            onRentalCategoryListFetched(rentalCategoryList);
        }


    }

    private void onFetchingCategoryListFailed(Throwable throwable) {
        hideProgressBar();
        categoryListErrorObservable.postValue(throwable.getMessage());
    }

    private void onGearCategoryListFetched(List<Category> categories) {
        this.gearCategoryList = categories;
        hideProgressBar();
        categoryListObservable.postValue(categories);
    }

    private void onRentalCategoryListFetched(List<Category> categories) {
        this.rentalCategoryList = categories;
        hideProgressBar();
        categoryListObservable.postValue(categories);
    }

    @Override
    protected void loadCartList(boolean ignoreCache) {

    }
}
