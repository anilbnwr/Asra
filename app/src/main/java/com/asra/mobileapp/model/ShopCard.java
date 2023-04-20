package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopCard {

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("image")
    @Expose
    public String imagePath;

    @SerializedName("price")
    @Expose
    public String price;


}
