package com.asra.mobileapp.ui.general.policy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.databinding.ActivityPolicyAgreementBinding;
import com.asra.mobileapp.model.PolicyAgreement;
import com.asra.mobileapp.ui.base.BaseActivity;
import com.asra.mobileapp.ui.base.model.ProgressData;
import com.asra.mobileapp.util.ResourceFetcher;

import androidx.annotation.Nullable;

public class PolicyActivity extends BaseActivity<ActivityPolicyAgreementBinding, PolicyViewModel> {

    public static final String  KEY_AGREEMENT = "agreemenet";
    public static final String  KEY_AGREEMENT_STATUS = "agreemenet.status";

    @Override
    public View getInfoContainer() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_policy_agreement;
    }

    @Override
    protected Class<PolicyViewModel> getViewModel() {
        return PolicyViewModel.class;
    }

    @Override
    public void showProgressBar(ProgressData progressData) {
        if (progressData == null || !progressData.show) {
            dataBinding.progressContainer.setVisibility(View.GONE);
        } else {
            dataBinding.progressContainer.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(progressData.progressText)) {
                dataBinding.progressMessage.setText(progressData.progressText);
            }else{
                dataBinding.progressMessage.setText("");
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();

        PolicyAgreement agreement = getIntent().getParcelableExtra(KEY_AGREEMENT);
        String content = "";
        String btnText = "";
        String acceptClause = "";
        if(agreement != null) {

            content = agreement.content;
            btnText = agreement.agreementButtonTitle;
            acceptClause = agreement.acceptTermsTitle;
        }

        btnText = TextUtils.isEmpty(btnText) ? "Save Agreement" : btnText;
        acceptClause = TextUtils.isEmpty(acceptClause) ? "I Accept to the Terms and Conditions" : acceptClause;
        if(TextUtils.isEmpty(content)){
            ResourceFetcher resourceFetcher = new ResourceFetcher(this);
            content = resourceFetcher.readFileFromRawDirectory(R.raw.evolve_policy);
        }


        content = content.replace("\\n", "");
        content = content.replace("\\t", "");
        dataBinding.btnAgree.setText(btnText);
        dataBinding.policyReadCheck.setText(acceptClause);
        dataBinding.policyContentView.loadData(content, "text/html; charset=utf-8", "UTF-8");

        dataBinding.policyReadCheck.setOnCheckedChangeListener((compoundButton, b) -> dataBinding.btnAgree.setEnabled(b));


        dataBinding.btnAgree.setOnClickListener(view1 -> {
           viewModel.acceptPolicyAgreement(true);
        });

        viewModel.policyAgreementSuccess.observe(this, status ->{
            Intent resultIntent = new Intent();
            resultIntent.putExtra(KEY_AGREEMENT_STATUS, true);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        viewModel.policyCheckFailed.observe(this, message ->{
            ETDialog dialog = showDialog(message, "Quit", new ETDialog.DialogListener() {
                @Override
                public void onPositiveButtonClicked() {
                    super.onPositiveButtonClicked();
                    finish();
                }
            });
            dialog.setCancelable(false);
        });
    }
}
