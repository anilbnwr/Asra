package com.asra.mobileapp.ui.general.ewaiver.waiverdetails;

import android.os.Bundle;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.databinding.FragementWaiverDetailsBinding;
import com.asra.mobileapp.model.WaiverEventDetails;
import com.asra.mobileapp.ui.base.ETFragment;

public class WaiverDetailsFragment extends ETFragment<WaiverDetailsViewMoel, FragementWaiverDetailsBinding> {



    private static final String KEY_EVENT_ID = "key.event.id";
    public static WaiverDetailsFragment newInstance(String eventId){
        WaiverDetailsFragment fragment = new WaiverDetailsFragment();
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
        return R.layout.fragement_waiver_details;
    }

    @Override
    public void initializeViews() {
        dataBinding.btnSignature.setOnClickListener(view -> loadFragment(WaiverSignatureFragment.newInstance(getArguments().getString(KEY_EVENT_ID))));

        dataBinding.cbAgreeTerms.setOnCheckedChangeListener((compoundButton, b) -> dataBinding.btnSignature.setEnabled(b));

        viewModel.fetchWaiverDetails(getArguments().getString(KEY_EVENT_ID));
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.waiverEventData.observe(this, this::setUpUi);
        viewModel.waiverDetailsError.observe(this, message -> showEmptyMessage(true, message));
    }


    private void setUpUi(WaiverEventDetails.WaiverData waiverData) {

        dataBinding.cbAgreeTerms.setText(waiverData.checkboxLabel);
        dataBinding.btnSignature.setText(waiverData.buttonText);
        dataBinding.webTermsNConditions.loadData(waiverData.termsHtml, "text/html; charset=utf-8", "UTF-8");

        dataBinding.eventName.setText(waiverData.title);
        dataBinding.eventDate.setText(String.format("Date: %s", waiverData.date));
        dataBinding.eventHostedBy.setText(String.format("%s%s", getString(R.string.hosted_by), waiverData.hostedBy));

        String imageUrl = UiUtils.getETAbsoluteURL(waiverData.logo);
        GlideHelper.setImage(dataBinding.eventImage, imageUrl, dataBinding.eventImageProgressContainer);

    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.eventName.setTextColor(UiUtils.getPrimaryColor(getContext()));
        dataBinding.btnSignature.setBackgroundResource(R.drawable.selector_button_moto_primary);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.eventName.setTextColor(UiUtils.getPrimaryColor(getContext()));
        dataBinding.btnSignature.setBackgroundResource(R.drawable.selector_button_primary);
    }
}