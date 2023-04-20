
package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class WaiverEvent {

    @SerializedName("event_date")
    private String eventDate;
    @SerializedName("event_id")
    private String eventId;
    @SerializedName("event_logo")
    private String eventLogo;
    @SerializedName("event_type")
    private String eventType;
    @SerializedName("full_event_date")
    private String fullEventDate;
    @SerializedName("full_event_logo")
    private String fullEventLogo;
    @Expose
    private String slug;
    @Expose
    private String title;

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

    public String getEventLogo() {
        return eventLogo;
    }

    public void setEventLogo(String eventLogo) {
        this.eventLogo = eventLogo;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFullEventDate() {
        return fullEventDate;
    }

    public void setFullEventDate(String fullEventDate) {
        this.fullEventDate = fullEventDate;
    }

    public String getFullEventLogo() {
        return fullEventLogo;
    }

    public void setFullEventLogo(String fullEventLogo) {
        this.fullEventLogo = fullEventLogo;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
