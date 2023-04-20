package com.asra.mobileapp.network.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ETResponse {

    @Expose
    @SerializedName("status")
    public int status;

    @Expose
    @SerializedName("errorCode")
    public int errorCode;

    @Expose @SerializedName("msg")
    public String message;

    public boolean dataError(){
        return status != 1;
    }
}
