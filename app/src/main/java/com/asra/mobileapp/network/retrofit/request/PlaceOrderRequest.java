package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderRequest {

    @SerializedName("paymentToken")
    @Expose
    public String paymentToken;

    @SerializedName("orderID")
    @Expose
    public String orderID;

    @SerializedName("payerID")
    @Expose
    public String payerID;

    @SerializedName("paymentID")
    @Expose
    public String paymentID;

    @SerializedName("intent")
    @Expose
    public String intent;

    @SerializedName("returnUrl")
    @Expose
    public String returnUrl;

    @SerializedName("payment")
    @Expose
    public String payment;

    @SerializedName("serial")
    @Expose
    public String userId;

    @SerializedName("coupon")
    @Expose
    public String coupon;
}
