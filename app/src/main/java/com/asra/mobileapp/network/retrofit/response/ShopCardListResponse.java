package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.ShopCard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopCardListResponse extends ETResponse {


    @SerializedName("data")
    @Expose
    public List<ShopCard> cardList;
}
