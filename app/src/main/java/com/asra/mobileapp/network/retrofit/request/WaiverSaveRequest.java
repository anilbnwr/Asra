
package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaiverSaveRequest {

    @Expose
    @SerializedName("user_id")
    public String userId;

    @Expose
    public String eventId;

    @Expose
    public String issuingState;

    @Expose
    public String license;
    @Expose
    public String nameAndLocation;
    @Expose
    public String signature;

    @Expose
    public boolean agree = true;


}
