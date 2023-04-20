package com.asra.mobileapp.database.adapter;

import com.asra.mobileapp.database.dao.GuestDBHandler;
import com.asra.mobileapp.database.entity.GuestCart;
import com.asra.mobileapp.model.BrainTreePaymentInfo;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.network.adapter.apiservices.CartApiServices;
import com.asra.mobileapp.network.retrofit.request.BrainTreePaymentRequest;
import com.asra.mobileapp.network.retrofit.response.CartListResponse;
import com.asra.mobileapp.network.retrofit.response.ETResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

public class GuestCartServices implements CartApiServices {

    private GuestDBHandler guestDBHandler;
    private GuestCartHelper guestCartHelper;

    private final static long DELAY = 300;

    @Inject
    public GuestCartServices(GuestDBHandler guestDBHandler,
                             GuestCartHelper guestCartHelper){
        this.guestDBHandler = guestDBHandler;
        this.guestCartHelper = guestCartHelper;
    }


    @Override
    public Single<CartListResponse> getCartList(String userId) {

        return Single.timer(DELAY, TimeUnit.MILLISECONDS).
                flatMap(aLong -> guestDBHandler.getGuestCartList())
                .toObservable()
                .flatMapIterable(guestCarts ->   {
                    Timber.i("Cart Count = %s", guestCarts.size());
                    return  guestCarts;
                })
                .map(guestCart -> guestCartHelper.getCartItem(guestCart))
                .toList()
                .map(cartItems -> {
                    CartListResponse cartListResponse = new CartListResponse();
                    cartListResponse.cartList = cartItems;
                    return cartListResponse;
                });
    }

    @Override
    public Completable addEvolveEventToCart(Event event, User user) {
        GuestCart guestCart = guestCartHelper.convertEventToCart(event);
        return Completable.timer(DELAY, TimeUnit.MILLISECONDS)
                .andThen(guestDBHandler.addToCart(guestCart)
                        .doOnSuccess(aLong -> Timber.i("New Cart Added - %s", aLong))
                        .ignoreElement());
    }

    @Override
    public Completable addTrackEventToCart(Event event, User user) {
        return Completable.complete();
    }

    @Override
    public Completable addMotoEventToCart(EventDetails eventDetails, User user) {
        return Completable.complete();
    }

    @Override
    public Completable addEvolveEventToCart(EventDetails eventDetails, User user) {
        List<GuestCart> guestCartList = guestCartHelper.convertEventToCart(eventDetails);
        return Completable.timer(DELAY, TimeUnit.MILLISECONDS)
                .andThen( guestDBHandler.addToCart(guestCartList));
    }

    @Override
    public Completable addGiftCardToCart(ShopCard card, String name, String email, User user) {
        GuestCart guestCart = guestCartHelper.convertGiftCardToCart(card,name, email);
        return Completable.timer(DELAY, TimeUnit.MILLISECONDS)
                .andThen( guestDBHandler.addToCart(guestCart)
                .doOnSuccess(aLong -> {
                    Timber.i("New Cart Added - %s", aLong);
                }).ignoreElement());
    }

    @Override
    public Completable addArchieCardToCart(ShopCard card, int qty, User user) {
        GuestCart guestCart = guestCartHelper.convertArchieCardToCart(card, qty);
        return Completable.timer(DELAY, TimeUnit.MILLISECONDS)
                .andThen( guestDBHandler.addToCart(guestCart)
                        .doOnSuccess(aLong -> Timber.i("New Cart Added - %s", aLong))
                        .ignoreElement());
    }

    @Override
    public Completable addMembershipToCart(Membership card, User user, boolean force) {
        return Completable.complete();
    }

    @Override
    public Completable addProductToCart(ProductDetail productDetail, User user) {

        GuestCart guestCart = guestCartHelper.convertProductToCart(productDetail);
        return Completable.timer(DELAY, TimeUnit.MILLISECONDS)
                .andThen( guestDBHandler.addToCart(guestCart)
                        .doOnSuccess(aLong -> Timber.i("New Cart Added - %s", aLong))
                        .ignoreElement());
    }

    @Override
    public Completable removeCartItem(String userId, String cartId, String method, String itemId) {
        return Completable.timer(DELAY, TimeUnit.MILLISECONDS)
                .andThen( guestDBHandler.deleteCart(cartId));
    }

    @Override
    public Completable resetCart(String userId) {
        return Completable.timer(DELAY, TimeUnit.MILLISECONDS)
                .andThen( guestDBHandler.clearTable());
    }

    @Override
    public Single<String> applyCoupon(String couponCode, String userId) {
        return Single.just("");
    }

    @Override
    public Single<String> getBrainTreeCheckoutToken(String mode, String userId, String email) {
        return Single.just("");
    }

    @Override
    public Single<BrainTreePaymentInfo> completBrainTreeTransaction(BrainTreePaymentRequest request) {
        return Single.just(new BrainTreePaymentInfo());
    }

    @Override
    public Single<String> placeOrder(String couponCode, String payment, String intent, String userId) {
        return Single.just("");
    }

    @Override
    public Completable syncLocalCart(List<CartItem> cartList, String userId) {
        return Completable.complete();
    }

    @Override
    public Single<ETResponse> validateBeforePay(String userId) {
        return Single.just(new ETResponse());
    }

}
