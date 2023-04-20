package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_id")
    @Expose
    public String productId;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("category_id")
    @Expose
    public String categoryId;



}
