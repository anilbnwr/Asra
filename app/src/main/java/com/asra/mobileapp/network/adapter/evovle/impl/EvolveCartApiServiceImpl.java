package com.asra.mobileapp.network.adapter.evovle.impl;

import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.model.BrainTreePaymentInfo;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.network.adapter.BaseApiService;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveCartApiServices;
import com.asra.mobileapp.network.api.EvolveCartApi;
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
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.util.ListUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

public class EvolveCartApiServiceImpl extends BaseApiService implements EvolveCartApiServices {

    private EvolveCartApi evolveCartApi;

    @Inject
    public EvolveCartApiServiceImpl(EvolveCartApi evolveCartApi) {
        this.evolveCartApi = evolveCartApi;
    }

    @Override
    public Single<CartListResponse> getCartList(String userId) {

        CartRequest request = new CartRequest();
        request.userId = userId;
        return evolveCartApi.getCartList(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }

    @Override
    public Completable addEvolveEventToCart(Event event, User user) {
        CartEventRequest request = new CartEventRequest();
        request.eventDate = event.eventDate;
        request.eventSlug = event.slug;
        request.eventPrice = event.price;
        request.memberNumber = user.getUserId();
        request.role = user.role;
        request.title = event.title;
        request.eventCouponCode = event.couponCode;


        return evolveCartApi.addEvolveEventToCart(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).ignoreElement()
                .doOnComplete(() -> analyticsHelper
                        .logAddToCartEvent(AnalyticsModel.AddToCart.getEventCartBundle(request)))
                .doOnError(Timber::w);
    }

    @Override
    public Completable addTrackEventToCart(Event event, User user) {
        CartEventRequest.TrackDayRequest request = new CartEventRequest.TrackDayRequest();

        request.eventId = event.eventId;
        request.memberNumber = user.getUserId();

        return evolveCartApi.addTrackEventToCart(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).ignoreElement()
                .doOnError(Timber::w);

    }

    @Override
    public Completable addEvolveEventToCart(EventDetails eventDetails, User user) {
        CartEventRequest request = new CartEventRequest();
        request.eventDate = eventDetails.eventDate;
        request.eventSlug = eventDetails.slug;
        request.eventPrice = eventDetails.price;
        request.memberNumber = user.getUserId();
        request.role = user.role;
        request.title = eventDetails.title;
        request.eventCouponCode = eventDetails.couponCode;


        if (!ListUtils.isEmpty(eventDetails.trainingData)) {
            for (EventDetails.TrainingDatum training : eventDetails.trainingData) {

                if(training.selected){
                    CartEventRequest.Training trainingRequest = new CartEventRequest.Training();
                    trainingRequest.price = training.price;
                    trainingRequest.slug = training.slug;
                    trainingRequest.trainingId = training.trainingId;
                    trainingRequest.trainingName = training.title;
                    request.trainingList.add(trainingRequest);
                    analyticsHelper.logAddToCartEvent(AnalyticsModel.AddToCart
                            .getTrainingCartBundle(trainingRequest));
                }
            }
        }

        if (!ListUtils.isEmpty(eventDetails.rentalData)) {
            for (EventDetails.RentalDatum rentalDatum : eventDetails.rentalData){
                if(rentalDatum.selected){
                    EventDetails.Variation variation = rentalDatum.selectedVariant;
                    if(variation != null){

                        CartEventRequest.Rental rental = new CartEventRequest.Rental();
                        rental.price = rentalDatum.price;
                        rental.slug = rentalDatum.slug;
                        rental.rentalId = rentalDatum.productId;
                        rental.selectedSize = variation.attributeValue;
                        rental.rentalName = rentalDatum.title;
                        request.rentalList.add(rental);

                        analyticsHelper.logAddToCartEvent(AnalyticsModel.AddToCart
                                .getRentalCartBundle(rental));

                    }
                }
            }

        }

        return evolveCartApi.addEvolveEventToCart(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).ignoreElement()
                .doOnError(Timber::w)
                .doOnComplete(() -> analyticsHelper.logAddToCartEvent(
                        AnalyticsModel.AddToCart.getEventCartBundle(request)));

    }


    @Override
    public Completable addMotoEventToCart(EventDetails eventDetails, User user) {
        CartEventRequest request = new CartEventRequest();
        request.eventId = eventDetails.eventId;
        request.memberNumber = user.getUserId();
        request.classTotalPrice = String.valueOf(eventDetails.classTotal);
        request.bikeNumber = eventDetails.bikeNumber;
        request.skillClass = eventDetails.racerStatus;
        request.transponderNo = eventDetails.transponderNo;
        request.classes = eventDetails.getSelectedClasses();
        request.eventCouponCode = eventDetails.couponCode;


        return evolveCartApi.addMotoEventToCart(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).ignoreElement()
                .doOnError(Timber::w)
                .doOnComplete(() -> analyticsHelper.logAddToCartEvent(
                        AnalyticsModel.AddToCart.getEventCartBundle(request)));

    }

    @Override
    public Completable addGiftCardToCart(ShopCard card, String name, String email, User user) {
        CartGiftCardRequest giftCardRequest = new CartGiftCardRequest();
        giftCardRequest.memberNumber = user.getUserId();
        giftCardRequest.receiverEmail = email;
        giftCardRequest.receiverName = name;
        giftCardRequest.slug = card.slug;
        giftCardRequest.price = card.price;
        giftCardRequest.title = card.title;
        giftCardRequest.image = card.imagePath;
        giftCardRequest.quantity = String.valueOf(1);

        return evolveCartApi.addGiftCardToCart(giftCardRequest)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).ignoreElement()
                .doOnError(Timber::w)
                .doOnComplete(() -> analyticsHelper.logAddToCartEvent(
                        AnalyticsModel.AddToCart.getGiftCardCartBundle(giftCardRequest)));

    }

