package com.asra.mobileapp.model;

import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MrlMessage extends ETResponse {

    @Expose
    @SerializedName("title")
    public String title;
}
