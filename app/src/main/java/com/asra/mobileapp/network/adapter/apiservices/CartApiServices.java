package com.asra.mobileapp.network.adapter.apiservices;

import com.asra.mobileapp.model.BrainTreePaymentInfo;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.network.retrofit.request.BrainTreePaymentRequest;
import com.asra.mobileapp.network.retrofit.response.CartListResponse;
import com.asra.mobileapp.network.retrofit.response.ETResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface CartApiServices {

    Single<CartListResponse> getCartList(String userId);


    Completable addEvolveEventToCart(EventDetails eventDetails, User user);
    Completable addEvolveEventToCart(Event event, User user);
    Completable addTrackEventToCart(Event event, User user);
    Completable addMotoEventToCart(EventDetails eventDetails, User user);

    Completable addGiftCardToCart(ShopCard card, String name, String email, User user);

    Completable addArchieCardToCart(ShopCard card, int qty, User user);

    Completable addMembershipToCart(Membership card, User user, boolean force);

    Completable addProductToCart(ProductDetail productDetail, User user);

    Completable removeCartItem(String userId, String cartId, String method, String itemId);

    Completable resetCart(String userId);

    Single<String> applyCoupon(String couponCode, String userId);

    Single<String> getBrainTreeCheckoutToken(String mode, String userId, String email);

    Single<BrainTreePaymentInfo> completBrainTreeTransaction(BrainTreePaymentRequest request);

    Single<String> placeOrder(String couponCode, String payment, String intent, String userId);

    Completable syncLocalCart(List<CartItem> cartList, String userId);

    Single<ETResponse> validateBeforePay(String userId);



}
