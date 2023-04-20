package com.asra.mobileapp.ui.admin.eventparticipants;

import android.text.TextUtils;

import com.asra.mobileapp.common.Constants;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.AdminDuty;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.network.retrofit.request.DeleteEventRequest;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.AdminUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventParticipantViewModel extends BaseViewModel {

    public SingleLiveEvent<List<EventParticipant>> eventParticipantsObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> errorObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<ETResponse> deleteApiObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> skillLevelMenuObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> trainingLevelMenuObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> rentalLevelMenuObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> classLevelMenuObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> notSignedUsersMenuObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> racersMenuObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> dutiesMenuObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> skillUpgradedObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<List<String>> filterBySkillLevelObservable = new SingleLiveEvent();
    public SingleLiveEvent<List<String>> filterByTrainingObservable = new SingleLiveEvent();
    public SingleLiveEvent<List<String>> filterByRentalsObservable = new SingleLiveEvent();
    public SingleLiveEvent<List<String>> filterByClassesObservable = new SingleLiveEvent();
    public SingleLiveEvent<List<String>> filterByDutiesObservable = new SingleLiveEvent();


    private List<String> skillLevelList;
    private List<String> trainingList;
    private List<String> rentalList;
    private List<String> classList;
    private List<String> dutyList;

    private AdminUseCase adminUseCase;
    private String eventId;
    private int dataType = Constants.KEY_DATA_USERS;

    @Inject
    public EventParticipantViewModel(AdminUseCase adminUseCase,
                                     AppEngine appEngine,
                                     ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.adminUseCase = adminUseCase;

        skillLevelList = new ArrayList<>();
        trainingList = new ArrayList<>();
        rentalList = new ArrayList<>();
        classList = new ArrayList<>();
        dutyList = new ArrayList<>();
    }

    public void fetchParticipantsList(String eventId, int dataType) {

        this.eventId = eventId;
        this.dataType = dataType;
        showProgressBar();
        Disposable disposable = adminUseCase.getEventParticipants(eventId, dataType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onParticipantListFetched,
                        this::onError);
        addDisposable(disposable);
    }

    public void deleteEvent(DeleteEventRequest deleteEventRequest) {

        deleteEventRequest.userId = appEngine.getUserId();

        showProgressBar();
        Disposable disposable = adminUseCase.getDeleteEvent(deleteEventRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDeleteEventFetched,
                        this::onError);
        addDisposable(disposable);
    }

    private void onError(Throwable throwable) {
        hideProgressBar();
        errorObservable.postValue(throwable.getMessage());
    }

    private void onDeleteEventFetched(ETResponse etResponse) {
        hideProgressBar();
        if(etResponse != null){
            deleteApiObservable.postValue(etResponse);
        }else{
//            deleteApiObservable.postValue(resourceFetcher.getConfigString(MessageProvider.error_generic_message));
//            deleteApiObservable.postValue(etResponse.message);
        }

    }

    private void onParticipantListFetched(List<EventParticipant> eventParticipants) {
        hideProgressBar();

        if(ListUtils.isEmpty(eventParticipants)){
            onError(new ETApiException(getConfigString(MessageProvider.err_no_user_events)));
        }else {

            Comparator<EventParticipant> compareByName = (o1, o2) ->
                    o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());

            Collections.sort(eventParticipants, compareByName);
            prepareSkillLevelList(eventParticipants);
            prepareTrainingList(eventParticipants);
            prepareRentalList(eventParticipants);
            prepareClassList(eventParticipants);
            prepareDutyList(eventParticipants);

            boolean notSignedUserPresent = false;
            for(EventParticipant participant : eventParticipants){
                if(!participant.eWaiver){
                    notSignedUserPresent = true;
                    break;
                }
            }

            boolean racersPresent = false;
            for(EventParticipant participant : eventParticipants){
                if(participant.motoPurchased){
                    racersPresent = true;
                    break;
                }
            }


            if (appEngine.isEvApp()){
                skillLevelMenuObservable.postValue(!ListUtils.isEmpty(skillLevelList));
                trainingLevelMenuObservable.postValue(!ListUtils.isEmpty(trainingList));
                rentalLevelMenuObservable.postValue(!ListUtils.isEmpty(rentalList));
                classLevelMenuObservable.postValue(!ListUtils.isEmpty(classList));
                notSignedUsersMenuObservable.postValue(dataType == Constants.KEY_DATA_USERS &&
                        notSignedUserPresent);
                racersMenuObservable.postValue(racersPresent);
            }else{
                skillLevelMenuObservable.postValue(!ListUtils.isEmpty(skillLevelList));
                classLevelMenuObservable.postValue(!ListUtils.isEmpty(classList));
                trainingLevelMenuObservable.postValue(false);
                rentalLevelMenuObservable.postValue(false);
                notSignedUsersMenuObservable.postValue(false);
                racersMenuObservable.postValue(false);
            }
            dutiesMenuObservable.postValue(!ListUtils.isEmpty(dutyList));


            eventParticipantsObservable.postValue(eventParticipants);
        }
    }

    private void prepareDutyList(List<EventParticipant> eventParticipants) {
        List<String> dutyList = new ArrayList<>();
        for(EventParticipant user: eventParticipants){
            if(!ListUtils.isEmpty(user.duties)) {
                for (AdminDuty adminDuty : user.duties) {
                    if (!TextUtils.isEmpty(adminDuty.getName()))
                        dutyList.add(adminDuty.getName());
                }
            }
            if(!TextUtils.isEmpty(user.jobAssigned)){
                dutyList.add(user.jobAssigned);
            }
        }
        Set<String> tempList = new HashSet<>(dutyList);
        this.dutyList.clear();
        this.dutyList.addAll(tempList);

        Collections.sort(dutyList);
    }

    public void upgradeSkillLevel(EventParticipant user, String skill) {
        showProgressBar(getConfigString(MessageProvider.upgrading_skill_level));
        Disposable disposable = adminUseCase.upgradeSkillLevel(skill, user.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSkillUpgraded,
                        this::onError);
        addDisposable(disposable);
    }

    private void onSkillUpgraded() {
        hideProgressBar();
        fetchParticipantsList(eventId, dataType);
        skillUpgradedObservable.postValue(getConfigString(MessageProvider.skill_level_upgraded_successfully));
    }

    private void prepareSkillLevelList(List<EventParticipant> eventParticipants){

        List<String> skillLevels = new ArrayList<>();
        for(EventParticipant user: eventParticipants){
            if(!TextUtils.isEmpty(user.skillLevel))
                skillLevels.add(user.skillLevel);
        }
        Set<String> tempList = new HashSet<>(skillLevels);
        skillLevelList.clear();
        skillLevelList.addAll(tempList);

        Collections.sort(skillLevelList);

    }



    private void prepareTrainingList(List<EventParticipant> eventParticipants){

        List<String> trainings = new ArrayList<>();
        for(EventParticipant user: eventParticipants){
            if(!ListUtils.isEmpty(user.trainingList))
                trainings.addAll(user.trainingList);
        }
        Set<String> tempList = new HashSet<>(trainings);
        trainingList.clear();
        trainingList.addAll(tempList);

        Collections.sort(trainingList);

    }

    private void prepareRentalList(List<EventParticipant> eventParticipants){

        List<String> rentals = new ArrayList<>();
        for(EventParticipant user: eventParticipants){
            if(!ListUtils.isEmpty(user.rentals))
                for(EventParticipant.Rental rental : user.rentals)
                    rentals.add(rental.name);
        }
        Set<String> tempList = new HashSet<>(rentals);
        rentalList.clear();
        rentalList.addAll(tempList);

        Collections.sort(rentalList);

    }

    private void prepareClassList(List<EventParticipant> eventParticipants){

        List<String> motoClasses = new ArrayList<>();
        for(EventParticipant user: eventParticipants){
            if(!ListUtils.isEmpty(user.motoClasses)) {
                //motoClasses.addAll(user.motoClasses);
                for (EventParticipant.MotoClass motoClass: user.motoClasses){
                    //motoClasses.add(motoClass.raceName);
                    for(EventParticipant.RaceClass race : motoClass.raceClasses){
                        motoClasses.add(race.className);
                    }
                }
            }
        }
        Set<String> tempList = new HashSet<>(motoClasses);
        classList.clear();
        classList.addAll(tempList);

        Collections.sort(classList);

    }

    public void filterBySkillLevel(){
        filterBySkillLevelObservable.postValue(skillLevelList);
    }

    public void filterByTraining(){
        filterByTrainingObservable.postValue(trainingList);
    }

    public void filterByRental(){
        filterByRentalsObservable.postValue(rentalList);
    }


    public void filterByClass() {
        filterByClassesObservable.postValue(classList);
    }


    public void filterByDuties() {
        filterByDutiesObservable.postValue(dutyList);
    }
}
