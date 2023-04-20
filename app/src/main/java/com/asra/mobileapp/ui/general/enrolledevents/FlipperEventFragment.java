package com.asra.mobileapp.ui.general.enrolledevents;

import android.os.Bundle;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentFlipperBinding;
import com.asra.mobileapp.ui.base.ETFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import timber.log.Timber;

public class FlipperEventFragment extends ETFragment<FlipperViewModel, FragmentFlipperBinding> {

    private int selectedTab = EventConstants.TYPE_UPCOMING;

    public static FlipperEventFragment newInstance(int category){
        FlipperEventFragment fragment = new FlipperEventFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EventConstants.KEY_TYPE, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected Class<FlipperViewModel> getViewModel() {
        return FlipperViewModel.class;
    }

    @Override
    public String getTitle() {

        Timber.i("Selected Tab - %s", selectedTab);
        String title = null;
        switch (selectedTab){
            case EventConstants.TYPE_UPCOMING:
                title = getString(R.string.title_upcoming_events);
                break;
            case EventConstants.TYPE_PAST:
                title = getString(R.string.title_past_events);break;
            case EventConstants.TYPE_ALL:
                title = getString(R.string.title_all_events);
                break;

        }
        return title;
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_flipper;
    }

    @Override
    public void initializeViews() {
        Bundle argumnts = getArguments();
        selectedTab = argumnts.getInt(EventConstants.KEY_TYPE);
        EventsPagerAdapter pagerAdapter = new EventsPagerAdapter(getContext(), getChildFragmentManager());
        dataBinding.pager.setAdapter(pagerAdapter);
        dataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        dataBinding.tablayout.setupWithViewPager(dataBinding.pager);

        dataBinding.pager.setCurrentItem(selectedTab, true);

        dataBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selectedTab = position;
                updateTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        showBackButton();
    }

    @Override
    public void observeEventsFromViewModel() {

    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.tablayout.setBackgroundColor(getColor(R.color.moto_primary));
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.tablayout.setBackgroundColor(getColor(R.color.colorPrimary));
    }
}
