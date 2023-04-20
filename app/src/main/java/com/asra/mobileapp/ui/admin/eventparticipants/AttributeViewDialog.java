package com.asra.mobileapp.ui.admin.eventparticipants;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.util.ListUtils;

import java.util.List;

public class AttributeViewDialog extends Dialog {




    private LinearLayout trainingContainer;
    private View trainingView;

    private LinearLayout rentalContainer;
    private View rentalView;

    private LayoutInflater inflater;

    private TextView trainingHeader;
    private TextView rentalHeader;

    private boolean showMotoDetails = false;

    private List<EventParticipant.MotoClass> motoClasses;
    private List<EventParticipant.Rental> rentals;
    private List<String> trainingList;

    public AttributeViewDialog(Context context){
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_user_event_attribute);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        trainingView = findViewById(R.id.trainingLayout);
        trainingContainer = findViewById(R.id.trainingContainer);
        trainingHeader = findViewById(R.id.trainingHeader);

        rentalHeader = findViewById(R.id.rentalHeader);
        rentalContainer = findViewById(R.id.rentalContainer);
        rentalView = findViewById(R.id.rentalLayout);

        View btnClose = findViewById(R.id.closeButton);
        btnClose.setOnClickListener((View v)->{
            dismiss();
        });

        updateTheme();

        inflater = LayoutInflater.from(getContext());

        if(!showMotoDetails) {
            if (ListUtils.isEmpty(trainingList)) {
                trainingView.setVisibility(View.GONE);
            } else {
                setUpTrainingViews();
                trainingView.setVisibility(View.VISIBLE);
            }

            if(ListUtils.isEmpty(rentals)){
                rentalView.setVisibility(View.GONE);
            }else{
                setUpRentalViews();
                rentalView.setVisibility(View.VISIBLE);
            }
        }else{
            if (ListUtils.isEmpty(motoClasses)) {
                trainingView.setVisibility(View.GONE);
            } else {
                setUpMotoClassesViews();
                trainingView.setVisibility(View.VISIBLE);
            }
            rentalView.setVisibility(View.GONE);
        }



    }

    private void updateTheme() {
        View btnClose = findViewById(R.id.closeButton);
        if(ETApplication.getInstance().isEvApp()){
            btnClose.setBackgroundResource(R.drawable.selector_button_primary);
        }else{
            btnClose.setBackgroundResource(R.drawable.selector_button_moto_primary);
        }
    }

    private void setUpTrainingViews(){
        trainingContainer.removeAllViews();
        for(String training : trainingList){
            View view = inflater.inflate(R.layout.simple_text, null, false);
            TextView title = view.findViewById(R.id.simpleText);
            title.setText(training);

            trainingContainer.addView(view);
        }
    }
    private void setUpMotoClassesViews(){
        trainingContainer.removeAllViews();
        trainingHeader.setText(getContext().getString(R.string.moto_classes));
        for(EventParticipant.MotoClass motoClass : motoClasses){

            View headerView = inflater.inflate(R.layout.moto_item_race_header, null, false);
            TextView headerText = headerView.findViewById(R.id.itemHeader);
            headerText.setText(motoClass.raceName);
            trainingContainer.addView(headerView);

            for(EventParticipant.RaceClass raceClass : motoClass.raceClasses) {

                View view = inflater.inflate(R.layout.admin_rentl_view, null, false);
                TextView title = view.findViewById(R.id.item_title);
                title.setText(raceClass.className);

                TextView size = view.findViewById(R.id.item_size);
                size.setText(raceClass.bikeData);
                trainingContainer.addView(view);
            }


        }
    }

    private void setUpRentalViews(){

        for(EventParticipant.Rental rental : rentals){
            View view = inflater.inflate(R.layout.admin_rentl_view, null, false);
            TextView title = view.findViewById(R.id.item_title);
            title.setText(rental.name);

            TextView size = view.findViewById(R.id.item_size);
            size.setText(rental.value);

            rentalContainer.addView(view);
        }
    }

    public boolean isShowMotoDetails() {
        return showMotoDetails;
    }

    public void setShowMotoDetails(boolean showMotoDetails) {
        this.showMotoDetails = showMotoDetails;
    }

    public void setMotoClasses(List<EventParticipant.MotoClass> motoClasses) {
        this.motoClasses = motoClasses;
    }

    public void setRentals(List<EventParticipant.Rental> rentals) {
        this.rentals = rentals;
    }

    public void setTrainingList(List<String> trainingList) {
        this.trainingList = trainingList;
    }
}
