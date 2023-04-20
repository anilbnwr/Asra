package com.asra.mobileapp.ui.general.ewaiver.waiverdetails;

import android.os.Bundle;
import android.text.TextUtils;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.common.statepicker.ETStatePicker;
import com.asra.mobileapp.databinding.FragmentWaiverSignatureBinding;
import com.asra.mobileapp.model.State;
import com.asra.mobileapp.model.WaiverEventDetails;
import com.asra.mobileapp.network.retrofit.request.WaiverSaveRequest;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.general.ETFragmentHostActivity;

import java.util.List;

public class WaiverSignatureFragment extends ETFragment<WaiverDetailsViewMoel, FragmentWaiverSignatureBinding> {

    private List<State> waiverStates;

    private static final String KEY_EVENT_ID = "key.event.id";

    public static WaiverSignatureFragment newInstance(String eventId){

        WaiverSignatureFragment fragment = new WaiverSignatureFragment();
        Bundle args = new Bundle();
        args.putString(KEY_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected Class<WaiverDetailsViewMoel> getViewModel() {
        return WaiverDetailsViewMoel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_waiver_signature;
    }

    @Override
    public void initializeViews() {
        viewModel.getCachedWaiverDetails();
        dataBinding.tilLicenceIssuedState.getEditText().setOnClickListener(view -> {
            ETStatePicker statePicker =
                    new ETStatePicker(getContext(), waiverStates, (state, flagResId) -> {
                        viewModel.onStateSelected(state);
                        dataBinding.tilLicenceIssuedState.setError(null);
                        dataBinding.tilLicenceIssuedState.getEditText().setText(state.name);

                    });
            statePicker.show();
        });

        dataBinding.btnClearSignPad.setOnClickListener(view -> dataBinding.signaturePad.clear());
        dataBinding.btnSignature.setOnClickListener(view -> {
            WaiverSaveRequest request = getValidatedRequest();
            if(request != null){
                viewModel.saveSignature(dataBinding.signaturePad.getSignatureBitmap(), request);
            }
        });
    }

    private WaiverSaveRequest getValidatedRequest() {
        WaiverSaveRequest request = new WaiverSaveRequest();
        UiUtils.hideKeyboard(getActivity());
        if (TextUtils.isEmpty(dataBinding.tilNameLocation.getEditText().getText())) {
            dataBinding.tilNameLocation.setError(getString(R.string.error_invalid_name_n_location));
            return null;
        } else {
            dataBinding.tilNameLocation.setError(null);
            request.nameAndLocation = dataBinding.tilNameLocation.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.tilLicenseNumber.getEditText().getText())) {
            dataBinding.tilLicenseNumber.setError(getString(R.string.error_empty_license_number));
            return null;
        } else {
            dataBinding.tilLicenseNumber.setError(null);
            request.license = dataBinding.tilLicenseNumber.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.tilLicenceIssuedState.getEditText().getText())) {
            dataBinding.tilLicenceIssuedState.setError(getString(R.string.error_license_state));
            return null;
        } else {
            dataBinding.tilLicenceIssuedState.setError(null);
            request.license = dataBinding.tilLicenceIssuedState.getEditText().getText().toString();
        }

        request.eventId = getArguments().getString(KEY_EVENT_ID);
        return request;
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.waiverEventData.observe(this, this::setUpUi);
        viewModel.stateListObservable.observe(this, this::prepareStateOptions);

        viewModel.waiverSignSaved.observe(this, message -> showDialog(message, "OK", new ETDialog.DialogListener() {
            @Override
            public void onPositiveButtonClicked() {
                ((ETFragmentHostActivity)getBaseActivity()).clearTabStack();
            }
        }));

        viewModel.waiverSignError.observe(this, this::showErrorMessage);
    }

    private void prepareStateOptions(List<State> waiverStates) {
        this.waiverStates = waiverStates;
    }

    private void setUpUi(WaiverEventDetails.WaiverData waiverData) {


        dataBinding.eventName.setText(waiverData.title);
        dataBinding.eventDate.setText(waiverData.date);
        dataBinding.eventHostedBy.setText(String.format("%s%s", getString(R.string.hosted_by), waiverData.hostedBy));

        String imageUrl = UiUtils.getETAbsoluteURL(waiverData.logo);
        GlideHelper.setImage(dataBinding.eventImage, imageUrl, dataBinding.eventImageProgressContainer);

    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.eventName.setTextColor(UiUtils.getPrimaryColor(getContext()));
        dataBinding.btnSignature.setBackgroundResource(R.drawable.selector_button_primary);

    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.eventName.setTextColor(UiUtils.getPrimaryColor(getContext()));
        dataBinding.btnSignature.setBackgroundResource(R.drawable.selector_button_moto_primary);

    }
}
