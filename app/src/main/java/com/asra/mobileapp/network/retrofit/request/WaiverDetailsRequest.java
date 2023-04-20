package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaiverDetailsRequest {

    @Expose
    @SerializedName("user_id")
    public String userId;

    @Expose
    @SerializedName("event_id")
    public String eventId;

}
