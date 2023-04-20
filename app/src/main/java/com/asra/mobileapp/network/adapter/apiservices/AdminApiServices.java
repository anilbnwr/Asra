package com.asra.mobileapp.network.adapter.apiservices;

import com.asra.mobileapp.model.CompletedEvent;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.network.retrofit.request.DeleteEventRequest;
import com.asra.mobileapp.network.retrofit.response.CoachDutyResponse;
import com.asra.mobileapp.network.retrofit.response.ETResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface AdminApiServices {

    Single<List<CompletedEvent>> getCompletedEvents(String userId);

    Single<List<EventParticipant>> getEventParticipants(String eventId);

    Single<ETResponse> DeleteEventApi(DeleteEventRequest eventRequest);

    Single<List<EventParticipant>> getDutyAssignedStaffs(String eventId);

    Completable saveSignature(String signatureImage, String signatureId);

    Single<String> getSignature(String signatureId);

    Single<CoachDutyResponse.DutyTypes> getEventDutyList(String userId);

    Completable upgradeSkillLevel(String newSkill, String userId);
}
