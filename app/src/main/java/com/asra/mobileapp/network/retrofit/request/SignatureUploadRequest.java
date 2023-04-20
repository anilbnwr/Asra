package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignatureUploadRequest {

    public static final String IMAGE_DATA = "data:image/png;base64,";
    @Expose
    @SerializedName("signature")
    public String signature;

    @Expose
    @SerializedName("signature_id")
    public String signatureId;
}
