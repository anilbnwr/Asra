
package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PolicyAgreementCheckRequest {

    @SerializedName("serial")
    private String serial;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

}
