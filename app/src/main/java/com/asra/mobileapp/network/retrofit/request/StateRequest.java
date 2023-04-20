package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateRequest {

    @Expose
    @SerializedName("country_code")
    public String countryCode;
}
