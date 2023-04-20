package com.asra.mobileapp.network.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponResponse extends ETResponse {


    @SerializedName("remain")
    @Expose
    public String couponValue;


}
