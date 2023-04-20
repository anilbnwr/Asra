package com.asra.mobileapp.ui.dashboard.shop;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.CategoryHeader;

public class GearFragment extends TabbedProductListFragment {

    public static GearFragment newInstance(){
        return new GearFragment();
    }

    @Override
    protected void getTabCategories() {
        viewModel.getGearCategories();
    }

    @Override
    protected String getProductCategory() {
        return CategoryHeader.TITLE_GEAR;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_gear);
    }
}
