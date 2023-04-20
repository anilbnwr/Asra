package com.asra.mobileapp.ui.dashboard;

import android.content.Intent;
import android.os.Handler;
import android.provider.CalendarContract;
import android.view.MenuItem;

import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.dialog.ETConfirmationDialog;
import com.asra.mobileapp.ui.base.ETFragment;

import java.util.Calendar;
import java.util.Date;

import androidx.databinding.ViewDataBinding;
import timber.log.Timber;

public abstract class ShoppeFragment<V extends ShoppeViewModel, D extends ViewDataBinding>
        extends ETFragment<V, D> {

    public abstract MenuItem getCartToolbarMenu() ;

    public void setCartBadgeCount(int count) {
        Timber.i("Setting badge count, %s", count);
        if (count > 0) {
            MenuItem menuItem = getCartToolbarMenu();

            if (menuItem != null) {
                new Handler().postDelayed(() -> {
                    UiUtils.setCartBadgeCount(getContext(), menuItem, count);
                    UiUtils.setCartBadgeCount(getContext(), menuItem, count);
                }, 300) ;
            }
        }
    }

    public void popFragmentsToRoot(){
        getDashboardActvity().clearTabStack(UiUtils.TAB_CART);
    }

    public void popFragment(int depth){
        getDashboardActvity().popFragment(depth);
    }
    @Override
    public void initializeViews() {
         viewModel.loadCartList();
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.getCartItemCountObservable().observe(this, this::setCartBadgeCount);
        viewModel.cartUpdatedObservable.observe(this, this::showSuccessMessage);
        viewModel.cartUpdateErrorObservable.observe(this, this::showErrorMessage);
    }

    protected void addEventToCalendar(String title, String date) {
        ETConfirmationDialog<Integer> dialog = new ETConfirmationDialog<>(getActivity(),
                new ETConfirmationDialog.ConfirmationListener<Integer>() {
                    @Override
                    public void onConfirmed(Integer passthrough) {
                        super.onConfirmed(passthrough);
                        addCalendarEvent(title, DateUtils.convertToDate(date, DateUtils.SIMPLE_DATE_NO_TIME));
                    }
                }, 0);
        dialog.setDialogTitle("Event Added To Cart");
        dialog.setDialogMessage("Would you like to add this event to calendar?");
        dialog.setSecondaryMessage("");
        dialog.setDialogBtnPositive("Add To Calendar");
        dialog.show();
    }

    private void addCalendarEvent(String eventTitle, Date eventDate) {
        Calendar calendarEvent = Calendar.getInstance();
       // Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        calendarEvent.setTime(eventDate);
        Intent i = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI);
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendarEvent.getTimeInMillis());
        i.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        //i.putExtra("rule", "FREQ=YEARLY");
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
        i.putExtra(CalendarContract.Events.TITLE, "Evolve GT Event");
        i.putExtra(CalendarContract.Events.DESCRIPTION, eventTitle);
        try {
            startActivity(i);
        }catch (Exception e){
            Timber.e("Calendar not found");
        }
    }

    public DashboardActivity getDashboardActvity(){
        return (DashboardActivity) getActivity();
    }
}
