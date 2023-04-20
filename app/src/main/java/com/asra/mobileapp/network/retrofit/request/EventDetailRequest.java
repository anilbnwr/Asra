package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDetailRequest {

    @Expose
    @SerializedName("slug")
    public String slug;

    @Expose
    @SerializedName("serial")
    public String userId;

    @Expose
    @SerializedName("post_status")
    public String postStatus = "publish";

}
