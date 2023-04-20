package com.asra.mobileapp.network.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartEventRequest extends CartRequestBase {



    @Expose
    @SerializedName("training_list")
    public List<Training> trainingList = new ArrayList<>();

    @Expose
    @SerializedName("rental_list")
    public List<Rental> rentalList = new ArrayList<>();

    @Expose
    @SerializedName("event")
    public String eventSlug;

    @Expose
    @SerializedName("secret_code")
    public String eventCouponCode;


    @Expose
    @SerializedName("event_price")
    public String eventPrice;


    @Expose
    @SerializedName("event_date")
    public String eventDate;

    public static class Training{

        @Expose
        @SerializedName("price")
        public String price;

        @Expose
        @SerializedName("id")
        public String trainingId;

        @Expose
        @SerializedName("slug")
        public String slug;

        public String trainingName;

    }

    public static class Rental{
        @Expose
        @SerializedName("price")
        public String price;

        @Expose
        @SerializedName("id")
        public String rentalId;

        @Expose
        @SerializedName("slug")
        public String slug;

        public String selectedSize;

        public String rentalName;
    }


    @Expose
    @SerializedName("event_id")
    public String eventId;

    @Expose
    @SerializedName("classes")
    public List<ClassRequest> classes = new ArrayList<>();

    @Expose
    @SerializedName("total_price")
    public String classTotalPrice;

    @Expose
    @SerializedName("racer_status")
    public String skillClass;

    @Expose
    @SerializedName("bike_no")
    public String bikeNumber;


    @Expose
    @SerializedName("transponder_no")
    public String transponderNo;


    public static class TrackDayRequest{
        @Expose
        @SerializedName("event")
        public String eventId;

        @Expose
        @SerializedName("serial")
        public String memberNumber;
    }
    public static class ClassRequest{

        @Expose
        @SerializedName("class_name")
        public String className;


        @Expose
        @SerializedName("class_id")
        public String classId;

        @Expose
        @SerializedName("race_name")
        public String raceName;

        @Expose
        @SerializedName("race_id")
        public String raceId;

        @Expose
        @SerializedName("checked")
        public String addedToCart;

        @Expose
        @SerializedName("bike_data")
        public String bikeData;

        @Expose
        @SerializedName("price")
        public String price;



    }

}
