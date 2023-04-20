package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartItem {


    public static final String IN_STOCK = "instock";

    @SerializedName("evtype")
    @Expose
    public String eventType;

    @SerializedName("parent_title")
    @Expose
    public String parentTitle;

    @SerializedName("item_attributes")
    @Expose
    public List<Attribute> attributes;

    @SerializedName("cart_id")
    @Expose
    public String cartId;

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("parent_slug")
    @Expose
    public String parentSlug;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("parent_id")
    @Expose
    public String parentId;

    @SerializedName("object_id")
    @Expose
    public String objectId;

    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("quantity")
    @Expose
    public String quantity;

    @SerializedName("stock_status")
    @Expose
    public String stockStatus;

    @SerializedName("source")
    @Expose
    public String method;

    @SerializedName("fee_amount")
    @Expose
    public String feeAmount;

    @SerializedName("can_remove")
    @Expose
    public boolean canRemove = true;


    public boolean isOutOfStock(){
        return !IN_STOCK.equalsIgnoreCase(stockStatus);
    }
    public static class Attribute{

        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("value")
        @Expose
        public String value;


        public boolean isValueName(){
            return "name".equalsIgnoreCase(name);
        }


    }

    public boolean isMotoEvent(){
        return  "Motogladiator".equalsIgnoreCase(eventType);
    }
    public boolean isRaceFee(){
        return "race-fee".equalsIgnoreCase(slug);
    }
}
