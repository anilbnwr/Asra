package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompletedEventRequest {

    @Expose
    @SerializedName("userId")
    public String userId;

    @Expose
    @SerializedName("is_motoevent")
    public int isMotoEvent = 0;
}
