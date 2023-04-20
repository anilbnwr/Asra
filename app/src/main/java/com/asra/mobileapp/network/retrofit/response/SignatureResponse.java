package com.asra.mobileapp.network.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignatureResponse extends ETResponse {

    @SerializedName("signature")
    @Expose
    public String signature;
}
