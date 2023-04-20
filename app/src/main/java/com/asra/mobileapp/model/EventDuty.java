
package com.asra.mobileapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EventDuty implements Parcelable {

    public EventDuty(){

    }

    @Expose
    private List<Duty> duties;

    @Expose
    private String event;

    @SerializedName("event_date")
    private String eventDate;

    @SerializedName("event_id")
    private String eventId;

    @SerializedName("event_type")
    private String eventType;

    protected EventDuty(Parcel in) {
        duties = in.createTypedArrayList(Duty.CREATOR);
        event = in.readString();
        eventDate = in.readString();
        eventId = in.readString();
        eventType = in.readString();
    }

    public static final Creator<EventDuty> CREATOR = new Creator<EventDuty>() {
        @Override
        public EventDuty createFromParcel(Parcel in) {
            return new EventDuty(in);
        }

        @Override
        public EventDuty[] newArray(int size) {
            return new EventDuty[size];
        }
    };

    public List<Duty> getDuties() {
        return duties;
    }

    public void setDuties(List<Duty> duties) {
        this.duties = duties;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(duties);
        parcel.writeString(event);
        parcel.writeString(eventDate);
        parcel.writeString(eventId);
        parcel.writeString(eventType);
    }
}
