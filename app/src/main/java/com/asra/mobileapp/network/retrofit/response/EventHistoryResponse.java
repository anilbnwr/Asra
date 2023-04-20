package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.EnrolledEvent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventHistoryResponse extends ETResponse {


    @SerializedName("allEventCount")
    @Expose
    public int eventsCount;

    @SerializedName("events")
    @Expose
    public List<EnrolledEvent> eventList;


}
