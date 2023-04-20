package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse extends ETResponse {

    @Expose @SerializedName("token")
    public String token;

    @Expose @SerializedName("currentUser")
    public User currentUser;

}
