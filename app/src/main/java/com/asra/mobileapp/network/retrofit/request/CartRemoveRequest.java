package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartRemoveRequest {

    @Expose
    @SerializedName("itemid")
    public String itemId;
    @Expose
    @SerializedName("id")
    public String userId;

    @Expose
    @SerializedName("cartid")
    public String cartId;

    @Expose
    @SerializedName("method")
    public String method;



}
