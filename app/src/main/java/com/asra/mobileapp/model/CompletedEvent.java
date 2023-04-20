package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompletedEvent {
    @Expose
    @SerializedName("event_id")
    public String eventId;

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("event_date")
    public String eventDate;

    @Expose
    @SerializedName("event_logo")
    public String eventLogo;

    @Expose
    @SerializedName("event_type")
    public String eventType;

    @Expose
    @SerializedName("training_type")
    public List<String> trainingTypes;
}
