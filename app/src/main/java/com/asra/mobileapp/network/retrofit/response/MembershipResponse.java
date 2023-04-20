package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.Membership;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MembershipResponse extends ETResponse {

    @SerializedName("memberships")
    @Expose
    public List<Membership> membershipList;

    @SerializedName("season")
    @Expose
    public String season;

    public List<Membership> getMembershipList() {
        return membershipList;
    }

    public void setMembershipList(List<Membership> membershipList) {
        this.membershipList = membershipList;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
