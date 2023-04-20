
package com.asra.mobileapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Membership {

    public static final String IN_STOCK = "instock";
    public static final String OUT_OF_STOCK = "outofstock";
    public static final String ROLE_GUEST = "Guest";

    public static final int ID_MRL_MEMBERSHIP = 5;

    @Expose
    private String image;

    @Expose
    private String price;

    @Expose
    private String slug;

    @Expose
    private String season;

    @SerializedName("membership_id")
    @Expose
    private int membershipId;


    @Expose
    @SerializedName("stock_status")
    private String stockStatus;

    @Expose
    private String title;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public boolean isOutOfStock(){
        return OUT_OF_STOCK.equalsIgnoreCase(stockStatus);
    }

    public boolean shouldHighlight(String userRole){
        return userRole.equalsIgnoreCase(title);
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }
    public boolean isGuest(){
        return ROLE_GUEST.equalsIgnoreCase(title);
    }


    public boolean canPurchase(UserMembership currentMembership){

        return !isGuest() && (currentMembership == null ||
                currentMembership.getMembershipId() < membershipId);


    }
}
