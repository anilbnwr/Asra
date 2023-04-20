package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignatureRequest {

    @Expose
    @SerializedName("signature_id")
    public String signatureId;
}
