package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("id")
    @Expose
    public String id;

    public String parentId;
}
