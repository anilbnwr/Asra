package com.asra.mobileapp.ui.login.regisrtation;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.FragmentRegistrationStep1Binding;
import com.asra.mobileapp.network.retrofit.request.SignUpRequest;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.util.StringUtils;

import java.util.Locale;
import java.util.Objects;

public class RegistrationStep1Fragment
        extends ETFragment<RegistrationViewModel, FragmentRegistrationStep1Binding> {


    SignUpRequest request = new SignUpRequest();

    public static RegistrationStep1Fragment newInstance() {
        return new RegistrationStep1Fragment();
    }

    @Override
    protected Class<RegistrationViewModel> getViewModel() {
        return RegistrationViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_registration_step1;
    }

    @Override
    public void initializeViews() {
        dataBinding.btnSave.setOnClickListener(view -> {
            SignUpRequest request = getValidatedData();
            if (request != null) {
                loadFragment(RegistrationStep2Fragment.newInstance(request));
            }
        });

        Objects.requireNonNull(dataBinding.tilDob.getEditText()).setOnClickListener((View v) -> {
            showDatePicker();
        });

        showToolbar();
        showBackButton();

        dataBinding.tilFirstName.getEditText().setText(request.firstName);
        dataBinding.tilLastName.getEditText().setText(request.lastName);
        dataBinding.tilEmail.getEditText().setText(request.email);
        dataBinding.tilPhone.getEditText().setText(request.phone);
        if (!StringUtils.isEmpty(request.dob)) {
            String formattedDate = DateUtils.getDateAsString(request.dob,
                    DateUtils.YYYY_MM_DD_DATE_PATTERN, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
            dataBinding.tilDob.getEditText().setText(formattedDate);
        }
        dataBinding.genderMale.setChecked("Male".equalsIgnoreCase(request.gender));
        dataBinding.genderFemale.setChecked("Female".equalsIgnoreCase(request.gender));
        dataBinding.genderUnspecified.setChecked("Unspecified".equalsIgnoreCase(request.gender));
    }

    private void showDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), (datePicker, year, month, day) -> {
                    UiUtils.hideKeyboard(getActivity());
            request.dob = String.format(Locale.US, "%d-%d-%d", year, month+1, day);
            String formattedDate = DateUtils.getDateAsString(request.dob,
                    DateUtils.YYYY_MM_DD_DATE_PATTERN, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
            dataBinding.tilDob.getEditText().setText(formattedDate);
            dataBinding.tilDob.setError(null);
        }, 2000, 0, 1);
        datePickerDialog.getDatePicker().setMaxDate(DateUtils.convertToDate("2013-12-31", DateUtils.SIMPLE_DATE_NO_TIME).getTime());
        datePickerDialog.show();
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

    private SignUpRequest getValidatedData() {

        UiUtils.hideKeyboard(getActivity());


        if (TextUtils.isEmpty(dataBinding.tilFirstName.getEditText().getText())) {
            dataBinding.tilFirstName.setError(getString(R.string.error_first_name_empty));
            scrollTop();
            return null;
        } else {
            dataBinding.tilFirstName.setError(null);
            request.firstName = dataBinding.tilFirstName.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.tilLastName.getEditText().getText())) {
            dataBinding.tilLastName.setError(getString(R.string.error_last_name_empty));
            scrollTop();
            return null;
        } else {
            dataBinding.tilLastName.setError(null);
            request.lastName = dataBinding.tilLastName.getEditText().getText().toString();
        }


        if (!UiUtils.isValidEmail(dataBinding.tilEmail.getEditText().getText().toString())) {

            dataBinding.tilEmail.setError(getString(R.string.error_invalid_email));
            scrollTop();
            return null;
        } else {
            dataBinding.tilEmail.setError(null);
            request.email = dataBinding.tilEmail.getEditText().getText().toString();
            request.confirmEmail = request.email;
        }

        if (dataBinding.tilDob.getEditText().getText().length() < 4) {

            dataBinding.tilDob.setError(getString(R.string.error_invalid_dob));
            scrollToView(dataBinding.tilDob);
            return null;
        } else {
            dataBinding.tilDob.setError(null);

        }

        if (!UiUtils.isValidPhone(dataBinding.tilPhone.getEditText().getText().toString())) {
            dataBinding.tilPhone.setError(getString(R.string.error_invalid_phone));
            scrollToView(dataBinding.tilPhone);
            return null;
        } else {
            dataBinding.tilPhone.setError(null);
            request.phone = dataBinding.tilPhone.getEditText().getText().toString();
        }


        request.gender = dataBinding.genderMale.isChecked() ? "Male" : "Female";

        return request;
    }

    @Override
    public void observeEventsFromViewModel() {

    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.tilDob.getEditText().setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.ic_calendar, 0);
        dataBinding.btnSave.setBackgroundResource(R.drawable.selector_button_primary);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.tilDob.getEditText().setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.ic_moto_calendar, 0);
        dataBinding.btnSave.setBackgroundResource(R.drawable.selector_button_moto_primary);

    }

    @Override
    public String getTitle() {
        return getString(R.string.title_sign_up);
    }
}
