package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.CreditHistory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditHistoryResponse extends ETResponse {

    @SerializedName("result")
    @Expose
    public List<CreditHistory> creditHistoryList;


}
