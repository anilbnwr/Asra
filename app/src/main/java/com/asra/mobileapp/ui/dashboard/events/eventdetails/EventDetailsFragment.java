package com.asra.mobileapp.ui.dashboard.events.eventdetails;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.dialog.ETConfirmationDialog;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.common.ETFlexRadioLayout;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.dialog.MRLPurchaseDialog;
import com.asra.mobileapp.common.dialog.TrackDayPurchaseDialog;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.databinding.FragmentEventDetailsBinding;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.ui.base.model.DialogData;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.ui.dashboard.events.eventdetails.model.EventData;
import com.asra.mobileapp.ui.dashboard.events.motoaccessoryselection.EventAccessorySelectionFragment;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.StringUtils;
import com.google.gson.Gson;

import androidx.core.text.HtmlCompat;
import timber.log.Timber;

public class EventDetailsFragment extends ShoppeFragment<EventDetailsViewModel, FragmentEventDetailsBinding> {

    private static final String KEY_EVENT_ID = "com.evolvegt.mobileapp.evenytid";
    private static final String KEY_EVENT_NAME = "com.evolvegt.mobileapp.evenytname";
    private static final String KEY_EVENT_TYPE = "com.evolvegt.mobileapp.evetType";

    private static final String KEY_EVENT_CART = "com.evolvegt.mobileapp.evenyt.cart";
    private MenuItem cartMenuItem;
    private Boolean shouldApplyTheme = true;


    public static EventDetailsFragment newInstance(String eventSlug, String eventName,
                                                   boolean isEvEvent) {

        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT_ID, eventSlug);
        bundle.putString(KEY_EVENT_NAME, eventName);
        bundle.putBoolean(KEY_EVENT_TYPE, isEvEvent);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static EventDetailsFragment newInstance(String eventSlug, String eventTitle,
                                                   CartItem item, boolean isEvEvent) {

        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT_ID, eventSlug);
        bundle.putString(KEY_EVENT_NAME, eventTitle);
        bundle.putBoolean(KEY_EVENT_TYPE, isEvEvent);

        String rawJson = new Gson().toJson(item);
        bundle.putString(KEY_EVENT_CART, rawJson);

        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public String getTitle() {
        return getArguments().getString(KEY_EVENT_NAME, "");
    }


    @Override
    public boolean isToolbarMenuEnabled() {
        return true;
    }

    @Override
    public MenuItem getCartToolbarMenu() {
        return cartMenuItem;
    }


