package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingAddressUpdateRequest {

    @Expose
    @SerializedName("user_id")
    public String userId;

    @Expose
    @SerializedName("data")
    public ShippingAddressRequest addressRequest = new ShippingAddressRequest();

    public static class ShippingAddressRequest {
        @Expose
        @SerializedName("shipping_first_name")
        public String firstName;

        @Expose
        @SerializedName("shipping_last_name")
        public String lastName;

        @Expose
        @SerializedName("shipping_company")
        public String company;

        @Expose
        @SerializedName("shipping_address_1")
        public String address1;

        @Expose
        @SerializedName("shipping_address_2")
        public String address2;

        @Expose
        @SerializedName("shipping_city")
        public String city;

        @Expose
        @SerializedName("shipping_state")
        public String state;


        @Expose
        @SerializedName("shipping_country")
        public String country;

        @Expose
        @SerializedName("shipping_postcode")
        public String postalCode;

        @Expose
        @SerializedName("shipping_email")
        public String email;

        @Expose
        @SerializedName("shipping_phone")
        public String phone;
    }

}
