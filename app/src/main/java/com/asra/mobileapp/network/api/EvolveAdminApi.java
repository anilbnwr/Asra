package com.asra.mobileapp.network.api;

import com.asra.mobileapp.network.retrofit.request.CoachDutyRequest;
import com.asra.mobileapp.network.retrofit.request.CompletedEventRequest;
import com.asra.mobileapp.network.retrofit.request.DeleteEventRequest;
import com.asra.mobileapp.network.retrofit.request.EventParticipantRequest;
import com.asra.mobileapp.network.retrofit.request.SignatureRequest;
import com.asra.mobileapp.network.retrofit.request.SignatureUploadRequest;
import com.asra.mobileapp.network.retrofit.request.SkillUpgradeRequest;
import com.asra.mobileapp.network.retrofit.response.CoachDutyResponse;
import com.asra.mobileapp.network.retrofit.response.CompletedEventsResponse;
import com.asra.mobileapp.network.retrofit.response.DutyAssignedStaffResponse;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.network.retrofit.response.EventParticipantResponse;
import com.asra.mobileapp.network.retrofit.response.SignatureResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EvolveAdminApi  {

    @POST("/evolve-api/public/app/v3/admin/completedEvents")
    Single<CompletedEventsResponse> getCompletedEventList(@Body CompletedEventRequest eventRequest);

    @POST("/evolve-api/public/app/v3/admin/eventParticipants")
    Single<EventParticipantResponse> getEventParticipantList(@Body EventParticipantRequest request);

    @POST("/evolve-api/public/app/v3/admin/getSignature")
    Single<SignatureResponse> getSignature(@Body SignatureRequest request);

    @POST("/evolve-api/public/app/v3/admin/cancelEventParticipant")
    Single<ETResponse> deleteEvent(@Body DeleteEventRequest request);

    @POST("/evolve-api/public/app/v3/admin/updateSignature")
    Single<ETResponse> saveSignature(@Body SignatureUploadRequest request);

    @POST("/evolve-api/public/app/v3/coach/duties")
    Single<CoachDutyResponse> getDutyList(@Body CoachDutyRequest request);

    @POST("/evolve-api/public/app/v3/admin/updateSkillLevel")
    Single<ETResponse> upgradeSkillLevel(@Body SkillUpgradeRequest request);

    @POST("/evolve-api/public/app/v3/admin/eventParticipants/jobAssignments")
    Single<DutyAssignedStaffResponse> getDutyAssignedStaffs(@Body EventParticipantRequest request);

}
