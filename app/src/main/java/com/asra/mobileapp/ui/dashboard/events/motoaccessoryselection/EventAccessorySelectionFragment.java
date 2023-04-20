package com.asra.mobileapp.ui.dashboard.events.motoaccessoryselection;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.ETCheckBox;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.databinding.FragmentMotoClassSelectionBinding;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.StringUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import androidx.annotation.Nullable;

public class EventAccessorySelectionFragment extends
        ShoppeFragment<EventAccessorySelectionViewModel,
        FragmentMotoClassSelectionBinding> {


    private static final String KEY_EVENT = "key.event.details";

    public static EventAccessorySelectionFragment newInstance(EventDetails eventDetails){
        EventAccessorySelectionFragment fragment = new EventAccessorySelectionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_EVENT, eventDetails);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }

    @Override
    protected Class<EventAccessorySelectionViewModel> getViewModel() {
        return EventAccessorySelectionViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_moto_class_selection;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();

        viewModel.eventDetailsObservable.observe(this, this::setViews);
        viewModel.classTotalObservable.observe(this, price ->
                dataBinding.eventClassTotal.setText(price));
        viewModel.transponderErrorObservable.observe(this, error ->
                dataBinding.transponderLayout.setVisibility(View.GONE));

        viewModel.eventAddedToCartObservable.observe(this, event -> addEventToCalendar(event.title, event.eventDate));
        viewModel.eventToCartErrorObservable.observe(this, this::showErrorMessage);

        viewModel.eventDetailsErrorObservable.observe(this,
                message -> showEmptyMessage(true, message));

        viewModel.errorValidationErrorObservable.observe(this, message -> showDialog( message,  "OK", new ETDialog.DialogListener() {
            @Override
            public void onPositiveButtonClicked() {
                super.onPositiveButtonClicked();
                popFragment();
            }
        }));
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventDetails eventDetails = getArguments().getParcelable(KEY_EVENT);
        viewModel.initWith(eventDetails);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        dataBinding.eventDetailsBtnAddToCart.setOnClickListener(view -> {
            String transponderNo = dataBinding.transponderNumber.getEditText()
                    .getText().toString();
            String bikeNumber = dataBinding.bikeNumber.getEditText().getText().toString();
            viewModel.onAddToCart(bikeNumber, transponderNo);
        });


        dataBinding.transponderNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.setTransponderNumber(editable.toString());

            }
        });

        dataBinding.bikeNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.setBikeNumber(editable.toString());

            }
        });

    }

    private void setViews(EventDetails eventDetails) {
        setSkillLevelViews(eventDetails.racerStatus, eventDetails.registeredSkill);

        if(ListUtils.isEmpty(eventDetails.eventClasses)){
            dataBinding.eventClassLayout.setVisibility(View.GONE);
        }else{
            populateClassSelectionView(eventDetails.eventClasses);
        }



        dataBinding.transponderNumber.getEditText().setText(eventDetails.transponderNo);
        dataBinding.bikeNumber.getEditText().setText(eventDetails.bikeNumber);

    }



    private void setSkillLevelViews(String racerStatus, String registeredSkill) {

        if(getString(R.string.racer_status_expert).equalsIgnoreCase(racerStatus)){
            dataBinding.rdExpert.setChecked(true);
            dataBinding.rdExpert.setEnabled(!racerStatus.equalsIgnoreCase(registeredSkill));
            dataBinding.rdAmateur.setEnabled(!racerStatus.equalsIgnoreCase(registeredSkill));

        }else if(getString(R.string.racerStatus_amateur).equalsIgnoreCase(racerStatus)){
            dataBinding.rdAmateur.setChecked(true);
        }

        dataBinding.skillLevelRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {

            if(i == dataBinding.rdExpert.getId()){
                viewModel.onSkillSelectionChanged(dataBinding.rdExpert.getText().toString());

            }else{
                viewModel.onSkillSelectionChanged(dataBinding.rdAmateur.getText().toString());

            }
        });

//        dataBinding.rdExpert.setOnCheckedChangeListener(skillListener);
//        dataBinding.rdAmateur.setOnCheckedChangeListener(skillListener);

    }

    private CompoundButton.OnCheckedChangeListener skillListener = (compoundButton, isSelected) -> {

    };


    private CompoundButton.OnCheckedChangeListener listener = (compoundButton, isSelected) -> {
        EventDetails.RaceClass raceClass = (EventDetails.RaceClass) compoundButton.getTag();
        EventDetails.Race race = (EventDetails.Race) compoundButton.getTag(R.id.selected_race);

        viewModel.onClassSelectionChanged(race, raceClass, isSelected);
    };


    private void populateClassSelectionView(List<EventDetails.Race> eventClasses) {
        dataBinding.eventClassContainer.removeAllViews();


        LayoutInflater inflater = LayoutInflater.from(getContext());


        for(EventDetails.Race eventClass : eventClasses){
            ViewGroup seriesContainerView = (ViewGroup) inflater.inflate(R.layout.item_event_race_series, null, false);

            ViewGroup eventClassContainer = seriesContainerView.findViewById(R.id.event_class_container);
            eventClassContainer.removeAllViews();

            TextView title = seriesContainerView.findViewById(R.id.raceTitle);
            title.setText(eventClass.raceName);
            for(EventDetails.RaceClass raceClass : eventClass.raceClasses){

                View itemView = inflater.inflate(R.layout.item_class_event, seriesContainerView, false);

                ETCheckBox checkBox = itemView.findViewById(R.id.item_selection);
                checkBox.setText(raceClass.className);
                checkBox.setEnabled(!raceClass.specialCase || (raceClass.specialCase && eventClass.canSelectSpecialClass()));
                checkBox.setChecked(raceClass.checked);
                checkBox.setTag(raceClass);
                checkBox.setTag(R.id.selected_race, eventClass);
                checkBox.setOnCheckedChangeListener(listener);

                TextInputLayout bikeDataInput = itemView.findViewById(R.id.bikeData);
                bikeDataInput.getEditText().setText(raceClass.bikeData);
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

                seriesContainerView.addView(itemView);
                bikeDataInput.setEnabled(checkBox.isChecked());
            }
            dataBinding.eventClassContainer.addView(seriesContainerView);

        }


    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.eventDetailsBtnAddToCart.setBackgroundResource(R.drawable.selector_button_primary);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.eventDetailsBtnAddToCart.setBackgroundResource(R.drawable.selector_button_moto_primary);
    }

    @Override
    public void onResume() {

        super.onResume();
    }
}
