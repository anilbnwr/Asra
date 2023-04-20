package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponRequest {

    @Expose
    @SerializedName("coupon")
    public String couponCode;

    @Expose
    @SerializedName("uid")
    public String userId;

}
