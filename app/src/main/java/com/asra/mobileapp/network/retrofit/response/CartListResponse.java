package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.model.CartItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartListResponse extends ETResponse {


    @SerializedName("data")
    @Expose
    public List<CartItem> cartList;

    @SerializedName("wallet")
    @Expose
    public String walletBalance;

    @SerializedName("walletEnabled")
    @Expose
    public String walletEnabled;


    public boolean isWalletEnabled(){
        return AppUtils.isTrue(walletEnabled);
    }

}
