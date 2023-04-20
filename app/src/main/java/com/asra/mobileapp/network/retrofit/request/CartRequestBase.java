package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartRequestBase  {

    @Expose
    @SerializedName("serial")
    public String memberNumber;

    @Expose
    @SerializedName("slug")
    public String slug;

    @Expose
    @SerializedName("price")
    public String price;

    @Expose
    @SerializedName("quantity")
    public String quantity;

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("role")
    public String role;

    @Expose
    @SerializedName("image")
    public String image;
}
