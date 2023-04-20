package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventParticipantRequest {

    @Expose
    @SerializedName("event_id")
    public String eventId;

}
