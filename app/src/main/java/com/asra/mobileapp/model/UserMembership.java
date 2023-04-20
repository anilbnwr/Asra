package com.asra.mobileapp.model;

import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMembership extends ETResponse {

    @Expose
    private String membership;

    @SerializedName("membership_id")
    @Expose
    private int membershipId;

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }
}
