package com.asra.mobileapp.ui.general.ewaiver;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentEtListBinding;
import com.asra.mobileapp.model.WaiverEvent;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.general.ewaiver.waiverdetails.WaiverDetailsFragment;

import java.util.List;

public class EWaiverListFragment extends ETFragment<EWaiverViewModel, FragmentEtListBinding> implements WaiverListAdapter.EWaiverListener{


    private WaiverListAdapter adapter;

    public static EWaiverListFragment newInstance(){
        return new EWaiverListFragment();
    }

    @Override
    protected Class<EWaiverViewModel> getViewModel() {
        return EWaiverViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_et_list;
    }

    @Override
    public void initializeViews() {
        dataBinding.etRecyclerView.setLayoutManager(getLinearLayoutManager());
        dataBinding.etRecyclerView.setHasFixedSize(true);
        dataBinding.etRecyclerView.addItemDecoration(itemDecoration);
        if(adapter == null){
            adapter = new WaiverListAdapter(this);
        }
        dataBinding.etRecyclerView.setAdapter(adapter);
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.waiverListObservable.observe(this, this::setUpListView);
        viewModel.waiverListError.observe(this, error ->  showEmptyMessage(true, error));
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_e_waiver);
    }

    private void setUpListView(List<WaiverEvent> waiverEventList) {

        if (adapter != null) {
            adapter.replaceDateSet(waiverEventList);
        }
    }

    @Override
    public void onItemClicked(WaiverEvent event) {
        loadFragment(WaiverDetailsFragment.newInstance(event.getEventId()));
    }
}
