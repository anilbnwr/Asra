package com.asra.mobileapp.model;

import android.text.TextUtils;

import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.util.ListUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class Event {

    public static final String NOT_LOGGED_IN = "not_logged_in";

    @SerializedName("event_id")
    @Expose
    public String eventId;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("event_image")
    @Expose
    public String eventImage;

    @SerializedName("hostings")
    @Expose
    public List<EventHost> eventHostList;


    @SerializedName("event_type")
    @Expose
    public String eventType;

    @SerializedName("event_date")
    @Expose
    public String eventDate;

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("full_event_logo")
    @Expose
    public String logoPath;

    @SerializedName("event_month")
    @Expose
    public String eventMonth;



    @SerializedName("max_quantity_of_e1_people")
    @Expose
    public String maxQuantityOfE1People;

    @SerializedName("max_quantity_of_e2_people")
    @Expose
    public String maxQuantityOfE2People;

    @SerializedName("max_quantity_of_e3_people")
    @Expose
    public String maxQuantityOfE3People;

    @SerializedName("max_quantity_of_e4_people")
    @Expose
    public String maxQuantityOfE4People;

    @SerializedName("max_quantity_of_gt1_people")
    @Expose
    public String maxQuantityOfGt1People;

    @SerializedName("is_coupon_enabled")
    @Expose
    public String isCouponEnabled;

    @SerializedName("coupon_code")
    @Expose
    public String couponCode;

    @SerializedName("coupon_based_price")
    @Expose
    public String couponBasedPrice;

    @SerializedName("ev_type")
    @Expose
    public String evType;


    @SerializedName("event_moto_url")
    @Expose
    public String eventMotoUrl;


    @SerializedName("rolePrice")
    @Expose
    public RoleBasedPrice roleBasedPrice;

    @SerializedName("is_cancelled")
    @Expose
    public boolean isCancelled;

    public long startingPrice;

    @SerializedName("inCart")
    @Expose
    public String inCart;

    @SerializedName("is_private_event")
    @Expose
    public boolean isPrivateEvent;

    @SerializedName("external")
    @Expose
    public ExternalEventHost externalEventHost;

    public static class RoleBasedPrice{

        @SerializedName("guest")
        @Expose
        public Price guestPrice;

        @SerializedName("grip")
        @Expose
        public Price gripPrice;

        @SerializedName("military")
        @Expose
        public Price militaryPrice;

        @SerializedName("coach")
        @Expose
        public Price coachPrice;

        @SerializedName("apex")
        @Expose
        public Price apexPrice;


        @SerializedName("logedout")
        @Expose
        public Price loggedOut;

        @SerializedName("yg")
        @Expose
        public Price ygPrice;

        @SerializedName("racer")
        @Expose
        public Price racerPrice;

        @SerializedName("vip")
        @Expose
        public Price vipPrice;

        @SerializedName("dealer")
        @Expose
        public Price dealerPrice;

        @SerializedName("motogirl")
        @Expose
        public Price motoGirlPrice;

        @SerializedName("nycr")
        @Expose
        public Price nycrPrice;


        public double getStartingPrice(double price){
            double minimum = price;

            if(guestPrice != null && guestPrice.getValue() < minimum && guestPrice.getValue() > 0)
                minimum = guestPrice.getValue();

            if(gripPrice != null && gripPrice.getValue() < minimum && gripPrice.getValue() > 0)
                minimum = gripPrice.getValue();

            if(militaryPrice != null && militaryPrice.getValue() < minimum && militaryPrice.getValue() > 0)
                minimum = militaryPrice.getValue();

            if(coachPrice != null &&coachPrice.getValue() < minimum && coachPrice.getValue() > 0)
                minimum = coachPrice.getValue();

            if(apexPrice != null && apexPrice.getValue() < minimum && apexPrice.getValue() > 0)
                minimum = apexPrice.getValue();

            if(ygPrice != null && ygPrice.getValue() < minimum && ygPrice.getValue() > 0)
                minimum = ygPrice.getValue();

            if(racerPrice != null && racerPrice.getValue() < minimum && racerPrice.getValue() > 0)
                minimum = racerPrice.getValue();

            if(vipPrice != null && vipPrice.getValue() < minimum && vipPrice.getValue() > 0)
                minimum = vipPrice.getValue();

            if(dealerPrice != null && dealerPrice.getValue() < minimum && dealerPrice.getValue() > 0)
                minimum = dealerPrice.getValue();

            if(motoGirlPrice != null && motoGirlPrice.getValue() < minimum && motoGirlPrice.getValue() > 0)
                minimum = motoGirlPrice.getValue();

            if(nycrPrice != null && nycrPrice.getValue() < minimum && nycrPrice.getValue() > 0)
                minimum = nycrPrice.getValue();

            return minimum;
        }

        public double getRoleBasedPrice(String role, double itemPrice){

            Timber.d("Role - %s", role);

            if(TextUtils.isEmpty(role)) return itemPrice;

            if(role.toLowerCase().equalsIgnoreCase("guest"))
                return  guestPrice == null ? itemPrice : guestPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("grip"))
                return  gripPrice == null ? itemPrice : gripPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("military"))
                return  militaryPrice == null ? itemPrice : militaryPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("coach"))
                return  coachPrice == null ? itemPrice : coachPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("apex"))
                return  apexPrice == null ? itemPrice : apexPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("logedout")
                    || role.toLowerCase().equalsIgnoreCase(NOT_LOGGED_IN))
                return  loggedOut == null ? itemPrice : loggedOut.getValue();

            if(role.toLowerCase().equalsIgnoreCase("yg"))
                return  ygPrice == null ? itemPrice : ygPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("racer"))
                return  racerPrice == null ? itemPrice : racerPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("vip"))
                return  vipPrice == null ? itemPrice : vipPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("dealer"))
                return  dealerPrice == null ? itemPrice : dealerPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("motogirl"))
                return  motoGirlPrice == null ? itemPrice : motoGirlPrice.getValue();

            if(role.toLowerCase().equalsIgnoreCase("nycr"))
                return  nycrPrice == null ? itemPrice : nycrPrice.getValue();


            return itemPrice;

        }
    }
    public static class Price{
        @SerializedName("regular_price")
        @Expose
        public String regularPrice;

        public double getValue(){
            try {
                return TextUtils.isEmpty(regularPrice) ? 0 : AppUtils.getDouble(regularPrice);
            }catch (Exception e){
                return 0;
            }

        }

    }

    public List<EventHost> getActiveEventHosts(){
        List<EventHost> filteredEventHosts = new ArrayList<>();
        if(!ListUtils.isEmpty(eventHostList)){
            for(EventHost host: eventHostList)
                if(host.getStatus())
                    filteredEventHosts.add(host);
        }
        return filteredEventHosts;
    }
    public void setEventMonth() {
        eventMonth = DateUtils.getDateAsString(eventDate, DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.MMYYYY_DATE_PATTERN);
    }

    public boolean isMotoEvent(){
        return  "Motogladiator".equalsIgnoreCase(eventType);
    }
}
