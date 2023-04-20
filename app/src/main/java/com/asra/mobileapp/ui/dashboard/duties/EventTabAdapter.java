package com.asra.mobileapp.ui.dashboard.duties;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.network.retrofit.response.CoachDutyResponse;
import com.asra.mobileapp.ui.base.ETFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import timber.log.Timber;

public class EventTabAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private CoachDutyResponse.DutyTypes eventDutyList;

    public EventTabAdapter(CoachDutyResponse.DutyTypes eventDutyList, Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.eventDutyList = eventDutyList;

    }

    @Override
    public ETFragment getItem(int position) {

        Timber.i("Fragment Adapter position - %s", position);
        return DutyListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return eventDutyList != null ? 3 : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0: return "Past";
            case 1: return "Current";
            case 2: return "Upcoming";
            default: return "";
        }

    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(context).inflate(R.layout.tab_view, null);
        TextView tv = tab.findViewById(R.id.custom_text);
        tv.setText(getPageTitle(position).toString());
        return tab;
    }
}
