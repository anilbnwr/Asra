package com.asra.mobileapp.ui.admin.completedevents;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.Constants;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.FragmentEtListBinding;
import com.asra.mobileapp.model.CompletedEvent;
import com.asra.mobileapp.ui.admin.eventparticipants.EventParticipantsFragment;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.util.ListUtils;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;

public class CompletedEventsFragment extends
        ETFragment<CompletedEventsViewModel, FragmentEtListBinding>
        implements CompletedEventsListener {

    public static CompletedEventsFragment newInstance(){
        return new CompletedEventsFragment();
    }

    private SearchView searchView;
    private MenuItem switchAppMenu;

    CompletedEventsAdapter adapter;
    @Override
    protected Class getViewModel() {
        return CompletedEventsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_et_list;
    }

    @Override
    public void initializeViews() {
        showHomeIcon(false);
        showEmptyMessage(false, null);
        setUpRecyclerView();

        viewModel.fetchCompletedEvents();
    }

    private void setUpRecyclerView() {
        dataBinding.etRecyclerView.setLayoutManager(getLinearLayoutManager());

        dataBinding.etRecyclerView.setHasFixedSize(true);
        dataBinding.etRecyclerView.removeItemDecoration(itemDecoration);
        dataBinding.etRecyclerView.addItemDecoration(itemDecoration);
        adapter = new CompletedEventsAdapter(this);
        dataBinding.etRecyclerView.setAdapter(adapter);
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.errorObservable.observe(this, errorMessage ->{
            showEmptyMessage(true, errorMessage);
        });
        viewModel.logoutObservable.observe(this, aBoolean -> {
            launchLoginActivity();
        });
        viewModel.completedEventsObservable.observe(this, eventList ->{
            adapter.updateDateSet(eventList);
        });

        viewModel.filterByMonthsObservable.observe(this, monthYeatList ->{
            adapter.setFilterType(CompletedEventsAdapter.FILTER_BY_MONTHS);
            showMonthYearFilterDialog(monthYeatList);

        });

        viewModel.filterByEventsObservable.observe(this, eventsList ->{
            adapter.setFilterType(CompletedEventsAdapter.FILTER_BY_EVENT_TYPE);
            showMonthYearFilterDialog(eventsList);

        });

        viewModel.filterByTrainingsObservable.observe(this, trainingList ->{
            adapter.setFilterType(CompletedEventsAdapter.FILTER_BY_TRAINING_TYPE);
            showMonthYearFilterDialog(trainingList);

        });
        viewModel.switchModuleObservable.observe(this, this::switchModule);
    }


    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        if (switchAppMenu != null)
            switchAppMenu.setIcon(R.drawable.ic_app_switch_ev);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        /*if (switchAppMenu != null)
            switchAppMenu.setIcon(R.drawable.ic_app_switch_moto);*/
    }

    @Override
    public boolean isToolbarMenuEnabled() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_admin, menu);

        MenuItem filterMenu = menu.findItem(R.id.menu_toolbar_events_filter);

        // Inflating the sub_menu menu this way, will add its menu items
        // to the empty SubMenu you created in the xml
        inflater.inflate(R.menu.menu_admin_events_filter, filterMenu.getSubMenu());

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        switchAppMenu = menu.findItem(R.id.menu_switch_app);
        switchAppMenu.setIcon(UiUtils.getSwitchAppMenuIcon(getContext()));



        TextView textView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        textView.setHint(R.string.hint_search_by_event_name);
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
                adapter.setFilterType(CompletedEventsAdapter.FILTER_BY_SEARCH);
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.menu_events_month:
                viewModel.filterByMonths();
                break;
            case R.id.menu_events_type:
                viewModel.filterByEventsTypes();
                break;

            case R.id.menu_filter_clear:
                applyFilter("");
                break;
            case R.id.menu_events_training_type:
                viewModel.filterByTrainings();
                break;

            case R.id.menu_toolbar_logout:
                viewModel.onLogout();
                break;
            case R.id.menu_switch_module:
                viewModel.switchModule();
                break;
            case R.id.menu_switch_app:
                viewModel.onSwitchApp();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void viewParticipants(CompletedEvent item) {
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(true);
        loadFragment(EventParticipantsFragment.newInstance(gson.toJson(item), Constants.KEY_DATA_USERS));
    }

    @Override
    public void viewDuties(CompletedEvent item) {
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(true);
        loadFragment(EventParticipantsFragment.newInstance(gson.toJson(item), Constants.KEY_DATA_DUTIES));
    }

    @Override
    public void onFilter(List<CompletedEvent> filteredList, String query) {
        if(ListUtils.isEmpty(filteredList)){
            String message = getConfigString(MessageProvider.error_no_search_result_for_events);
            message = String.format(message, query);
            showEmptyMessage(true, message );
        }else{

            showEmptyMessage(false, null);
        }
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_events);
    }

    private void showMonthYearFilterDialog(List<String> monthYearList){

        final String[] arrayOfMonthYearList = monthYearList.toArray(new String[monthYearList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter by Month");

        int checkedItem = -1; //this will mark  the item checked when user open the dialog
        builder.setSingleChoiceItems(arrayOfMonthYearList, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                applyFilter(arrayOfMonthYearList[which]);
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

    private void applyFilter(String query){
        adapter.getFilter().filter(query);
    }

    @Override
    public void switchModule(Boolean isAdmin) {
        if(isAdmin) {
            switchToUserDashboard();
        }else {
            getBaseActivity().finish();
        }
    }
}
