package com.asra.mobileapp.ui.general.enrolledevents;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.util.ListUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView sectionHeader;
    public TextView eventTitle;
    public TextView orderDate;
    public TextView eventDate;
    public ImageView eventPhoto;
    public TextView menuIcon;
    public TextView btnShowPassport;
    private boolean canCancelEvents;

    public ProgressBar progressBar;


    private EventActionListener listener;


    public EventViewHolder(@NonNull View itemView,boolean canCancelEvents, EventActionListener listener) {
        super(itemView);

        sectionHeader = itemView.findViewById(R.id.event_item_header_section_title);
        eventTitle = itemView.findViewById(R.id.event_item_header_event_title);
        orderDate = itemView.findViewById(R.id.event_item_header_order_date);
        eventDate = itemView.findViewById(R.id.event_item_header_event_date);
        menuIcon = itemView.findViewById(R.id.menuIndicator);

        eventPhoto = itemView.findViewById(R.id.event_item_header_event_image);
        progressBar = itemView.findViewById(R.id.eventImageProgressbar);
        btnShowPassport = itemView.findViewById(R.id.btnShowPassport);

        this.canCancelEvents = canCancelEvents;
        this.listener = listener;

        eventTitle.setTextColor(UiUtils.getPrimaryColor(eventTitle.getContext()));
    }

    public void setView(EnrolledEvent item, boolean isUpComing, boolean canCancelEvents){
        this.canCancelEvents = canCancelEvents;

        String eventMonth = DateUtils.getMonthYear(item.eventMonth,
                DateUtils.MMYYYY_DATE_PATTERN);
        sectionHeader.setText(eventMonth);
        eventTitle.setText("Event: "+item.productName);

        String formattedOrderDate = DateUtils.getDateAsString(item.orderDate,
                DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        orderDate.setText("Order Date: "+formattedOrderDate);

        String formattedEventDate = DateUtils.getDateAsString(item.eventDate,
                DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        eventDate.setText("Event Date: "+formattedEventDate);


        if (!TextUtils.isEmpty(item.eventImage)) {

            GlideHelper.setImage(eventPhoto, item.eventImage, progressBar);

        } else {
            // make sure Glide doesn't load anything into this view until told otherwise

            // remove the placeholder (optional); read comments below
            progressBar.setVisibility(View.GONE);
            eventPhoto.setVisibility(View.VISIBLE);
            eventPhoto.setImageDrawable(null);
        }


        menuIcon.setVisibility(isUpComing ? View.VISIBLE : View.GONE);
        menuIcon.setOnClickListener(view -> {
            showPopUpMenu(item, canCancelEvents);
        });


        if(item.hasPassport){
            btnShowPassport.setText(R.string.tech_passport);
            btnShowPassport.setEnabled(true);
        }else{
            btnShowPassport.setText(R.string.i_am_here);
            btnShowPassport.setEnabled(item.enableSelfSign);
        }
        btnShowPassport.setVisibility(isUpComing ? View.VISIBLE : View.GONE);

        btnShowPassport.setOnClickListener(view -> {
            if(listener != null){
                if(item.hasPassport)
                    listener.showPassport(item);
                else
                    listener.uploadSelfie(item);
            }
        });
        if(ETApplication.getInstance().isEvApp()){
            btnShowPassport.setBackgroundResource(R.drawable.selector_button_primary);
        }else{
            btnShowPassport.setBackgroundResource(R.drawable.selector_button_moto_primary);

        }
    }


    private void showPopUpMenu(EnrolledEvent event, boolean canCancelEvents){
        PopupMenu popup = new PopupMenu(itemView.getContext(), menuIcon);
        //inflating menu from xml resource
        popup.inflate(R.menu.menu_context_upoming_event);
        //adding click listener

        MenuItem menuCancel = popup.getMenu().findItem(R.id.menu_cancel);
        menuCancel.setVisible(canCancelEvents);

        MenuItem menuIamHere = popup.getMenu().findItem(R.id.menu_i_am_here);
        menuIamHere.setVisible(false);
        menuIamHere.setEnabled(event.canUploadPassport() && !event.hasPassport);

        MenuItem menuAccessories = popup.getMenu().findItem(R.id.menu_accessories);
        menuAccessories.setVisible(event.hasAccessories());

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_cancel:
                    if(listener != null){
                        listener.onCancel(event);
                    }
                    break;
                case R.id.menu_i_am_here:
                    if(listener != null){
                        listener.uploadSelfie(event);
                    }
                    break;
                case R.id.menu_accessories:
                    if(listener != null){
                        if (ListUtils.isNotEmpty(event.motoClasses)) {
                            listener.showMotoAccessories(event.motoClasses);
                        } else {
                            listener.showEvolveAccessories(event.rentals, event.trainingList);

                        }
                    }
                    break;
            }
            return false;
        });
        //displaying the popup
        popup.show();
    }

}
