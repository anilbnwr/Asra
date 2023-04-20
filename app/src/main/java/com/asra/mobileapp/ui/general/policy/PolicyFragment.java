package com.asra.mobileapp.ui.general.policy;

import android.os.Bundle;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentPolicyAgreementBinding;
import com.asra.mobileapp.model.PolicyAgreement;
import com.asra.mobileapp.ui.base.ETFragment;

public class PolicyFragment extends ETFragment<PolicyViewModel, FragmentPolicyAgreementBinding> {

    private static final String  KEY_AGREEMENT = "agreemenet";
    public static PolicyFragment newInstance(PolicyAgreement agreement) {
        PolicyFragment fragment = new PolicyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_AGREEMENT, agreement);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected Class<PolicyViewModel> getViewModel() {
        return PolicyViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_policy_agreement;
    }

    @Override
    public void initializeViews() {


        PolicyAgreement agreement = getArguments().getParcelable(KEY_AGREEMENT);
        dataBinding.policyContentView.loadData(agreement.content, "text/html; charset=utf-8", "UTF-8");
    }

    private void showTermsNConditions() {
        //loadFragment(TermsNConditionsFragment.newInstance());

    }

    @Override
    public void observeEventsFromViewModel() {
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_terms_n_condition);
    }


    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
       // dataBinding.lnEnglish.setTextColor(getColor(R.color.colorPrimary));
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
       // dataBinding.lnEnglish.setTextColor(getColor(R.color.moto_primary));

    }
}
