package com.asra.mobileapp.ui.dashboard.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.model.Event;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventListAdapter extends RecyclerView.Adapter<ETEventsViewHolder> implements Filterable {

    public static final int FILTER_BY_MONTHS = 1;
    public static final int FILTER_BY_EVENT_TYPE = 2;

    private List<Event> mDataset;
    private List<Event> filteredDataSet;
    private EventItemClickListener EventClickListener;

    private String userRole;
    private int filterType;

    private boolean showAsList = false;
    private boolean shouldApplyTheme = true;

    public EventListAdapter(EventItemClickListener EventClickListener) {
        if(mDataset == null){
            mDataset = new ArrayList<>();
        }
        filteredDataSet = mDataset;
        this.EventClickListener = EventClickListener;
        
    }

    public void setShouldApplyTheme(boolean shouldApplyTheme){
        this.shouldApplyTheme = shouldApplyTheme;
    }

    public void updateDateSet(List<Event> newList){
        if(newList != null){
            mDataset.clear();
            mDataset.addAll(newList);
            filteredDataSet = mDataset;
            notifyDataSetChanged();
        }
    }

    public void addDateSet(List<Event> newList){
        if(newList != null){
            mDataset.addAll(0,newList);
            notifyDataSetChanged();
        }
    }

    public List<Event> getDataset(){
        return mDataset;
    }
    @NonNull
    @Override
    public ETEventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        int layoutId = showAsList ? R.layout.list_item_et_event_as_list :
                R.layout.list_item_et_event_as_grid;
        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(layoutId, viewGroup, false);


        return new ETEventsViewHolder(v, userRole, shouldApplyTheme);

    }

    public void setFilterType(int type){
        this.filterType = type;
    }
    public void displayAsList(){
        showAsList = true;
    }

    public void displayAsGrid(){
        showAsList = false;
    }

    @Override
    public void onBindViewHolder(@NonNull ETEventsViewHolder eventsViewHolder, int i) {

        eventsViewHolder.setView(filteredDataSet.get(i));

        eventsViewHolder.setEventListener(EventClickListener, filteredDataSet.get(i));
    }

    @Override
    public int getItemCount() {
        return filteredDataSet.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredDataSet = mDataset;
                } else {
                    List<Event> filteredList = new ArrayList<>();
                    for (Event row : mDataset) {

                        switch (filterType){
                            case FILTER_BY_EVENT_TYPE:
                                if (row.eventType.toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
                            case FILTER_BY_MONTHS:
                                String monthYear = DateUtils.getDateAsString(row.eventDate,
                                        DateUtils.YYYY_MM_DD_DATE_PATTERN,
                                        DateUtils.MMMM_YYYY_DATE_PATTERN);
                                if (monthYear.toLowerCase().equalsIgnoreCase(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
                        }

                    }

                    filteredDataSet = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDataSet;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataSet = (ArrayList<Event>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}