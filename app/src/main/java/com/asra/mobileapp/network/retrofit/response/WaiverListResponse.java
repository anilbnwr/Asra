package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.WaiverEvent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WaiverListResponse extends ETResponse {

    @SerializedName("result")
    @Expose
    public List<WaiverEvent> waiverEvents;
}
