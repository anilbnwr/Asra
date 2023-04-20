package com.asra.mobileapp.model;

import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.util.ListUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.List;

public class EnrolledEvent {

    @SerializedName("event_id")
    @Expose
    public String eventId;

    @SerializedName("product_name")
    @Expose
    public String productName;

    @SerializedName("order_date")
    @Expose
    public String orderDate;

    @SerializedName("event_date")
    @Expose
    public String eventDate;

    @SerializedName("event_image")
    @Expose
    public String eventImage;

    @SerializedName("order_item_id")
    @Expose
    public String orderItemId;

    @SerializedName("order_status")
    @Expose
    public String orderStatus;

    @SerializedName("event_month")
    @Expose
    public String eventMonth;

    @SerializedName("enable_selfsign")
    @Expose
    public boolean enableSelfSign;

    @SerializedName("has_passport")
    @Expose
    public boolean hasPassport;

    @SerializedName("passport_id")
    @Expose
    public String passportId;


    @Expose
    public List<EventParticipant.Rental> rentals;

    @SerializedName("classes")
    public List<EventParticipant.MotoClass> motoClasses;

    @SerializedName("training")
    @Expose
    public List<String> trainingList;

    public boolean hasAccessories(){
        return ListUtils.isNotEmpty(trainingList) || ListUtils.isNotEmpty(rentals) || ListUtils.isNotEmpty(motoClasses);
    }

    public boolean canUploadPassport(){
        Calendar today = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.setTime(DateUtils.convertToDate(eventDate, DateUtils.SIMPLE_DATE_NO_TIME));
        int dayDiff = date.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR);
        if( dayDiff <= 1 && dayDiff >= 0){
            if(dayDiff == 1){
                return today.get(Calendar.HOUR_OF_DAY) >= 18;
            }else {
                return true;
            }
        }
        return false;
    }
}
