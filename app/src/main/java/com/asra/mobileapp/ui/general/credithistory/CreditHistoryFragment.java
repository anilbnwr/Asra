package com.asra.mobileapp.ui.general.credithistory;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.databinding.FragmentCreditHistoryBinding;
import com.asra.mobileapp.ui.base.ETFragment;

public class CreditHistoryFragment extends ETFragment
        <CreditHistoryViewModel, FragmentCreditHistoryBinding> {

    public static CreditHistoryFragment newInstance(){
        return new CreditHistoryFragment();
    }

    private CreditHistoryAdapter adapter;

    @Override
    protected Class<CreditHistoryViewModel> getViewModel() {
        return CreditHistoryViewModel.class;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_credit_history);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_credit_history;
    }

    @Override
    public void initializeViews() {
        dataBinding.creditRecyclerView.setLayoutManager(getLinearLayoutManager());

        dataBinding.creditRecyclerView.setHasFixedSize(true);

        dataBinding.creditRecyclerView.addItemDecoration(itemDecoration);
        adapter = new CreditHistoryAdapter();
        dataBinding.creditRecyclerView.setAdapter(adapter);
        analyticsHelper.logViewListEvent(AnalyticsModel.CATEGORY_USER_CREDITS);

        showBackButton();

    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.creditHistoryErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));
        viewModel.creditHistoryListObservable.observe(this, adapter::updateData);
    }
}
