package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.ShopCard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopCardItemRespose extends ETResponse {

    @Expose
    @SerializedName("data")
    public ShopCard shopCard;

}
