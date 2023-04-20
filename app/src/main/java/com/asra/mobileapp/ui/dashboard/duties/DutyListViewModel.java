package com.asra.mobileapp.ui.dashboard.duties;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Duty;
import com.asra.mobileapp.model.EventDuty;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class DutyListViewModel extends BaseViewModel {


    private List<EventDuty> eventDuty;
    private List<DutyDisplayItem> displayItems = new ArrayList<>();
    public SingleLiveEvent<List<DutyDisplayItem>>  assignedDutiesObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<List<Duty>>  unassignedDutiesObservable = new SingleLiveEvent<>();

    @Inject
    public DutyListViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
    }

    public void setEventDuty(int position) {

        switch (position){
            case 0: eventDuty = appEngine.getCoachDutyTypes().pastDutyList;break;
            case 1: eventDuty = appEngine.getCoachDutyTypes().currentutyList;break;
            case 2: eventDuty = appEngine.getCoachDutyTypes().upcomingDutyList;break;
        }
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();
        displayItems.clear();
        for(EventDuty eventDuty: eventDuty) {
            List<Duty> sortedDutyList = eventDuty.getDuties().stream()
                    .sorted()
                    .collect(Collectors.toList());
            eventDuty.setDuties(sortedDutyList);
            displayItems.addAll(DutyDisplayItem.fromEventDuty(eventDuty));

        }
        assignedDutiesObservable.postValue(displayItems);

//        List<Duty> unassignedDutyList = eventDuty.getDuties().stream()
//                .filter(duty -> !duty.getStatus())
//                .collect(Collectors.toList());
//        unassignedDutiesObservable.postValue(unassignedDutyList);
    }


    public static class DutyDisplayItem{
        public boolean isHeader = false;

        public EventDuty eventDuty;
        public Duty duty;

        public static List<DutyDisplayItem> fromEventDutyList(List<EventDuty> eventDutyList) {
            List<DutyDisplayItem> dutyDisplayItems = new ArrayList<>();

            for(EventDuty duty: eventDutyList){
                dutyDisplayItems.addAll(fromEventDuty(duty));
            }
            return dutyDisplayItems;
        }

        public static List<DutyDisplayItem> fromEventDuty(EventDuty eventDuty){
            List<DutyDisplayItem> dutyDisplayItems = new ArrayList<>();

            DutyDisplayItem item = new DutyDisplayItem();
            item.isHeader = true;
            item.eventDuty = eventDuty;

            dutyDisplayItems.add(item);

            for(Duty duty: eventDuty.getDuties()){
                DutyDisplayItem dutyItem = new DutyDisplayItem();
                dutyItem.isHeader = false;
                dutyItem.duty = duty;
                dutyDisplayItems.add(dutyItem);
            }
           // item.eventDuty.setDuties(null);
            return dutyDisplayItems;
        }

    }
}
