package com.asra.mobileapp.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.asra.mobileapp.BuildConfig;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.google.firebase.analytics.FirebaseAnalytics;

import timber.log.Timber;

public class FirebaseAnalyticsHelper {

    private static FirebaseAnalyticsHelper instance;

    private FirebaseAnalytics mFirebaseAnalytics;

    private FirebaseAnalyticsHelper(Context context){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

    }

    public static FirebaseAnalyticsHelper getInstance(Context context){
        if(instance == null){
            instance = new FirebaseAnalyticsHelper(context);
        }


        return instance;
    }

    public void trackScreen(Activity activity, String frgmentName){
        try {

            if (analyticsEnabled())
            mFirebaseAnalytics.setCurrentScreen(activity, frgmentName, null);
        }catch (Exception e){
            Timber.e(e, "Analytics failed");
        }
    }

    public void setUserProperties(String userId, String role){
        if (analyticsEnabled()) {
            mFirebaseAnalytics.setUserId(userId);
            mFirebaseAnalytics.setUserProperty("role", role);
            mFirebaseAnalytics.setUserProperty("app_version", "" + BuildConfig.VERSION_CODE);
        }
    }

    public void setProperties(String key, String value) {
        if (analyticsEnabled()){
            mFirebaseAnalytics.setUserProperty(key, value);
        }
    }

    public void logPlaceOrderError(String userId, String paymentStatus, String cartStatus, String message){
        if (analyticsEnabled()) {
            Bundle bundle = new Bundle();
            bundle.putString("user_id", userId);
            bundle.putString("payment_status", paymentStatus);
            bundle.putString("cart_status", cartStatus);
            bundle.putString("message", message);
            mFirebaseAnalytics.logEvent(AnalyticsModel.ET_EVENT_PLACE_ORDER_ERROR, bundle);
        }
    }

    public void logEvent(String eventName, Bundle params){
        if (analyticsEnabled()) {
            mFirebaseAnalytics.logEvent(eventName, params);
        }
    }

    public void logViewListEvent(String category){
        if (analyticsEnabled()) {
            Bundle bundle = AnalyticsModel.ViewItemList.getBundle(category);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle);
        }
    }

    public void logViewItemDetailEvent(String itemId){
        if (analyticsEnabled()) {
            Bundle bundle = AnalyticsModel.ViewItem.getBundle(itemId);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        }
    }

    public void logAddToCartEvent(Bundle bundle){

        if (analyticsEnabled()) {
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle);
        }
    }

    public void logSearchEvent(String searchKey, String category){

        if (analyticsEnabled()) {
            Bundle bundle = AnalyticsModel.SearchItem.getBundle(searchKey, category);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, bundle);
        }
    }

    public void logBeginCheckout(String coupon, String dueTotal){

        if (analyticsEnabled()) {
            if (TextUtils.isEmpty(coupon)) coupon = "";
            Bundle bundle = AnalyticsModel.BeginCheckout.getBundle(coupon, dueTotal);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, bundle);
        }
    }

    public void logCheckoutComplete(String coupon, String dueTotal, String transId){

        if (analyticsEnabled()) {
            if (TextUtils.isEmpty(coupon)) coupon = "";
            Bundle bundle = AnalyticsModel.PurchaseComplete.getBundle(coupon, dueTotal, transId);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);
        }
    }

    public boolean analyticsEnabled(){

            String status = MessageProvider.getInstance().getText(MessageProvider.analytics_enabled);
            return AppUtils.isTrue(status);

    }
}
