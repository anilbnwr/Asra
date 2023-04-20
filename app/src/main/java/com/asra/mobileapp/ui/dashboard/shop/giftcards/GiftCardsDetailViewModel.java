package com.asra.mobileapp.ui.dashboard.shop.giftcards;

import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.common.UiUtils;
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

public class GiftCardsDetailViewModel extends ShoppeViewModel {


    private ShopCard shopCard;


    SingleLiveEvent<ShopCard> cardDetailsObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> cardDetailsErrorObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> priceObservable = new SingleLiveEvent<>();

    SingleLiveEvent<String> validationObservable = new SingleLiveEvent<>();

    private ShopUseCase shopUseCase;
    @Inject
    public GiftCardsDetailViewModel(ShopUseCase shopUseCase,
                                    CartUseCase cartUseCase,
                                    AppEngine appEngine,
                                    ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.shopUseCase = shopUseCase;
    }

    public void recreateShopCard(String slug) {
        fetchGiftCardDetails(slug);
    }

    private void fetchGiftCardDetails(String slug) {
        showProgressBar();
        Disposable disposable = shopUseCase.getGiftCardDetails(slug)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGiftCardDetailsFetched,
                        this::onFetchingGiftCardFailed);
        addDisposable(disposable);
    }

    private void onFetchingGiftCardFailed(Throwable throwable) {
        hideProgressBar();
        cardDetailsErrorObservable.postValue(throwable.getMessage());
    }

    private void onGiftCardDetailsFetched(ShopCard shopCard) {
        hideProgressBar();
        this.shopCard = shopCard;
        cardDetailsObservable.postValue(shopCard);
        updateTotal();
        analyticsHelper.logViewItemDetailEvent(AnalyticsModel.VIEW_ITEM_ARCHIE_DETAILS);
    }



    private void updateTotal() {
        if(shopCard != null){

            String formattedPrice = "Total : $"+String.format("%.2f", AppUtils.getDouble(shopCard.price));
            priceObservable.postValue(formattedPrice);
        }
    }



    public void onAddToCart(String receiverName, String email) {
        if (StringUtils.isEmpty(receiverName)) {
            validationObservable.postValue(getConfigString(MessageProvider.error_specify_receiver_name));
        } else if (!UiUtils.isValidEmail(email)) {
            validationObservable.postValue(getConfigString(MessageProvider.error_specify_receiver_email));
        }else {

            if (shopCard != null) {
                showProgressBar(getConfigString(MessageProvider.msg_adding_to_cart));

                Disposable disposable = cartUseCase.addGiftToCart(shopCard, receiverName, email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> onCartUpdatedSuccessfully(
                                getConfigString(MessageProvider.msg_cart_gift_added)),
                                throwable -> onCartUpdateError(throwable.getMessage()));
                addDisposable(disposable);
            }
        }
    }

}
