package com.asra.mobileapp.ui.main;

import android.os.Bundle;


import com.asra.mobileapp.R;
import com.asra.mobileapp.ui.base.ETFragment;

import androidx.annotation.Nullable;

public class MainFragment extends ETFragment {

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.main_fragment;
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void observeEventsFromViewModel() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected Class getViewModel() {
        return MainViewModel.class;
    }
}
