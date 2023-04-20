package com.asra.mobileapp.ui.dashboard.events.eventdetails;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.ui.base.model.DialogData;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.EventsUseCase;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventDetailsViewModel extends ShoppeViewModel {

    public SingleLiveEvent<EventDetails> eventDetailsObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> eventDetailsErrorObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> totalAmountObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> userRoleObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> userRolePriceObservable = new SingleLiveEvent<>();

    public MutableLiveData<String> eventToCartErrorObservable = new SingleLiveEvent<>();
    public MutableLiveData<EventDetails> eventAddedToCartObservable = new SingleLiveEvent<>();
    public MutableLiveData<Boolean> eventCancelObservable = new SingleLiveEvent<>();
    public MutableLiveData<Boolean> applyThemeObservable = new SingleLiveEvent<>();

    public MutableLiveData<DialogData> userNotEligibleObservable = new SingleLiveEvent<>();
    public MutableLiveData<EventDetails> eventAccessoriesObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> privateEventCodeObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<EventDetails.MrlData> mrlPurchaseRequired = new SingleLiveEvent<>();
    public SingleLiveEvent<String> registrClosedObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<List<Event>> trackPurchaseRequired = new SingleLiveEvent<>();


    public SingleLiveEvent<String> membershipAddedToCartObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> membershipCartErrorObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> trackDayAddedToCartObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> trackDayErrorObservable = new SingleLiveEvent<>();


    private EventsUseCase eventsUseCase;
    private EventDetails eventDetails;

    private String slug = "";

    private boolean motoEventValidation = false;
    private boolean trackDayPurchase = false;
    private boolean mrlPurchase = false;

    @Inject
    public EventDetailsViewModel(EventsUseCase eventsUseCase,
                                 CartUseCase cartUseCase,
                                 MemberUseCase memberUseCase,
                                 AppEngine appEngine,
                                 ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.eventsUseCase = eventsUseCase;
        motoEventValidation = false;
    }

    public void getEventDetails(String slug, boolean isEvEvent) {

        this.slug = slug;
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
        eventDetailsObservable.setValue(eventDetails);
        updateRoleBasedPrice(eventDetails);
        updateTotal(eventDetails);
        eventCancelObservable.postValue(eventDetails.isCancelled);
        applyThemeObservable.postValue(shouldApplyTheme(eventDetails));

        if(motoEventValidation){

            if(trackDayPurchase)
            successMessageObservable.postValue("Track day has been added to cart successfully.");
            else if (mrlPurchase)
                successMessageObservable.postValue("MRL has been added to cart successfully.");

            motoEventValidation = false;
            onAddToCart();

            mrlPurchase = false;
            trackDayPurchase = false;
        }
    }

    public void onAddToCart(){

        if(eventDetails.externalEventHost != null){
            openWebPage(eventDetails.externalEventHost .externalUrl);
            return;
        }
        if(eventDetails.isPrivateEvent &&
                StringUtils.isEmpty(eventDetails.couponCode)){
            privateEventCodeObservable.postValue(appEngine.isUserLoggedIn());
            return;
        }

        if(!eventDetails.isMotoEvent()){
            addEventToCart();
        }else{

            if(eventDetails.canAddMotoEventToCart()){
                eventDetails.slug = slug;
                eventAccessoriesObservable.postValue(eventDetails);
            }else{
                DialogData dialogData = new DialogData();
                dialogData.positiveButton = getString(R.string.et_ok);
                if(eventDetails.registrationClosed){
                    registrClosedObservable.postValue(getString(R.string.registration_closed));
                }else if (!eventDetails.skillEligible){
                    dialogData.message = resourceFetcher.getConfigString(MessageProvider.message_skill_not_eligible);
                    dialogData.title = getString(R.string.title_skill_not_eligible);
                    userNotEligibleObservable.postValue(dialogData);

                }else if (!eventDetails.trackValidation){
                    trackPurchaseRequired.postValue(eventDetails.trackDayEvents);
                }else if(!eventDetails.mrlValidation){
                        eventDetails.mrlData.messageContent = eventDetails.mrlHTML;
                        eventDetails.mrlData.canAddToCart = eventDetails.mrlAddToCart;
                        mrlPurchaseRequired.postValue(eventDetails.mrlData);

                }/*else if(!eventDetails.hasRaceLicense) {
                    dialogData.message = resourceFetcher.readFileFromRawDirectory(R.raw.race_licence);
                    dialogData.title = getString(R.string.title_race_licence_required);
                    userNotEligibleObservable.postValue(dialogData);

                }*/
            }
        }
    }


    private void addEventToCart() {

        if(eventDetails != null) {
            showProgressBar(getConfigString(MessageProvider.msg_add_event_to_cart));

            Disposable disposable = cartUseCase.addEventToCart(eventDetails)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onEventAddedToCart,
                            this::onAddingEventToCartFailed);
            addDisposable(disposable);
        }
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


    public void updateTotal(EventDetails eventDetail) {

        if (eventDetail != null) {
            double total = AppUtils.getDouble(eventDetail.price);
            if (AppUtils.isTrue(eventDetail.isRoleBasedPricing) && eventDetail.roleBasedPrice != null) {
                total = AppUtils.getDouble(eventDetail.roleBasedPrice.getRoleBasedPrice(
                        appEngine.getUserRole()));
            }
            if (!ListUtils.isEmpty(eventDetail.rentalData)) {
                for (EventDetails.RentalDatum rental : eventDetail.rentalData) {
                    if (rental.selectedVariant != null && rental.selected) {
                        total += AppUtils.getDouble(rental.selectedVariant.price);
                    }
                }
            }

            if (!ListUtils.isEmpty(eventDetail.trainingData)) {
                for (EventDetails.TrainingDatum training : eventDetail.trainingData) {
                    if (training.selected) {
                        total += AppUtils.getDouble(training.price);
                    }
                }
            }

            totalAmountObservable.postValue(StringUtils.formatAmount(total));
        }
    }
    private void updateRoleBasedPrice(EventDetails eventDetail){
        if(eventDetail != null) {
            String role = appEngine.getUserRole();
            userRoleObservable.postValue(AppUtils.toTitleCase(role) + ": ");
            userRolePriceObservable.postValue( StringUtils.formatAmount(AppUtils.getDouble(eventDetail.price)));

            if (AppUtils.isTrue(eventDetail.isRoleBasedPricing) && eventDetail.roleBasedPrice != null) {
                double roleTotal = AppUtils.getDouble(eventDetail.roleBasedPrice.getRoleBasedPrice(
                        appEngine.getUserRole()));
                userRolePriceObservable.postValue(StringUtils.formatAmount(roleTotal));
            }
        }
    }

    private boolean shouldApplyTheme(EventDetails eventDetails){
        return appEngine.isEvApp() || eventDetails.isMotoEvent();
    }
    public void onRentalVariantSelected() {
        updateTotal(eventDetails);
    }

    public void onSecretCode(String userInput) {
        eventDetails.couponCode = userInput;
        onAddToCart();
    }

    public void onPurchase(EventDetails.MrlData mrlData) {
        mrlPurchase = true;
        showProgressBar();
        Membership membership = new Membership();
        membership.setImage(mrlData.image);
        membership.setTitle(mrlData.title);
        membership.setPrice(mrlData.price);
        membership.setMembershipId(mrlData.membershipId);
        membership.setSlug(mrlData.slug);
        membership.setSeason(mrlData.season);

        Disposable disposable = cartUseCase.addMembershipToCart(membership)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            hideProgressBar();
                            motoEventValidation = true;
                            getEventDetails(slug, false);
                            //membershipAddedToCartObservable.postValue(getConfigString(MessageProvider.msg_cart_membership_added));
                        },
                        throwable -> {
                            hideProgressBar();
                            membershipCartErrorObservable.postValue(throwable.getMessage());
                        });
        addDisposable(disposable);
    }


    public void addTrackDay(Event event) {

        trackDayPurchase = true;
        showProgressBar();
        Disposable disposable = cartUseCase.addTrackEventToCart(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            hideProgressBar();

                            //trackDayAddedToCartObservable.postValue(getConfigString(MessageProvider.msg_cart_event_added));
                            motoEventValidation = true;
                            getEventDetails(slug, false);

                        },
                        throwable -> {
                            hideProgressBar();
                            errorMessageObservable.postValue(throwable.getMessage());
                        });
        addDisposable(disposable);
    }
}
