package com.asra.mobileapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckoutData implements Parcelable {
    public double appliedCouponDiscount;
    public double cartListTotal;
    public double dueAmount;
    public double walletBalance = 0d;

    public String couponCode;
    public double couponBalance;
    public boolean walletEnabled;
    public int cartCount;

    public String orderNumber;

    public CheckoutData(){

    }

    protected CheckoutData(Parcel in) {
        appliedCouponDiscount = in.readDouble();
        cartListTotal = in.readDouble();
        dueAmount = in.readDouble();
        walletBalance = in.readDouble();
        couponCode = in.readString();
        couponBalance = in.readDouble();
        walletEnabled = in.readByte() != 0;
        cartCount = in.readInt();
        orderNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(appliedCouponDiscount);
        dest.writeDouble(cartListTotal);
        dest.writeDouble(dueAmount);
        dest.writeDouble(walletBalance);
        dest.writeString(couponCode);
        dest.writeDouble(couponBalance);
        dest.writeByte((byte) (walletEnabled ? 1 : 0));
        dest.writeInt(cartCount);
        dest.writeString(orderNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckoutData> CREATOR = new Creator<CheckoutData>() {
        @Override
        public CheckoutData createFromParcel(Parcel in) {
            return new CheckoutData(in);
        }

        @Override
        public CheckoutData[] newArray(int size) {
            return new CheckoutData[size];
        }
    };
}
