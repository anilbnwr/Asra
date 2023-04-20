package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditHistoryRequest {

    @Expose
    @SerializedName("user_id")
    public String userId;

}
