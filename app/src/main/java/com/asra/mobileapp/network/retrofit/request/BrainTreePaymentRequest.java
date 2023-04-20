package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrainTreePaymentRequest {

    @Expose
    @SerializedName("mode")
    public String mode;

    @Expose
    @SerializedName("amount")
    public String amount;

    @Expose
    @SerializedName("nonce")
    public String brainTreeNonce;

    @Expose
    @SerializedName("serial")
    public String userId;

    @Expose
    @SerializedName("payment")
    public String paymentType;

    @Expose
    @SerializedName("coupon")
    public String coupon;

}
