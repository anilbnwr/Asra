package com.asra.mobileapp.ui.admin.completedevents;

import com.asra.mobileapp.model.CompletedEvent;

import java.util.List;

public interface CompletedEventsListener {

    void viewParticipants(CompletedEvent item);
    void viewDuties(CompletedEvent item);
    void onFilter(List<CompletedEvent> filteredList, String query);

}
