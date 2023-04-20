package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopCardDetailRequest {
    @Expose
    @SerializedName("slug")
    public String slug;
}
