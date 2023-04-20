package com.asra.mobileapp.ui.dashboard.duties;

import android.os.Bundle;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentCoachDutiesBinding;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.util.ListUtils;

import java.util.List;

import androidx.annotation.Nullable;

public class DutyListFragment extends ETFragment<DutyListViewModel, FragmentCoachDutiesBinding> {

    private static final String KEY_TAB_POSITION = "fragment.event.tab.position";

    public static DutyListFragment newInstance(int tabPosition){
        DutyListFragment fragment = new DutyListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TAB_POSITION, tabPosition);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int tabPosition = getArguments().getInt(KEY_TAB_POSITION);
        viewModel.setEventDuty(tabPosition);
    }

    @Override
    protected Class<DutyListViewModel> getViewModel() {
        return DutyListViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_coach_duties;
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.assignedDutiesObservable.observe(this, this::setUpAssignedDuties);
    }

    private void setUpAssignedDuties(List<DutyListViewModel.DutyDisplayItem> duties) {
        if(ListUtils.isEmpty(duties)){
            dataBinding.activeDutyList.setVisibility(View.GONE);
            dataBinding.emptyDuties.setVisibility(View.VISIBLE);
            dataBinding.emptyDuties.setText(R.string.no_assigned_duties);
            return;
        }

        dataBinding.emptyDuties.setVisibility(View.GONE);


        dataBinding.activeDutyList.setLayoutManager(getLinearLayoutManager());
        dataBinding.activeDutyList.setHasFixedSize(true);
        dataBinding.activeDutyList.addItemDecoration(itemDecoration);
        DutyListAdapter adapter = new DutyListAdapter();
        adapter.updateDateSet(duties);
        dataBinding.activeDutyList.setAdapter(adapter);
    }


}
