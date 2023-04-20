package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartGiftCardRequest extends CartRequestBase {

    @Expose
    @SerializedName("name")
    public String receiverName;

    @Expose
    @SerializedName("email")
    public String receiverEmail;


}
