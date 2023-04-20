package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransferCreditRequest {

    @Expose
    @SerializedName("user_id")
    public String userId;

    @Expose
    @SerializedName("transferEmail")
    public String recipientEmail;

    @Expose
    @SerializedName("transferAmount")
    public double amount;


}
