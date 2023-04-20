package com.asra.mobileapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.network.retrofit.request.CartEventRequest;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.StringUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EventDetails extends ETResponse implements Parcelable {

    private static final String NOT_LOGGED_IN = "not_logged_in";

    private static final String IN_STOCK = "instock";

    @SerializedName("event_id")
    @Expose
    public String eventId;

    @SerializedName("event_date")
    @Expose
    public String eventDate;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("event_image")
    @Expose
    public String eventImage;

    @SerializedName("logo_icon")
    @Expose
    public String logoIcon;

    @SerializedName("event_banner")
    @Expose
    public String eventBanner;

    @SerializedName("event_type")
    @Expose
    public String eventType;

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

    @SerializedName("product_info")
    @Expose
    public String productInfo;

    @SerializedName("is_role_based_pricing")
    @Expose
    public String isRoleBasedPricing;

    @SerializedName("have_rental_items")
    @Expose
    public String haveRentalItems;

    @SerializedName("is_coupon_enabled")
    @Expose
    public String isCouponEnabled;

    @SerializedName("coupon_code")
    @Expose
    public String couponCode;

    @SerializedName("coupon_based_price")
    @Expose
    public String couponBasedPrice;

    @SerializedName("roleBasedPrice")
    @Expose
    public RoleBasedPrice roleBasedPrice;

    @SerializedName("is_private_event")
    @Expose
    public boolean isPrivateEvent;

    @SerializedName("trainingData")
    @Expose
    public List<TrainingDatum> trainingData = null;

    @SerializedName("trainingStatus")
    @Expose
    public String trainingStatus;

    @SerializedName("trainingMsg")
    @Expose
    public String trainingMsg;

    @SerializedName("rentalData")
    @Expose
    public List<RentalDatum> rentalData = null;

    @SerializedName("rentalEnable")
    @Expose
    public boolean rentalEnable;

    @SerializedName("rentalStatus")
    @Expose
    public int rentalStatus;

    @SerializedName("rentalMsg")
    @Expose
    public String rentalMsg;

    @SerializedName("is_cancelled")
    @Expose
    public boolean isCancelled;

    @SerializedName("bike_no")
    @Expose
    public String bikeNumber;

    @SerializedName("transponder_no")
    @Expose
    public String transponderNo;

    @SerializedName("racer_status")
    @Expose
    public String racerStatus;



    @SerializedName("has_race_licence")
    @Expose
    public boolean hasRaceLicense;

    @SerializedName("skill_eligible")
    @Expose
    public boolean skillEligible;



    @SerializedName("trackDays")
    @Expose
    public List<Event> trackDayEvents;

    @SerializedName("external")
    @Expose
    public ExternalEventHost externalEventHost;


    @SerializedName("classes")
    @Expose
    public List<Race> eventClasses;



    @SerializedName("trackValidation")
    @Expose
    public boolean trackValidation;

    @SerializedName("mrlValidation")
    @Expose
    public boolean mrlValidation;

    @SerializedName("mrlHTML")
    @Expose
    public String mrlHTML;

    @SerializedName("mrlData")
    @Expose
    public MrlData mrlData;

    @SerializedName("mrlAddToCart")
    @Expose
    public boolean mrlAddToCart;

    @SerializedName("registration_closed")
    @Expose
    public boolean registrationClosed;



    public transient double classTotal = 0;
    public transient boolean transponderRented = false;
    public transient String registeredSkill = "";

    public List<CartEventRequest.ClassRequest> getSelectedClasses() {

        List<CartEventRequest.ClassRequest> requestList = new ArrayList<>();
        if(ListUtils.isNotEmpty(eventClasses)){
            for(Race race: eventClasses){
                for(RaceClass raceClass: race.raceClasses){

                    if(raceClass.checked && !StringUtils.isEmpty(raceClass.bikeData)) {
                        CartEventRequest.ClassRequest classRequest = new CartEventRequest.ClassRequest();
                        classRequest.bikeData = raceClass.bikeData;
                        classRequest.classId = raceClass.classId;
                        classRequest.className = raceClass.className;
                        classRequest.price = raceClass.classPrice + "";
                        classRequest.raceId = race.raceId;
                        classRequest.raceName = race.raceName;

                        requestList.add(classRequest);
                    }
                }
            }
        }
        return requestList;
    }


    public boolean canAddMotoEventToCart(){
        return trackValidation &&  mrlValidation && !registrationClosed;
    }


    public static class RentalDatum {


        @SerializedName("product_id")
        @Expose
        public String productId;

        @SerializedName("title")
        @Expose
        public String title;

        @SerializedName("slug")
        @Expose
        public String slug;

        @SerializedName("price")
        @Expose
        public String price;

        @SerializedName("variations")
        @Expose
        public List<Variation> variations = null;

        @SerializedName("image")
        @Expose
        public String image;


        public boolean selected;
        public Variation selectedVariant = null;
    }

    public static class RoleBasedPrice {


        @SerializedName("guest")
        @Expose
        public String guest;

        @SerializedName("grip")
        @Expose
        public String grip;

        @SerializedName("military")
        @Expose
        public String military;

        @SerializedName("apex")
        @Expose
        public String apex;

        @SerializedName("coach")
        @Expose
        public String coach;

        @SerializedName("logedout")
        @Expose
        public String logedout;

        @SerializedName("dealer")
        @Expose
        public String dealer;

        @SerializedName("racer")
        @Expose
        public String racer;

        @SerializedName("yg")
        @Expose
        public String yg;

        @SerializedName("vendor")
        @Expose
        public String vendor;

        @SerializedName("vip")
        @Expose
        public String vip;

        @SerializedName("motogirl")
        @Expose
        public String motogirl;

        @SerializedName("nycr")
        @Expose
        public String nycr;


        public String getRoleBasedPrice(String role) {

            String itemPrice = "0";
            if (TextUtils.isEmpty(role)) return itemPrice;

            if (role.toLowerCase().equalsIgnoreCase("guest"))
                itemPrice = guest == null ? itemPrice : guest;

            if (role.toLowerCase().equalsIgnoreCase("grip"))
                itemPrice = grip == null ? itemPrice : grip;

            if (role.toLowerCase().equalsIgnoreCase("military"))
                itemPrice = military == null ? itemPrice : military;

            if (role.toLowerCase().equalsIgnoreCase("coach"))
                itemPrice = coach == null ? itemPrice : coach;

            if (role.toLowerCase().equalsIgnoreCase("apex"))
                itemPrice = apex == null ? itemPrice : apex;

            if (role.toLowerCase().equalsIgnoreCase("logedout")
                    || role.toLowerCase().equalsIgnoreCase(NOT_LOGGED_IN))
                itemPrice = logedout != null ? logedout : guest;

            if (role.toLowerCase().equalsIgnoreCase("yg"))
                itemPrice = yg == null ? itemPrice : yg;

            if (role.toLowerCase().equalsIgnoreCase("racer"))
                itemPrice = racer == null ? itemPrice : racer;

            if (role.toLowerCase().equalsIgnoreCase("vip"))
                itemPrice = vip == null ? itemPrice : vip;

            if (role.toLowerCase().equalsIgnoreCase("dealer"))
                itemPrice = dealer == null ? itemPrice : dealer;

            if (role.toLowerCase().equalsIgnoreCase("motogirl"))
                itemPrice = motogirl == null ? itemPrice : motogirl;

            if (role.toLowerCase().equalsIgnoreCase("vendor"))
                itemPrice = vendor == null ? itemPrice : vendor;

            if (role.toLowerCase().equalsIgnoreCase("nycr"))
                itemPrice = nycr == null ? itemPrice : nycr;


            return itemPrice;

        }
    }

    public class TrainingDatum {


        @SerializedName("training_id")
        @Expose
        public String trainingId;

        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("title")
        @Expose
        public String title;

        @SerializedName("price")
        @Expose
        public String price;

        @SerializedName("slug")
        @Expose
        public String slug;

        @SerializedName("image")
        @Expose
        public String image;

        public boolean selected;
    }

    public static class Variation {


        @SerializedName("attribute_name")
        @Expose
        public String variantName;

        @SerializedName("attribute_value")
        @Expose
        public String attributeValue;

        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("price")
        @Expose
        public String price;

        @SerializedName("stock_status")
        @Expose
        public String stockStatus;

        public boolean isOutOfStock() {
            return !IN_STOCK.equalsIgnoreCase(stockStatus);
        }

    }





    public static class Transponder {

        @SerializedName("price")
        @Expose
        public String price;


        @SerializedName("image_url")
        @Expose
        public String image_url;


        @SerializedName("inCart")
        @Expose
        public boolean inCart;


        @SerializedName("number")
        @Expose
        public String number;


    }

    protected EventDetails(Parcel in) {
        eventId = in.readString();
        eventDate = in.readString();
        title = in.readString();
        slug = in.readString();
        price = in.readString();
        eventImage = in.readString();
        logoIcon = in.readString();
        eventBanner = in.readString();
        eventType = in.readString();
        maxQuantityOfE1People = in.readString();
        maxQuantityOfE2People = in.readString();
        maxQuantityOfE3People = in.readString();
        maxQuantityOfE4People = in.readString();
        maxQuantityOfGt1People = in.readString();
        productInfo = in.readString();
        isRoleBasedPricing = in.readString();
        haveRentalItems = in.readString();
        isCouponEnabled = in.readString();
        couponCode = in.readString();
        couponBasedPrice = in.readString();
        trainingStatus = in.readString();
        trainingMsg = in.readString();
        rentalEnable = in.readByte() != 0;
        rentalStatus = in.readInt();
        rentalMsg = in.readString();
        isCancelled = in.readByte() != 0;
        hasRaceLicense = in.readByte() != 0;
        skillEligible = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(eventDate);
        dest.writeString(title);
        dest.writeString(slug);
        dest.writeString(price);
        dest.writeString(eventImage);
        dest.writeString(logoIcon);
        dest.writeString(eventBanner);
        dest.writeString(eventType);
        dest.writeString(maxQuantityOfE1People);
        dest.writeString(maxQuantityOfE2People);
        dest.writeString(maxQuantityOfE3People);
        dest.writeString(maxQuantityOfE4People);
        dest.writeString(maxQuantityOfGt1People);
        dest.writeString(productInfo);
        dest.writeString(isRoleBasedPricing);
        dest.writeString(haveRentalItems);
        dest.writeString(isCouponEnabled);
        dest.writeString(couponCode);
        dest.writeString(couponBasedPrice);
        dest.writeString(trainingStatus);
        dest.writeString(trainingMsg);
        dest.writeByte((byte) (rentalEnable ? 1 : 0));
        dest.writeInt(rentalStatus);
        dest.writeString(rentalMsg);
        dest.writeByte((byte) (isCancelled ? 1 : 0));
        dest.writeByte((byte) (hasRaceLicense ? 1 : 0));
        dest.writeByte((byte) (skillEligible ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventDetails> CREATOR = new Creator<EventDetails>() {
        @Override
        public EventDetails createFromParcel(Parcel in) {
            return new EventDetails(in);
        }

        @Override
        public EventDetails[] newArray(int size) {
            return new EventDetails[size];
        }
    };




    public void filterActiveClasses() {
        //TODO filter out inactive races
    }

    public boolean hasActiveClass() {
            if(ListUtils.isNotEmpty(eventClasses)){
                for(Race race: eventClasses){
                    if(race.hasClassSelected()){
                        return true;
                    }
                }
            }

            return false;


    }


    public static class Race{
        @SerializedName("race_id")
        public String raceId;

        @SerializedName("race_name")
        public String raceName;

        @SerializedName("race_classes")
        public List<RaceClass> raceClasses;


        public boolean hasSpecialClass(){
            if(ListUtils.isNotEmpty(raceClasses)){
                for(RaceClass raceClass: raceClasses){
                    if(raceClass.specialCase){
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean validateRaceClasses(){
            boolean isValid = true;
            if(ListUtils.isNotEmpty(raceClasses)){
                for(RaceClass raceClass: raceClasses){
                    raceClass.hasError = raceClass.checked && StringUtils.isEmpty(raceClass.bikeData);
                    if(raceClass.hasError){
                        isValid = false;
                    }
                }
            }

            return isValid;
        }
        public boolean hasClassSelected(){
            if(ListUtils.isNotEmpty(raceClasses)){
                for(RaceClass raceClass: raceClasses){
                    if(raceClass.checked){
                        return true;
                    }
                }
            }

            return false;
        }
        public double getSelectedClassPrice(){

            double total = 0;
            for(RaceClass raceClass: raceClasses){
                if(raceClass.checked){
                    total += raceClass.classPrice;
                }
            }
            return total;
        }

        public boolean canSelectSpecialClass() {
            for (RaceClass raceClass : raceClasses) {
                if (!raceClass.specialCase && raceClass.checked) {
                    return true;
                }
            }
            return false;
        }

        public void validateSpecialCase(RaceClass raceClass) {
            boolean hasChecked = false;
            for (RaceClass raceClassItem : raceClasses) {
                if(raceClass.classId.equalsIgnoreCase(raceClassItem.classId)){
                    continue;
                }else{
                    if(!raceClassItem.specialCase && raceClassItem.checked){
                        hasChecked = true;
                    }
                }

            }
            if(!hasChecked){
                deselectSpecialClasses();
            }
        }

        private void deselectSpecialClasses() {
            for (RaceClass raceClassItem : raceClasses) {
                if (raceClassItem.specialCase) {
                    raceClassItem.checked = false;
                }
            }
        }
    }
    public static class RaceClass{
        @SerializedName("bike_data")
        public String bikeData;
        @Expose
        public Boolean checked;

        @SerializedName("class_id")
        public String classId;
        @SerializedName("class_name")
        public String className;
        @SerializedName("class_price")
        public double classPrice;
        @SerializedName("special_case")
        public Boolean specialCase;

        public transient boolean hasError = false;



    }

    public static class MrlData{

        @SerializedName("membership")
        public String membership;

        @SerializedName("title")
        public String title;

        @SerializedName("price")
        public String price;

        @SerializedName("image")
        public String image;

        @SerializedName("force")
        public int force;

        @SerializedName("serial")
        public String serial;

        public transient String messageContent;
        public transient boolean canAddToCart;


        @Expose
        public String season = DateUtils.getCurrentYear() + " Season";

        @Expose
        public String slug;

        @SerializedName("membership_id")
        @Expose
        public int membershipId;

    }
    public boolean isMotoEvent(){
        return  "Motogladiator".equalsIgnoreCase(eventType);
    }
}
