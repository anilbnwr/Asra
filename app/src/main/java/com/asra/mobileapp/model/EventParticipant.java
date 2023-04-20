
package com.asra.mobileapp.model;

import java.util.List;

import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.StringUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EventParticipant {

    @SerializedName("display_name")
    public String displayName;
    @SerializedName("e_waiver")
    public boolean eWaiver;
    @Expose
    public String email;
    @SerializedName("ev_dob")
    public String dateOfBirth;
    @SerializedName("event_date")
    public String eventDate;
    @SerializedName("event_id")
    public String eventId;

    @SerializedName("moto_classes")
    public List<MotoClass> motoClasses;

    @SerializedName("duties")
    public List<AdminDuty> duties;

    @SerializedName("moto_purchased")
    public boolean motoPurchased;

    @SerializedName("no_td")
    public boolean noTd;
    @SerializedName("order_id")
    public String orderNumber;
    @Expose
    public List<Rental> rentals;
    @Expose
    public String role;
    @Expose
    public boolean show;
    @SerializedName("sign_enabled")
    public int signEnabled;

    @Expose
    public boolean signature;

    @SerializedName("skill_level")
    public String skillLevel;
    @SerializedName("td_purchased")
    public boolean tdPurchased;
    @Expose
    public String title;

    @SerializedName("general_skills")
    public List<String> skillList;


    @SerializedName("training")
    @Expose
    public List<String> trainingList;

    @SerializedName("user_id")
    public String userId;


    @SerializedName("signature_id")
    public String signatureId;

    @SerializedName("jobAssigned")
    public String jobAssigned;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean getEWaiver() {
        return eWaiver;
    }

    public void setEWaiver(boolean eWaiver) {
        this.eWaiver = eWaiver;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEvDob() {
        return dateOfBirth;
    }

    public void setEvDob(String evDob) {
        this.dateOfBirth = evDob;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MotoClass> getMotoClasses() {
        return motoClasses;
    }

    public void setMotoClasses(List<MotoClass> motoClasses) {
        this.motoClasses = motoClasses;
    }

    public boolean getMotoPurchased() {
        return motoPurchased;
    }

    public void setMotoPurchased(boolean motoPurchased) {
        this.motoPurchased = motoPurchased;
    }

    public boolean getNoTd() {
        return noTd;
    }

    public void setNoTd(boolean noTd) {
        this.noTd = noTd;
    }

    public String getOrderId() {
        return orderNumber;
    }

    public void setOrderId(String orderId) {
        this.orderNumber = orderId;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getSignEnabled() {
        return signEnabled;
    }

    public void setSignEnabled(int signEnabled) {
        this.signEnabled = signEnabled;
    }

    public boolean getSignature() {
        return signature;
    }

    public void setSignature(boolean signature) {
        this.signature = signature;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public boolean getTdPurchased() {
        return tdPurchased;
    }

    public void setTdPurchased(boolean tdPurchased) {
        this.tdPurchased = tdPurchased;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTraining() {
        return trainingList;
    }

    public void setTraining(List<String> trainings) {
        this.trainingList = trainings;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public static class MotoClass {

        @SerializedName("race_classes")
        public List<RaceClass> raceClasses;
        @SerializedName("race_name")
        public String raceName;

        public List<RaceClass> getRaceClasses() {
            return raceClasses;
        }

        public void setRaceClasses(List<RaceClass> raceClasses) {
            this.raceClasses = raceClasses;
        }

        public String getRaceName() {
            return raceName;
        }

        public void setRaceName(String raceName) {
            this.raceName = raceName;
        }

    }

    public static class RaceClass {

        @SerializedName("bike_data")
        public String bikeData;
        @SerializedName("class_name")
        public String className;

        public String getBikeData() {
            return bikeData;
        }

        public void setBikeData(String bikeData) {
            this.bikeData = bikeData;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

    }

    public String getConsolidatedDuties(){
        if(ListUtils.isEmpty(this.duties)){
            return "";
        }
        String separator = ", ";
        StringBuilder dutiesBuilder = new StringBuilder();
        for(AdminDuty duty : this.duties){
            dutiesBuilder.append(duty.getName()).append(separator);
        }
        String duties = dutiesBuilder.toString();
        if(duties.endsWith(separator)){
            duties = duties.substring(0, duties.length() - separator.length());
        }
        return duties;
    }

    public static class Rental{
        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("attribute")
        @Expose
        public String attribute;

        @SerializedName("value")
        @Expose
        public String value;


    }

    public String getDisplayName(){
        return StringUtils.isEmpty(displayName) ? "" : displayName;
    }

    public boolean isSignatureAvailable(){
        return signature;
    }

    public static EventParticipant convertFromDutyAssignedStaff(DutyAssignedStaff staff){
        EventParticipant participant = new EventParticipant();
        participant.duties = staff.getDuties();
        participant.dateOfBirth = staff.getEvDob();
        participant.eventId = staff.getEventId();
        participant.displayName = staff.getDisplayName();
        participant.email = staff.getEmail();
        participant.skillLevel = staff.getSkillLevel();
        participant.orderNumber = staff.getOrderId();
        participant.userId = staff.getUserId();
        participant.role = staff.getRole();
        participant.signEnabled = staff.getSignEnabled();
        participant.signatureId = staff.getSignatureId();
        participant.signature = staff.getSignature() == 1;
        participant.show = staff.isShow();
        return participant;
    }
}
