package com.asra.mobileapp.analytics;

import android.os.Bundle;

import com.asra.mobileapp.network.retrofit.request.CartArchieCardRequest;
import com.asra.mobileapp.network.retrofit.request.CartEventRequest;
import com.asra.mobileapp.network.retrofit.request.CartGiftCardRequest;
import com.asra.mobileapp.network.retrofit.request.CartMembershipRequest;
import com.asra.mobileapp.network.retrofit.request.CartProductRequest;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsModel {

    public static final String EVENT_LOGIN = "log_in";
    public static final String EVENT_LOGOUT = "log_out";


    public static final String CATEGORY_CART = "Shopping Cart";
    public static final String CATEGORY_ADMIN_EVENTS = "Admin EnrolledEvent List";
    public static final String CATEGORY_ADMIN_EVENTS_USERS = "Admin EnrolledEvent Users List";

    public static final String CATEGORY_USER_CREDITS = "User Credit List";
    public static final String CATEGORY_EVENTS = "Events";
    public static final String CATEGORY_RENTALS = "Rentals";
    public static final String CATEGORY_TRAININGS = "Trainings";

    public static final String CATEGORY_UPCOMING_EVENTS = "Upcoming Events";
    public static final String CATEGORY_PAST_EVENTS = "Past Events";
    public static final String CATEGORY_ALL_EVENTS = "All Events";


    public static final String CATEGORY_ARCHIE_CARDS = "Archie Cards";
    public static final String CATEGORY_GIFT_CARDS = "Gift Cards";
    public static final String CATEGORY_GEAR_PRODUCTS = "Gear Products";
    public static final String CATEGORY_MEMBERSHIP = "Membership";
    public static final String CATEGORY_PRODUCT = "Product";


    public static final String VIEW_ITEM_EVENT_DETAILS = "EnrolledEvent Detail";
    public static final String VIEW_ITEM_ARCHIE_DETAILS = "Archie Detail";
    public static final String VIEW_ITEM_GIFT_DETAILS = "Gift Detail";
    public static final String VIEW_ITEM_PRODUCT_DETAILS = "Product Detail";


    public static final String ET_EVENT_PLACE_ORDER_ERROR = "error_place_order";

    public static class ViewItem{
        public String itemId;

        public static Bundle getBundle(String itemId){
            Bundle bundle = new Bundle();
            //bundle.putString(Fire);
            return bundle;
        }
    }

    public static class ViewItemList{


        public static Bundle getBundle(String category) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
            return bundle;
        }
    }

    public static class SearchItem{


        public static Bundle getBundle(String key, String category) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, key);
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
            return bundle;
        }
    }

    public static class PurchaseComplete{


        public static Bundle getBundle(String coupon, String cartTotal, String transId) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.COUPON, coupon);
            bundle.putString(FirebaseAnalytics.Param.VALUE, cartTotal);
            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transId);
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");
            return bundle;
        }
    }

    public static class BeginCheckout{


        public static Bundle getBundle(String coupon, String dueTotal) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.COUPON, coupon);
            bundle.putString(FirebaseAnalytics.Param.VALUE, dueTotal);
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");
            return bundle;
        }
    }

    public static class AddToCart{



        public static Bundle getEventCartBundle(CartEventRequest request){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.QUANTITY, request.quantity);
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, CATEGORY_EVENTS);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, request.title);
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, request.slug);
            bundle.putString(FirebaseAnalytics.Param.VALUE, request.price);
            bundle.putString(FirebaseAnalytics.Param.PRICE, request.price);
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");

            return bundle;
        }

        public static Bundle getRentalCartBundle(CartEventRequest.Rental request){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.QUANTITY, "1");
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, CATEGORY_RENTALS);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, request.rentalName);
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, request.slug);
            bundle.putString(FirebaseAnalytics.Param.VALUE, request.price);
            bundle.putString(FirebaseAnalytics.Param.PRICE, request.price);
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");

            return bundle;
        }

        public static Bundle getTrainingCartBundle(CartEventRequest.Training training){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.QUANTITY, "1");
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, CATEGORY_TRAININGS);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, training.trainingName);
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, training.slug);
            bundle.putString(FirebaseAnalytics.Param.VALUE, training.price);
            bundle.putString(FirebaseAnalytics.Param.PRICE, training.price);
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");

            return bundle;
        }

        public static Bundle getGiftCardCartBundle(CartGiftCardRequest request){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.QUANTITY, request.quantity);
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, CATEGORY_GIFT_CARDS);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, request.title);
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, request.slug);
            bundle.putString(FirebaseAnalytics.Param.VALUE, request.price);
            bundle.putString(FirebaseAnalytics.Param.PRICE, request.price);
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");

            return bundle;
        }

        public static Bundle getArchieCardCartBundle(CartArchieCardRequest archieCard){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.QUANTITY, archieCard.quantity);
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, CATEGORY_ARCHIE_CARDS);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, archieCard.title);
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, archieCard.slug);
            bundle.putString(FirebaseAnalytics.Param.VALUE, archieCard.price);
            bundle.putString(FirebaseAnalytics.Param.PRICE, archieCard.price);
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");

            return bundle;
        }

        public static Bundle getProductCartBundle(CartProductRequest request){
            Bundle bundle = new Bundle();
            bundle.putInt(FirebaseAnalytics.Param.QUANTITY, request.quantity);
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, CATEGORY_PRODUCT);
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, request.slug);
            bundle.putString(FirebaseAnalytics.Param.VALUE, request.price);
            bundle.putString(FirebaseAnalytics.Param.PRICE, request.price);
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");

            return bundle;
        }

        public static Bundle getMembershipCartBundle(CartMembershipRequest membershipRequest){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, CATEGORY_MEMBERSHIP);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, membershipRequest.getTitle());
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, membershipRequest.getMembership());
            bundle.putString(FirebaseAnalytics.Param.VALUE, membershipRequest.getPrice());
            bundle.putString(FirebaseAnalytics.Param.PRICE, membershipRequest.getPrice());
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");

            return bundle;
        }
    }


    public static Bundle loggedInEvent(String userId){
        Bundle bundle = new Bundle();
        bundle.putBoolean("logged_in", true);
        bundle.putString("user_id", userId);
        return bundle;
    }

    public static Bundle loggedOutEvent(String userId){
        Bundle bundle = new Bundle();
        bundle.putBoolean("logged_in", false);
        bundle.putString("user_id", userId);
        return bundle;
    }
}
