package com.asra.mobileapp.ui.general.enrolledevents;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.databinding.FragmentEnrolledEventsBinding;
import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.ui.admin.eventparticipants.AttributeViewDialog;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.general.enrolledevents.passport.showpassport.ShowPassportFragment;
import com.asra.mobileapp.ui.general.enrolledevents.passport.uploadpassport.PassportSignatureFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;


public class EnrolledEventListFragment extends
        ETFragment<EnrolledEventViewModel, FragmentEnrolledEventsBinding>
        implements EventActionListener {

    private int type = EventConstants.TYPE_UPCOMING;
    private EnrolledEventListAdapter adapter;

    public static EnrolledEventListFragment newInstance(int category) {
        EnrolledEventListFragment fragment = new EnrolledEventListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EventConstants.KEY_TYPE, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected Class<EnrolledEventViewModel> getViewModel() {
        return EnrolledEventViewModel.class;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(EventConstants.KEY_TYPE);
        viewModel.setType(type);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_enrolled_events;
    }

    @Override
    public void initializeViews() {

        dataBinding.eventsRecyclerView.setLayoutManager(getLinearLayoutManager());

        dataBinding.eventsRecyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.item_divider));

    }

    private void setAdapter(boolean canCancelEvents){
        adapter = new EnrolledEventListAdapter(type);
        adapter.setEventActionListener(this);
        dataBinding.eventsRecyclerView.setAdapter(adapter);
        adapter.setCanCancelEvents(canCancelEvents);
        dataBinding.eventsRecyclerView.setAdapter(adapter);

    }

    private void fetchData() {
        switch (type) {
            case EventConstants.TYPE_UPCOMING:
                viewModel.fetchUpcomingEvents();
                break;
            case EventConstants.TYPE_PAST:
                viewModel.fetchPastEvents();
                break;
            case EventConstants.TYPE_ALL:
                viewModel.fetchAllEvents();
                break;

        }
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.canCancelEventsObserver.observe(this, this::setAdapter);
        viewModel.eventHistoryObserver.observe(this, enrolledEvents -> {
            dataBinding.emptyView.setVisibility(View.GONE);
            dataBinding.eventsRecyclerView.setVisibility(View.VISIBLE);

            adapter.updateDataSet(enrolledEvents);
        });
        viewModel.eventHistoryErrorObserver.observe(this, this::setErrorView);
        viewModel.eventHistoryStatusObserver.observe(this, data -> fetchData());
        viewModel.uploadSelfieObserver.observe(this, upload ->
                loadFragment(PassportSignatureFragment.newInstance()));
    }

    private void setErrorView(String message){
        dataBinding.eventsRecyclerView.setVisibility(View.GONE);
        dataBinding.emptyView.setText(message);
        dataBinding.emptyView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onCancel(EnrolledEvent event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String title = getConfigString(MessageProvider.event_cancel_confirm_title);
        if(TextUtils.isEmpty(title))
            title = getString(R.string.app_name);
        builder.setTitle(title);

        String message = getConfigString(MessageProvider.event_cancel_confirm_message);
        message = String.format(message, event.productName);

        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> viewModel.cancelEvent(event));

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    public void showPassport(EnrolledEvent event) {
        loadFragment(ShowPassportFragment.newInstance(event.passportId, event.productName, event.eventDate));
    }

    @Override
    public void uploadSelfie(EnrolledEvent event) {
        viewModel.requestUploadSelfie(event);
    }

    @Override
    public void showMotoAccessories(List<EventParticipant.MotoClass> motoClasses) {
        AttributeViewDialog dialog = new AttributeViewDialog(getContext());
        dialog.setMotoClasses(motoClasses);
        dialog.setShowMotoDetails(true);
        dialog.show();
    }

    @Override
    public void showEvolveAccessories(List<EventParticipant.Rental> rentals, List<String> trainings) {
        AttributeViewDialog dialog = new AttributeViewDialog(getContext());
        dialog.setRentals(rentals);
        dialog.setTrainingList(trainings);
        dialog.setShowMotoDetails(false);
        dialog.show();
    }
}


