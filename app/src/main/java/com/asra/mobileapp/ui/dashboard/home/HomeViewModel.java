package com.asra.mobileapp.ui.dashboard.home;

import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.CreditHistory;
import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.model.PolicyAgreement;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.SharedPrefKeys;
import com.asra.mobileapp.util.SharedPrefsHelper;
import com.asra.mobileapp.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class HomeViewModel extends ShoppeViewModel {

    public SingleLiveEvent<Boolean> canSwicthModuleObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<UserDetails> userDetailsObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<String> userDetailsErrorObserver = new SingleLiveEvent<>();

    public SingleLiveEvent<EnrolledEvent> upcomingEventObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<EnrolledEvent> pastEventObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<Integer> pastEventCountObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<Integer> upcomingEventCountObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<Integer> allEventCountObserver = new SingleLiveEvent<>();

    public SingleLiveEvent<String> creditListErrorObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> refernalInviationObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<CreditHistory> creditHistoryObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> myDutiesObservable = new SingleLiveEvent();


    public SingleLiveEvent<PolicyAgreement> policyNotAcceptedObservable = new SingleLiveEvent();
    public SingleLiveEvent<String> policyCheckFailed = new SingleLiveEvent();
    public SingleLiveEvent<String> membershipStatusObservable = new SingleLiveEvent();

    private MemberUseCase memberUseCase;
    private int apiCount = 0;

    List<EnrolledEvent> pastEvents;
    List<EnrolledEvent> upComingEvents;
    List<EnrolledEvent> allEvents;

    boolean termsAgreementCheckingDone = false;

    private SharedPrefsHelper sharedPrefsHelper;
    @Inject
    public HomeViewModel(CartUseCase cartUseCase,
                         AppEngine appEngine,
                         ResourceFetcher resourceFetcher,
                         MemberUseCase memberUseCase,
                         SharedPrefsHelper sharedPrefsHelper) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
        this.sharedPrefsHelper = sharedPrefsHelper;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();



        if(!termsAgreementCheckingDone) {
            if (!sharedPrefsHelper.get(SharedPrefKeys.KEY_AGREEMENT_STATUS, false)) {
                checkPolicyAccepted();
            }
        }

        if (!appEngine.getDeviceTokenSyncStatus()) {
            updateDeviceToken(appEngine.getDeviceToken());
        }else{
            Timber.i("HV - Device Token already synced!");
        }
    }


    private void checkPolicyAccepted(){
        addDisposable(memberUseCase.checkPolicyAccepted()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPolicyAcceptanceStatus,
                        throwable -> onPolicyAcceptanceStatusCheckFailed(throwable.getMessage())));
    }

    private void postMembershipStatus(UserDetails user){
        String status = "ACTIVE (" + StringUtils.toTitleCase(user.role) + ")";
        if(DateUtils.isBeforeToday(user.membershipExpDate, DateUtils.SIMPLE_DATE_FORMAT)){
            status = "INACTIVE";
        }

        membershipStatusObservable.postValue(status);
    }
    private void onPolicyAcceptanceStatus(PolicyAgreement policyAgreement){

        if(!termsAgreementCheckingDone) {
            policyNotAcceptedObservable.postValue(policyAgreement);
            Timber.i("User Agreement Status posted");
        }
        termsAgreementCheckingDone = true;
    }

    private void onPolicyAcceptanceStatusCheckFailed(String message){
        Timber.e(message);
        policyCheckFailed.postValue(getConfigString(MessageProvider.error_generic_message));
    }

    private void updateDeviceToken(String deviceToken) {
        if (StringUtils.isEmpty(deviceToken)) {
            Timber.e("FCM Error! Device Token not generated");
        } else {
            addDisposable(memberUseCase.updateDeviceToken(deviceToken, "Android")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> Timber.i("Device Token updated"),
                            throwable -> Timber.e(throwable, "Device Token sync failed")));
        }
    }

    /*
    private void getCachedUserDetails() {
        UserDetails cachedUserDetails = appEngine.getCachedUserDetails();
        if (cachedUserDetails != null) {
            postUserDetails(cachedUserDetails);
        }
    }

     */

    public void updateMenu() {

        canSwicthModuleObserver.postValue(appEngine.hasAdminPrevilege());
    }

    public void switchToAdminModule() {
        if (appEngine.isUserAdmin()) {
            exitActivityObservable.postValue(true);
        } else {
            switchModuleObservable.postValue(true);
        }
    }

    public void loadUserDetails() {

        showProgressBar();
        apiCount++;

        Disposable disposable = memberUseCase.getUserDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUserDetailsFetched,
                        this::onFetchingUserDetailsFailed);
        addDisposable(disposable);
    }

    private void onUserDetailsFetched(UserDetails userDetails) {
        apiCount--;
        hideProgressBar();
        postUserDetails(userDetails);
    }

    private void postUserDetails(UserDetails userDetails) {
        userDetailsObserver.postValue(userDetails);
        postMembershipStatus(userDetails);
    }

    private void onFetchingUserDetailsFailed(Throwable throwable) {
        apiCount--;
        hideProgressBar();
        userDetailsErrorObserver.postValue(throwable.getMessage());
    }

    public void loadEventHistory() {

        int upcomingEventCount = ListUtils.getListSize(upComingEvents);
        int pastEventCount = ListUtils.getListSize(upComingEvents);



        if (upcomingEventCount == 0 || pastEventCount == 0) {
            apiCount++;
            showProgressBar();
            Disposable disposable = memberUseCase.getEventsHistory()
                    .map(eventHistories -> {
                        List<EnrolledEvent> sortedList = new ArrayList<>(eventHistories);
                        Collections.sort(sortedList, (eventHistory, t1) ->
                                eventHistory.eventDate.compareTo(t1.eventDate));
                        return sortedList;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onEventHistoryFetched,
                            this::onFetchingEventHistoryFailed);
            addDisposable(disposable);
        } else {

            if(ListUtils.isEmpty(upComingEvents)) {
                upcomingEventCountObserver.postValue(0);

            }else{
                upcomingEventObserver.postValue(upComingEvents.get(0));
                upcomingEventCountObserver.postValue(upcomingEventCount);
            }

            if(ListUtils.isEmpty(pastEvents)){
                pastEventCountObserver.postValue(0);
            }else {
                pastEventObserver.postValue(pastEvents.get(0));
                pastEventCountObserver.postValue(pastEventCount);
            }

            allEventCountObserver.postValue(ListUtils.getListSize(allEvents));


        }
    }

    private void onFetchingEventHistoryFailed(Throwable throwable) {
        apiCount--;
        hideProgressBar();

        upcomingEventCountObserver.postValue(0);
        pastEventCountObserver.postValue(0);
    }

    private void onEventHistoryFetched(List<EnrolledEvent> eventHistories) {
        apiCount--;
        hideProgressBar();


        allEventCountObserver.postValue(eventHistories.size());
        this.allEvents = eventHistories;

        String today = DateUtils.getToday(DateUtils.YYYY_MM_DD_DATE_PATTERN);
        List<EnrolledEvent> upComingHistory = eventHistories.stream()
                .filter(event -> event.eventDate.compareTo(today) >= 0)
                .collect(Collectors.toList());
        if (!ListUtils.isEmpty(upComingHistory)) {
            upcomingEventObserver.postValue(upComingHistory.get(0));
            upcomingEventCountObserver.postValue(upComingHistory.size());
        } else {
            upcomingEventCountObserver.postValue(0);
        }
        this.upComingEvents = upComingHistory;

        List<EnrolledEvent> pastHistory = eventHistories.stream()
                .filter(event -> event.eventDate.compareTo(today) < 0)
                .collect(Collectors.toList());
        if (!ListUtils.isEmpty(pastHistory)) {
            pastEventCountObserver.postValue(pastHistory.size());
            pastEventObserver.postValue(pastHistory.get(0));
        } else {
            pastEventCountObserver.postValue(0);
        }
        this.pastEvents = pastHistory;

    }

    @Override
    public void hideProgressBar() {
        if (apiCount <= 0)
            super.hideProgressBar();
    }

    public void loadCreditHistory() {

        CreditHistory cachedCreditHistory = creditHistoryObserver.getValue();
        if (cachedCreditHistory == null) {

            apiCount++;
            showProgressBar();
            Disposable disposable = memberUseCase.getCreditHistory()
                    .map(creditHistoryList -> {

                        List<CreditHistory> sortedList = new ArrayList<>(creditHistoryList);
                        Collections.sort(sortedList, (creditHistory, creditHistory2) ->
                                creditHistory.postDate.compareTo(creditHistory2.postDate));
                        return sortedList;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onCreditHistoryFetched,
                            this::onFetchingCreditHistoryFailed);
            addDisposable(disposable);
        } else {
            creditHistoryObserver.postValue(cachedCreditHistory);
        }
    }

    private void onFetchingCreditHistoryFailed(Throwable throwable) {
        apiCount--;
        hideProgressBar();
        creditListErrorObserver.postValue(throwable.getMessage());
    }

    private void onCreditHistoryFetched(List<CreditHistory> creditHistoryList) {
        apiCount--;
        hideProgressBar();
        if (!ListUtils.isEmpty(creditHistoryList)) {
            creditHistoryObserver.postValue(creditHistoryList.get(0));
        } else {
            onFetchingCreditHistoryFailed(new ETApiException("No Credit found"));
        }
    }

    public void invalidate() {
        upComingEvents = null;
        pastEvents = null;
        allEvents = null;


        loadUserDetails();
        loadEventHistory();
        loadCreditHistory();
    }

    public void sendInvitation(String email) {
        memberUseCase.sendReferralInvitation(email);

        showProgressBar();
        Disposable disposable = memberUseCase.sendReferralInvitation(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    hideProgressBar();
                    refernalInviationObservable.postValue(true);
                            },
                        throwable -> {
                            hideProgressBar();
                            showErrorMessage(throwable.getMessage());
                });
        addDisposable(disposable);
    }
}
