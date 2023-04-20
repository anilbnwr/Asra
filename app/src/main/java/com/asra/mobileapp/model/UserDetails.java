package com.asra.mobileapp.model;

import android.text.TextUtils;

import com.asra.mobileapp.common.AppUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetails {

    private static final String ROLE_ADMIN = "administrator";
    private static final String ROLE_COACH = "coach";

    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";

    @SerializedName("user_id")
    @Expose
    public String userId;

    @SerializedName("old_id")
    @Expose
    public String oldId;

    @SerializedName("userName")
    @Expose
    public String username;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("niceName")
    @Expose
    public String nicename;

    @SerializedName("customer_number")
    @Expose
    public String customerID;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("registered")
    @Expose
    public String registeredDate;

    @SerializedName("activation_key")
    @Expose
    public String activationKey;

    @SerializedName("hash_code")
    @Expose
    public String hashCode;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("display_name")
    @Expose
    public String displayName;

    @SerializedName("nickName")
    @Expose
    public String nickname;

    @SerializedName("first_name")
    @Expose
    public String firstName;

    @SerializedName("last_name")
    @Expose
    public String lastName;

    @SerializedName("full_profile_image")
    @Expose
    public String profileImagePath;

    @SerializedName("billing_phone")
    @Expose
    public String billingPhone;

    @SerializedName("billing_first_name")
    @Expose
    public String billingFirstName;

    @SerializedName("billing_last_name")
    @Expose
    public String billingLastName;

    @SerializedName("billing_company")
    @Expose
    public String billingCompany;

    @SerializedName("billing_email")
    @Expose
    public String billingEmail;

    @SerializedName("billing_country")
    @Expose
    public String billingCountryCode;

    @SerializedName("billing_address_1")
    @Expose
    public String billingAddress1;

    @SerializedName("billing_address_2")
    @Expose
    public String billingAddress2;

    @SerializedName("billing_city")
    @Expose
    public String billingCity;

    @SerializedName("billing_state")
    @Expose
    public String billingStateCode;

    @SerializedName("billing_postcode")
    @Expose
    public String billingPostcode;

    @SerializedName("shipping_first_name")
    @Expose
    public String shippingFirstName;

    @SerializedName("shipping_last_name")
    @Expose
    public String shippingLastName;

    @SerializedName("shipping_company")
    @Expose
    public String shippingCompany;

    @SerializedName("shipping_address_1")
    @Expose
    public String shippingAddress1;

    @SerializedName("shipping_address_2")
    @Expose
    public String shippingAddress2;

    @SerializedName("shipping_city")
    @Expose
    public String shippingCity;

    @SerializedName("shipping_email")
    @Expose
    public String shippingEmail;

    @SerializedName("shipping_phone")
    @Expose
    public String shippingPhone;

    @SerializedName("shipping_postcode")
    @Expose
    public String shippingPostcode;

    @SerializedName("shipping_country")
    @Expose
    public String shippingCountryCode;

    @SerializedName("shipping_state")
    @Expose
    public String shippingStateCode;

    @SerializedName("shipping_method")
    @Expose
    public String shippingMethod;

    @SerializedName("ev_role")
    @Expose
    public String role;

    @SerializedName("ev_gender")
    @Expose
    public String gender;

    @SerializedName("ev_race_licence")
    @Expose
    public String raceLicence;

    @SerializedName("ev_dob")
    @Expose
    public String dateOfBirth;

    @SerializedName("ev_motocycle_year")
    @Expose
    public String motocycleYear;

    @SerializedName("ev_motorcycle")
    @Expose
    public String motorcycle;

    @SerializedName("ev_motorcycle_number")
    @Expose
    public String motorcycleNumber;

    @SerializedName("ev_motorcycle_shift_pattern")
    @Expose
    public String motorcycleShiftPattern;

    @SerializedName("ev_medications")
    @Expose
    public String medications;

    @SerializedName("ev_medical_conditions")
    @Expose
    public String medicalConditions;

    @SerializedName("ev_emergency_first_name")
    @Expose
    public String emergencyFirstName;

    @SerializedName("ev_emergency_last_name")
    @Expose
    public String emergencyLastName;

    @SerializedName("ev_emergency_phone")
    @Expose
    public String emergencyPhone;

    @SerializedName("ev_emergency_relationship")
    @Expose
    public String emergencyRelationship;

    @SerializedName("ev_staff")
    @Expose
    public String staff;

    @SerializedName("memo")
    @Expose
    public String memo;

    @SerializedName("skill_level")
    @Expose
    public String skillLevel;

    @SerializedName("n2_rider")
    @Expose
    public String n2Rider;

    @SerializedName("wallet_amount")
    @Expose
    public String walletAmount;

    @SerializedName("membership_exp_date")
    @Expose
    public String membershipExpDate;

    @SerializedName("newsletter")
    @Expose
    public String newsletter;

    @SerializedName("ever_been_track")
    @Expose
    public String everBeenTrack;

    @SerializedName("ev_dob_year")
    @Expose
    public String dobYear;

    @SerializedName("ev_dob_month")
    @Expose
    public String dobMonth;

    @SerializedName("ev_dob_day")
    @Expose
    public String dobDay;

    @SerializedName("phone")
    @Expose
    public String phone;

    @Expose @SerializedName("event_cancel")
    public boolean canCancelEvents;


    @SerializedName("shipping_country_name")
    @Expose
    public String shippingCountry;

    @SerializedName("billing_country_name")
    @Expose
    public String billingCountry;

    @SerializedName("shipping_state_name")
    @Expose
    public String shippingState;

    @SerializedName("billing_state_name")
    @Expose
    public String billingState;

    //Moto Profile Info
    @SerializedName("race_no")
    @Expose
    public String motoRaceNo;

    @SerializedName("ama_no")
    @Expose
    public String motoAmaNo;

    @SerializedName("ama_expires")
    @Expose
    public String motoAmaExpiryDate;

    @SerializedName("ccs_no")
    @Expose
    public String motoCCSNo;

    @SerializedName("asra_no")
    @Expose
    public String motoAsraNo;

    @SerializedName("nationality")
    @Expose
    public String nationality;

    @SerializedName("sponsors")
    @Expose
    public String motoSponsors;

    @SerializedName("moto_skill")
    @Expose
    public String motoSkill;

    @SerializedName("teamnames")
    @Expose
    public String motoTeamMates;

    @SerializedName("enable_my_duties")
    @Expose
    public boolean enableMyDuties;

    public boolean isMale(){
        return GENDER_MALE.equalsIgnoreCase(gender);
    }

    public boolean isFeMale(){
        return GENDER_FEMALE.equalsIgnoreCase(gender);
    }

    public String getFullName(){
        return "" + firstName+ " "+ lastName;
    }

    public boolean isAdmin(){
        return ROLE_ADMIN.equalsIgnoreCase(role);
    }

    public boolean isCoach(){
        return ROLE_COACH.equalsIgnoreCase(role);
    }


    public boolean hasRaceLicense(){
        return AppUtils.isTrue(raceLicence);
    }
    public boolean hasValidBillingAddress(){
        return !TextUtils.isEmpty(billingFirstName) &&
                !TextUtils.isEmpty(billingLastName) &&
                !TextUtils.isEmpty(billingAddress1) &&
                !TextUtils.isEmpty(billingCountry) &&
                !TextUtils.isEmpty(billingState) &&
                !TextUtils.isEmpty(billingPhone) &&
                !TextUtils.isEmpty(billingEmail);
    }
}
