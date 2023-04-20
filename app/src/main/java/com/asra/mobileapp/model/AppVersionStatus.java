package com.asra.mobileapp.model;

import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppVersionStatus extends ETResponse {

    public static final int VERSION_NOT_AVAILABLE = 0;
    private static String UPDATE_MANDATORY = "mandatory";

    @SerializedName("android_version_code")
    @Expose
    public String versionCode;

    @SerializedName("android_version")
    @Expose
    public String versionName;

    @SerializedName("mode")
    @Expose
    public String mode;

    public String displayMessage;

    @SerializedName("android_store")
    @Expose
    public String playStoreURL;


    public boolean shouldForceUpdate(){

        return UPDATE_MANDATORY.equalsIgnoreCase(mode);
    }
}
