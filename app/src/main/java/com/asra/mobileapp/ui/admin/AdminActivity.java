package com.asra.mobileapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.ActivityEtFragmentBinding;
import com.asra.mobileapp.ui.admin.completedevents.CompletedEventsFragment;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.base.FragmentNavigatorActivity;
import com.asra.mobileapp.ui.base.model.ProgressData;
import com.asra.mobileapp.util.StringUtils;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminActivity extends FragmentNavigatorActivity<ActivityEtFragmentBinding, AdminViewModel> {

    @Override
    public int getFragmentContainer() {
        return R.id.fragment_container;
    }


    @Override
    protected Class<AdminViewModel> getViewModel() {
        return AdminViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void observeViewModelLiveData() {
        super.observeViewModelLiveData();

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_et_fragment;
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

    @Override
    public void onAppModeSwitched() {
        super.onAppModeSwitched();
        ETFragment fragment = getCurrentFragment();
        if(fragment != null)
            fragment.invalidateUi();
    }


    @NotNull
    @Override
    public Fragment getRootFragment(int index) {
        return CompletedEventsFragment.newInstance();
    }

    @Override
    public int getNumberOfRootFragments() {
        return 1;
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


}
