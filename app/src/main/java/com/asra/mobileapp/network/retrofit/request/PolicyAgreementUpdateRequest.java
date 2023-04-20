package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolicyAgreementUpdateRequest {

    @SerializedName("serial")
    @Expose
    public String userId;

    @SerializedName("has_agreed")
    @Expose
    public boolean hasAgreed;

}
