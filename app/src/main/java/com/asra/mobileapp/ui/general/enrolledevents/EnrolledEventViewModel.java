package com.asra.mobileapp.ui.general.enrolledevents;

import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EnrolledEventViewModel extends BaseViewModel {

    private MemberUseCase memberUseCase;
    public SingleLiveEvent<List<EnrolledEvent>> eventHistoryObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> eventHistoryStatusObserver = new SingleLiveEvent<>();

    public SingleLiveEvent<String> eventHistoryErrorObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> canCancelEventsObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> uploadSelfieObserver = new SingleLiveEvent<>();

    private List<EnrolledEvent> pastEvents = new ArrayList<>();
    private List<EnrolledEvent> upcomingEvents = new ArrayList<>();
    private List<EnrolledEvent> allEvents = new ArrayList<>();
    private int type;

    private boolean eventCancelling = false;
    @Inject
    public EnrolledEventViewModel(MemberUseCase memberUseCase,
                                  AppEngine appEngine,
                                  ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();

        canCancelEventsObserver.postValue(appEngine.canCancelEvents());
        fetchEventsHistory();
    }

    private boolean shouldFetchEventHistory(){
        return ListUtils.isEmpty(allEvents);
    }

    private void fetchEventsHistory(){
        if(!eventCancelling)
            showProgressBar(getLoadingMessage());

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
    }

    private void onFetchingEventHistoryFailed(Throwable throwable) {
        hideProgressBar();
        eventHistoryErrorObserver.postValue(throwable.getMessage());
    }

    private void onEventHistoryFetched(List<EnrolledEvent> eventHistories) {
        hideProgressBar();
        eventHistoryStatusObserver.postValue(true);

        String today = DateUtils.getToday(DateUtils.YYYY_MM_DD_DATE_PATTERN);
        upcomingEvents= eventHistories.stream()
                .filter(event -> event.eventDate.compareTo(today) >=0)
                .collect(Collectors.toList());


        pastEvents = eventHistories.stream()
                .filter(event -> event.eventDate.compareTo(today) < 0)
                .collect(Collectors.toList());


        allEvents = eventHistories;

        if(eventCancelling){
            postEventData();
            eventCancelling = false;
        }

    }

    private void postEventData() {
        if(type == EventConstants.TYPE_UPCOMING){
            eventHistoryObserver.postValue(upcomingEvents);
        }else if(type == EventConstants.TYPE_PAST){
            eventHistoryObserver.postValue(pastEvents);
        }else{
            eventHistoryObserver.postValue(allEvents);
        }
    }

    public void fetchPastEvents(){

        if (!ListUtils.isEmpty(pastEvents)) {
            eventHistoryObserver.postValue(pastEvents);
        } else {
            eventHistoryErrorObserver.postValue(getConfigString(MessageProvider.error_no_event));
        }

    }

    public void fetchUpcomingEvents(){

        if (!ListUtils.isEmpty(upcomingEvents)) {
            eventHistoryObserver.postValue(upcomingEvents);
        } else {
            eventHistoryErrorObserver.postValue(getConfigString(MessageProvider.error_no_event));
        }
    }

    public void fetchAllEvents(){

        if (!ListUtils.isEmpty(allEvents)) {
            eventHistoryObserver.postValue(allEvents);
        } else {
            eventHistoryErrorObserver.postValue(getConfigString(MessageProvider.error_no_event));
        }

    }

    public void cancelEvent(EnrolledEvent event) {
        eventCancelling = true;
        showProgressBar(getConfigString(MessageProvider.event_cancel_indicator_message));

        Disposable disposable = memberUseCase.cancelEvent(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventCancelled,
                        this::onEventCancelFailed);
        addDisposable(disposable);
    }

    private void onEventCancelFailed(Throwable throwable) {
        hideProgressBar();
        eventCancelling = false;
    }

    private void onEventCancelled() {
        fetchEventsHistory();
    }

    private String getLoadingMessage(){
        String message;
        if(type == EventConstants.TYPE_UPCOMING){
            message = getConfigString(MessageProvider.msg_reading_upcoming_events);
        }else if(type == EventConstants.TYPE_PAST){
            message = getConfigString(MessageProvider.msg_reading_past_events);
        }else{
            message = getConfigString(MessageProvider.msg_reading_all_events);
        }
        return message;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void requestUploadSelfie(EnrolledEvent event) {
        appEngine.setEnrolledEvent(event);
        uploadSelfieObserver.postValue(true);
    }
}
