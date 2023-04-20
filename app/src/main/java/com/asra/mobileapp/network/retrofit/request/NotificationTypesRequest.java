package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationTypesRequest {

    @SerializedName("user_id")
    @Expose
    public String userId;
}
