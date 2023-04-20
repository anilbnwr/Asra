package com.asra.mobileapp.ui.admin.signature;

import android.os.Bundle;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.databinding.FragmentSignatureBinding;
import com.asra.mobileapp.ui.base.ETFragment;

import androidx.core.text.HtmlCompat;

public class SignatureFragment extends ETFragment<SignatureViewModel, FragmentSignatureBinding>{

    private static final String KEY_SIGNATURE = "com.evolvegt.mobileapp.signature";

    public static SignatureFragment newInstance(String jsonSignature) {
        SignatureFragment fragment = new SignatureFragment();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_SIGNATURE, jsonSignature);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected Class getViewModel() {
        return SignatureViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_signature;
    }

    @Override
    public void initializeViews() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            String json = bundle.getString(KEY_SIGNATURE);
            viewModel.processEventParticipantJson(json);
        }

        dataBinding.btnRedrawSign.setVisibility(View.GONE);
        showBackButton();

        setUpListeners();


    }

    private void setUpListeners(){
        dataBinding.btnClearSignPad.setOnClickListener((View v)-> dataBinding.signaturePad.clear());
        dataBinding.btnCloseSignPad.setOnClickListener((View v)-> getActivity().onBackPressed());
        dataBinding.btnCloseSignView.setOnClickListener((View v)-> getActivity().onBackPressed());
        dataBinding.btnSaveSign.setOnClickListener((View v)-> onSaveButtonClicked());

        dataBinding.btnRedrawSign.setOnClickListener((View v)-> setUpSignaturePad());

        dataBinding.cbSignatureDisclaimer.setOnCheckedChangeListener((compoundButton, b) -> dataBinding.btnSaveSign.setEnabled(b));
    }

    private void setUpSignatureView(){
        dataBinding.signaturePage.setVisibility(View.GONE);
        dataBinding.signatureViewContainer.setVisibility(View.VISIBLE);

        viewModel.fetchUserSignature();
    }

    private void onSaveButtonClicked(){
        if(dataBinding.cbSignatureDisclaimer.isChecked()) {
            viewModel.submitSignature(dataBinding.signaturePad.getSignatureBitmap());

        }else{
            showErrorMessage("Please read and accept the agreement.");
        }
    }

    private void setUpSignaturePad(){
        dataBinding.signaturePage.setVisibility(View.VISIBLE);
        dataBinding.signatureViewContainer.setVisibility(View.GONE);

        dataBinding.btnSaveSign.setEnabled(dataBinding.cbSignatureDisclaimer.isChecked());
    }
    @Override
    public void observeEventsFromViewModel() {

        viewModel.participantObservable.observe(this, participant -> {
            if(participant.isSignatureAvailable()){
                setUpSignatureView();
            }else{
                setUpSignaturePad();
            }
        });

        viewModel.desclaimerObservable.observe(this, disclaimer ->
                dataBinding.signatureDisclaimerDescription.setText(HtmlCompat.fromHtml(disclaimer,
                        HtmlCompat.FROM_HTML_MODE_LEGACY)));

        viewModel.signatureSavedObservable.observe(this, saved ->{
                if(saved)
                    showSignatureSavedDialog(getConfigString(MessageProvider.msg_signature_saved));
                });

        viewModel.signatureSaveFailureObservable.observe(this, this::showErrorMessage);

        viewModel.signatureFetchObservable.observe(this, signature ->
                dataBinding.signatureView.setImageBitmap(signature));
        viewModel.signatureFetchFailureObservable.observe(this, this::showErrorMessage);
    }

    private void showSignatureSavedDialog(String message) {
        ETDialog dialog = new ETDialog(getActivity(), new ETDialog.DialogListener() {
            @Override
            public void onPositiveButtonClicked() {
                super.onPositiveButtonClicked();
                popFragment();
            }
        });
        dialog.setDialogTitle("Signature Saved");
        dialog.setDialogMessage(message);
        dialog.show();

    }

    @Override
    public String getTitle() {
        return getString(R.string.title_signature);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.btnSaveSign.setBackgroundResource(R.drawable.selector_button_primary);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.btnSaveSign.setBackgroundResource(R.drawable.selector_button_moto_primary);
    }
}
