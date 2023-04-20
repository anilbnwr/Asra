package com.asra.mobileapp.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.ActivityEtFragmentBinding;
import com.asra.mobileapp.model.AppVersionStatus;
import com.asra.mobileapp.ui.base.FragmentNavigatorActivity;
import com.asra.mobileapp.ui.base.model.ProgressData;
import com.asra.mobileapp.ui.login.login.LoginFragment;
import com.ncapdevi.fragnav.FragNavController;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginActivity extends FragmentNavigatorActivity <ActivityEtFragmentBinding, LoginActivityViewModel> implements
        FragNavController.RootFragmentListener{


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_et_fragment;
    }

    @Override
    public int getFragmentContainer() {
        return R.id.fragment_container;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getToolbar().setVisibility(View.GONE);
    }

    @Override
    protected Class<LoginActivityViewModel> getViewModel() {
        return LoginActivityViewModel.class;
    }

    @Override
    public void showProgressBar(ProgressData progressData) {
        if(progressData == null || !progressData.show){
            dataBinding.progressContainer.setVisibility(View.GONE);
        }else{
            dataBinding.progressContainer.setVisibility(View.VISIBLE);

            if(!TextUtils.isEmpty(progressData.progressText)){
                dataBinding.progressMessage.setText(progressData.progressText);
            }else{
                dataBinding.progressMessage.setText("");
            }
        }
    }


    @Override
    public void handleAppUpdate(AppVersionStatus appVersionStatus, boolean optional) {

    }

    @NotNull
    @Override
    public Fragment getRootFragment(int index) {
        return LoginFragment.newInstance();
    }
    @Override
    public int getNumberOfRootFragments() {
        return 1;
    }


    @Override
    public void observeViewModelLiveData() {
        super.observeViewModelLiveData();

        viewModel.adminLoggedInObservable.observe(this, status ->
                launchAdminDashboard(true));

        viewModel.userLoggedInObservable.observe(this, status ->
                launchUserDashboard(true));

        viewModel.coachLoggedInObservable.observe(this, status ->
                launchUserDashboard(true));
    }


}
