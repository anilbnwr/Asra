package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SkillUpgradeRequest {

    @Expose
    @SerializedName("user_id")
    public String userId;

    @Expose
    @SerializedName("skilllevel")
    public String skillLevel;
}
