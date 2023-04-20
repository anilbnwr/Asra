package com.asra.mobileapp.ui.admin.adminduties;

import android.os.Bundle;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentAdminDutiesBinding;
import com.asra.mobileapp.ui.base.ETFragment;

public class AdminDutyFragment extends ETFragment<AdminDutyViewModel, FragmentAdminDutiesBinding> {

    private static final String KEY_EVENT = "com.evolvegt.mobileapp.event";

    public static AdminDutyFragment newInstance(String eventJson){
        AdminDutyFragment fragment = new AdminDutyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT, eventJson);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    protected Class getViewModel() {
        return AdminDutyViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_admin_duties;
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void observeEventsFromViewModel() {

    }

    @Override
    public String getTitle() {
        return getString(R.string.title_duties);
    }
}
