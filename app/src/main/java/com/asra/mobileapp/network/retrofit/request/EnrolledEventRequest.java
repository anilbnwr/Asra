package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnrolledEventRequest {

    @Expose
    @SerializedName("user_id")
    public String userId;

    @Expose
    @SerializedName("is_motoevent")
    public int appCode = 0;
}
