package com.asra.mobileapp.model;

import com.asra.mobileapp.util.ListUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassportInfo{

	@SerializedName("event_id")
	private String eventId;

	@SerializedName("user_type")
	private String userType;

	@SerializedName("membership_level")
	private String membershipLevel;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("signature")
	private int signature;

	@SerializedName("is_stamped")
	private int isStamped;
	@SerializedName("day_worker_job")
	private String dayWorkerJob;

	@SerializedName("group_logo")
	private String groupLogo;

	@SerializedName("rider_name")
	private String riderName;

	@SerializedName("skill_level")
	private String skillLevel;

	@SerializedName("training")
	private List<String> trainingList;

	@SerializedName("picture")
	private String picture;

	@SerializedName("passport_id")
	private String passportId;

	@SerializedName("signed_date")
	private String signedDate;

	@SerializedName("rentals")
	private List<Rental> rentals;

	public void setTrainingList(List<String> trainingList) {
		this.trainingList = trainingList;
	}

	public void setEventId(String eventId){
		this.eventId = eventId;
	}

	public String getEventId(){
		return eventId;
	}

	public void setUserType(String userType){
		this.userType = userType;
	}

	public String getUserType(){
		return userType;
	}

	public void setMembershipLevel(String membershipLevel){
		this.membershipLevel = membershipLevel;
	}

	public String getMembershipLevel(){
		return membershipLevel;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setSignature(int signature){
		this.signature = signature;
	}

	public int getSignature(){
		return signature;
	}

	public int getIsStamped() {
		return isStamped;
	}

	public void setIsStamped(int isStamped) {
		this.isStamped = isStamped;
	}

	public void setDayWorkerJob(String dayWorkerJob){
		this.dayWorkerJob = dayWorkerJob;
	}

	public String getDayWorkerJob(){
		return dayWorkerJob;
	}

	public void setGroupLogo(String groupLogo){
		this.groupLogo = groupLogo;
	}

	public String getGroupLogo(){
		return groupLogo;
	}

	public void setRiderName(String riderName){
		this.riderName = riderName;
	}

	public String getRiderName(){
		return riderName;
	}

	public void setSkillLevel(String skillLevel){
		this.skillLevel = skillLevel;
	}

	public String getSkillLevel(){
		return skillLevel;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}

	public void setPassportId(String passportId){
		this.passportId = passportId;
	}

	public String getPassportId(){
		return passportId;
	}


	public boolean isDayWorker(){
		return "Worker".equalsIgnoreCase(userType);
	}

	public String getTrainingList(){
			if(ListUtils.isEmpty(this.trainingList)){
				return "";
			}
			String separator = ", ";
			StringBuilder itemText = new StringBuilder();
			for(String training : this.trainingList){
				itemText.append(training).append(separator);
			}
			String result = itemText.toString();
			if(result.endsWith(separator)){
				result = result.substring(0,
						result.length() - separator.length());
			}
			return result;

	}

	public String getConsolidatedRentals(){
		if(ListUtils.isEmpty(this.rentals)){
			return "";
		}
		String separator = ", ";
		StringBuilder itemText = new StringBuilder();
		for(Rental rental : this.rentals){
			itemText.append(rental.name).append(separator);
		}
		String result = itemText.toString();
		if(result.endsWith(separator)){
			result = result.substring(0,
					result.length() - separator.length());
		}
		return result;

	}

	public String getSignedDate() {
		return signedDate;
	}

	public void setSignedDate(String signedDate) {
		this.signedDate = signedDate;
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
}