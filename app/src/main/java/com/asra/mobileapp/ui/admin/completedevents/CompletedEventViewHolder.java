package com.asra.mobileapp.ui.admin.completedevents;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.CompletedEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompletedEventViewHolder extends RecyclerView.ViewHolder {


    //EnrolledEvent
    private TextView eventName;
    private TextView eventDate;
    private View container;
    public View progressbar;
    private ImageView logoView;
    private TextView btnViewParticipants;
    private TextView btnViewDuties;



    public CompletedEventViewHolder(@NonNull View itemView) {
        super(itemView);

        eventName = itemView.findViewById(R.id.admin_event_value);
        eventDate = itemView.findViewById(R.id.admin_event_date_value);
        container = itemView.findViewById(R.id.admin_event_container);
        logoView = itemView.findViewById(R.id.admin_event_logo);
        progressbar = itemView.findViewById(R.id.progressContainer);
        btnViewDuties = itemView.findViewById(R.id.btnViewDuties);
        btnViewParticipants = itemView.findViewById(R.id.btnViewParticipants);

        btnViewParticipants.setTextColor(UiUtils.getPrimaryColor(btnViewParticipants.getContext()));
        btnViewDuties.setTextColor(UiUtils.getPrimaryColor(btnViewParticipants.getContext()));

        if(ETApplication.getInstance().isEvApp()) {
            eventName.setTextColor(UiUtils.getColorFromResource(eventName.getContext(),
                    R.color.colorPrimary));
            btnViewParticipants.setBackgroundResource(R.drawable.selector_border_green);
            btnViewDuties.setBackgroundResource(R.drawable.selector_border_green);

        }else {
            eventName.setTextColor(UiUtils.getColorFromResource(eventName.getContext(),
                    R.color.moto_primary));
            btnViewParticipants.setBackgroundResource(R.drawable.selector_border_blue);
            btnViewDuties.setBackgroundResource(R.drawable.selector_border_blue);

        }


    }

    public void setView(CompletedEvent item) {


        eventName.setText(item.title);


        String formattedDate = DateUtils.getDateAsString(item.eventDate, DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        eventDate.setText("Event Date: "+formattedDate);

        GlideHelper.setImage(logoView, item.eventLogo, R.drawable.et_fallback_image, progressbar);

    }


    public void setEventListener(final CompletedEventsListener listener, final CompletedEvent item) {
        if (listener != null) {

                btnViewParticipants.setOnClickListener((View view)-> listener.viewParticipants(item));
                btnViewDuties.setOnClickListener((View view)-> listener.viewDuties(item));

        }
    }

}
