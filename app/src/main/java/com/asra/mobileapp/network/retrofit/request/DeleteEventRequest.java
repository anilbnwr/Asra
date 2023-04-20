package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.SerializedName;

public class DeleteEventRequest {


    @SerializedName("order_id")
    public String orderId ;

    @SerializedName("event_id")
    public String eventId;

    @SerializedName("skill_level")
    public String skillLevel ;

    @SerializedName("user_id")
    public String userId ;



    public DeleteEventRequest(String orderId,String eventId ,String skillLevel,String userId) {
        this.orderId = orderId;
        this.eventId = eventId;
        this.skillLevel = skillLevel;
        this.userId = userId;
    }
}
