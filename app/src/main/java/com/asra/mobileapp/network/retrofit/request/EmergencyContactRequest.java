package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmergencyContactRequest {

    @Expose
    @SerializedName("user_id")
    public String userId;

    @Expose
    @SerializedName("emergency_first_name")
    public String firstName;

    @Expose
    @SerializedName("emergency_last_name")
    public String lastName;

    @Expose
    @SerializedName("emergency_phone")
    public String phone;

    @Expose
    @SerializedName("emergency_relationship")
    public String relationShip;

}
