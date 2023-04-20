
package com.asra.mobileapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Duty implements Parcelable, Comparable<Duty> {

    @Expose
    private String duty;
    @Expose
    private Boolean status;

    public Duty() {

    }
    protected Duty(Parcel in) {
        duty = in.readString();
        byte tmpStatus = in.readByte();
        status = tmpStatus == 0 ? null : tmpStatus == 1;
    }

    public static final Creator<Duty> CREATOR = new Creator<Duty>() {
        @Override
        public Duty createFromParcel(Parcel in) {
            return new Duty(in);
        }

        @Override
        public Duty[] newArray(int size) {
            return new Duty[size];
        }
    };

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(duty);
        parcel.writeByte((byte) (status == null ? 0 : status ? 1 : 2));
    }

    @Override
    public int compareTo(Duty duty) {
        return (status != duty.getStatus()) ? (status) ? -1 : 1 : 0;
    }
}
