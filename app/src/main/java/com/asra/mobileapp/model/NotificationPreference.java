package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationPreference {
    @Expose
    @SerializedName("notification_type")
    public String notificationType;

    @Expose
    @SerializedName("notification_id")
    public String notificationId;

    @Expose
    @SerializedName("status")
    public String notificationStatus;
}
