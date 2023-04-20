package com.asra.mobileapp.usecase;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.network.adapter.apiservices.ShopsApiServices;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveShopsApiServices;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class EventsUseCase extends BaseUseCase {

    private EvolveShopsApiServices evolveApiServices;

    @Inject
    public EventsUseCase(EvolveShopsApiServices evolveApiServices,

                         AppEngine appEngine) {
        super(appEngine);
        this.evolveApiServices = evolveApiServices;
    }

    private ShopsApiServices getApiServices() {
        return evolveApiServices;
    }

    public Single<List<Event>> getEvents() {

        if(appEngine.isEvApp())
        return getApiServices().getEvolveEvents();
        else
            return getApiServices().getMotoEvents();
    }

    public Single<EventDetails> getEventDetails(String eventSlug) {
        if (appEngine.isEvApp())
            return getApiServices().getEvolveEventDetail(eventSlug, appEngine.getUserId());
        else {
            return getApiServices().getMotoEventDetail(eventSlug, appEngine.getUserId());
        }
    }

    public Single<EventDetails> getEventDetails(String eventSlug, boolean evEvent) {
        if (evEvent)
            return evolveApiServices.getEvolveEventDetail(eventSlug, appEngine.getUserId());
        else
            return evolveApiServices.getMotoEventDetail(eventSlug, appEngine.getUserId());
    }
}
