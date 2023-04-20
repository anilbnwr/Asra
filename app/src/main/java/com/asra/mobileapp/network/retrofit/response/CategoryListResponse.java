package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.CategoryHeader;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryListResponse extends ETResponse {



    @SerializedName("data")
    @Expose
    public List<CategoryHeader> categoryList;

}
