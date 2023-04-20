package com.asra.mobileapp.ui.general.enrolledevents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.EnrolledEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EnrolledEventListAdapter extends RecyclerView.Adapter<EventViewHolder>{

    private List<EnrolledEvent> mDataset;

    private EventActionListener eventActionListener;
    private int type;
    private boolean canCancelEvents;

    public EnrolledEventListAdapter(int type) {
        if(mDataset == null){
            mDataset = new ArrayList<>();
        }
        this.type = type;
    }

    public void setEventActionListener(EventActionListener listener){
        this.eventActionListener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.events_group_list_raw_item, viewGroup, false);

        return new EventViewHolder(v, canCancelEvents, eventActionListener);

    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {

        eventViewHolder.setView(mDataset.get(i), type == EventConstants.TYPE_UPCOMING, canCancelEvents);

        if(i > 0){
            String prevEventMonth = mDataset.get(i-1).eventMonth;
            if(mDataset.get(i).eventMonth.equalsIgnoreCase(prevEventMonth)){
                eventViewHolder.sectionHeader.setVisibility(View.GONE);
            }else{
                eventViewHolder.sectionHeader.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setCanCancelEvents(boolean canCancelEvents) {
        this.canCancelEvents = canCancelEvents;
        notifyDataSetChanged();
    }

    public void updateDataSet(List<EnrolledEvent> enrolledEvents) {
        if(enrolledEvents != null){
            mDataset.clear();
            mDataset.addAll(enrolledEvents);
            notifyDataSetChanged();
        }
    }


}
