package com.asra.mobileapp.ui.dashboard.duties;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.databinding.FragmentFlipperBinding;
import com.asra.mobileapp.network.retrofit.response.CoachDutyResponse;
import com.asra.mobileapp.ui.base.ETFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;

public class TabbedDutyEventsFragment extends
        ETFragment<CoachEventDutyListViewModel, FragmentFlipperBinding> {

    private int selectedTab = 1;

    public static TabbedDutyEventsFragment newInstance(){
        return new TabbedDutyEventsFragment() ;
    }

    @Override
    protected Class<CoachEventDutyListViewModel> getViewModel() {
        return CoachEventDutyListViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_flipper;
    }

    @Override
    public void initializeViews() {
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

        viewModel.getDutyEventList();
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.dutyListObservable.observe(this, this::setTabs);
    }

    private void setTabs(CoachDutyResponse.DutyTypes dutyTypes){
        EventTabAdapter pagerAdapter = new EventTabAdapter(dutyTypes, getContext(), getChildFragmentManager());
            dataBinding.pager.setAdapter(pagerAdapter);
            dataBinding.tablayout.setupWithViewPager(dataBinding.pager);

            dataBinding.pager.setCurrentItem(selectedTab, true);
            showEmptyMessage(false, null);
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_duty_list);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.tablayout.setBackgroundColor(getColor(R.color.colorPrimary));
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.tablayout.setBackgroundColor(getColor(R.color.moto_primary));
    }
}
