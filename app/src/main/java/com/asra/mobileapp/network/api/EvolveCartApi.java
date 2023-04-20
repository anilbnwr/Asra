package com.asra.mobileapp.network.api;


import com.asra.mobileapp.model.BrainTreePaymentInfo;
import com.asra.mobileapp.network.retrofit.request.BrainTreePaymentRequest;
import com.asra.mobileapp.network.retrofit.request.CartArchieCardRequest;
import com.asra.mobileapp.network.retrofit.request.CartEventRequest;
import com.asra.mobileapp.network.retrofit.request.CartGiftCardRequest;
import com.asra.mobileapp.network.retrofit.request.CartMembershipRequest;
import com.asra.mobileapp.network.retrofit.request.CartProductRequest;
import com.asra.mobileapp.network.retrofit.request.CartRemoveRequest;
import com.asra.mobileapp.network.retrofit.request.CartRequest;
import com.asra.mobileapp.network.retrofit.request.CartResetRequest;
import com.asra.mobileapp.network.retrofit.request.CheckoutTokenRequest;
import com.asra.mobileapp.network.retrofit.request.CouponRequest;
import com.asra.mobileapp.network.retrofit.request.GuestCartSyncRequest;
import com.asra.mobileapp.network.retrofit.request.PlaceOrderRequest;
import com.asra.mobileapp.network.retrofit.response.CartListResponse;
import com.asra.mobileapp.network.retrofit.response.CheckoutToken;
import com.asra.mobileapp.network.retrofit.response.CouponResponse;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.network.retrofit.response.PlaceOrderResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EvolveCartApi {


    @POST("/evolve-api/public/app/v3/cart/beforeOrder")
    Single<CartListResponse> getCartList(@Body CartRequest request);


    @POST("/evolve-api/public/app/v3/cart/removeCartItem")
    Single<ETResponse> removeFromCart(@Body CartRemoveRequest cartItem);

    @POST("/evolve-api/public/app/v3/checkout/resetCart")
    Single<ETResponse> resetCart(@Body CartResetRequest request);


    //Add to Cart

    @POST("/evolve-api/public/app/v3/giftCard/addToCart")
    Single<ETResponse> addGiftCardToCart(@Body CartGiftCardRequest giftCart);

    @POST("/evolve-api/public/app/v3/archieCard/addToCart")
    Single<ETResponse> addArchieToCart(@Body CartArchieCardRequest archieCart);

    @POST("/evolve-api/public/app/v3/membership/addToCart")
    Single<ETResponse> addMembershipToCart(@Body CartMembershipRequest membershipRequest);

    @POST("/evolve-api/public/app/v3/products/addToCart")
    Single<ETResponse> addProductToCart(@Body CartProductRequest request);

    @POST("/evolve-api/public/app/v3/evolveEvents/addToCart")
    Single<ETResponse> addEvolveEventToCart(@Body CartEventRequest cartEventRequest);

    @POST("/evolve-api/public/app/v3/trackday/addToCart")
    Single<ETResponse> addTrackEventToCart(@Body CartEventRequest.TrackDayRequest cartEventRequest);


    @POST("/evolve-api/public/app/v3/motoEvents/addToCart")
    Single<ETResponse> addMotoEventToCart(@Body CartEventRequest cartEventRequest);


    //Checkout Apis

    @POST("/evolve-api/public/app/v3/checkout/validateCoupon")
    Single<CouponResponse> validateCoupon(@Body CouponRequest request);

    @POST("/evolve-api/public/app/v3/checkout/placeOrder")
    Single<PlaceOrderResponse> placeOrder(@Body PlaceOrderRequest request);


    @POST("/evolve-api/public/app/v3/braintree/token")
    Single<CheckoutToken> getCheckoutToken(@Body CheckoutTokenRequest tokenRequest);

    @POST("/evolve-api/public/app/v3/braintree/transaction")
    Single<BrainTreePaymentInfo> completeBrainTreePayment(@Body BrainTreePaymentRequest request);


    //Sync Local Cart table
    @POST("/evolve-api/public/app/v3/user/localToCartTable")
    Single<ETResponse> syncLocalCart(@Body GuestCartSyncRequest request);

    @POST("/evolve-api/public/app/v3/checkout/validateBeforePay")
    Single<ETResponse> validateBeforePay(@Body CartRequest request);



}
