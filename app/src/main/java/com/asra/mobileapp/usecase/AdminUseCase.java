package com.asra.mobileapp.usecase;

import android.graphics.Bitmap;

import com.asra.mobileapp.common.Constants;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.CompletedEvent;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.network.adapter.apiservices.AdminApiServices;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveAdminApiServices;
import com.asra.mobileapp.network.retrofit.request.DeleteEventRequest;
import com.asra.mobileapp.network.retrofit.response.CoachDutyResponse;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.util.StringUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AdminUseCase extends BaseUseCase {

    private EvolveAdminApiServices evolveAdminApiServices;


    @Inject
    public AdminUseCase(AppEngine appEngine,
                        EvolveAdminApiServices evolveAdminApiServices){
        super(appEngine);
        this.evolveAdminApiServices = evolveAdminApiServices;

    }

    private AdminApiServices getAdminApiServices(){
        return evolveAdminApiServices;
    }

    public Single<List<CompletedEvent>> getCompletedEvents(String userId) {

        return getAdminApiServices().getCompletedEvents(userId);
    }

    public Single<List<EventParticipant>> getEventParticipants(String eventId, int dataType) {

        if(dataType == Constants.KEY_DATA_USERS) {
            return getAdminApiServices().getEventParticipants(eventId);
        }else{
            return getAdminApiServices().getDutyAssignedStaffs(eventId);
        }
    }
    public Single<ETResponse> getDeleteEvent(DeleteEventRequest deleteEventRequest) {
        return getAdminApiServices().DeleteEventApi(deleteEventRequest);
    }

    public Completable saveSignature(Bitmap signature, String signatureId) {

        return Single.just(signature).map(processingSignature -> {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    signature, 500, 500, false);
            return StringUtils.bitMapToString(resizedBitmap);
        }).flatMapCompletable(signatureText -> getAdminApiServices().saveSignature(signatureText, signatureId));

    }

    public Single<String> getSignature(String signatureId) {

        return getAdminApiServices().getSignature(signatureId);
    }

    public Single<CoachDutyResponse.DutyTypes> getDutyList() {
        return getAdminApiServices().getEventDutyList(appEngine.getUserId());
    }

    public Completable upgradeSkillLevel(String skill, String userId){
        return getAdminApiServices().upgradeSkillLevel(skill, userId);
    }
}
