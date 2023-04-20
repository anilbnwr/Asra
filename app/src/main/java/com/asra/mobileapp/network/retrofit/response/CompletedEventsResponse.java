package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.CompletedEvent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompletedEventsResponse extends ETResponse {

    @SerializedName("result")
    @Expose
    public List<CompletedEvent> completedEvents;
}
