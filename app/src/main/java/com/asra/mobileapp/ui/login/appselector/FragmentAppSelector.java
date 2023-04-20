package com.asra.mobileapp.ui.login.appselector;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentAppSelectorBinding;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.login.login.LoginFragment;

public class FragmentAppSelector extends ETFragment<ViewModelAppSelector, FragmentAppSelectorBinding> {


    public static FragmentAppSelector newInstance(){
        return new FragmentAppSelector();
    }
    @Override
    protected Class getViewModel() {
        return ViewModelAppSelector.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_app_selector;
    }

    @Override
    public void initializeViews() {
        dataBinding.appEv.setOnClickListener(v -> {
            viewModel.onEvAppSelected();
        });
        dataBinding.appMoto.setOnClickListener(v -> {
            viewModel.onMotoAppSelected();
        });
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.loginRequired.observe(this, login -> loadFragment(LoginFragment.newInstance()));
    }


}
