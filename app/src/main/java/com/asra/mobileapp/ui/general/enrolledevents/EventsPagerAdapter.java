package com.asra.mobileapp.ui.general.enrolledevents;

import android.content.Context;

import com.asra.mobileapp.R;
import com.asra.mobileapp.ui.base.ETFragment;

import org.jetbrains.annotations.NotNull;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class EventsPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    EventsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @NotNull
    @Override
    public ETFragment getItem(int position) {
        switch (position) {
            case 0:
                return EnrolledEventListFragment.newInstance(EventConstants.TYPE_UPCOMING);
            case 1:
                return EnrolledEventListFragment.newInstance(EventConstants.TYPE_PAST);
            case 2:
                return EnrolledEventListFragment.newInstance(EventConstants.TYPE_ALL);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.title_upcoming_events);
            case 1:
                return context.getString(R.string.title_past_events);
            case 2:
                return context.getString(R.string.title_all_events);
            default:
                return null;
        }
    }
}
