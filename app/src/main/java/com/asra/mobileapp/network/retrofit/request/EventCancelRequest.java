package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventCancelRequest {

    @Expose
    @SerializedName("order_item_id")
    public String orderItemId;

    @Expose
    @SerializedName("user_id")
    public String userId;

    @Expose
    @SerializedName("skill_level")
    public String skillLevel;

}
