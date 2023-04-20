package com.asra.mobileapp.model;

import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrainTreePaymentInfo extends ETResponse {

    @Expose
    @SerializedName("payment_status")
    public String paymentStatus;

    @Expose
    @SerializedName("cart_status")
    public String cartStatus;

    @Expose
    @SerializedName("order_id")
    public String orderId;



}
