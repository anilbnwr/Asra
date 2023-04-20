package com.asra.mobileapp.ui.general.address;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.countrypicker.ETCountryPicker;
import com.asra.mobileapp.common.statepicker.ETStatePicker;
import com.asra.mobileapp.databinding.FragmentAddressEditBinding;
import com.asra.mobileapp.model.Address;
import com.asra.mobileapp.model.Country;
import com.asra.mobileapp.model.State;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.util.ListUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.asra.mobileapp.ui.general.address.AddressConstants.TYPE_BILLING;

public class AddressFragment extends ETFragment<AddressViewModel, FragmentAddressEditBinding> {

    private static final String CODE_REQUEST_TYPE = "com.evolvegt.mobileapp.type";
    private static final String CODE_NEW_ADDRESS = "com.evolvegt.mobileapp.new.address";

    private List<State> stateList;
    private List<Country> countryList;

    public static AddressFragment newInstance(int type, boolean newAddress) {
        AddressFragment fragemnt = new AddressFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(CODE_REQUEST_TYPE, type);
        arguments.putBoolean(CODE_NEW_ADDRESS, newAddress);


        fragemnt.setArguments(arguments);
        return fragemnt;
    }


    private int requestType;
    private boolean isNewAddress;


    @Override
    protected Class<AddressViewModel> getViewModel() {
        return AddressViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_address_edit;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestType = getArguments().getInt(CODE_REQUEST_TYPE, TYPE_BILLING);
        isNewAddress = getArguments().getBoolean(CODE_NEW_ADDRESS, false);
    }

    @Override
    public void initializeViews() {

        viewModel.setAddressType(requestType);
        dataBinding.addressBtnSubmit.setOnClickListener((View v) -> updateAddress());

        if (requestType == AddressConstants.TYPE_SHIPPING) {
            dataBinding.addressTilEmail.setHint(getString(R.string.hint_address_email_optional));
            dataBinding.addressTilPhone.setHint(getString(R.string.hint_address_phone_optional));
        }
    }

