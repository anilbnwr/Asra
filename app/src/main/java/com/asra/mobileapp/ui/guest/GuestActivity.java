package com.asra.mobileapp.ui.guest;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.ActivityEtFragmentBinding;
import com.asra.mobileapp.ui.base.FragmentNavigatorActivity;
import com.asra.mobileapp.ui.base.model.ProgressData;
import com.asra.mobileapp.ui.dashboard.cart.CartFragment;
import com.asra.mobileapp.ui.guest.home.GuestHomeFragment;
import com.asra.mobileapp.ui.login.login.LoginFragment;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

public class GuestActivity extends FragmentNavigatorActivity<ActivityEtFragmentBinding, GuestViewModel> {

    @Override
    public int getFragmentContainer() {
        return R.id.fragment_container;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_et_fragment;
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
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }


    }


    @Override
    protected Class<GuestViewModel> getViewModel() {
        return GuestViewModel.class;
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

    public void showEmptyMessage(boolean show, String message) {

        dataBinding.emptyMessage.setText(message);

        if (show) {
            dataBinding.progressContainer.setVisibility(View.GONE);
            dataBinding.emptyViewContainer.setVisibility(View.VISIBLE);
        } else {
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
        return GuestHomeFragment.newInstance();
    }


    private MenuItem loginMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_guest, menu);
        loginMenuItem = menu.findItem(R.id.menu_toolbar_login);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_toolbar_login:
                onLoginRequested();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void onLoginRequested() {
        loadFragmentToTab(LoginFragment.newInstance(false));
    }



    public void showLoginMenu(boolean show) {
        if (loginMenuItem != null) {
            loginMenuItem.setVisible(show);
        }
    }

    @Override
    public void switchToCart() {
        loadFragmentToTab(CartFragment.newInstance());

    }

    @Override
    public void launchLoginActivity() {
        onLoginRequested();
    }
}
