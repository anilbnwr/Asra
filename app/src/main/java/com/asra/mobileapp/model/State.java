package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class State {
    @SerializedName("sort_name")
    @Expose
    public String shortName;


    @SerializedName("name")
    @Expose
    public String name;



}
