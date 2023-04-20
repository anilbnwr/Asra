package com.asra.mobileapp.ui.general.membershipdetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.databinding.FragmentMembershipDetailsBinding;
import com.asra.mobileapp.model.MembershipDetail;
import com.asra.mobileapp.ui.base.ETFragment;

public class MembershipDetailsFragment extends ETFragment<MembershipDetailsViewModel,
        FragmentMembershipDetailsBinding> {

    private static final String MEMBERSHIP_SLUG = "membership.slug";

    public static MembershipDetailsFragment newInstance(String slug){
        MembershipDetailsFragment fragment = new MembershipDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MEMBERSHIP_SLUG, slug);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected Class<MembershipDetailsViewModel> getViewModel() {
        return MembershipDetailsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_membership_details;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_membership_detail);
    }



    @Override
    public void initializeViews() {

        dataBinding.btnUpgrade.setOnClickListener(view -> {
            viewModel.addMembershipToCart();
        });
        setupWebView();
        String slug = getArguments().getString(MEMBERSHIP_SLUG, "");
        viewModel.fetchMembershipDetails(slug);
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.membershipDetailObservable.observe(this, this::setUpView);
        viewModel.membershipErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));
        viewModel.canPurchaseMembershipObservable.observe(this,
                canPurchase -> dataBinding.btnUpgrade
                        .setVisibility(canPurchase ? View.VISIBLE : View.GONE));

        viewModel.membershipAddedToCartObservable.observe(this, this::showSuccessMessage);
        viewModel.membershipCartErrorObservable.observe(this, this::showErrorMessage);

        viewModel.mrlConfirmationObservable.observe(this, mrlMessage -> {
            showSimpleAlert(mrlMessage.title, mrlMessage.message, "I Agree", new ETDialog.DialogListener() {
                @Override
                public void onPositiveButtonClicked() {
                    super.onPositiveButtonClicked();
                    viewModel.onUpgrade();
                }
            });
        });
    }

    private void setUpView(MembershipDetail membershipDetail) {

        GlideHelper.setImage(this, dataBinding.membershipImage, membershipDetail.getImage(),
                R.drawable.et_fallback_image, dataBinding.membershipProgressContainer);
        dataBinding.membershipDescription.loadData(membershipDetail.getDescription(), "text/html", "UTF-8");

        dataBinding.btnUpgrade.setEnabled(!membershipDetail.isOutOfStock());

        if(membershipDetail.isOutOfStock()) {
            dataBinding.btnUpgrade.setText(getString(R.string.tracks_label_sold_out));
        }

    }
    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        dataBinding.membershipDescription.setWebChromeClient(new WebChromeClient());
        dataBinding.membershipDescription.setWebViewClient(new WebViewClient());
        dataBinding.membershipDescription.clearCache(true);
        dataBinding.membershipDescription.clearHistory();
        dataBinding.membershipDescription.getSettings().setJavaScriptEnabled(true);
        dataBinding.membershipDescription.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.btnUpgrade.setBackgroundResource(R.drawable.selector_button_primary);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.btnUpgrade.setBackgroundResource(R.drawable.selector_button_moto_primary);
    }
}
