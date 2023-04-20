package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditHistory {

    @SerializedName("payment_log_id")
    @Expose
    public String paymentLogId;

    @SerializedName("amount")
    @Expose
    public String amount;

    @SerializedName("mode")
    @Expose
    public String mode;

    @SerializedName("is_credit")
    @Expose
    public String isCredit;

    @SerializedName("order_id")
    @Expose
    public String orderId;

    @SerializedName("user_id")
    @Expose
    public String userId;

    @SerializedName("post_date")
    @Expose
    public String postDate;

    @SerializedName("post_modified")
    @Expose
    public String postModified;

    @SerializedName("description")
    @Expose
    public String description;
}