    @Override
    protected Class<EventDetailsViewModel> getViewModel() {
        return EventDetailsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_event_details;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        showBackButton();

        String slug = getArguments().getString(KEY_EVENT_ID, "");
        boolean isEvEvent = getArguments().getBoolean(KEY_EVENT_TYPE, true);

        viewModel.getEventDetails(slug, isEvEvent);

        dataBinding.eventDetailsBtnAddToCart.setOnClickListener(view -> viewModel.onAddToCart());
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();

        viewModel.eventDetailsErrorObservable.observe(this, error -> {
            dataBinding.eventDetailContainer.setVisibility(View.GONE);
            showEmptyMessage(true, error);
        });
        viewModel.eventDetailsObservable.observe(this, this::setUpViews);
        viewModel.userRoleObservable.observe(this, userRole -> {
            dataBinding.eventDetailsRole.setText(userRole);
            Timber.d("User Role - %s", userRole);
        });
        viewModel.userRolePriceObservable.observe(this, price -> {
            dataBinding.eventDetailsRolePrice.setText(price);
            Timber.d("Role Based Price- %s", price);
        });
        viewModel.totalAmountObservable.observe(this, total -> {
            dataBinding.eventDetailsTotalPriceValue.setText(total);
        });


        viewModel.eventAddedToCartObservable.observe(this, event -> addEventToCalendar(event.title, event.eventDate));
        viewModel.eventToCartErrorObservable.observe(this, this::showErrorMessage);
        viewModel.eventCancelObservable.observe(this, cancelled -> {
            dataBinding.eventCancelStatus.setVisibility(
                    cancelled ? View.VISIBLE : View.GONE);
            dataBinding.eventDetailsBtnAddToCart.setEnabled(!cancelled);
        });

        viewModel.eventAccessoriesObservable.observe(this, eventDetails -> {
            loadFragment(EventAccessorySelectionFragment.newInstance(eventDetails));
        });
        viewModel.userNotEligibleObservable.observe(this, this::showNotEligibleDialog
        );

        viewModel.applyThemeObservable.observe(this, apply -> {

            if (!apply)
                updateToEvAppTheme();
        });
        viewModel.privateEventCodeObservable.observe(this, this::showPrivateCodeDialog);

        viewModel.mrlPurchaseRequired.observe(this, mrlData -> {
            MRLPurchaseDialog dialog = new MRLPurchaseDialog(getActivity(),
                    new MRLPurchaseDialog.DialogListener() {
                        @Override
                        public void onPurchase(EventDetails.MrlData mrlData) {
                            viewModel.onPurchase(mrlData);
                        }

                        @Override
                        public void onDismiss() {
                            super.onDismiss();
                        }
                    });
            dialog.setAddToCartEnabled(mrlData.canAddToCart);
            dialog.setWarningMessage(mrlData.canAddToCart ? "" : mrlData.messageContent);

            dialog.setMrlData(mrlData);
            dialog.show();
        });

        viewModel.membershipAddedToCartObservable.observe(this, message ->{
            showSimpleDialog("", message, null);

        });

        viewModel.membershipCartErrorObservable.observe(this, error ->{
            showSimpleDialog("", error, null);
        });
        viewModel.trackPurchaseRequired.observe(this, events -> {
            TrackDayPurchaseDialog dialog = new TrackDayPurchaseDialog(getActivity(),
                    new TrackDayPurchaseDialog.DialogListener() {
                        @Override
                        public void onPurchase(Event event) {
                            viewModel.addTrackDay(event);
                        }


                    });
            dialog.setTrackDays(events);
            dialog.show();
        });

        viewModel.registrClosedObservable.observe(this, error ->{
            showDialog( error, null, new ETDialog.DialogListener() {
                @Override
                public void onPositiveButtonClicked() {
                    popFragment();
                }
            });
        });
    }

    private void showNotEligibleDialog(DialogData dialogData) {
        ETDialog simpleDialog = new ETDialog(getBaseActivity(), null);
        simpleDialog.setHeaderTextColor(R.color.colorTextRed);
        simpleDialog.setButtonBackgroundResource(R.drawable.selector_button_moto_primary);
        simpleDialog.setDialogTitle(dialogData.title);
        simpleDialog.setDialogMessage(HtmlCompat.fromHtml(dialogData.message,
                HtmlCompat.FROM_HTML_MODE_LEGACY));
        simpleDialog.show();
    }

    protected void setUpViews(EventDetails eventDetail) {

        dataBinding.eventImageProgressContainer.setVisibility(View.VISIBLE);
        Timber.d("Image URL : %s", eventDetail.eventBanner);
        //set the image
        GlideHelper.setImage(this, dataBinding.eventDetailImage, eventDetail.eventBanner,
                R.drawable.et_fallback_image, dataBinding.eventImageProgressContainer);


        dataBinding.eventDetailsEventDate.setText("Date: " + DateUtils.getDateAsString(eventDetail.eventDate,
                DateUtils.SIMPLE_DATE_NO_TIME,
                DateUtils.DD_MMM_YYYY_DATE_PATTERN));


        //  syncCartData();
        if (ListUtils.isEmpty(eventDetail.trainingData)) {
            dataBinding.trainingListCardContainer.setVisibility(View.GONE);
            Timber.e("No trainings available for this event");
        } else {
            setUpTrainingList(eventDetail);
            dataBinding.trainingListCardContainer.setVisibility(View.VISIBLE);
        }

        if (ListUtils.isEmpty(eventDetail.rentalData)) {
            dataBinding.eventDetailRentalCardContainer.setVisibility(View.GONE);
            Timber.e("No trainings available for this event");
        } else {
            setUpRentalList(eventDetail);
            dataBinding.eventDetailRentalCardContainer.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(eventDetail.productInfo)) {
            dataBinding.eventDetailProductInfoContainer.setVisibility(View.GONE);
        } else {
            dataBinding.eventDetailProductInfo.setText(Html.fromHtml(eventDetail.productInfo, Html.FROM_HTML_OPTION_USE_CSS_COLORS | Html.FROM_HTML_MODE_COMPACT));
            dataBinding.eventDetailProductInfo.setMovementMethod(LinkMovementMethod.getInstance());
        }


        viewModel.updateTotal(eventDetail);


        analyticsHelper.logViewItemDetailEvent(AnalyticsModel.VIEW_ITEM_EVENT_DETAILS);

        if(eventDetail.externalEventHost != null){
            dataBinding.eventExternalHost.setVisibility(View.VISIBLE);
            dataBinding.eventExternalHost.setText(eventDetail.externalEventHost.text);
        }else{
            dataBinding.eventExternalHost.setVisibility(View.GONE);
        }
        enableAddToCart(eventDetail);

    }

