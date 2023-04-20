package com.asra.mobileapp.ui.admin.eventparticipants;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.common.Constants;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.dialog.ETConfirmationDialog;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.databinding.FragmentAdminEventUsersBinding;
import com.asra.mobileapp.model.CompletedEvent;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.network.retrofit.request.DeleteEventRequest;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.ui.admin.signature.SignatureFragment;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.util.ListUtils;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;

public class EventParticipantsFragment extends ETFragment<EventParticipantViewModel,
        FragmentAdminEventUsersBinding> implements ParticipantActionListener{

    private static final String KEY_EVENT = "com.evolvegt.mobileapp.event";
    private static final String KEY_DATA_TYPE = "com.evolvegt.mobileapp.data.type";

    boolean isParticipant;

    private EventUserListAdapter adapter;

    private SearchView searchView;
    private MenuItem menuFilterByTraining;
    private MenuItem menuFilterByRental;
    private MenuItem menuFilterByClasses;
    private MenuItem menuFilterByNotSignedUsers;
    private MenuItem menuFilterByRacers;
    private MenuItem menuFilterByDuties;

    private int checkedIndex = -1;
    public AppEngine appEngine;

    public static EventParticipantsFragment newInstance(String eventJson, int dataType){
        EventParticipantsFragment fragment = new EventParticipantsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT, eventJson);
        bundle.putInt(KEY_DATA_TYPE, dataType);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected Class getViewModel() {
        return EventParticipantViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_admin_event_users;
    }

    @Override
    public void initializeViews() {

        showBackButton();
        showEmptyMessage(false, null);

        dataBinding.etRecyclerView.setLayoutManager(getLinearLayoutManager());

        dataBinding.etRecyclerView.setHasFixedSize(true);
        Log.d("TAG", "initializeViews: "+isParticipant);
        adapter = new EventUserListAdapter(this);
        dataBinding.etRecyclerView.setAdapter(adapter);

        dataBinding.etRecyclerView.addItemDecoration(itemDecoration);
        dataBinding.signIndicator.setText(getConfigString(MessageProvider.label_signature_status_indicator));

        updateEventDetails();
    }

    private void updateEventDetails() {
        Bundle bundle = getArguments();
        updateUserCount(0);

        if(bundle != null) {
            String rawJson = bundle.getString(KEY_EVENT);
            int dataType = bundle.getInt(KEY_DATA_TYPE);
            isParticipant = dataType == Constants.KEY_DATA_USERS;
            CompletedEvent completedEvent = gson.fromJson(rawJson, CompletedEvent.class);

            dataBinding.eventTitle.setText(completedEvent.title);

            String formattedDate = DateUtils.getDateAsString(completedEvent.eventDate, DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
            dataBinding.eventDate.setText(formattedDate);

            dataBinding.signIndicator.setText(getConfigString(MessageProvider.label_signature_status_indicator));

            GlideHelper.setImage(this, dataBinding.eventBanner, completedEvent.eventLogo, R.drawable.et_fallback_image, dataBinding.imageProgressContainer);

            viewModel.fetchParticipantsList(completedEvent.eventId, dataType);
        }


    }

    private void updateUserCount(int count){
        if(count > 0) {
            String userCountTest = String.format(getConfigString(MessageProvider.label_user_count), count);
            dataBinding.userCount.setText(userCountTest);
            dataBinding.userCount.setVisibility(View.VISIBLE);
        }else{
            dataBinding.userCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.errorObservable
                .observe(this, error -> showEmptyMessage(true, error));
        viewModel.eventParticipantsObservable
                .observe(this, participants -> {
                    adapter.setParticipant(isParticipant);
                    adapter.updateDateSet(participants);
                    showEmptyMessage(false, null);
                    dataBinding.etRecyclerView.setVisibility(View.VISIBLE);

                    updateUserCount(participants.size());
                    analyticsHelper.logViewListEvent(AnalyticsModel.CATEGORY_ADMIN_EVENTS_USERS);

                });
        viewModel.rentalLevelMenuObservable.observe(this, available ->{
            if(menuFilterByRental != null){
                menuFilterByRental.setVisible(available);
            }
        });

        viewModel.skillLevelMenuObservable.observe(this, available ->{

        });
        viewModel.trainingLevelMenuObservable.observe(this, available ->{
            if(menuFilterByTraining != null){
                menuFilterByTraining.setVisible(available);
            }
        });

        viewModel.racersMenuObservable.observe(this, available ->{
            if(menuFilterByRacers != null){
                menuFilterByRacers.setVisible(available);
            }
        });

        viewModel.dutiesMenuObservable.observe(this, available ->{
            if(menuFilterByDuties != null){
                menuFilterByDuties.setVisible(available);
            }
        });

        viewModel.notSignedUsersMenuObservable.observe(this, available ->{
            if(menuFilterByNotSignedUsers != null){
                menuFilterByNotSignedUsers.setVisible(available);
            }
        });

        viewModel.classLevelMenuObservable.observe(this, available ->{
            if(menuFilterByClasses != null){
                menuFilterByClasses.setVisible(available);
            }
        });

        viewModel.filterByRentalsObservable.observe(this, this::filterByRental);
        viewModel.filterByTrainingObservable.observe(this, this::filterByTraining);

        viewModel.filterBySkillLevelObservable.observe(this, this::filterBySkillLevel);
        viewModel.filterByClassesObservable.observe(this, this::filterByClassLevel);
        viewModel.filterByDutiesObservable.observe(this, this::filterByDuties);

        viewModel.skillUpgradedObservable.observe(this, this::showSuccessToast);

        viewModel.deleteApiObservable.observe(this,this::getDeleteData);


    }

    private void getDeleteData(ETResponse etResponse) {

        Log.d("TAG", "getDeleteData: "+etResponse.message);
        if(etResponse.status == 1){
            getActivity().onBackPressed();
        }
    }

    private void filterByDuties(List<String> duties) {
        if(ListUtils.isEmpty(duties)){
            return;
        }

        adapter.setFilterType(EventUserListAdapter.FILTER_BY_DUTIES);

        final String[] dutyList = duties.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter by Duties");

        int checkedItem = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(dutyList, checkedItem, (dialog, which) ->
                applyFilter(dutyList[which]));

        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void filterByClassLevel(List<String> motoClasses) {
        if(ListUtils.isEmpty(motoClasses)){
            return;
        }

        adapter.setFilterType(EventUserListAdapter.FILTER_BY_CLASSES);

        final String[] arrayOfMotoClasses = motoClasses.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter by Classes");

        int checkedItem = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayOfMotoClasses, checkedItem, (dialog, which) ->
                applyFilter(arrayOfMotoClasses[which]));

        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public String getTitle() {
        int dataType = getArguments().getInt(KEY_DATA_TYPE);
         if(dataType != Constants.KEY_DATA_DUTIES) {
             return getString(R.string.title_users);
         }else{
             return getString(R.string.title_staffs);
         }
    }

    @Override
    public boolean isToolbarMenuEnabled() {
        return true;
    }

    @Override
    public void onSignIconClicked(EventParticipant item) {
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(true);

        String json = gson.toJson(item);
        loadFragment(SignatureFragment.newInstance(json));
    }

    @Override
    public void onDeleteButtonClicked(EventParticipant item) {
        Log.d("TAG", "onDeleteButtonClicked: "+item.userId);
        ETConfirmationDialog<Integer> dialog = new ETConfirmationDialog<>(getActivity(),
                new ETConfirmationDialog.ConfirmationListener<Integer>() {
                    @Override
                    public void onConfirmed(Integer passthrough) {
                        super.onConfirmed(passthrough);
                        DeleteEventRequest request = new DeleteEventRequest(item.orderNumber,item.eventId,item.skillLevel,"");
                        viewModel.deleteEvent(request);
                    }
                }, 0);
        dialog.setDialogTitle("Alert");
        dialog.setDialogMessage("Are you sure you want to delete?");
        dialog.setSecondaryMessage("");
        dialog.setDialogBtnPositive("Delete");
        dialog.show();
    }


    @Override
    public void onStarIconClicked(EventParticipant item) {
        AttributeViewDialog dialog = new AttributeViewDialog(getContext());
        dialog.setMotoClasses(item.motoClasses);
        dialog.setRentals(item.rentals);
        dialog.setTrainingList(item.trainingList);
        dialog.show();
    }

    @Override
    public void onSkillUpgrade(EventParticipant item) {

        showSkillLevelPopup(item, viewModel.getGeneralSkills());
    }

    @Override
    public void onMotoIconClicked(EventParticipant item) {
        AttributeViewDialog dialog = new AttributeViewDialog(getContext());
        dialog.setMotoClasses(item.motoClasses);
        dialog.setRentals(item.rentals);
        dialog.setTrainingList(item.trainingList);
        dialog.setShowMotoDetails(true);
        dialog.show();
    }

    @Override
    public void onFilterApplied(List<EventParticipant> filteredList, String query) {
        if(ListUtils.isEmpty(filteredList)){
            String message = getConfigString(MessageProvider.error_no_search_result_for_user);
            message = String.format(message, query);
            showEmptyMessage(true, message );
            updateUserCount(0);

        }else{
            updateUserCount(filteredList.size());
            showEmptyMessage(false, null);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_admin_users, menu);

        MenuItem filterMenu = menu.findItem(R.id.menu_toolbar_user_filter);

        // Inflating the sub_menu menu this way, will add its menu items
        // to the empty SubMenu you created in the xml
        inflater.inflate(R.menu.submenu_admin_user_filter, filterMenu.getSubMenu());

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);



        TextView textView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        textView.setHint(R.string.hint_search_by_user_name);
        textView.setHintTextColor(UiUtils.getColorFromResource(getContext(), R.color.colorHintWhite));

        ImageView iconView = searchView.findViewById(androidx.appcompat.R.id.search_button);
        iconView.setImageResource(R.drawable.ic_toolbar_search);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                // adapter.getFilter().filter(query);
                UiUtils.hideKeyboard(getActivity());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.setFilterType(EventUserListAdapter.FILTER_BY_SEARCH);
                adapter.getFilter().filter(query);
                return false;
            }
        });

        menuFilterByTraining = menu.findItem(R.id.menu_filter_training);
        menuFilterByRental = menu.findItem(R.id.menu_filter_rental);
        menuFilterByClasses = menu.findItem(R.id.menu_filter_classes);
        menuFilterByNotSignedUsers = menu.findItem(R.id.menu_filter_not_signed_users);
        menuFilterByRacers = menu.findItem(R.id.menu_filter_racers);
        menuFilterByDuties = menu.findItem(R.id.menu_filter_duties);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_skill_level:
                viewModel.filterBySkillLevel();
                break;
            case R.id.menu_filter_training:
                viewModel.filterByTraining();
                break;
            case R.id.menu_filter_rental:
                viewModel.filterByRental();
                break;

            case R.id.menu_filter_classes:
                viewModel.filterByClass();
                break;

            case R.id.menu_filter_not_signed_users:
                adapter.setFilterType(EventUserListAdapter.FILTER_BY_NOT_SIGNED_USERS);
                applyFilter("EventUserListAdapter.FILTER_BY_NOT_SIGNED_USERS");
                break;

            case R.id.menu_filter_racers:
                adapter.setFilterType(EventUserListAdapter.FILTER_BY_RACERS);
                applyFilter("EventUserListAdapter.FILTER_BY_RACERS");
                break;
            case R.id.menu_filter_duties:
                viewModel.filterByDuties();
                break;

            case R.id.menu_filter_clear:
                applyFilter("");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSkillLevelPopup(EventParticipant user, List<String> skillLevelList){
        final String[] arrayOfSkillList = skillLevelList.toArray(new String[skillLevelList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Upgrade Skill Level");

        checkedIndex  = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayOfSkillList, checkedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                checkedIndex = which;

            }
        });

        builder.setPositiveButton("Done", (dialog, which) -> {

            dialog.dismiss();
            if(checkedIndex != -1) {
                viewModel.upgradeSkillLevel(user, arrayOfSkillList[checkedIndex]);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void applyFilter(String query){
        adapter.getFilter().filter(query);
    }

    private void filterBySkillLevel(List<String> skillLevelList){
        if(ListUtils.isEmpty(skillLevelList)){
            return;
        }

        adapter.setFilterType(EventUserListAdapter.FILTER_BY_SKILL_LEVEL);

        final String[] arrayOfSkillList = skillLevelList.toArray(new String[skillLevelList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter by Skill Level");

        int checkedItem = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayOfSkillList, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                applyFilter(arrayOfSkillList[which]);
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void filterByTraining(List<String> trainingList){

        if(ListUtils.isEmpty(trainingList)){
            return;
        }

        adapter.setFilterType(EventUserListAdapter.FILTER_BY_TRAINING);

        final String[] arrayOfTrainingList = trainingList.toArray(new String[trainingList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter by Training");

        int checkedItem = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayOfTrainingList, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                applyFilter(arrayOfTrainingList[which]);
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void filterByRental(List<String> rentalList){


        if(ListUtils.isEmpty(rentalList)){
            return;
        }

        adapter.setFilterType(EventUserListAdapter.FILTER_BY_RENTAL);


        final String[] arrayOfRentalList = rentalList.toArray(new String[rentalList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter by Rental");

        int checkedItem = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayOfRentalList, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                applyFilter(arrayOfRentalList[which]);
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.eventTitle.setTextColor(getColor(R.color.moto_primary));
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.eventTitle.setTextColor(getColor(R.color.colorPrimary));
    }
}
