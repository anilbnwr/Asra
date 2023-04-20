package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartResetRequest {
    @Expose
    @SerializedName("serial")
    public String memberNumber;
}
