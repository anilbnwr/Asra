package com.asra.mobileapp.ui.dashboard.events;

import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.EventsUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EventsViewModel extends ShoppeViewModel {


    private EventsUseCase eventsUseCase;
    public SingleLiveEvent<List<Event>> eventsObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> eventsFetchErrorObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<List<String>> filterbyMonthsObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<List<String>> filterbyEventTypeObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<Event> eventAddedToCartObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> eventToCartErrorObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> privateEventCodeObservable = new SingleLiveEvent<>();


    private List<String> eventMonthFilterList;
    private List<String> eventTypeFilterList;

    private Event selectedEvent;


    @Inject
    public EventsViewModel(EventsUseCase eventsUseCase,
                           CartUseCase cartUseCase,
                           AppEngine appEngine,
                           ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);

        this.eventsUseCase = eventsUseCase;
        eventMonthFilterList = new ArrayList<>();
        eventTypeFilterList = new ArrayList<>();
    }


    @Override
    public void onViewStarted() {
        super.onViewStarted();

//        List<Event> eventList = eventsObservable.getValue();
//        if (ListUtils.isEmpty(eventList))
//
//        else eventsObservable.postValue(eventList);

        fetchEvents();
    }

    private void fetchEvents() {
        showProgressBar();
        Disposable disposable = eventsUseCase.getEvents()
                .doOnSuccess(events -> {
                    setMonthYearList(events);
                    setFilterTypesList(events);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventListFetched,
                        this::onFetchingEventsFailed);
        addDisposable(disposable);
    }

    private void setFilterTypesList(List<Event> events) {
        SortedSet<Event> set = new TreeSet<>((o1, o2) -> o1.eventType.equalsIgnoreCase(o2.eventType) ? 0 : 1);
        set.addAll(events);
        eventTypeFilterList.clear();
        for (Event event : set) {
            eventTypeFilterList.add(event.eventType);
        }
    }

    private void onFetchingEventsFailed(Throwable throwable) {
        hideProgressBar();
        eventsFetchErrorObservable.postValue(throwable.getMessage());
    }

    private void onEventListFetched(List<Event> events) {
        hideProgressBar();
        eventsObservable.postValue(events);
    }

    private void setMonthYearList(List<Event> events) {
        SortedSet<Event> set = new TreeSet<>((o1, o2) -> {
            String monthYear1 = getMonthYear(o1);
            String monthYear2 = getMonthYear(o2);
            return monthYear1.equalsIgnoreCase(monthYear2) ? 0 : 1;
        });

        set.addAll(events);
        eventMonthFilterList.clear();
        for (Event event : set) {
            eventMonthFilterList.add(getMonthYear(event));
        }
        Timber.i("Found %s Unique MonthYear", set.size());
    }

    private String getMonthYear(Event event) {
        return DateUtils.getMonthYear(event.eventDate, DateUtils.YYYY_MM_DD_DATE_PATTERN);
    }

    public void filterByMonths() {
        if (!ListUtils.isEmpty(eventMonthFilterList))
            filterbyMonthsObservable.postValue(eventMonthFilterList);
    }

    public void filterByEventTypes() {
        if (!ListUtils.isEmpty(eventTypeFilterList))
            filterbyEventTypeObservable.postValue(eventTypeFilterList);
    }

    public void clearFilter() {
        List<Event> events = eventsObservable.getValue();
        if (!ListUtils.isEmpty(events)) {
            eventsObservable.postValue(events);
        }

    }

    public void addEventToCart(Event event) {

        if(event.externalEventHost != null){
            openWebPage(event.externalEventHost.externalUrl);
            return;
        }else if (event.isPrivateEvent &&
                StringUtils.isEmpty(event.couponCode)) {
            selectedEvent = event;
            privateEventCodeObservable.postValue(appEngine.isUserLoggedIn());
            return;
        }

        showProgressBar(getConfigString(MessageProvider.msg_add_event_to_cart));
        Disposable disposable = cartUseCase.addEventToCart(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {onEventAddedToCart(event);},
                        this::onAddingEventToCartFailed);
        addDisposable(disposable);

    }

    private void onAddingEventToCartFailed(Throwable throwable) {
        hideProgressBar();
        eventToCartErrorObservable.postValue(throwable.getMessage());
    }

    private void onEventAddedToCart(Event event) {
        hideProgressBar();
        eventAddedToCartObservable.postValue(event);
        updateCartCountBy(1);
    }

    public void onSecretCode(String userInput) {
        if (this.selectedEvent != null) {
            selectedEvent.couponCode = userInput;
            addEventToCart(selectedEvent);
        }
    }

    @Override
    public void invalidate(){
        fetchEvents();
    }
}
