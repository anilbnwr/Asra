package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.EventParticipant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventParticipantResponse extends ETResponse {


    @SerializedName("result")
    @Expose
    public List<EventParticipant> eventParticiapnts;

    @SerializedName("general_skills")
    @Expose
    public List<String> generalSkills;
}
