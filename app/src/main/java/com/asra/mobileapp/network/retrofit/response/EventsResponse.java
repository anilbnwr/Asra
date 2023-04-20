package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.Event;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsResponse extends ETResponse {

    @SerializedName("results")
    @Expose
    public List<Event> eventList;
}
