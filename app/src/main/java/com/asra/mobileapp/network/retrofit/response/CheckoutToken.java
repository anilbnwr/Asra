package com.asra.mobileapp.network.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutToken extends ETResponse {

    @SerializedName("client_token")
    @Expose
    public String paypalToken;
}
