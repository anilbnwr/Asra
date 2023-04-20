package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryHeader {

    public static final String TITLE_GEAR = "Gear";
    public static final String TITLE_RENTAL = "Rentals";

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("children")
    @Expose
    public List<Category> categoryItemList;

    public boolean isGear() {
        return TITLE_GEAR.equalsIgnoreCase(title);
    }

    public boolean isRentals() {
        return TITLE_RENTAL.equalsIgnoreCase(title);
    }
}
