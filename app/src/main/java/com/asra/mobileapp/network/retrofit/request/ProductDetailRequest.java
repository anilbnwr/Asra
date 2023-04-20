package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailRequest {

    @Expose
    @SerializedName("slug")
    public String slug;
}
