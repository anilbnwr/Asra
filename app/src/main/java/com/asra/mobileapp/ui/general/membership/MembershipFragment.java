package com.asra.mobileapp.ui.general.membership;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.databinding.FragmentMembershipBinding;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.general.membershipdetails.MembershipDetailsFragment;

import java.util.List;

public class MembershipFragment extends ETFragment<MembershipViewModel, FragmentMembershipBinding> implements MembershipActionsCallback {

    public static MembershipFragment newInstance() {
        return new MembershipFragment();
    }

    private MembershipListAdapter adapter;

    @Override
    protected Class<MembershipViewModel> getViewModel() {
        return MembershipViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_membership;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_membership);
    }


    @Override
    public void initializeViews() {
        dataBinding.membershipRecyclerView.setLayoutManager(getGridLayoutManager());
        dataBinding.membershipRecyclerView.setHasFixedSize(true);
        dataBinding.membershipRecyclerView.addItemDecoration(itemGridDecoration);
        if(adapter == null){
            adapter = new MembershipListAdapter(this);
        }
        dataBinding.membershipRecyclerView.setAdapter(adapter);
    }

    @Override
    public void observeEventsFromViewModel() {
        if(adapter == null){
            adapter = new MembershipListAdapter(this);
        }
        viewModel.memberRoleObservable.observe(this, adapter::setRole);

        viewModel.memberShipObservable.observe(this, adapter::setCurrentMembership);
        viewModel.memberShipErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));
        viewModel.memberShipListErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));
        viewModel.memberShipListObservable.observe(this, this::setUpListView);
        viewModel.membershipAddedToCartObservable.observe(this, this::showSuccessMessage);

        viewModel.membershipCartErrorObservable.observe(this, this::showErrorMessage);

        viewModel.mrlConfirmationObservable.observe(this, mrlMessage -> {
            showSimpleAlert(mrlMessage.title, mrlMessage.message, "I Agree", new ETDialog.DialogListener() {
                @Override
                public void onPositiveButtonClicked() {
                    super.onPositiveButtonClicked();
                    viewModel.submitMembershipCartRequest();
                }
            });
        });
    }

    private void setUpListView(List<Membership> membershipList) {

        if (adapter != null) {
            adapter.replaceDateSet(membershipList);
        }
    }

    @Override
    public void onUpgrade(Membership selectedMembership) {

        viewModel.addMembershipToCart(selectedMembership);
    }

    @Override
    public void onViewMore(Membership selectedMembership) {
        loadFragment(MembershipDetailsFragment.newInstance(selectedMembership.getSlug()));
    }
}
