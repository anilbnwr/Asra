package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasswordChangeRequest {


    @Expose
    @SerializedName("data")
    public PasswordRequest passwordRequest;

    @Expose
    @SerializedName("user_id")
    public String userId;

    public static class PasswordRequest{

        @Expose
        @SerializedName("currentpassword")
        public String oldPassword;

        @Expose
        @SerializedName("password")
        public String newPassword;


        @Expose
        @SerializedName("confirm-password")
        public String confirmPassword;


    }
}
