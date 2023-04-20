package com.asra.mobileapp.ui.dashboard.events;

import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.common.dialog.ETConfirmationDialog;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.FragmentShopEventsBinding;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.ui.dashboard.events.eventdetails.EventDetailsFragment;

import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class EventListFragment extends ShoppeFragment<EventsViewModel, FragmentShopEventsBinding>
        implements EventItemClickListener {
    private MenuItem cartMenuItem;
    private MenuItem gridMenuItem;
    private MenuItem listMenuItem;
    private MenuItem switchAppMenu;


    public static EventListFragment newInstance() {
        return new EventListFragment();
    }

    private EventListAdapter eventListAdapter;

    @Override
    public MenuItem getCartToolbarMenu() {
        return cartMenuItem;
    }


    @Override
    public String getTitle() {
        return getString(R.string.title_events);
    }

    @Override
    protected Class<EventsViewModel> getViewModel() {
        return EventsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_shop_events;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();
        viewModel.guestUserObservable.observe(this, guestLoggedIn -> {
            if (guestLoggedIn)
                showBackButton();
            else hideBackButton();

            if(switchAppMenu != null){
                switchAppMenu.setVisible(!guestLoggedIn);
            }
        });

        viewModel.eventsObservable.observe(this, events -> eventListAdapter.updateDateSet(events));
        viewModel.eventsFetchErrorObservable.observe(this, error -> showEmptyMessage(true, error));

        viewModel.filterbyMonthsObservable.observe(this, filters -> {
            analyticsHelper.logSearchEvent("Filter by Month", AnalyticsModel.CATEGORY_EVENTS);
            eventListAdapter.setFilterType(EventListAdapter.FILTER_BY_MONTHS);
            showFilterDialog("Filter by Month", filters);

        });

        viewModel.filterbyEventTypeObservable.observe(this, filters -> {
            analyticsHelper.logSearchEvent("Filter by Event Type", AnalyticsModel.CATEGORY_EVENTS);

            eventListAdapter.setFilterType(EventListAdapter.FILTER_BY_EVENT_TYPE);
            showFilterDialog("Filter by Event Type", filters);
        });
        viewModel.eventAddedToCartObservable.observe(this, event -> addEventToCalendar(event.title, event.eventDate));
        viewModel.eventToCartErrorObservable.observe(this, this::showErrorMessage);
        viewModel.privateEventCodeObservable.observe(this, userLoggedIn -> showPrivateCodeDialog(userLoggedIn));
    }



    private void showPrivateCodeDialog(boolean userLoggedIn) {

        if(userLoggedIn){
            ETDialog etDialog = new ETDialog(getActivity(), new ETDialog.DialogListener() {
                @Override
                public void onPositiveButtonClicked(String userInput) {


                    viewModel.onSecretCode(userInput);

                }
            });
            etDialog.setDialogTitle(getConfigString(MessageProvider.private_event_code_required));
            etDialog.setTextInputHint("Event Code");
            etDialog.setDialogBtnPositive("Add To Cart");
            etDialog.setTextInputError(getConfigString(MessageProvider.error_private_event_code));
            etDialog.setInputMode(true);
            etDialog.show();
        }else{
            ETConfirmationDialog<Integer> dialog = new ETConfirmationDialog<>(getActivity(),
                    new ETConfirmationDialog.ConfirmationListener<Integer>() {
                        @Override
                        public void onConfirmed(Integer passthrough) {
                            super.onConfirmed(passthrough);
                            onLoginRequested(true);
                        }
                    }, 0);
            dialog.setDialogTitle("Login Required");
            dialog.setDialogMessage("You must be logged in to enroll to private events");
            dialog.setSecondaryMessage("");
            dialog.setDialogBtnPositive("Login");
            dialog.show();
        }


    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        eventListAdapter = new EventListAdapter(this);
        dataBinding.tracksRecyclerView.setLayoutManager(getLinearLayoutManager());

        dataBinding.tracksRecyclerView.setHasFixedSize(true);
        eventListAdapter.displayAsList();
        dataBinding.tracksRecyclerView.setAdapter(eventListAdapter);

        dataBinding.tracksRecyclerView.removeItemDecoration(itemDecoration);
        dataBinding.tracksRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_tab_events, menu);


        // Inflating the sub_menu menu this way, will add its menu items
        // to the empty SubMenu you created in the xml


        // Switch App Menu disabled
        MenuItem filterMenu = menu.findItem(R.id.menu_toolbar_events_filter);

        inflater.inflate(R.menu.events_filter_items, filterMenu.getSubMenu());
        switchAppMenu = menu.findItem(R.id.menu_switch_app);
        switchAppMenu.setIcon(UiUtils.getSwitchAppMenuIcon(getContext()));

       // switchAppMenu.setVisible(isEvApp());



        cartMenuItem = menu.findItem(R.id.menu_toolbar_cart);

        gridMenuItem = menu.findItem(R.id.menu_toolbar_as_grid);
        listMenuItem = menu.findItem(R.id.menu_toolbar_as_list);



        super.onCreateOptionsMenu(menu, inflater);

        viewModel.updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_cart:
                switchToCart();
                return true;

            case R.id.menu_switch_app:
                viewModel.onSwitchApp();
                return true;

            case R.id.menu_event_month:
                viewModel.filterByMonths();
                return true;

            case R.id.menu_event_clear:
                analyticsHelper.logSearchEvent("Clear", AnalyticsModel.CATEGORY_EVENTS);
                viewModel.clearFilter();
                return true;
            case R.id.menu_event_filter_type:
                viewModel.filterByEventTypes();
                return true;

            case R.id.menu_toolbar_as_grid:
                dataBinding.tracksRecyclerView.setLayoutManager(getGridLayoutManager());
                eventListAdapter.displayAsGrid();
                dataBinding.tracksRecyclerView.setAdapter(eventListAdapter);
                gridMenuItem.setVisible(false);
                listMenuItem.setVisible(true);

                dataBinding.tracksRecyclerView.removeItemDecoration(itemDecoration);
                dataBinding.tracksRecyclerView.removeItemDecoration(itemGridDecoration);
                dataBinding.tracksRecyclerView.addItemDecoration(itemGridDecoration);
                return true;
            case R.id.menu_toolbar_as_list:
                dataBinding.tracksRecyclerView.setLayoutManager(getLinearLayoutManager());
                eventListAdapter.displayAsList();
                dataBinding.tracksRecyclerView.setAdapter(eventListAdapter);
                dataBinding.tracksRecyclerView.removeItemDecoration(itemGridDecoration);
                dataBinding.tracksRecyclerView.removeItemDecoration(itemDecoration);
                dataBinding.tracksRecyclerView.addItemDecoration(itemDecoration);
                gridMenuItem.setVisible(true);
                listMenuItem.setVisible(false);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onViewDetails(Event item) {
        loadFragment(EventDetailsFragment.newInstance(item.slug, item.title, !item.isMotoEvent()));
    }

    @Override
    public void onAddToCart(Event item) {
        viewModel.addEventToCart(item);
    }

    private void showFilterDialog(String title, List<String> modelList) {
        final String[] arrayOfModelList = modelList.toArray(new String[modelList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);

        int checkedItem = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayOfModelList, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                eventListAdapter.getFilter().filter(arrayOfModelList[which]);
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
    public boolean isToolbarMenuEnabled() {
        return true;
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

       /* if (switchAppMenu != null)
            switchAppMenu.setIcon(R.drawable.ic_app_switch_moto);
*/
    }
}
