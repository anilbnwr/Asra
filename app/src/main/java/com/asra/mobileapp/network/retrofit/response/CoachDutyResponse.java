package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.EventDuty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoachDutyResponse extends ETResponse {

    @Expose
    @SerializedName("result")
    public DutyTypes dutyList;



    public static class DutyTypes{
        @Expose
        @SerializedName("past")
        public List<EventDuty> pastDutyList;

        @Expose
        @SerializedName("current")
        public List<EventDuty> currentutyList;

        @Expose
        @SerializedName("upcoming")
        public List<EventDuty> upcomingDutyList;
    }
}
