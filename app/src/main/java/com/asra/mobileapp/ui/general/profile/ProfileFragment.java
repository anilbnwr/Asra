package com.asra.mobileapp.ui.general.profile;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.ScrollView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.common.imageloader.ImageUtils;
import com.asra.mobileapp.databinding.FragmentEditProfileBinding;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.general.address.AddressConstants;
import com.asra.mobileapp.ui.general.address.AddressFragment;
import com.asra.mobileapp.ui.general.profile.model.SimpleDate;
import com.asra.mobileapp.util.StringUtils;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import gun0912.tedimagepicker.builder.TedImagePicker;
import timber.log.Timber;

public class ProfileFragment extends ETFragment<ProfileViewModel, FragmentEditProfileBinding>
        implements DatePickerDialog.OnDateSetListener {


    public static ETFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    protected Class<ProfileViewModel> getViewModel() {
        return ProfileViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_edit_profile;
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.userDetailsObservable.observe(this, this::setUpProfileView);

        viewModel.shippingAddressObservable.observe(this,
                this::updateMailingAddress);

        viewModel.billingAddressObservable.observe(this,
                this::updateBillingAddress);

        viewModel.dobObservable.observe(this, date -> showDatePicker(date, dataBinding.tilDob));
        viewModel.amaExpiryChangeObservable.observe(this, date -> showDatePicker(date, dataBinding.tilMotoAmaExpiry));

        viewModel.profileUpdateObservable
                .observe(this, message -> showSuccessToast(
                        getConfigString(MessageProvider.profile_updated_successfully)));
        viewModel.profileUpdateErrorObservable.observe(this, this::showErrorToast);
        viewModel.showMotoInfoObservable.observe(this, show ->
                dataBinding.motoInfoGroup.setVisibility(show ? View.VISIBLE : View.GONE));

    }

    @Override
    public void initializeViews() {
        showBackButton();

        Objects.requireNonNull(dataBinding.tilDob.getEditText()).setOnClickListener((View v) -> {
            viewModel.changeDoB();
        });

        Objects.requireNonNull(dataBinding.tilMotoAmaExpiry.getEditText()).setOnClickListener((View v) -> {
            viewModel.changeAmaExpiry();
        });
        dataBinding.btnSave.setOnClickListener((View v) -> {
            onSaveUserProfile();
        });

        dataBinding.btnProfileCamera.setOnClickListener((View v) -> {
            onCameraRequest();
        });

        Objects.requireNonNull(dataBinding.tilEmergencyRelationship.getEditText()).setOnClickListener((View v) -> {
            showRelationPicker();
        });

        dataBinding.btnEditBillingAddress.setOnClickListener((View v) ->
                loadFragment(AddressFragment.newInstance(AddressConstants.TYPE_BILLING,
                        false)));

        dataBinding.btnEditMailingAddress.setOnClickListener((View v) ->
                loadFragment(AddressFragment.newInstance(AddressConstants.TYPE_SHIPPING,
                        false)));

        dataBinding.btnAddBillingAddress.setOnClickListener((View v) ->
                loadFragment(AddressFragment.newInstance(AddressConstants.TYPE_BILLING,
                        true)));

        dataBinding.btnAddMailingAddress.setOnClickListener((View v) ->
                loadFragment(AddressFragment.newInstance(AddressConstants.TYPE_SHIPPING,
                        true)));
    }

    private void onCameraRequest() {

        TedImagePicker.with(getContext())
                .start(uri -> {
                    Timber.i("Selected Image %s", uri.getPath());
                    GlideHelper.setImageUri(dataBinding.profileImage, uri);
                    String profileImagePath = ImageUtils.prepareImageFromUri(uri);
                    viewModel.onImageCaptured(profileImagePath);
                });
    }



    private void showRelationPicker() {
        String relationArrayJson = getConfigString(MessageProvider.emergency_relation_ships);
        List<String> relationsList = gson.fromJson(relationArrayJson, new TypeToken<List<String>>() {
        }.getType());

        Collections.sort(relationsList);
        final String[] arrayOfRelations = relationsList.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Relationship");

        int checkedIndex = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayOfRelations, checkedIndex, (dialog, which) ->
                dataBinding.tilEmergencyRelationship.getEditText().setText(arrayOfRelations[which]));

        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onSaveUserProfile() {

        UserDetails userDetails = validateData();
        if (userDetails != null)
            viewModel.saveProfileChanges(userDetails);
    }



    private void updateMailingAddress(String shipping) {
        if (TextUtils.isEmpty(shipping)) {
            dataBinding.mailingAddress.setText(getConfigString(
                    MessageProvider.error_no_mailing_address));
            dataBinding.btnEditMailingAddress.setVisibility(View.GONE);
            dataBinding.btnAddMailingAddress.setVisibility(View.VISIBLE);
        } else {
            dataBinding.mailingAddress.setText(shipping);
            dataBinding.btnEditMailingAddress.setVisibility(View.VISIBLE);
            dataBinding.btnAddMailingAddress.setVisibility(View.GONE);
        }
    }

    private void updateBillingAddress(String billing) {
        if (TextUtils.isEmpty(billing)) {
            dataBinding.billingAddress.setText(getConfigString(MessageProvider.error_no_billing_address));
            dataBinding.btnEditBillingAddress.setVisibility(View.GONE);
            dataBinding.btnAddBillingAddress.setVisibility(View.VISIBLE);
        } else {
            dataBinding.billingAddress.setText(billing);
            dataBinding.btnEditBillingAddress.setVisibility(View.VISIBLE);
            dataBinding.btnAddBillingAddress.setVisibility(View.GONE);
        }
    }

    private void setUpProfileView(UserDetails user) {
        GlideHelper.setImage(this, dataBinding.profileImage, user.profileImagePath, R.drawable.et_fallback_image);

        dataBinding.tilFirstName.getEditText().setText(user.firstName);
        dataBinding.tilLastName.getEditText().setText(user.lastName);
        if (user.isMale()) {
            dataBinding.gender.check(R.id.genderMale);
        } else if (user.isFeMale()) {
            dataBinding.gender.check(R.id.genderFemale);
        }

        dataBinding.tilEmail.getEditText().setText(user.email);

        if (TextUtils.isEmpty(user.phone)) {
            dataBinding.tilPhone.getEditText().setText(user.billingPhone);
        } else {
            dataBinding.tilPhone.getEditText().setText(user.phone);
        }

        if (!TextUtils.isEmpty((user.dateOfBirth)) && user.dateOfBirth.length() > 4) {
            dataBinding.tilDob.getEditText().setText(user.dateOfBirth);
        }

        if (AppUtils.isTrue(user.everBeenTrack)) {
            dataBinding.trackYes.setChecked(true);
            dataBinding.trackNo.setChecked(false);
        } else {
            dataBinding.trackYes.setChecked(false);
            dataBinding.trackNo.setChecked(true);
        }

        if (AppUtils.isTrue(user.raceLicence)) {
            dataBinding.licenceYes.setChecked(true);
            dataBinding.licenceNo.setChecked(false);
        } else {
            dataBinding.licenceYes.setChecked(false);
            dataBinding.licenceNo.setChecked(true);
        }

        dataBinding.tilMotorcycle.getEditText().setText(user.motorcycle);
        dataBinding.tilMotorcycleNumber.getEditText().setText(user.motorcycleNumber);


        if(isEvApp() || user.isCoach() || user.isAdmin()){
            dataBinding.tilSkillLevel.getEditText().setText(user.skillLevel);
        }else{
            if(StringUtils.isEmpty(user.motoSkill)){
                dataBinding.tilSkillLevel.getEditText().setText("-");

            }else {
                dataBinding.tilSkillLevel.getEditText().setText(user.motoSkill);
            }
        }




        dataBinding.tilEmergencyFirstName.getEditText().setText(user.emergencyFirstName);
        dataBinding.tilEmergencyLastName.getEditText().setText(user.emergencyLastName);
        dataBinding.tilEmergencyPhone.getEditText().setText(user.emergencyPhone);
        dataBinding.tilEmergencyRelationship.getEditText().setText(user.emergencyRelationship);

        //MotoInfo
        dataBinding.tilMotoAmaExpiry.getEditText().setText(user.motoAmaExpiryDate);
        dataBinding.tilMotoAmaNo.getEditText().setText(user.motoAmaNo);
        dataBinding.tilMotoRaceNo.getEditText().setText(user.motoRaceNo);
        dataBinding.tilMotoAsraNumber.getEditText().setText(user.motoAsraNo);
        dataBinding.tilMotoCcsNumber.getEditText().setText(user.motoCCSNo);
        dataBinding.tilMotoSponsors.getEditText().setText(user.motoSponsors);
        dataBinding.tilMotoTeammates.getEditText().setText(user.motoTeamMates);
        dataBinding.tilMotoNationality.getEditText().setText(user.nationality);

    }

    @Override
    public String getTitle() {
        return getString(R.string.title_edit_profile);
    }

    private void showDatePicker(SimpleDate dob, View requester) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), this, dob.year, dob.month, dob.dayOfMonth);
        datePickerDialog.updateDate(dob.year, dob.month, dob.dayOfMonth);
        datePickerDialog.getDatePicker().setTag(requester);
        //datePickerDialog.getDatePicker().setMaxDate(DateUtils.convertToDate("2013-12-31", DateUtils.SIMPLE_DATE_NO_TIME).getTime());

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        month = month + 1; //Picker starts months from zero.
        View requester = (View) datePicker.getTag();
        String date = String.format(Locale.US, "%d-%d-%d", year, month, day);
        if (requester != null) {
            if(requester.getId() == dataBinding.tilDob.getId()) {
                dataBinding.tilDob.getEditText().setText(date);
                viewModel.updateDoB(year, month, day);
            }else if(requester.getId() == dataBinding.tilMotoAmaExpiry.getId()) {
                dataBinding.tilMotoAmaExpiry.getEditText().setText(date);
                viewModel.updateAmaExpiry(year, month, day);
            }
        }
    }

    private void scrollTop() {
        dataBinding.scrollContainer.post(() ->
                dataBinding.scrollContainer.fullScroll(ScrollView.FOCUS_UP));
    }

    private void scrollToView(View v) {
        dataBinding.scrollContainer.post(() ->
                dataBinding.scrollContainer
                        .smoothScrollTo((int) v.getX(), (int) v.getY()));
    }

    private UserDetails validateData() {
        UserDetails userDetails = new UserDetails();

        if (TextUtils.isEmpty(dataBinding.tilFirstName.getEditText().getText())) {
            dataBinding.tilFirstName.setError(getString(R.string.error_first_name_empty));
            scrollTop();
            return null;
        } else {
            dataBinding.tilFirstName.setError(null);
            userDetails.firstName = dataBinding.tilFirstName.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.tilLastName.getEditText().getText())) {
            dataBinding.tilLastName.setError(getString(R.string.error_last_name_empty));
            scrollTop();
            return null;
        } else {
            dataBinding.tilLastName.setError(null);
            userDetails.lastName = dataBinding.tilLastName.getEditText().getText().toString();
        }

        if (!UiUtils.isValidPhone(dataBinding.tilPhone.getEditText().getText().toString())) {
            dataBinding.tilPhone.setError(getString(R.string.error_invalid_phone));
            scrollTop();
            return null;
        } else {
            dataBinding.tilPhone.setError(null);
            userDetails.phone = dataBinding.tilPhone.getEditText().getText().toString();
        }

        if (!UiUtils.isValidEmail(dataBinding.tilEmail.getEditText().getText().toString())) {

            dataBinding.tilEmail.setError(getString(R.string.error_invalid_email));
            scrollTop();
            return null;
        } else {
            dataBinding.tilEmail.setError(null);
            userDetails.email = dataBinding.tilEmail.getEditText().getText().toString();
        }

        if (dataBinding.tilDob.getEditText().getText().length() < 4) {

            dataBinding.tilDob.setError(getString(R.string.error_invalid_dob));
            scrollToView(dataBinding.tilDob);
            return null;
        } else {
            dataBinding.tilDob.setError(null);
            userDetails.dateOfBirth = dataBinding.tilDob.getEditText().getText().toString();

        }

        if (TextUtils.isEmpty(dataBinding.tilMotorcycle.getEditText().getText())) {

            dataBinding.tilMotorcycle.setError(getString(R.string.error_invalid_motorcycle));
            scrollToView(dataBinding.tilMotorcycle);
            return null;
        } else {
            dataBinding.tilMotorcycle.setError(null);
            userDetails.motorcycle = dataBinding.tilMotorcycle.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.tilMotorcycleNumber.getEditText().getText())) {

            dataBinding.tilMotorcycleNumber.setError(getString(R.string.error_invalid_motorcycle_number));
            scrollToView(dataBinding.tilMotorcycleNumber);
            return null;
        } else {
            dataBinding.tilMotorcycleNumber.setError(null);
            userDetails.motorcycleNumber = dataBinding.tilMotorcycleNumber.getEditText().getText().toString();
        }


        if(dataBinding.motoInfoGroup.getVisibility() == View.VISIBLE){
            if(!validateMotoProfileData(userDetails)){
                return null;
            }
        }
        if (TextUtils.isEmpty(dataBinding.tilEmergencyFirstName.getEditText().getText())) {

            dataBinding.tilEmergencyFirstName.setError(getString(R.string.error_emergency_first_name_empty));
            return null;
        } else {
            dataBinding.tilEmergencyFirstName.setError(null);
            userDetails.emergencyFirstName = dataBinding.tilEmergencyFirstName.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.tilEmergencyLastName.getEditText().getText())) {

            dataBinding.tilEmergencyLastName.setError(getString(R.string.error_emergency_last_name_empty));
            return null;
        } else {
            dataBinding.tilEmergencyLastName.setError(null);
            userDetails.emergencyLastName = dataBinding.tilEmergencyLastName.getEditText().getText().toString();
        }

        if (!UiUtils.isValidPhone(dataBinding.tilEmergencyPhone.getEditText().getText().toString())) {

            dataBinding.tilEmergencyPhone.setError(getString(R.string.error_invalid_emergency_phone));
            return null;
        } else {
            dataBinding.tilEmergencyPhone.setError(null);
            userDetails.emergencyPhone = dataBinding.tilEmergencyPhone.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.tilEmergencyRelationship.getEditText().getText())) {
            dataBinding.tilEmergencyRelationship.setError(getString(R.string.error_invalid_emergency_relation));
            return null;
        } else {
            dataBinding.tilEmergencyPhone.setError(null);
            userDetails.emergencyRelationship = dataBinding.tilEmergencyRelationship.getEditText().getText().toString();
        }



        int selectedId = dataBinding.gender.getCheckedRadioButtonId();
        RadioButton radioButton = dataBinding.gender.findViewById(selectedId);
        userDetails.gender = radioButton.getText().toString();


        selectedId = dataBinding.trackCheck.getCheckedRadioButtonId();
        radioButton = dataBinding.trackCheck.findViewById(selectedId);
        String everBeenTrackCheck = radioButton.getText().toString();
        userDetails.everBeenTrack = AppUtils.isTrue(everBeenTrackCheck)
                ? "1" : "0";

        selectedId = dataBinding.raceLicence.getCheckedRadioButtonId();
        radioButton = dataBinding.raceLicence.findViewById(selectedId);
        String hasRaceLicence = radioButton.getText().toString();
        userDetails.raceLicence = AppUtils.isTrue(hasRaceLicence)
                ? "1" : "0";

        return userDetails;
    }

    private boolean validateMotoProfileData(UserDetails userDetails) {

        /*
        UiUtils.hideKeyboard(getActivity());
        String data = dataBinding.tilMotoRaceNo.getEditText().getText().toString();
        if (StringUtils.isEmpty(data)) {
            dataBinding.tilMotoRaceNo.setError(getString(R.string.error_invalid_race_no));
            return false;
        } else {
            dataBinding.tilMotoRaceNo.setError(null);
            userDetails.motoRaceNo = data;
        }

        data = dataBinding.tilMotoAmaNo.getEditText().getText().toString();
        if (StringUtils.isEmpty(data)) {
            dataBinding.tilMotoAmaNo.setError(getString(R.string.error_invalid_ama_no));
            return false;
        } else {
            dataBinding.tilMotoAmaNo.setError(null);
            userDetails.motoAmaNo = data;
        }

        data = dataBinding.tilMotoAmaExpiry.getEditText().getText().toString();
        if (StringUtils.isEmpty(data)) {
            dataBinding.tilMotoAmaExpiry.setError(getString(R.string.error_invalid_ama_expiry));
            return false;
        } else {
            dataBinding.tilMotoAmaExpiry.setError(null);
            userDetails.motoAmaExpiryDate = data;
        }

        data = dataBinding.tilMotoAsraNumber.getEditText().getText().toString();
        if (StringUtils.isEmpty(data)) {
            dataBinding.tilMotoAsraNumber.setError(getString(R.string.error_invalid_asra_no));
            return false;
        } else {
            dataBinding.tilMotoAsraNumber.setError(null);
            userDetails.motoAsraNo = data;
        }

        data = dataBinding.tilMotoCcsNumber.getEditText().getText().toString();
        if (StringUtils.isEmpty(data)) {
            dataBinding.tilMotoCcsNumber.setError(getString(R.string.error_invalid_ccs_no));
            return false;
        } else {
            dataBinding.tilMotoCcsNumber.setError(null);
            userDetails.motoCCSNo = data;
        }

        data = dataBinding.tilMotoNationality.getEditText().getText().toString();
        if (StringUtils.isEmpty(data)) {
            dataBinding.tilMotoNationality.setError(getString(R.string.error_invalid_nationality));
            return false;
        } else {
            dataBinding.tilMotoNationality.setError(null);
            userDetails.nationality = data;
        }

        data = dataBinding.tilMotoSponsors.getEditText().getText().toString();
        if (StringUtils.isEmpty(data)) {
            dataBinding.tilMotoSponsors.setError(getString(R.string.error_invalid_sponsors));
            return false;
        } else {
            dataBinding.tilMotoSponsors.setError(null);
            userDetails.motoSponsors = data;
        }

        data = dataBinding.tilMotoTeammates.getEditText().getText().toString();
        if (StringUtils.isEmpty(data)) {
            dataBinding.tilMotoTeammates.setError(getString(R.string.error_invalid_teammates));
            return false;
        } else {
            dataBinding.tilMotoTeammates.setError(null);
        }


         */

        userDetails.motoRaceNo = dataBinding.tilMotoRaceNo.getEditText().getText().toString();

        userDetails.motoAmaNo = dataBinding.tilMotoAmaNo.getEditText().getText().toString();


        userDetails.motoAmaExpiryDate = dataBinding.tilMotoAmaExpiry.getEditText().getText().toString();

        userDetails.motoAsraNo = dataBinding.tilMotoAsraNumber.getEditText().getText().toString();

        userDetails.motoCCSNo = dataBinding.tilMotoCcsNumber.getEditText().getText().toString();

        userDetails.nationality = dataBinding.tilMotoNationality.getEditText().getText().toString();

        userDetails.motoSponsors = dataBinding.tilMotoSponsors.getEditText().getText().toString();

        userDetails.motoTeamMates = dataBinding.tilMotoTeammates.getEditText().getText().toString();

        return true;
    }


    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.btnProfileCamera.setImageResource(R.drawable.camera_moto);
        dataBinding.tilDob.getEditText().setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.ic_moto_calendar, 0);

        dataBinding.tilMotoAmaExpiry.getEditText().setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.ic_moto_calendar, 0);

        dataBinding.btnEditMailingAddress.setImageResource(R.drawable.selector_button_edit_moto);
        dataBinding.btnEditBillingAddress.setImageResource(R.drawable.selector_button_edit_moto);

        dataBinding.btnAddMailingAddress.setImageResource(R.drawable.selector_button_plus_moto);
        dataBinding.btnAddBillingAddress.setImageResource(R.drawable.selector_button_plus_moto);


        dataBinding.btnSave.setBackgroundResource(R.drawable.selector_button_moto_primary);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.btnProfileCamera.setImageResource(R.drawable.ic_camera);
        dataBinding.tilDob.getEditText().setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.ic_calendar, 0);
        dataBinding.btnEditMailingAddress.setImageResource(R.drawable.selector_button_edit_ev);
        dataBinding.btnEditBillingAddress.setImageResource(R.drawable.selector_button_edit_ev);

        dataBinding.btnAddMailingAddress.setImageResource(R.drawable.selector_button_plus_moto);
        dataBinding.btnAddBillingAddress.setImageResource(R.drawable.selector_button_plus_moto);


        dataBinding.btnSave.setBackgroundResource(R.drawable.selector_button_primary);
    }

    @Override
    public boolean isToolbarMenuEnabled() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_only, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                onSaveUserProfile();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
