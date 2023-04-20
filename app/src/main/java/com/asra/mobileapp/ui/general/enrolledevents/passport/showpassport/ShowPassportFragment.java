package com.asra.mobileapp.ui.general.enrolledevents.passport.showpassport;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.dialog.ETConfirmationDialog;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.databinding.FragmentShowPassportBinding;
import com.asra.mobileapp.model.PassportInfo;
import com.asra.mobileapp.model.StampPassword;
import com.asra.mobileapp.ui.base.ETFragment;

import java.util.Locale;

public class ShowPassportFragment extends ETFragment<ShowPassportViewModel, FragmentShowPassportBinding> {

    private static final String KEY_PASSPORT_ID = "passport_id";
    private static final String KEY_EVENT_NAME = "event_name";
    private static final String KEY_EVENT_DATE = "event_date";
    AlertDialog.Builder builder;
    public static ShowPassportFragment newInstance(String passportId, String eventName, String eventDate){
        ShowPassportFragment fragment = new ShowPassportFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_PASSPORT_ID, passportId);
        arguments.putString(KEY_EVENT_NAME, eventName);
        arguments.putString(KEY_EVENT_NAME, eventName);
        arguments.putString(KEY_EVENT_DATE, eventDate);
        fragment.setArguments(arguments  );
        return fragment;
    }
    @Override
    protected Class<ShowPassportViewModel> getViewModel() {
        return ShowPassportViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_show_passport;
    }

    @Override
    public void initializeViews() {

        viewModel.getPassport(getArguments().getString(KEY_PASSPORT_ID, ""));

        dataBinding.trackName.setTextColor(UiUtils.getPrimaryColor(getContext()));
        dataBinding.trackDate.setTextColor(UiUtils.getPrimaryColor(getContext()));
        dataBinding.trackName.setText(getArguments().getString(KEY_EVENT_NAME, ""));
        String formattedEventDate = DateUtils.getDateAsString(getArguments().getString(KEY_EVENT_DATE, ""),
                DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        dataBinding.trackDate.setText(String.format(Locale.ENGLISH, "Event Date: %s", formattedEventDate));

        dataBinding.btnStampPassPort.setOnClickListener((View v) -> {
            Log.d("getActivity", "initializeViews: Stamp PassPort "+KEY_PASSPORT_ID);
            stampPassPortDialog();

        });
    }

    private void stampPassPortDialog() {

        ETConfirmationDialog<Integer> dialog = new ETConfirmationDialog<>(getActivity(),
                new ETConfirmationDialog.ConfirmationListener<Integer>() {
                    @Override
                    public void onConfirmed(Integer passthrough) {
                        super.onConfirmed(passthrough);
                      viewModel.getStampPassport(getArguments().getString(KEY_PASSPORT_ID,""));
                    }
                }, 0);
        dialog.setDialogTitle("STAMP PASSPORT");
        dialog.setDialogMessage("For Tech Personnel Only: Stamping this passport indicates that the rider has passed tech and voids its future use.");
        dialog.setSecondaryMessage("");
        dialog.setDialogBtnPositive("Stamp Passport");
        dialog.show();
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.passportErrorObserver.observe(this, this::showErrorMessage);
        viewModel.passportInfoObservable.observe(this, this::updateUI);
        viewModel.stampPasswordObservable.observe(this,this::stampUpdateUI);

    }

    private void stampUpdateUI(StampPassword stampPassword) {


        Log.d("TAG", "stampUpdateUI: "+stampPassword.message);
        if( stampPassword.status == 1){
            dataBinding.btnStampPassPort.setVisibility(View.GONE);
            dataBinding.cardView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
            Log.d("TAG", "stampUpdateUI: "+stampPassword.status);
        }else {
            Log.d("TAG", "stampUpdateUI: "+stampPassword.status);

        }
    }

    private void updateUI(PassportInfo passportInfo) {
        GlideHelper.setImage(this, dataBinding.passportImage, passportInfo.getPicture(), R.drawable.et_fallback_image, dataBinding.passportProgressbar);
       if(passportInfo.getIsStamped() == 1){
           dataBinding.btnStampPassPort.setVisibility(View.GONE);
           dataBinding.cardView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
       }else{
           dataBinding.btnStampPassPort.setVisibility(View.VISIBLE);

       }

        if(passportInfo.isDayWorker()){
            dataBinding.groupBadge.setVisibility(View.GONE);
        }else{
            GlideHelper.setImage(this, dataBinding.groupBadge, passportInfo.getGroupLogo());
        }
        dataBinding.userName.setText(passportInfo.getRiderName());
        dataBinding.membershipLevel.setText(String.format("Membership Level: %s", passportInfo.getMembershipLevel()));
        String rentals = passportInfo.getConsolidatedRentals();
        if(TextUtils.isEmpty(rentals)){
            dataBinding.rentals.setVisibility(View.GONE);
        }else {
            dataBinding.rentals.setText(String.format("Rentals: %s", rentals));
        }

        String trainings = passportInfo.getTrainingList();
        if(TextUtils.isEmpty(trainings)){
            dataBinding.trainings.setVisibility(View.GONE);
        }else {
            dataBinding.trainings.setText(String.format("Trainings: %s", trainings));
        }
        if(TextUtils.isEmpty(passportInfo.getDayWorkerJob())){
            dataBinding.dayWorker.setVisibility(View.GONE);
        }else {
            dataBinding.dayWorker.setText(String.format("Job: %s", passportInfo.getDayWorkerJob()));
        }
        dataBinding.signedDate.setText(String.format(Locale.ENGLISH, "Date : %s",
                DateUtils.getDateAsString(passportInfo.getSignedDate(),
                        DateUtils.FORMAT_MMMM_DD_YYYY, DateUtils.DD_MMM_YYYY_DATE_PATTERN)));

    }

    @Override
    public String getTitle() {
        return "Passport - "+getArguments().getString(KEY_EVENT_NAME);
    }





}
