package com.asra.mobileapp.ui.dashboard.shop;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.CategoryHeader;

public class RentalFragment extends TabbedProductListFragment {

    public static RentalFragment newInstance(){
        return new RentalFragment();
    }

    @Override
    protected void getTabCategories() {
        viewModel.getRentalCategories();
    }

    @Override
    protected String getProductCategory() {
        return CategoryHeader.TITLE_RENTAL;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_shop_rental);
    }
}
