
package com.asra.mobileapp.model;

import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class MembershipDetail extends ETResponse {

    private static final String ROLE_GUEST = "Guest";
    private static final String OUT_OF_STOCK = "outofstock";

    @Expose
    private String description;
    @Expose
    private String image;
    @SerializedName("membership_id")
    private int membershipId;
    @SerializedName("old_post_id")
    private String oldPostId;
    @Expose
    private String packages;
    @SerializedName("post_author")
    private String postAuthor;
    @SerializedName("post_date")
    private String postDate;
    @SerializedName("post_modified")
    private String postModified;
    @SerializedName("post_status")
    private String postStatus;
    @Expose
    private String price;
    @Expose
    private String slug;
    @SerializedName("stock_status")
    private String stockStatus;
    @Expose
    private String title;



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public String getOldPostId() {
        return oldPostId;
    }

    public void setOldPostId(String oldPostId) {
        this.oldPostId = oldPostId;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostModified() {
        return postModified;
    }

    public void setPostModified(String postModified) {
        this.postModified = postModified;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
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

    public boolean isOutOfStock(){
        return OUT_OF_STOCK.equalsIgnoreCase(stockStatus);
    }

    public boolean isGuest(){
        return ROLE_GUEST.equalsIgnoreCase(title);
    }

    public boolean canPurchase(UserMembership currentMembership){

        return !isGuest() && (currentMembership == null ||
                currentMembership.getMembershipId() < membershipId);


    }
}
