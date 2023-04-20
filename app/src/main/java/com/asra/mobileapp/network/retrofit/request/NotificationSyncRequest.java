package com.asra.mobileapp.network.retrofit.request;

import com.asra.mobileapp.model.NotificationPreference;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NotificationSyncRequest {

    @Expose
    @SerializedName("user_id")
    public String userId;


    @Expose
    @SerializedName("preferences")
    public List<NotificationPreference> notificationPreferenceList = new ArrayList<>();


}
