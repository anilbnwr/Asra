package com.asra.mobileapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country implements Comparable <Country>{

    @SerializedName("country_id")
    @Expose
    public String countryCode;

    @SerializedName("value")
    @Expose
    public String shortCode;

    @SerializedName("country")
    @Expose
    public String name;

    public static Country getUnitedStates(){
        Country country = new Country();
        country.countryCode = "US";
        country.name = "United States";

        return country;
    }


    @Override
    public int compareTo(Country o) {
        return name.compareTo(o.name);
    }
}
