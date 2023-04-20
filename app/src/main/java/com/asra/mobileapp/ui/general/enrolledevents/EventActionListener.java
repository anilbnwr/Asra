package com.asra.mobileapp.ui.general.enrolledevents;

import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.model.EventParticipant;

import java.util.List;

public interface EventActionListener {

    void onCancel(EnrolledEvent event);
    void showPassport(EnrolledEvent event);
    void uploadSelfie(EnrolledEvent event);
    void showMotoAccessories(List<EventParticipant.MotoClass> motoClasses);
    void showEvolveAccessories(List<EventParticipant.Rental> rentals, List<String> trainings);

}