    protected void setUpRentalList(EventDetails eventDetail) {


        if (eventDetail != null && !ListUtils.isEmpty(eventDetail.rentalData)) {
            dataBinding.eventDetailRentalContainer.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            int index = 0;
            for (final EventDetails.RentalDatum rental : eventDetail.rentalData) {
                final View view = inflater.inflate(R.layout.event_rental_selection_item,
                        null, false);
                CheckBox rentalTitle = view.findViewById(R.id.event_rental_title);
                rentalTitle.setText(rental.title);
                rentalTitle.setTag(rental);
                rentalTitle.setChecked(rental.selected);
                if (rental.selected && rental.selectedVariant != null) {
                    TextView rentalPrice = view.findViewById(R.id.event_rental_price);

                    rentalPrice.setText(StringUtils.formatAmount(rental.selectedVariant.price));
                }


                view.setTag(rental);
                dataBinding.eventDetailRentalContainer.addView(view, index++);

                View sizeListContainer = view.findViewById(R.id.size_list_layout);
                ETFlexRadioLayout sizeLayout = view.findViewById(R.id.event_felx_radio_group);
                sizeLayout.removeAllViews();
                rentalTitle.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {

                    if (isChecked) {
                        sizeListContainer.setVisibility(View.VISIBLE);
                    } else {
                        sizeListContainer.setVisibility(View.GONE);
                    }
                    EventDetails.RentalDatum rentalDatum = (EventDetails.RentalDatum) buttonView.getTag();
                    rentalDatum.selected = isChecked;
                    viewModel.updateTotal(eventDetail);
                    enableAddToCart(eventDetail);
                });

                if (rentalTitle.isChecked()) {
                    sizeListContainer.setVisibility(View.VISIBLE);
                } else {
                    sizeListContainer.setVisibility(View.GONE);
                }

                int variationIndex = 0;
                TableRow row = null;
                int rowIndex = 0;

                for (EventDetails.Variation variation : rental.variations) {
                    if (variationIndex % 3 == 0) {
                        if (variationIndex != 0) {
                            sizeLayout.addView(row, rowIndex++);
                        }
                        row = new TableRow(getContext());
                    }
                    RadioButton rdbtn = new RadioButton(getContext());
                    rdbtn.setId(View.generateViewId());
                    rdbtn.setText(variation.attributeValue);

                    rdbtn.setPadding(0, 0, 20, 0);
                    row.addView(rdbtn);

                    EventData data = new EventData();
                    data.selectedRental = rental;
                    data.selectedVariation = variation;

                    rdbtn.setTag(data);

                    rdbtn.setOnCheckedChangeListener(rdListener);
                    if (variationIndex == rental.variations.size() - 1) {
                        sizeLayout.addView(row);
                    }

                    variationIndex++;
                }
            }
        }

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
    private CompoundButton.OnCheckedChangeListener rdListener = (compoundButton, b) -> {
        EventData rentalData = (EventData) compoundButton.getTag();
        rentalData.selectedRental.selectedVariant = rentalData.selectedVariation;
        if (b) {
            if (rentalData.selectedVariation != null) {
                if (rentalData.selectedVariation.isOutOfStock()) {
                    dataBinding.eventRentalStockStatus.setVisibility(View.VISIBLE);
                    dataBinding.eventDetailsBtnAddToCart.setEnabled(false);
                } else {
                    dataBinding.eventRentalStockStatus.setVisibility(View.GONE);
                    dataBinding.eventDetailsBtnAddToCart.setEnabled(true);
                }

            } else {
                Timber.e("Tagged event variation is null. Could not check stock status");
            }

            View parent = (View) compoundButton.getParent().getParent().getParent().getParent();
            TextView rentalPrice = parent.findViewById(R.id.event_rental_price);
            rentalPrice.setText(StringUtils.formatAmount(rentalData.selectedVariation.price));

        }
        viewModel.onRentalVariantSelected();
    };

