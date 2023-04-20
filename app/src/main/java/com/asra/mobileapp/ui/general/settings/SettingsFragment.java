package com.asra.mobileapp.ui.general.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.FragmentSettingsBinding;
import com.asra.mobileapp.model.NotificationType;
import com.asra.mobileapp.ui.base.ETFragment;

import java.util.List;

import androidx.appcompat.widget.SwitchCompat;

public class SettingsFragment extends ETFragment<SettingsViewModel, FragmentSettingsBinding> {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    protected Class<SettingsViewModel> getViewModel() {
        return SettingsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_settings;
    }

    @Override
    public void initializeViews() {
        dataBinding.prefTermsNConditions.setOnClickListener((View v) -> showTermsNConditions());
    }

    private void showTermsNConditions() {
        loadFragment(TermsNConditionsFragment.newInstance());

    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.notificationTypesObservable.observe(this, this::inflateNotificationViews);
        viewModel.notificationTypesErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_settings);
    }

    private void inflateNotificationViews(List<NotificationType> notificationList) {
        dataBinding.notificationContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (NotificationType type : notificationList) {
            View view = inflater.inflate(R.layout.notification_template, null, false);
            SwitchCompat switchCompat = view.findViewById(R.id.notification);
            switchCompat.setText(type.notificationType);
            switchCompat.setTag(type);
            switchCompat.setOnCheckedChangeListener(checkedChangeListener);
            switchCompat.setChecked(type.isEnabled());
            if (isEvApp())
                switchCompat.setThumbTintList(UiUtils.getColorStateFromResource(switchCompat.getContext(),
                        R.color.switch_ev_color));
            else
                switchCompat.setThumbTintList(UiUtils.getColorStateFromResource(switchCompat.getContext(),
                        R.color.switch_moto_color));

            dataBinding.notificationContainer.addView(view);
        }
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            NotificationType type = (NotificationType) compoundButton.getTag();

            viewModel.onPrefChanged(type, b);
        }
    };

    @Override
    public void onDetach() {
        viewModel.syncSettings();

        super.onDetach();
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.lnEnglish.setTextColor(getColor(R.color.colorPrimary));
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.lnEnglish.setTextColor(getColor(R.color.moto_primary));

    }
}
