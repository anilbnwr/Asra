package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileUpdateRequest {

    @SerializedName("user_id")
    @Expose
    public String userId;

    @SerializedName("data")
    @Expose
    public ProfileRequest request = new ProfileRequest();

    public static class ProfileRequest{
        @SerializedName("phone")
        @Expose
        public String phone;

        @SerializedName("first_name")
        @Expose
        public String firstName;


        @SerializedName("last_name")
        @Expose
        public String lastName;



        @SerializedName("ev_gender")
        @Expose
        public String gender;

        @SerializedName("ev_race_licence")
        @Expose
        public String raceLicence;

        @SerializedName("ev_dob")
        @Expose
        public String dateOfBirth;


        @SerializedName("ev_motorcycle")
        @Expose
        public String motorcycle;

        @SerializedName("ev_motorcycle_number")
        @Expose
        public String motorcycleNumber;


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


        @SerializedName("ever_been_track")
        @Expose
        public String everBeenTrack;

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

        @SerializedName("teamnames")
        @Expose
        public String motoTeamMates;

    }


}
