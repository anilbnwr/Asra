package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartRequest {

    @Expose
    @SerializedName("serial")
    public String userId;

}
