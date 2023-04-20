package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillingAddressUpdateRequest {


    public static class BillingAddressRequest {
        @Expose
        @SerializedName("billing_first_name")
        public String firstName;

        @Expose
        @SerializedName("billing_last_name")
        public String lastName;

        @Expose
        @SerializedName("billing_company")
        public String company;

        @Expose
        @SerializedName("billing_address_1")
        public String address1;

        @Expose
        @SerializedName("billing_address_2")
        public String address2;

        @Expose
        @SerializedName("billing_city")
        public String city;

        @Expose
        @SerializedName("billing_state")
        public String state;


        @Expose
        @SerializedName("billing_country")
        public String country;

        @Expose
        @SerializedName("billing_postcode")
        public String postalCode;

        @Expose
        @SerializedName("billing_email")
        public String email;

        @Expose
        @SerializedName("billing_phone")
        public String phone;
    }

    @Expose
    @SerializedName("data")
    public BillingAddressRequest addressRequest = new BillingAddressRequest();

    @Expose
    @SerializedName("user_id")
    public String userId;
}
