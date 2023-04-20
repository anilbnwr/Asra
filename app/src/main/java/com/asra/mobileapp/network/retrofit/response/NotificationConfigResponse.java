package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.NotificationType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationConfigResponse extends ETResponse {



    @SerializedName("result")
    @Expose
    public List<NotificationType> notificationList;




}
