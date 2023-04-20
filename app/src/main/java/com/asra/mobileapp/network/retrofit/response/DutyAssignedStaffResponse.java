package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.DutyAssignedStaff;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DutyAssignedStaffResponse extends ETResponse {


    @SerializedName("result")
    @Expose
    public List<DutyAssignedStaff> staffs;

}
