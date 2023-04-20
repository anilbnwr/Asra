package com.asra.mobileapp.ui.dashboard.duties;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DutyViewHolder extends RecyclerView.ViewHolder {

    private TextView dutyName;

    private ImageView status;
    private View headerView;
    private View dutyView;
    boolean isEveApp;

    private TextView eventName;
    private TextView eventDate;

    public DutyViewHolder(@NonNull View itemView) {
        super(itemView);

        dutyName = itemView.findViewById(R.id.dutyName);
        status = itemView.findViewById(R.id.status);

        headerView = itemView.findViewById(R.id.headerContainer);
        dutyView = itemView.findViewById(R.id.dutyContainer);

        eventName = itemView.findViewById(R.id.eventName);
        eventDate = itemView.findViewById(R.id.eventDate);

        isEveApp = ETApplication.getInstance().isEvApp();
    }

    public void setView(DutyListViewModel.DutyDisplayItem item){

        if(item.isHeader){
            headerView.setVisibility(View.VISIBLE);
            dutyView.setVisibility(View.GONE);

            eventName.setText(item.eventDuty.getEvent());
            eventDate.setText(String.format("Date: %s", DateUtils.getDateAsString(item.eventDuty.getEventDate(),
                    DateUtils.YYYY_MM_DD_DATE_PATTERN, DateUtils.DD_MMM_YYYY_DATE_PATTERN)));



        }else{
            headerView.setVisibility(View.GONE);
            dutyView.setVisibility(View.VISIBLE);

            dutyName.setText(item.duty.getDuty());
            if(item.duty.getStatus()){
                if(isEveApp)
                    status.setImageResource(R.drawable.ic_evolve_tik);
                else
                    status.setImageResource(R.drawable.ic_moto_tik);
            }else{
                status.setImageResource(R.drawable.ic_not_assigned);
            }
        }

    }
}
