package com.asra.mobileapp.ui.admin.completedevents;

import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.CompletedEvent;
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
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CompletedEventsViewModel extends BaseViewModel {

    public SingleLiveEvent<List<CompletedEvent>> completedEventsObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> errorObservable = new SingleLiveEvent<>();


    public SingleLiveEvent<List<String>> filterByMonthsObservable = new SingleLiveEvent();
    public SingleLiveEvent<List<String>> filterByEventsObservable = new SingleLiveEvent();
    public SingleLiveEvent<List<String>> filterByTrainingsObservable = new SingleLiveEvent();


    private List<String> monthYearList;
    private List<String> eventTypeList;
    private List<String> trainingTypeList;

    private List<CompletedEvent> completedEvents;

    private AdminUseCase adminUseCase;
    @Inject
    public CompletedEventsViewModel(AdminUseCase adminUseCase,
                                    AppEngine appEngine,
                                    ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.adminUseCase = adminUseCase;

        monthYearList = new ArrayList<>();
        eventTypeList = new ArrayList<>();
        trainingTypeList = new ArrayList<>();
    }

    public void fetchCompletedEvents() {

        if(ListUtils.isEmpty(completedEvents)) {
            showProgressBar();

            Disposable disposable = adminUseCase.getCompletedEvents(appEngine.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onCompletedEventsFetched,
                            this::onFailed);
            addDisposable(disposable);
        }else{
            onCompletedEventsFetched(completedEvents);
        }
    }

    private void onFailed(Throwable throwable) {
        hideProgressBar();
        errorObservable.postValue(throwable.getMessage());

    }

    private void onCompletedEventsFetched(List<CompletedEvent> completedEvents) {
        hideProgressBar();
        if(ListUtils.isEmpty(completedEvents))
            onFailed(new ETApiException(resourceFetcher.getConfigString(MessageProvider.err_no_sign_events)));
        else {
            this.completedEvents = completedEvents;
            setMonthYearList(completedEvents);
            setEventTypeList(completedEvents);
            setTrainingTypeList(completedEvents);
            completedEventsObservable.postValue(completedEvents);
        }
    }

    private void setMonthYearList(List<CompletedEvent> completedEvents){
        SortedSet<CompletedEvent> set = new TreeSet<>((o1, o2) -> {
            // return 0 if objects are equal in terms of your properties
            String monthYear1 = getMonthYear(o1);
            String monthYear2 = getMonthYear(o2);
            return monthYear1.equalsIgnoreCase(monthYear2) ? 0 : 1;
        });

        set.addAll(completedEvents);
        monthYearList.clear();
        for(CompletedEvent event : set){
            monthYearList.add(getMonthYear(event));
        }
        Timber.i("Found %s Unique MonthYear", set.size());
    }

    private String getMonthYear(CompletedEvent event){
        return  DateUtils.getMonthYear(event.eventDate,DateUtils.YYYY_MM_DD_DATE_PATTERN);
    }

    private void setEventTypeList(List<CompletedEvent> completedEvents){

        Comparator<CompletedEvent> compareByType = (o1, o2) -> o1.eventType.compareTo(o2.eventType);

        List<CompletedEvent> tempList = new ArrayList<>();
        tempList.addAll(completedEvents);
        Collections.sort(tempList, compareByType);

        SortedSet<CompletedEvent> set = new TreeSet<>((o1, o2) -> {
            // return 0 if objects are equal in terms of your properties
            return o1.eventType.equalsIgnoreCase(o2.eventType) ? 0 : 1;
        });

        set.addAll(tempList);
        eventTypeList.clear();
        for(CompletedEvent event : set){
            eventTypeList.add(event.eventType);
        }
        Timber.i("Found %s Unique EventType", set.size());
    }
    private void setTrainingTypeList(List<CompletedEvent> completedEvents){

        List<String> trainingList = new ArrayList<>();
        for(CompletedEvent event : completedEvents){
            if(!ListUtils.isEmpty(event.trainingTypes)){
                trainingList.addAll(event.trainingTypes);
            }
        }

        if(!ListUtils.isEmpty(trainingList)){

            //Set<String> uniqueGas = new HashSet<String>(gasList);
            Collections.sort(trainingList);
            Set<String> uniqueTrainingList = new HashSet<>(trainingList);

            trainingTypeList.clear();
            trainingTypeList.addAll(uniqueTrainingList);
            Timber.i("Found %s Unique EventType", trainingTypeList.size());
        }

    }

    @Override
    public void invalidate() {
        super.invalidate();
        completedEvents = null;
        fetchCompletedEvents();
    }

    public void filterByMonths(){
        filterByMonthsObservable.postValue(monthYearList);
    }

    public void filterByEventsTypes(){
        filterByEventsObservable.postValue(eventTypeList);
    }

    public void filterByTrainings(){
        filterByTrainingsObservable.postValue(trainingTypeList);
    }
}
