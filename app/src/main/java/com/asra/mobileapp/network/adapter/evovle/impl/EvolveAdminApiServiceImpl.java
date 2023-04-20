package com.asra.mobileapp.network.adapter.evovle.impl;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.CompletedEvent;
import com.asra.mobileapp.model.DutyAssignedStaff;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.network.adapter.BaseApiService;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveAdminApiServices;
import com.asra.mobileapp.network.api.EvolveAdminApi;
import com.asra.mobileapp.network.retrofit.request.CoachDutyRequest;
import com.asra.mobileapp.network.retrofit.request.CompletedEventRequest;
import com.asra.mobileapp.network.retrofit.request.DeleteEventRequest;
import com.asra.mobileapp.network.retrofit.request.EventParticipantRequest;
import com.asra.mobileapp.network.retrofit.request.SignatureRequest;
import com.asra.mobileapp.network.retrofit.request.SignatureUploadRequest;
import com.asra.mobileapp.network.retrofit.request.SkillUpgradeRequest;
import com.asra.mobileapp.network.retrofit.response.CoachDutyResponse;
import com.asra.mobileapp.network.retrofit.response.ETResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

public class EvolveAdminApiServiceImpl extends BaseApiService implements EvolveAdminApiServices {

    private EvolveAdminApi apiServices;
    private AppEngine appEngine;

    @Inject
    public EvolveAdminApiServiceImpl(EvolveAdminApi apiServices, AppEngine appEngine) {
        this.apiServices = apiServices;
        this.appEngine = appEngine;
    }


    @Override
    public Single<List<CompletedEvent>> getCompletedEvents(String userId) {
        CompletedEventRequest request = new CompletedEventRequest();
        request.userId = userId;
        request.isMotoEvent = appEngine.isEvApp() ? 0 : 1;
        return apiServices.getCompletedEventList(request).map(response -> {
            checkApiError(response);
            return response.completedEvents;
        }).doOnError(Timber::w);
    }

    @Override
    public Single<List<EventParticipant>> getEventParticipants(String eventId) {

        EventParticipantRequest request = new EventParticipantRequest();
        request.eventId = eventId;

        return apiServices.getEventParticipantList(request).map(response -> {
            checkApiError(response);
            appEngine.generalSkills = response.generalSkills;
            return response.eventParticiapnts;
        }).doOnError(Timber::w);
    }

    @Override
    public Single<ETResponse> DeleteEventApi(DeleteEventRequest eventRequest) {
        DeleteEventRequest request = new DeleteEventRequest(eventRequest.orderId,eventRequest.eventId,eventRequest.skillLevel,eventRequest.userId);
        return apiServices.deleteEvent(request)
                .map(etResponse -> {
                    checkApiError(etResponse);
                    return etResponse;
                }).doOnError(Timber::w);
    }

//    @Override
//    public Single<StampPassword> viewStampPassport(String passportId) {
//        ViewPassportRequest request = new ViewPassportRequest(passportId);
//
//        return apiServices.getStampPassport(request)
//                .map(response -> {
//                    checkApiError(response);
//                    return  response;
//                }).doOnError(Timber::w);
//    }

    @Override
    public Single<List<EventParticipant>> getDutyAssignedStaffs(String eventId) {
        EventParticipantRequest request = new EventParticipantRequest();
        request.eventId = eventId;

        return apiServices.getDutyAssignedStaffs(request)
                .map(response -> {
                    checkApiError(response);
                    return response.staffs;
                }).map(staffs -> {
                    List<EventParticipant> eventParticipantList = new ArrayList<>();
                    for (DutyAssignedStaff staff : staffs) {
                        eventParticipantList.add(EventParticipant.convertFromDutyAssignedStaff(staff));
                    }
                    return eventParticipantList;
                }).doOnError(Timber::w);
    }

    @Override
    public Completable saveSignature(String signatureImage, String signatureId) {

        SignatureUploadRequest request = new SignatureUploadRequest();
        request.signatureId = signatureId;
        request.signature = SignatureUploadRequest.IMAGE_DATA + signatureImage;

        return apiServices.saveSignature(request).map(response -> {
            checkApiError(response);
            return response;
        }).ignoreElement().doOnError(Timber::w);

    }

    @Override
    public Single<String> getSignature(String signatureId) {
        SignatureRequest request = new SignatureRequest();
        request.signatureId = signatureId;

        return apiServices.getSignature(request).map(response -> {
            checkApiError(response);
            return response.signature;
        }).doOnError(Timber::w);
    }

    @Override
    public Single<CoachDutyResponse.DutyTypes> getEventDutyList(String userId) {
        CoachDutyRequest request = new CoachDutyRequest();
        request.userId = userId;
        request.isMoto = !appEngine.isEvApp();

        return apiServices.getDutyList(request).map(response -> {
            checkApiError(response);

            return response.dutyList;
        }).doOnError(Timber::w);
    }

    @Override
    public Completable upgradeSkillLevel(String newSkill, String userId) {

        SkillUpgradeRequest request = new SkillUpgradeRequest();
        request.skillLevel = newSkill;
        request.userId = userId;

        return apiServices.upgradeSkillLevel(request).map(response -> {
            checkApiError(response);
            return response;
        }).ignoreElement().doOnError(Timber::w);
    }
}
