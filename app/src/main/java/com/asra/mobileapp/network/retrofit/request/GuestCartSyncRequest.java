package com.asra.mobileapp.network.retrofit.request;

import com.asra.mobileapp.model.CartItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GuestCartSyncRequest {

    @SerializedName("userId")
    @Expose
    public String userId;

    @SerializedName("cartList")
    @Expose
    public List<CartItem> cartItems;
}
