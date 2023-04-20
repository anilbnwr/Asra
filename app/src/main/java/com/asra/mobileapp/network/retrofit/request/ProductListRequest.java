package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductListRequest {

    @Expose
    @SerializedName("post_status")
    public String post_status = "publish";

    @Expose
    @SerializedName("category")
    public String category = "gear";

    @Expose
    @SerializedName("status")
    public int status = 0;

    @Expose
    @SerializedName("method")
    public String method = "product";

    @Expose
    @SerializedName("category_id_list")
    public String categoryList = "";


}
