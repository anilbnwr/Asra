
package com.asra.mobileapp.network.retrofit.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SignUpRequest implements Parcelable {

    @Expose
    public int accept = 1;

    @SerializedName("confirm_email")
    public String confirmEmail;

    @SerializedName("confirm_password")
    public String confirmPassword;

    @Expose
    public String dob;

    @Expose
    public String email;

    @SerializedName("ever_been_track")
    public int everBeenTrack = 0;

    @SerializedName("firstname")
    @Expose
    public String firstName;

    @Expose
    public String gender = "Male";

    @SerializedName("lastname")
    @Expose
    public String lastName;

    @Expose
    public String password;

    @Expose
    public String phone;

    @SerializedName("race_license")
    public int raceLicense = 0;

    @SerializedName("skill_level")
    public String skillLevel;

    @SerializedName("subscribe_for_discounts")
    public int subscribeForDiscounts;

    public SignUpRequest() {

    }

    protected SignUpRequest(Parcel in) {
        accept = in.readInt();
        confirmEmail = in.readString();
        confirmPassword = in.readString();
        dob = in.readString();
        email = in.readString();
        everBeenTrack = in.readInt();
        firstName = in.readString();
        gender = in.readString();
        lastName = in.readString();
        password = in.readString();
        phone = in.readString();
        raceLicense = in.readInt();
        skillLevel = in.readString();
        subscribeForDiscounts = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(accept);
        dest.writeString(confirmEmail);
        dest.writeString(confirmPassword);
        dest.writeString(dob);
        dest.writeString(email);
        dest.writeInt(everBeenTrack);
        dest.writeString(firstName);
        dest.writeString(gender);
        dest.writeString(lastName);
        dest.writeString(password);
        dest.writeString(phone);
        dest.writeInt(raceLicense);
        dest.writeString(skillLevel);
        dest.writeInt(subscribeForDiscounts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignUpRequest> CREATOR = new Creator<SignUpRequest>() {
        @Override
        public SignUpRequest createFromParcel(Parcel in) {
            return new SignUpRequest(in);
        }

        @Override
        public SignUpRequest[] newArray(int size) {
            return new SignUpRequest[size];
        }
    };

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEverBeenTrack() {
        return everBeenTrack;
    }

    public void setEverBeenTrack(int everBeenTrack) {
        this.everBeenTrack = everBeenTrack;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRaceLicense() {
        return raceLicense;
    }

    public void setRaceLicense(int raceLicense) {
        this.raceLicense = raceLicense;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getSubscribeForDiscounts() {
        return subscribeForDiscounts;
    }

    public void setSubscribeForDiscounts(int subscribeForDiscounts) {
        this.subscribeForDiscounts = subscribeForDiscounts;
    }

}