    private void enableAddToCart(EventDetails eventDetail) {
        boolean outOfStock = false;
        if (eventDetail != null && !ListUtils.isEmpty(eventDetail.rentalData)) {
            for (EventDetails.RentalDatum rentals : eventDetail.rentalData) {
                if (rentals.selected && rentals.selectedVariant != null) {
                    outOfStock = rentals.selectedVariant.isOutOfStock();
                    if (outOfStock) break;
                }
            }
        }
        dataBinding.eventDetailsBtnAddToCart.setEnabled(!eventDetail.isCancelled && !outOfStock);
        if(eventDetail.isCancelled){
            dataBinding.eventDetailsBtnAddToCart.setText(R.string.txt_event_cancelled);
        }else if(outOfStock){
            dataBinding.eventDetailsBtnAddToCart.setText(R.string.tracks_label_sold_out);
        }else{
            if(eventDetail.isMotoEvent()) {
                dataBinding.eventDetailsBtnAddToCart.setText(R.string.tracks_label_add_to_cart);
            }else{
                dataBinding.eventDetailsBtnAddToCart.setText(R.string.tracks_label_add_to_cart);
            }
        }
    }

    private void setUpTrainingList(EventDetails eventDetail) {
        if (eventDetail != null && !ListUtils.isEmpty(eventDetail.trainingData)) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            dataBinding.trainingListContainer.removeAllViews();
            int index = 0;
            for (EventDetails.TrainingDatum training : eventDetail.trainingData) {
                View view = inflater.inflate(R.layout.training_item, null, false);
                CheckBox checkBox = view.findViewById(R.id.checked_text_view);
                checkBox.setText(training.title);
                checkBox.setTag(training);
                checkBox.setChecked(training.selected);
                TextView trainingPrice = view.findViewById(R.id.event_training_price);
                trainingPrice.setText("$" + training.price);

                view.setTag(training);
                dataBinding.trainingListContainer.addView(view, index++);

                checkBox.setOnCheckedChangeListener((CompoundButton var1, boolean checked) -> {
                    EventDetails.TrainingDatum trainingDatum = (EventDetails.TrainingDatum) var1.getTag();
                    trainingDatum.selected = checked;
                    viewModel.updateTotal(eventDetail);
                });
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_shop, menu);
        cartMenuItem = menu.findItem(R.id.menu_toolbar_cart);
        super.onCreateOptionsMenu(menu, inflater);

        viewModel.updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar_cart) {
            switchToCart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateToMotoAppTheme() {

        super.updateToMotoAppTheme();

        dataBinding.roleBasedPriceLayout.setBackgroundColor(
                UiUtils.getColorFromResource(getContext(), R.color.colorLightBlue));

        dataBinding.totalPriceLayout.setBackgroundColor(
                UiUtils.getColorFromResource(getContext(), R.color.color_moto_bg_price));

        dataBinding.eventDetailsBtnAddToCart
                .setBackgroundResource(R.drawable.selector_button_moto_primary);

    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.roleBasedPriceLayout.setBackgroundColor(
                UiUtils.getColorFromResource(getContext(), R.color.color_bg_item_price));

        dataBinding.totalPriceLayout.setBackgroundColor(
                UiUtils.getColorFromResource(getContext(), R.color.color_bg_item_total_price));

        dataBinding.eventDetailsBtnAddToCart.setBackgroundResource(R.drawable.selector_button_primary);

    }

}
