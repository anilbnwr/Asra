package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListResponse extends ETResponse {


    @SerializedName("results")
    @Expose
    public List<Product> productList;
}
