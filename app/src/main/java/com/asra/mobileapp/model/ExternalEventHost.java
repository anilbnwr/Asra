package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalEventHost {

    @SerializedName("url")
    @Expose
    public String externalUrl;

    @SerializedName("text")
    @Expose
    public String text;

}
