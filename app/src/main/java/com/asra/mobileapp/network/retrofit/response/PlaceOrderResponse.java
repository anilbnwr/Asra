package com.asra.mobileapp.network.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderResponse extends ETResponse {

    @SerializedName("ack_id")
    @Expose
    public String referenceNumber;


}
