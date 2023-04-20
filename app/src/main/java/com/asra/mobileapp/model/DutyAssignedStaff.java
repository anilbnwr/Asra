package com.asra.mobileapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DutyAssignedStaff{

	@SerializedName("role")
	private String role;

	@SerializedName("signature")
	private int signature;

	@SerializedName("skill_level")
	private String skillLevel;

	@SerializedName("show")
	private boolean show;

	@SerializedName("title")
	private String title;

	@SerializedName("ev_dob")
	private String evDob;

	@SerializedName("display_name")
	private String displayName;

	@SerializedName("sign_enabled")
	private int signEnabled;

	@SerializedName("signature_id")
	private String signatureId;

	@SerializedName("event_id")
	private String eventId;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	@SerializedName("duties")
	private List<AdminDuty> duties;

	public void setRole(String role){
		this.role = role;
	}

	public String getRole(){
		return role;
	}

	public void setSignature(int signature){
		this.signature = signature;
	}

	public int getSignature(){
		return signature;
	}

	public void setSkillLevel(String skillLevel){
		this.skillLevel = skillLevel;
	}

	public String getSkillLevel(){
		return skillLevel;
	}

	public void setShow(boolean show){
		this.show = show;
	}

	public boolean isShow(){
		return show;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setEvDob(String evDob){
		this.evDob = evDob;
	}

	public String getEvDob(){
		return evDob;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setSignEnabled(int signEnabled){
		this.signEnabled = signEnabled;
	}

	public int getSignEnabled(){
		return signEnabled;
	}

	public void setSignatureId(String signatureId){
		this.signatureId = signatureId;
	}

	public String getSignatureId(){
		return signatureId;
	}

	public void setEventId(String eventId){
		this.eventId = eventId;
	}

	public String getEventId(){
		return eventId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setDuties(List<AdminDuty> duties){
		this.duties = duties;
	}

	public List<AdminDuty> getDuties(){
		return duties;
	}
}