package com.asra.mobileapp.ui.dashboard.shop.archiecards;

import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.ShopUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArchieCardsDetailViewModel extends ShoppeViewModel {


    private int qty;
    private ShopCard shopCard;


    SingleLiveEvent<ShopCard> cardDetailsObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> cardDetailsErrorObservable = new SingleLiveEvent<>();
    SingleLiveEvent<Integer> quantityObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> priceObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> totalObservable = new SingleLiveEvent<>();

    private ShopUseCase shopUseCase;
    @Inject
    public ArchieCardsDetailViewModel(ShopUseCase shopUseCase,
                                      CartUseCase cartUseCase,
                                      AppEngine appEngine,
                                      ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.shopUseCase = shopUseCase;
    }

    public void recreateShopCard(String slug, int qty) {
        this.qty = qty;
        fetchArchieCardDetails(slug);
    }

    private void fetchArchieCardDetails(String slug) {
        showProgressBar();
        Disposable disposable = shopUseCase.getArchieCardDetails(slug)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onArchieCardDetailsFetched,
                        this::onFetchingArchieCardFailed);
        addDisposable(disposable);
    }

    private void onFetchingArchieCardFailed(Throwable throwable) {
        hideProgressBar();
        cardDetailsErrorObservable.postValue(throwable.getMessage());
    }

    private void onArchieCardDetailsFetched(ShopCard shopCard) {
        hideProgressBar();
        this.shopCard = shopCard;
        cardDetailsObservable.postValue(shopCard);
        String formattedPrice = "Price : "+ StringUtils.formatAmount(shopCard.price);
        priceObservable.postValue(formattedPrice);
        updateTotal();
        quantityObservable.postValue(qty);
        analyticsHelper.logViewItemDetailEvent(AnalyticsModel.VIEW_ITEM_ARCHIE_DETAILS);
    }

    public void incrementQtyBy1() {

        qty = qty +1;
        quantityObservable.postValue(qty);
        updateTotal();
    }

    private void updateTotal() {
        if(shopCard != null){
            double total = AppUtils.getDouble(shopCard.price) * qty;
            String formattedPrice = "Total : "+ StringUtils.formatAmount(total);
            totalObservable.postValue(formattedPrice);
        }
    }


    public void decrementQtyBy1() {
        if(qty > 0){
            --qty;
        }
        quantityObservable.postValue(qty);
        updateTotal();
    }

    public void onAddToCart() {
        if(shopCard != null) {
            showProgressBar(getConfigString(MessageProvider.msg_adding_to_cart));

            Disposable disposable = cartUseCase.addArchieToCart(shopCard, qty)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> onCartUpdatedSuccessfully(
                            getConfigString(MessageProvider.msg_cart_archie_added)),
                            throwable -> onCartUpdateError(throwable.getMessage()));
            addDisposable(disposable);
        }

    }

}
