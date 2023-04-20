package com.asra.mobileapp.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.ETCheckBox;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.StringUtils;
import com.google.android.material.textfield.TextInputLayout;

import timber.log.Timber;

public class RaceSelectionDialog extends Dialog {


    private DialogListener dialogListener;


    private EventDetails.Race race;

    public RaceSelectionDialog(Activity activity, DialogListener dialogListener){
        super(activity);
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_select_event_class);
        setDialogMargin();

        TextView btnPositive = findViewById(R.id.dialog_btn_positive);

        TextView btnNegative = findViewById(R.id.dialog_btn_negative);

        btnPositive.setOnClickListener((v)->{
            if(dialogListener != null){



                if(!race.validateRaceClasses()) {
                    setUpList();
                }else{
                    dialogListener.onClassSelected(race);
                }
                dismiss();
            }else{
                dismiss();
            }

        });
        btnNegative.setOnClickListener(view -> {
            if(dialogListener != null){
                dialogListener.onDismiss();
            }
            dismiss();
        });


        int btnDrawable = ETApplication.getInstance().isEvApp() ?
                R.drawable.selector_button_primary : R.drawable.selector_button_moto_primary;
        btnPositive.setBackgroundResource(btnDrawable);


        setUpList();

    }

    private CompoundButton.OnCheckedChangeListener listener = (compoundButton, isSelected) ->{
        EventDetails.RaceClass raceClass = (EventDetails.RaceClass) compoundButton.getTag();


        raceClass.checked = isSelected;
    };


    private void setUpList() {

        race.validateRaceClasses();
        ViewGroup container = findViewById(R.id.eventContainer);
        container.removeAllViews();
        if(race != null && ListUtils.isNotEmpty(race.raceClasses)){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for(EventDetails.RaceClass raceClass : race.raceClasses){

                View itemView = inflater.inflate(R.layout.item_class_event, container, false);

                ETCheckBox checkBox = itemView.findViewById(R.id.item_selection);
                checkBox.setText(raceClass.className);
                checkBox.setEnabled(!raceClass.checked);
                checkBox.setChecked(raceClass.checked);
                checkBox.setTag(raceClass);
                checkBox.setOnCheckedChangeListener(listener);

                TextInputLayout bikeDataInput = itemView.findViewById(R.id.bikeData);
                bikeDataInput.getEditText().setText(raceClass.className);
                bikeDataInput.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        raceClass.bikeData = charSequence.toString();

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                if(raceClass.hasError){
                    bikeDataInput.setError("Bike Data Required.");
                }

                TextView price = itemView.findViewById(R.id.item_price);
                price.setText(StringUtils.formatAmount(raceClass.classPrice));

                container.addView(itemView);
            }
        }
    }

    private void setDialogMargin() {
        try{
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }catch (Exception e){
            Timber.e(e, "Dialog Margin Adjustment failed");
        }
    }



    public void setRace(EventDetails.Race race) {
        this.race = race;
    }

    public static abstract class DialogListener{

        public void onClassSelected(EventDetails.Race race){

        }
        public void onDismiss(){

        }
    }

}
