package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @Expose @SerializedName("email_login")
    public String email;

    @Expose @SerializedName("password_login")
    public String password;

    @Expose @SerializedName("remember")
    public boolean rememberMe = true;
}
