package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceTokenRequest {
    @Expose
    @SerializedName("user_id")
    public String userId;

    @Expose
    @SerializedName("token")
    public String token;

    @Expose
    @SerializedName("device_type")
    public String deviceType = "android";
}
