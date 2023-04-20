package com.asra.mobileapp.ui.dashboard.shop;

import android.view.MenuItem;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.databinding.FragmentFlipperBinding;
import com.asra.mobileapp.model.Category;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.viewpager.widget.ViewPager;

public abstract class TabbedProductListFragment extends ShoppeFragment<TabbedProductsViewModel, FragmentFlipperBinding> {
    private int selectedTab;

    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }

    @Override
    protected Class<TabbedProductsViewModel> getViewModel() {
        return TabbedProductsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_flipper;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();

        viewModel.categoryListErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));

        viewModel.categoryListObservable.observe(this, this::setTabs);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        showBackButton();

        dataBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        analyticsHelper.logViewListEvent(AnalyticsModel.CATEGORY_GEAR_PRODUCTS);

        getTabCategories();



    }

    private void setTabs(List<Category> categories){
        CategoryTabAdapter pagerAdapter = new CategoryTabAdapter(getProductCategory(), categories, getContext(), getChildFragmentManager());
            dataBinding.pager.setAdapter(pagerAdapter);
            dataBinding.tablayout.setupWithViewPager(dataBinding.pager);

            dataBinding.pager.setCurrentItem(selectedTab, true);
            showEmptyMessage(false, null);
    }
    protected abstract void getTabCategories();
    protected abstract String getProductCategory();

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.tablayout.setBackgroundResource(R.color.colorPrimary);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.tablayout.setBackgroundResource(R.color.moto_primary);
    }
}
