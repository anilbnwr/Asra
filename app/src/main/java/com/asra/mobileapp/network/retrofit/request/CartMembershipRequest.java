
package com.asra.mobileapp.network.retrofit.request;


import com.google.gson.annotations.SerializedName;


public class CartMembershipRequest {

    @SerializedName("image")
    private String mImage;
    @SerializedName("membership")
    private String mMembershipName;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("serial")
    private String mSerial;
    @SerializedName("title")
    private String mTitle;

    @SerializedName("force")
    private String force = "0";

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getMembership() {
        return mMembershipName;
    }

    public void setMembership(String membership) {
        mMembershipName = membership;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getSerial() {
        return mSerial;
    }

    public void setUserId(String serial) {
        mSerial = serial;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }
}
