package com.asra.mobileapp.ui.admin.completedevents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.model.CompletedEvent;
import com.asra.mobileapp.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompletedEventsAdapter extends RecyclerView.Adapter<CompletedEventViewHolder> implements Filterable {



    static final int FILTER_BY_SEARCH = 0;
    static final int FILTER_BY_MONTHS = 1;

    static final int FILTER_BY_EVENT_TYPE = 2;
    static final int FILTER_BY_TRAINING_TYPE = 3;

    private List<CompletedEvent> mDataset;
    private List<CompletedEvent> filteredDataSet;
    private CompletedEventsListener completedEventsListener;

    private int filterType = FILTER_BY_SEARCH;

    CompletedEventsAdapter(CompletedEventsListener completedEventsListener) {
        if (mDataset == null) {
            mDataset = new ArrayList<>();
        }
        filteredDataSet = new ArrayList<>();
        this.completedEventsListener = completedEventsListener;

    }



    @NonNull
    @Override
    public CompletedEventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_admin_events, viewGroup, false);

        return new CompletedEventViewHolder(v);

    }



    @Override
    public void onBindViewHolder(@NonNull CompletedEventViewHolder viewHolder, int i) {

        viewHolder.setView(filteredDataSet.get(i));

        viewHolder.setEventListener(completedEventsListener, filteredDataSet.get(i));
    }

    @Override
    public int getItemCount() {
        return filteredDataSet.size();
    }

    public void updateDateSet(List<CompletedEvent> newList) {
        if (newList != null) {
            mDataset.clear();
            mDataset.addAll(newList);

            filteredDataSet.clear();
            filteredDataSet.addAll(mDataset);

            notifyDataSetChanged();
        }
    }



    void setFilterType(int type){
        filterType = type;
    }

    private String charString;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredDataSet = mDataset;
                } else {
                    List<CompletedEvent> filteredList = new ArrayList<>();
                    for (CompletedEvent row : mDataset) {

                        switch (filterType){
                            case FILTER_BY_SEARCH:
                                if (row.title.toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
                            case FILTER_BY_EVENT_TYPE:
                                if (row.eventType.toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
                            case FILTER_BY_MONTHS:
                                String monthYear = DateUtils.getMonthYear(row.eventDate, DateUtils.YYYY_MM_DD_DATE_PATTERN);
                                if (monthYear.toLowerCase().equalsIgnoreCase(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
                            case FILTER_BY_TRAINING_TYPE:

                                if(!ListUtils.isEmpty(row.trainingTypes)){
                                    for(String trainingType: row.trainingTypes){
                                        if(charString.toLowerCase().equalsIgnoreCase(trainingType.toLowerCase())){
                                            filteredList.add(row);
                                        }
                                    }
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
                filteredDataSet = (ArrayList<CompletedEvent>) filterResults.values;
                notifyDataSetChanged();

                completedEventsListener.onFilter(filteredDataSet, charSequence.toString());
            }
        };
    }

}