    @Override
    public Completable addArchieCardToCart(ShopCard card, int qty, User user) {
        CartArchieCardRequest archieCardRequest = new CartArchieCardRequest();
        archieCardRequest.memberNumber = user.getUserId();
        archieCardRequest.slug = card.slug;
        archieCardRequest.price = card.price;
        archieCardRequest.title = card.title;
        archieCardRequest.quantity = String.valueOf(qty);
        archieCardRequest.image = card.imagePath;
        archieCardRequest.quantity = String.valueOf(1);

        return evolveCartApi.addArchieToCart(archieCardRequest)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).ignoreElement()
                .doOnError(Timber::w)
                .doOnComplete(() -> analyticsHelper.logAddToCartEvent(
                        AnalyticsModel.AddToCart.getArchieCardCartBundle(archieCardRequest)));
    }

    @Override
    public Completable addMembershipToCart(Membership membership, User user, boolean force) {
        CartMembershipRequest request = new CartMembershipRequest();
        request.setUserId(user.getUserId());
        request.setMembership(membership.getSlug());
        request.setPrice(membership.getPrice());
        request.setTitle(membership.getTitle());
        request.setImage(membership.getImage());
        request.setForce(force ? "1" : "0");

        return evolveCartApi.addMembershipToCart(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).ignoreElement()
                .doOnError(Timber::w).doOnComplete(() ->
                        analyticsHelper.logAddToCartEvent(AnalyticsModel.AddToCart
                        .getMembershipCartBundle(request)));
    }

    @Override
    public Completable addProductToCart(ProductDetail productDetail, User user) {
        CartProductRequest request = new CartProductRequest();
        request.userId = user.getUserId();
        request.price = productDetail.getVariantPrice();
        request.quantity = productDetail.quantity;
        request.slug = productDetail.slug;
        request.selectedAttributes = request.getAttributes(productDetail);

        return evolveCartApi.addProductToCart(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).ignoreElement()
                .doOnError(Timber::w)
                .doOnComplete(() -> analyticsHelper.logAddToCartEvent(
                        AnalyticsModel.AddToCart.getProductCartBundle(request)));

    }

    @Override
    public Completable removeCartItem(String userId, String cartId, String method, String itemId) {
        CartRemoveRequest request = new CartRemoveRequest();
        request.userId = userId;
        request.method = method;
        request.cartId = cartId;
        request.itemId = itemId;

        return evolveCartApi.removeFromCart(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Completable resetCart(String userId) {
        CartResetRequest request = new CartResetRequest();
        request.memberNumber = userId;

        return evolveCartApi.resetCart(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Single<String> applyCoupon(String couponCode, String userId) {
        CouponRequest request = new CouponRequest();
        request.couponCode = couponCode;
        request.userId = userId;

        return evolveCartApi.validateCoupon(request)
                .map(response -> {
                    checkApiError(response);
                    return response.couponValue;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<String> getBrainTreeCheckoutToken(String mode, String userId, String email) {

        CheckoutTokenRequest tokenRequest = new CheckoutTokenRequest();
        tokenRequest.checkoutMode = mode;
        tokenRequest.userId = userId;
        tokenRequest.email = email;
        return evolveCartApi.getCheckoutToken(tokenRequest)
                .map(response -> {
                    checkApiError(response);
                    return response.paypalToken;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<BrainTreePaymentInfo> completBrainTreeTransaction(BrainTreePaymentRequest request) {
        return evolveCartApi.completeBrainTreePayment(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }


    @Override
    public Single<String> placeOrder(String couponCode, String payment, String intent, String userId) {

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.coupon = couponCode;
        request.intent = intent;
        request.payment = payment;
        request.userId = userId;

        return evolveCartApi.placeOrder(request)
                .map(response -> {
                    checkApiError(response);
                    return response.referenceNumber;
                }).doOnError(Timber::w);
    }

    @Override
    public Completable syncLocalCart(List<CartItem> cartList, String userId) {
        GuestCartSyncRequest request = new GuestCartSyncRequest();
        request.userId = userId;
        request.cartItems = cartList;

        return evolveCartApi.syncLocalCart(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .onErrorResumeNext(throwable -> Single.just(new ETResponse()))
                .ignoreElement();
    }

    @Override
    public Single<ETResponse> validateBeforePay(String userId) {
        CartRequest request = new CartRequest();
        request.userId = userId;

        return evolveCartApi.validateBeforePay(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }
}
