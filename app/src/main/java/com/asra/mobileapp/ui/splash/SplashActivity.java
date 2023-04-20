package com.asra.mobileapp.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.ActivitySplashBinding;
import com.asra.mobileapp.model.AppVersionStatus;
import com.asra.mobileapp.ui.base.BaseActivity;
import com.asra.mobileapp.ui.base.model.ProgressData;

import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> {



    @Override
    public View getInfoContainer() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        observeLiveData();
        new Handler().postDelayed(this::launchDashboard, 1000);

    }

    @Override
    protected Class<SplashViewModel> getViewModel() {
        return SplashViewModel.class;
    }

    @Override
    public void showProgressBar(ProgressData progressData) {

    }

    private void launchDashboard(){
        viewModel.onSplashFinished();

    }

    @Override
    public void handleAppUpdate(AppVersionStatus appVersionStatus, boolean optional) {
    }

    private void observeLiveData(){
        viewModel.loginRequiredObserver.observe(this, aBoolean -> launchLoginActivity());
        viewModel.adminLoggedObserver.observe(this, aBoolean -> launchAdminDashboard(true));
        viewModel.regularUserLoggedObserver.observe(this, aBoolean -> launchUserDashboard(true));
    }
}
