package com.asra.mobileapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolicyAgreement implements Parcelable {

    @SerializedName("agreed")
    @Expose
    public boolean agreementStatus;

    @SerializedName("terms_html")
    @Expose
    public String content;

    @SerializedName("terms_checkbox")
    @Expose
    public String acceptTermsTitle;

    @SerializedName("terms_button")
    @Expose
    public String agreementButtonTitle;

    protected PolicyAgreement(Parcel in) {
        agreementStatus = in.readByte() != 0;
        content = in.readString();
        acceptTermsTitle = in.readString();
        agreementButtonTitle = in.readString();
    }

    public static final Creator<PolicyAgreement> CREATOR = new Creator<PolicyAgreement>() {
        @Override
        public PolicyAgreement createFromParcel(Parcel in) {
            return new PolicyAgreement(in);
        }

        @Override
        public PolicyAgreement[] newArray(int size) {
            return new PolicyAgreement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (agreementStatus ? 1 : 0));
        parcel.writeString(content);
        parcel.writeString(acceptTermsTitle);
        parcel.writeString(agreementButtonTitle);
    }
}