    private void updateAddress() {


        Address address = validateAddressFields();
        if (address != null) {
            if (requestType == TYPE_BILLING)
                viewModel.updateBillingAddress(address);
            else
                viewModel.updateShippingAddress(address);
        }

    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.userDetailsObservable.observe(this, this::setUpViews);
        viewModel.stateListObservable.observe(this, this::setUpStatePicker);
        viewModel.countryListObservable.observe(this, this::setUpCountryPicker);
        viewModel.stateListErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));

        viewModel.countrySelectionObservable.observe(this,
                country -> dataBinding.addressTilCountry.getEditText().setText(
                        country.name));

        viewModel.stateSelectionObservable.observe(this,
                state -> {
                    if (state != null)
                        dataBinding.addressTilState.getEditText().setText(
                                state.name);
                    else {
                        dataBinding.addressTilState.getEditText().setText("");
                    }
                });

        viewModel.addressUpdatedObservable.observe(this, this::showSuccessToast);
        viewModel.addressUpdateFailedObservable.observe(this, this::showErrorToast);
    }

    @Override
    public String getTitle() {

        String title;
        if (requestType == TYPE_BILLING) {
            title = isNewAddress ? getString(R.string.title_new_billing_address)
                    : getString(R.string.title_edit_billing_address);
        } else {
            title = isNewAddress ? getString(R.string.title_new_mailing_address)
                    : getString(R.string.title_edit_mailing_address);
        }
        return title;
    }

    private void setUpViews(UserDetails currentUser) {


        if (requestType == TYPE_BILLING) {

            dataBinding.addressTilFirstName.getEditText().setText(currentUser.billingFirstName);
            dataBinding.addressTilSecondName.getEditText().setText(currentUser.billingLastName);
            dataBinding.addressTilCompanyName.getEditText().setText(currentUser.billingCompany);
            dataBinding.addressTilCountry.getEditText().setText(currentUser.billingCountry);
            dataBinding.addressTilAddress1.getEditText().setText(currentUser.billingAddress1);
            dataBinding.addressTilAddress2.getEditText().setText(currentUser.billingAddress2);
            dataBinding.addressTilCity.getEditText().setText(currentUser.billingCity);
            dataBinding.addressTilState.getEditText().setText(currentUser.billingState);
            dataBinding.addressTilEmail.getEditText().setText(currentUser.billingEmail);
            dataBinding.addressTilPhone.getEditText().setText(currentUser.billingPhone);
            dataBinding.addressTilZip.getEditText().setText(currentUser.billingPostcode);

        } else {
            dataBinding.addressTilFirstName.getEditText().setText(currentUser.shippingFirstName);
            dataBinding.addressTilSecondName.getEditText().setText(currentUser.shippingLastName);
            dataBinding.addressTilCompanyName.getEditText().setText(currentUser.shippingCompany);
            dataBinding.addressTilCountry.getEditText().setText(currentUser.shippingCountry);
            dataBinding.addressTilAddress1.getEditText().setText(currentUser.shippingAddress1);
            dataBinding.addressTilAddress2.getEditText().setText(currentUser.shippingAddress2);
            dataBinding.addressTilCity.getEditText().setText(currentUser.shippingCity);
            dataBinding.addressTilState.getEditText().setText(currentUser.shippingState);
            dataBinding.addressTilEmail.getEditText().setText(currentUser.shippingEmail);
            dataBinding.addressTilPhone.getEditText().setText(currentUser.shippingPhone);
            dataBinding.addressTilZip.getEditText().setText(currentUser.shippingPostcode);

        }
    }

    private void setUpCountryPicker(List<Country> supportedCountries) {

        this.countryList = supportedCountries;
        dataBinding.dummyCountryView.setOnClickListener((View v) -> {
            if (ListUtils.isEmpty(supportedCountries)) {
                return;
            }

            ETCountryPicker countryPicker =
                    new ETCountryPicker(getContext(), countryList, (country, flagResId) -> {
                        viewModel.onCountrySelected(country);
                        dataBinding.addressTilCountry.getEditText().setText(country.shortCode);
                    });

            countryPicker.show();
        });

    }

    private void setUpStatePicker(List<State> statesInSelectedCountry) {
        this.stateList = statesInSelectedCountry;
        dataBinding.dummyStateView.setOnClickListener((View v) -> {
            if (stateList == null || stateList.isEmpty()) {
                showErrorToast(getConfigString(MessageProvider.error_generic_message));
                return;
            }
            ETStatePicker countryPicker =
                    new ETStatePicker(getContext(), stateList, (state, flagResId) -> {
                        viewModel.onStateSelected(state);
                        dataBinding.addressTilState.getEditText().setText(state.name);

                    });
            countryPicker.show();
        });

    }

    private Address validateAddressFields() {

        Address address = new Address();
        boolean validAddress = true;
        if (TextUtils.isEmpty(dataBinding.addressTilFirstName.getEditText().getText())) {
            dataBinding.addressTilFirstName.setError(getString(R.string.error_billing_first_name));
            validAddress = false;
        } else {
            dataBinding.addressTilFirstName.setError(null);
            address.firstName = dataBinding.addressTilFirstName.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.addressTilSecondName.getEditText().getText())) {
            validAddress = false;
            dataBinding.addressTilSecondName.setError(getString(R.string.error_billing_last_name));
        } else {
            dataBinding.addressTilSecondName.setError(null);
            address.lastName = dataBinding.addressTilSecondName.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.addressTilCountry.getEditText().getText())) {
            validAddress = false;
            dataBinding.addressTilCountry.setError(getString(R.string.error_billing_country));
        } else {
            dataBinding.addressTilCountry.setError(null);
        }

        if (TextUtils.isEmpty(dataBinding.addressTilAddress1.getEditText().getText())) {
            validAddress = false;
            dataBinding.addressTilAddress1.setError(getString(R.string.error_billing_address1));
        } else {
            dataBinding.addressTilAddress1.setError(null);
            address.address1 = dataBinding.addressTilAddress1.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.addressTilCity.getEditText().getText())) {
            validAddress = false;
            dataBinding.addressTilCity.setError(getString(R.string.error_billing_city));
        } else {
            dataBinding.addressTilCity.setError(null);
            address.city = dataBinding.addressTilCity.getEditText().getText().toString();
        }

        String selectedState = dataBinding.addressTilCountry.getEditText().getText().toString();
        if (TextUtils.isEmpty(selectedState)) {
            validAddress = false;
            dataBinding.addressTilState.setError(getString(R.string.error_billing_state));
        } else {
            dataBinding.addressTilState.setError(null);
        }


        String zipCode = dataBinding.addressTilZip.getEditText().getText().toString();
        if (TextUtils.isEmpty(zipCode) || zipCode.length() < AddressConstants.MIN_ZIPCODE_LENGTH) {
            validAddress = false;
            dataBinding.addressTilZip.setError(getString(R.string.error_billing_zip));
        } else {
            dataBinding.addressTilZip.setError(null);
            address.postalCode = zipCode;
        }

        if (requestType == TYPE_BILLING) {
            if (!UiUtils.isValidEmail(dataBinding.addressTilEmail.getEditText().getText().toString())) {
                validAddress = false;
                dataBinding.addressTilEmail.setError(getString(R.string.error_billing_email));
            } else {
                dataBinding.addressTilEmail.setError(null);
                address.email = dataBinding.addressTilEmail.getEditText().getText().toString();
            }


            if (!UiUtils.isValidPhone(dataBinding.addressTilPhone.getEditText().getText().toString())) {
                validAddress = false;
                dataBinding.addressTilPhone.setError(getString(R.string.error_billing_phone));
            } else {
                dataBinding.addressTilPhone.setError(null);
                address.phone = dataBinding.addressTilPhone.getEditText().getText().toString();
            }
        }
        return validAddress ? address : null;
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
                updateAddress();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.addressBtnSubmit.setBackgroundResource(R.drawable.selector_button_primary);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.addressBtnSubmit.setBackgroundResource(R.drawable.selector_button_moto_primary);
    }
}
