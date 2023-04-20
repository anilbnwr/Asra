package com.asra.mobileapp.ui.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.FirebaseAnalyticsHelper;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.common.UiUtils;
import com.ncapdevi.fragnav.FragNavController;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

public abstract class FragmentNavigatorActivity <D extends ViewDataBinding, V extends ActivityViewModel>
        extends BaseActivity <D, V> implements  FragNavController.RootFragmentListener {

    private FragNavController fragNavController;
    protected SingleLiveEvent<Boolean> cartTabSwitch = new SingleLiveEvent<>();

    public abstract int getFragmentContainer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        fragNavController = new FragNavController(getSupportFragmentManager(), R.id.fragment_container);

        setUpFragmentNavigationController(savedInstanceState);


    }

    private  void setUpFragmentNavigationController(Bundle savedInstanceState){

        fragNavController.setRootFragmentListener(this);

        fragNavController.initialize(UiUtils.TAB_HOME, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        fragNavController.onSaveInstanceState(outState);
    }

    public void popFragment(int depth) {
        fragNavController.popFragments(depth);
    }

    public void popFragment() {
        fragNavController.popFragment();
    }


    public void loadFragmentToTab(ETFragment fragment) {
        fragNavController.pushFragment(fragment);
        FirebaseAnalyticsHelper.getInstance(this).trackScreen(this,
                fragment.getClass().getSimpleName());
    }

    @Override
    public void onBackPressed() {
        if (fragNavController.isRootFragment()) {
            super.onBackPressed();
        }else{
            popFragment();
        }
    }


    @Override
    public View getInfoContainer() {
        return findViewById(R.id.infoLayout);
    }

    public void clearTabStack(){
        fragNavController.clearStack();
    }

    public void clearTabStack(int tab){
        fragNavController.clearStack(tab);
    }
    public void switchTab(int index){
        fragNavController.switchTab(index);
    }

    protected int getSelectedTab(){
        return fragNavController.getCurrentStackIndex();
    }

    public ETFragment getCurrentFragment(){
        if(fragNavController.getCurrentFrag() instanceof ETFragment){
            return ((ETFragment)fragNavController.getCurrentFrag());
        }else return null;
    }
}
