package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutTokenRequest {

    @Expose
    @SerializedName("mode")
    public String checkoutMode;

    @Expose
    @SerializedName("userId")
    public String userId;

    @Expose
    @SerializedName("email")
    public String email;
}
