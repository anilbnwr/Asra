package com.asra.mobileapp.ui.general;

import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.ActivityEtFragmentBinding;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.base.FragmentNavigatorActivity;
import com.asra.mobileapp.ui.base.model.ProgressData;
import com.asra.mobileapp.ui.general.aboutus.AboutUsFragment;
import com.asra.mobileapp.ui.general.changepassword.ChangePasswordFragment;
import com.asra.mobileapp.ui.general.credithistory.CreditHistoryFragment;
import com.asra.mobileapp.ui.general.enrolledevents.EventConstants;
import com.asra.mobileapp.ui.general.enrolledevents.FlipperEventFragment;
import com.asra.mobileapp.ui.general.ewaiver.EWaiverListFragment;
import com.asra.mobileapp.ui.general.membership.MembershipFragment;
import com.asra.mobileapp.ui.general.profile.ProfileFragment;
import com.asra.mobileapp.ui.general.settings.SettingsFragment;
import com.asra.mobileapp.ui.general.transfercredit.TransferCreditFragment;
import com.asra.mobileapp.util.StringUtils;

import org.jetbrains.annotations.NotNull;

import androidx.fragment.app.Fragment;

public class ETFragmentHostActivity extends FragmentNavigatorActivity<ActivityEtFragmentBinding, HostAvtivityViewModel> {

    public static final int CODE_PAST_EVENTS = 101;
    public static final int CODE_UPCOMING_EVENTS = 102;
    public static final int CODE_CREDIT_HISTORY = 103;
    public static final int CODE_SETTINGS = 104;
    public static final int CODE_HELP = 105;
    public static final int CODE_PRIVACY = 106;
    public static final int CODE_ABOUT_US = 107;
    public static final int CODE_PROFILE_EDIT = 108;
    public static final int CODE_CHANGE_PASSWORD = 109;
    public static final int CODE_MEMBERSHIP = 110;
    public static final int CODE_EWAIVER = 111;
    public static final int CODE_TRANSFER_CREDIT = 112;
    public static final String EXTRA_NAV_ID = "com.evolvegt.mobileapp.navid";


    private int navId;

    @Override
    public int getFragmentContainer() {
        return R.id.fragment_container;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_et_fragment;
    }

    @Override
    protected Class<HostAvtivityViewModel> getViewModel() {
        return HostAvtivityViewModel.class;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        navId = getIntent().getIntExtra(EXTRA_NAV_ID, 0);
        showBackButton();
    }

    @Override
    public void showProgressBar(ProgressData progressData) {
        if(progressData == null || !progressData.show){
            dataBinding.progressContainer.setVisibility(View.GONE);
        }else{
            dataBinding.progressContainer.setVisibility(View.VISIBLE);

            if(!StringUtils.isEmpty(progressData.progressText)){
                dataBinding.progressMessage.setText(progressData.progressText);
            }else{
                dataBinding.progressMessage.setText("");
            }
        }
    }

    public void showEmptyMessage(boolean show, String message){

        dataBinding.emptyMessage.setText(message);

        if(show) {
            dataBinding.progressContainer.setVisibility(View.GONE);
            dataBinding.emptyViewContainer.setVisibility(View.VISIBLE);
        }else{
            dataBinding.progressContainer.setVisibility(View.GONE);
            dataBinding.emptyViewContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getNumberOfRootFragments() {
        return 1;
    }

    @NotNull
    @Override
    public Fragment getRootFragment(int i) {
        return getMainFragment();
    }

    private ETFragment getMainFragment() {
        ETFragment fragment = null;
        switch (navId) {
            case CODE_ABOUT_US:
                fragment = AboutUsFragment.newInstance();
                break;
            case CODE_SETTINGS:
                fragment = SettingsFragment.newInstance();
                break;
            case CODE_CHANGE_PASSWORD:
                fragment = ChangePasswordFragment.newInstance();
                break;

            case CODE_MEMBERSHIP:
                fragment = MembershipFragment.newInstance();
                break;

            case CODE_CREDIT_HISTORY:
                fragment = CreditHistoryFragment.newInstance();
                break;
            case CODE_PAST_EVENTS:
                fragment = FlipperEventFragment.newInstance(EventConstants.TYPE_PAST);
                break;
            case CODE_UPCOMING_EVENTS:
                fragment = FlipperEventFragment.newInstance(EventConstants.TYPE_UPCOMING);
                break;
            case CODE_EWAIVER:
                fragment = EWaiverListFragment.newInstance();
                break;

            case CODE_TRANSFER_CREDIT:
                fragment = TransferCreditFragment.newInstance();
                break;

            case CODE_PROFILE_EDIT:
                fragment = ProfileFragment.newInstance();
                break;


        }
        return fragment;
    }
}
