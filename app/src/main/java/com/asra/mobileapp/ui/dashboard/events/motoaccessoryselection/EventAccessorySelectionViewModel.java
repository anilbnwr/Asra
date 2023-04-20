package com.asra.mobileapp.ui.dashboard.events.motoaccessoryselection;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.EventsUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventAccessorySelectionViewModel extends ShoppeViewModel {

    SingleLiveEvent<EventDetails> eventDetailsObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> eventDetailsErrorObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> classTotalObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> errorValidationErrorObservable = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> transponderErrorObservable = new SingleLiveEvent<>();
    SingleLiveEvent<EventDetails.Transponder> transponderObservable = new SingleLiveEvent<>();
    public MutableLiveData<String> eventToCartErrorObservable = new SingleLiveEvent<>();
    public MutableLiveData<EventDetails> eventAddedToCartObservable = new SingleLiveEvent<>();


    private EventDetails eventDetails;
    private double total = 0;
    private boolean isTransponderRented = false;
    private EventsUseCase eventsUseCase;
    private String slug = "";
    @Inject
    public EventAccessorySelectionViewModel(
            EventsUseCase eventsUseCase,
            CartUseCase cartUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.eventsUseCase = eventsUseCase;
    }

    @Override
    public boolean shouldSyncCart() {
        return false;
    }

    public void initWith(EventDetails eventDetails) {
        this.eventDetails = eventDetails;
        this.slug = eventDetails.slug;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();

        getEventDetails(eventDetails.slug, false);

    }

    public void getEventDetails(String slug, boolean isEvEvent) {

        showProgressBar();
        Disposable disposable = eventsUseCase.getEventDetails(slug, isEvEvent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventDetailsFetched,
                        this::onFetchingEventDetailsFailed);
        addDisposable(disposable);
    }

    private void onFetchingEventDetailsFailed(Throwable throwable) {
        hideProgressBar();
        eventDetailsErrorObservable.postValue(throwable.getMessage());
    }

    private void onEventDetailsFetched(EventDetails eventDetails) {
        hideProgressBar();
        this.eventDetails = eventDetails;
        this.eventDetails.registeredSkill = eventDetails.racerStatus;
        this.eventDetails.slug = slug;
        eventDetailsObservable.postValue(eventDetails);
        updateClassTotal();


        if(!eventDetails.canAddMotoEventToCart()){

            if(eventDetails.registrationClosed){
                errorValidationErrorObservable.postValue(getString(R.string.registration_closed));
            }else if (!eventDetails.skillEligible){
                String message = resourceFetcher.getConfigString(MessageProvider.message_skill_not_eligible);
                errorValidationErrorObservable.postValue(message);

            }else if (!eventDetails.trackValidation){
                errorValidationErrorObservable.postValue("It looks like your track day has been removed from the cart.");
            }else if(!eventDetails.mrlValidation){
                errorValidationErrorObservable.postValue("MRL membership has been expired");

            }else{
                String message = resourceFetcher.getConfigString(MessageProvider.error_generic_message);
                errorValidationErrorObservable.postValue(message);
            }

        }

    }

    /*
    private void checkSkillSelection() {
        for(EventDetails.SkillLevel skill : eventDetails.skillLevels){
            if(skill.active) {
                hasSkillSelected = true;
                skill.isSelected = true;
                break;
            }
        }
    }

     */



    public void onClassSelectionChanged(EventDetails.Race race,EventDetails.RaceClass raceClass, boolean isSelected) {
        raceClass.checked = isSelected;

        if(race.hasSpecialClass() && !raceClass.specialCase){
            race.validateSpecialCase(raceClass);
        }
        updateClassTotal();
        eventDetailsObservable.postValue(eventDetails);

    }

    private void updateClassTotal() {
       total = 0;

        for(EventDetails.Race eventClass : eventDetails.eventClasses){
                total += eventClass.getSelectedClassPrice();
        }
        classTotalObservable.postValue("Total : " + StringUtils.formatAmount(total));
    }

    public void onAddToCart(String bikeNumber, String transponderNumber) {

        boolean isValid = true;
        for(EventDetails.Race eventClass : eventDetails.eventClasses){
            isValid = isValid && eventClass.validateRaceClasses();
        }

        if(!isValid){
            eventDetailsObservable.postValue(eventDetails);
        }else if(!eventDetails.hasActiveClass()){
            eventToCartErrorObservable.postValue("Please select at least one class");

        }else if(StringUtils.isEmpty(transponderNumber)){
            eventToCartErrorObservable.postValue("Please enter your transponder number.");

        }else if(StringUtils.isEmpty(bikeNumber)){
            eventToCartErrorObservable.postValue("Please enter your bike number.");

        }else if(StringUtils.isEmpty(eventDetails.racerStatus)){
            showErrorMessage(getConfigString(MessageProvider.error_no_skill_set));
        }else{
            addMotoEventToCart(bikeNumber, transponderNumber);

        }
        /*
        if(!eventDetails.hasActiveClass()){
            showErrorMessage(getConfigString(MessageProvider.error_no_active_class));
        }else if(!hasSkillSelected){
            showErrorMessage(getConfigString(MessageProvider.error_no_skill_set));
        }else if(!isTransponderRented && StringUtils.isEmpty(transponderNumber)){
            showErrorMessage(getConfigString(MessageProvider.error_no_transponder));
        }else{
            addMotoEventToCart(transponderNumber);
        }*/
    }

    private void addMotoEventToCart(String bikeNumber, String transponderNumber) {
        showProgressBar(getConfigString(MessageProvider.msg_add_event_to_cart));

        eventDetails.classTotal = total;
        eventDetails.bikeNumber = bikeNumber;
        eventDetails.transponderRented = isTransponderRented;

        eventDetails.transponderNo = transponderNumber;
        Disposable disposable = cartUseCase.addEventToCart(eventDetails)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAddedToCart,
                        this::onAddingEventToCartFailed);
        addDisposable(disposable);
    }

    private void onAddingEventToCartFailed(Throwable throwable) {
        hideProgressBar();
        eventToCartErrorObservable.postValue(throwable.getMessage());
    }

    private void onEventAddedToCart() {
        hideProgressBar();

        eventAddedToCartObservable.postValue(eventDetails);
        updateCartCountBy(1);
    }

    public void onSkillSelectionChanged(String racerStatus) {
        eventDetails.racerStatus = racerStatus;
    }

    public void onTransponderRentalChecked(boolean checked) {
        isTransponderRented = checked;
    }

    public void addTrackToCart(Event event) {

        showProgressBar(getConfigString(MessageProvider.msg_add_event_to_cart));
        Disposable disposable = cartUseCase.addEventToCart(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventAddedToCart,
                        this::onAddingEventToCartFailed);
        addDisposable(disposable);
    }


    public void setTransponderNumber(String transponderNumber){
        eventDetails.transponderNo = transponderNumber;
    }

    public void setBikeNumber(String bikeNumber){
        eventDetails.bikeNumber = bikeNumber;
    }
}
