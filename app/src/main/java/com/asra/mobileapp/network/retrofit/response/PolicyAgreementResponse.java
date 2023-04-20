package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.PolicyAgreement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolicyAgreementResponse extends ETResponse {

    @SerializedName("data")
    @Expose
    public PolicyAgreement agreement;


}
