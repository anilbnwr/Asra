package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class NotificationType {
    private static final String PN_ENABLED = "1";
    private static final String PN_DISABLED = "0";

    @SerializedName("id")
    @Expose
    public String notificationId;

    @SerializedName("type")
    @Expose
    public String notificationType;

    @SerializedName("status")
    @Expose
    public String notificationStatus;


    public boolean isEnabled(){
        return PN_ENABLED.equals(notificationStatus);
    }

    public void setEnabled(boolean enable) {

        notificationStatus = enable ? PN_ENABLED : PN_DISABLED;
    }
}
