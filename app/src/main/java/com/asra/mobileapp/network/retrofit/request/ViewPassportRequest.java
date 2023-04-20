package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.SerializedName;

public class ViewPassportRequest {

    @SerializedName("passport_id")
    public String passportId;

    public ViewPassportRequest(String passportId) {
        this.passportId = passportId;
    }
}
