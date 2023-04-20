package com.asra.mobileapp.ui.admin.eventparticipants;

import com.asra.mobileapp.model.EventParticipant;

import java.util.List;

public interface ParticipantActionListener {

    void onSignIconClicked(EventParticipant item);

    void onDeleteButtonClicked(EventParticipant item);

    void onStarIconClicked(EventParticipant item);

    void onSkillUpgrade(EventParticipant item);

    void onMotoIconClicked(EventParticipant item);

    void onFilterApplied(List<EventParticipant> filteredList, String query);

}
