package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.PassportInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewPassportResponse extends ETResponse {


    @SerializedName("data")
    @Expose
    public PassportInfo passportInfo;

}
