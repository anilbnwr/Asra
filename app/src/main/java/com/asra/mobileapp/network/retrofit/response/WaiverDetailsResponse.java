package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.WaiverEventDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WaiverDetailsResponse extends ETResponse {

    @SerializedName("event_data")
    @Expose
    public WaiverEventDetails.WaiverData waiverData;

    @SerializedName("states")
    @Expose
    public List<WaiverEventDetails.WaiverState> waiverStates;


}
