package com.asra.mobileapp.usecase;

import com.asra.mobileapp.BuildConfig;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.database.adapter.GuestCartServices;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.BrainTreePaymentInfo;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.network.adapter.apiservices.CartApiServices;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveCartApiServices;
import com.asra.mobileapp.network.retrofit.request.BrainTreePaymentRequest;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.ui.dashboard.checkout.CheckoutConstants;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class CartUseCase extends BaseUseCase {


    private CartApiServices evolveApiServices;
    private CartApiServices dbServices;
    private ResourceFetcher resourceFetcher;

    @Inject
    public CartUseCase(EvolveCartApiServices evolveCartApiServices,
                       AppEngine appEngine,
                       GuestCartServices guestCartServices,
                       ResourceFetcher resourceFetcher) {
        super(appEngine);

        this.evolveApiServices = evolveCartApiServices;
        this.dbServices = guestCartServices;
        this.resourceFetcher = resourceFetcher;
    }


    private CartApiServices getCartApiSerices() {
        CartApiServices cartApiServices;
        if (appEngine.isUserLoggedIn()) {
            cartApiServices =  evolveApiServices ;

        } else {
            cartApiServices = dbServices;
        }
        return cartApiServices;
    }

    public Single<List<CartItem>> getCartList() {
        return getCartApiSerices().getCartList(appEngine.getUserId())
                .doOnSuccess(cartListResponse -> {
                    appEngine.saveWalletBalance(cartListResponse.walletBalance);
                    appEngine.setWalletEnabled(cartListResponse.walletEnabled);
                })
                .map(cartListResponse -> {
                    if (ListUtils.isEmpty(cartListResponse.cartList)) {
                        cartListResponse.cartList = new ArrayList<>();
                        throw new ETApiException(resourceFetcher.getConfigString(MessageProvider.cart_empty_message));
                    }else{
                        CartItem raceFee = null;
                        for(CartItem item : cartListResponse.cartList){
                            if(item.isRaceFee()){
                                raceFee = item;
                                break;
                            }
                        }
                        if(raceFee != null){
                            cartListResponse.cartList.remove(raceFee);
                            cartListResponse.cartList.add(0, raceFee);
                        }
                    }
                    return cartListResponse.cartList;
                });
    }

    public Completable addEventToCart(Event event) {
        if(event.isMotoEvent()){
           // Ignored
            return Completable.complete();
        }else if(appEngine.isUserLoggedIn()){
            return evolveApiServices.addEvolveEventToCart(event, appEngine.getCurrentUser());
        }else{
            return dbServices.addEvolveEventToCart(event, appEngine.getCurrentUser());
        }

    }

    public Completable addTrackEventToCart(Event event) {

        return evolveApiServices.addTrackEventToCart(event, appEngine.getCurrentUser());


    }

    public Completable addEventToCart(EventDetails eventDetails) {
        if(eventDetails.isMotoEvent()){
            return evolveApiServices.addMotoEventToCart(eventDetails, appEngine.getCurrentUser());
        }else if(appEngine.isUserLoggedIn()){
            return evolveApiServices.addEvolveEventToCart(eventDetails, appEngine.getCurrentUser());
        }else{
            return dbServices.addEvolveEventToCart(eventDetails, appEngine.getCurrentUser());
        }

    }

    public Completable addGiftToCart(ShopCard card, String name, String email) {
        return getCartApiSerices().addGiftCardToCart(card, name, email, appEngine.getCurrentUser());
    }

    public Completable addArchieToCart(ShopCard card, int qty) {
        return getCartApiSerices().addArchieCardToCart(card, qty, appEngine.getCurrentUser());
    }

    public Completable addMembershipToCart(Membership membership) {
        boolean force = !appEngine.getUserDetails().hasRaceLicense();
        return getCartApiSerices().addMembershipToCart(membership, appEngine.getCurrentUser(), force);
    }

    public Completable addProductToCart(ProductDetail productDetail) {
        return getCartApiSerices().addProductToCart(productDetail, appEngine.getCurrentUser());
    }

    public Single<String> applyCoupon(String couponCode) {
        return getCartApiSerices().applyCoupon(couponCode, appEngine.getUserId());
    }



    public Completable deleteCartItem(CartItem cartItem) {
        return getCartApiSerices().removeCartItem(appEngine.getUserId(),
                cartItem.cartId,
                cartItem.method,
                cartItem.objectId);
    }

    public Single<String> getCheckoutToken() {
        return getCartApiSerices().getBrainTreeCheckoutToken(BuildConfig.CHECKOUT_MODE,
                appEngine.getUserId(),
                appEngine.getCurrentUser().email);
    }

    public Single<String> placeOrder(String couponCode, String payment) {
        return getCartApiSerices().placeOrder(couponCode, payment,
                CheckoutConstants.PAYMENT_INTENT,
                appEngine.getUserId());
    }

    public Completable resetCart() {
        return getCartApiSerices().resetCart(appEngine.getUserId())
                .doOnComplete(() -> appEngine.setCartItems(new ArrayList<>()));
    }

    public Single<BrainTreePaymentInfo> placeOrder(BrainTreePaymentRequest request) {
        return getCartApiSerices().completBrainTreeTransaction(request);
    }

    public Completable syncLocalCart() {
        return dbServices.getCartList(appEngine.getUserId())
                .flatMapCompletable(cartListResponse -> {
                    if (!ListUtils.isEmpty(cartListResponse.cartList)) {
                        return getCartApiSerices()
                                .syncLocalCart(cartListResponse.cartList, appEngine.getUserId())
                                .andThen(dbServices.resetCart(appEngine.getUserId())
                                        .doOnComplete(() -> appEngine.setCartItems(new ArrayList<>()))
                                );
                    } else {
                        return Completable.complete();
                    }
                });
    }
    public Single<ETResponse> validateBeforePay() {
        return getCartApiSerices().validateBeforePay(appEngine.getUserId());
    }
}
