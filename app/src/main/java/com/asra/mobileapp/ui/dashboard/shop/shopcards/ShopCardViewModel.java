package com.asra.mobileapp.ui.dashboard.shop.shopcards;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.ShopUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShopCardViewModel extends ShoppeViewModel {

    public SingleLiveEvent<List<ShopCard>> archieCardsObservale = new SingleLiveEvent<>();
    public SingleLiveEvent<String> archieCardsErrorObservale = new SingleLiveEvent<>();

    public SingleLiveEvent<List<ShopCard>> giftCardsObservale = new SingleLiveEvent<>();
    public SingleLiveEvent<String> giftCardsErrorObservale = new SingleLiveEvent<>();



    private ShopUseCase shopUseCase;
    @Inject
    public ShopCardViewModel(ShopUseCase shopUseCase,
                             CartUseCase cartUseCase,
                             AppEngine appEngine,
                             ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.shopUseCase = shopUseCase;
    }


    public void getArchieCards(){
        showProgressBar();
        Disposable disposable = shopUseCase.getArchieCard()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onArchieCardsFetched,
                        this::onFetchingArchieCardsFailed);
        addDisposable(disposable);
    }

    public void onFetchingArchieCardsFailed(Throwable throwable) {
        hideProgressBar();
        archieCardsErrorObservale.postValue(throwable.getMessage());
    }

    protected void onArchieCardsFetched(List<ShopCard> shopCards) {
        hideProgressBar();
        archieCardsObservale.postValue(shopCards);
    }

    public void getGiftCards(){
        showProgressBar();
        Disposable disposable = shopUseCase.getGiftCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGiftCardsFetched,
                        this::onFetchingGiftCardsFailed);
        addDisposable(disposable);
    }

    protected void onFetchingGiftCardsFailed(Throwable throwable) {
        hideProgressBar();
        giftCardsErrorObservale.postValue(throwable.getMessage());
    }

    protected void onGiftCardsFetched(List<ShopCard> shopCards) {
        hideProgressBar();
        giftCardsObservale.postValue(shopCards);
    }
}
