package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.UserDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailsResponse extends ETResponse {

    @Expose
    @SerializedName("result")
    public UserDetails userDetails;
}
