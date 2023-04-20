package com.asra.mobileapp;

import android.os.Bundle;
import android.view.View;

import com.asra.mobileapp.ui.base.BaseActivity;
import com.asra.mobileapp.ui.base.model.ProgressData;
import com.asra.mobileapp.ui.main.MainFragment;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    protected Class getViewModel() {
        return null;
    }

    @Override
    public void showProgressBar(ProgressData progressData) {

    }

    @Override
    public View getInfoContainer() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.main_activity;
    }
}
