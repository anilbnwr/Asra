package com.asra.mobileapp.ui.admin.eventparticipants;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class EventUserListAdapter extends RecyclerView.Adapter<ParticipantViewHolder> implements Filterable {


    public static final int FILTER_BY_SEARCH = 0;
    public static final int FILTER_BY_SKILL_LEVEL = 1;
    public static final int FILTER_BY_TRAINING = 2;
    public static final int FILTER_BY_RENTAL = 3;
    public static final int FILTER_BY_CLASSES = 4;
    public static final int FILTER_BY_NOT_SIGNED_USERS = 5;
    public static final int FILTER_BY_RACERS = 6;
    public static final int FILTER_BY_DUTIES = 7;

    private List<EventParticipant> mDataset;
    private List<EventParticipant> filteredDataSet;
    private ParticipantActionListener participantActionListener;

    private int filterType = FILTER_BY_SEARCH;
    private boolean isParticipant;

    public EventUserListAdapter(ParticipantActionListener participantActionListener) {
        if (mDataset == null) {
            mDataset = new ArrayList<>();
        }
        filteredDataSet = mDataset;
        this.participantActionListener = participantActionListener;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int cartType) {


        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_sign_event_with_delete, viewGroup, false);

        return new ParticipantViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder participantViewHolder, int i) {

        participantViewHolder.setView(filteredDataSet.get(i), isParticipant);

        participantViewHolder.setEventListener(participantActionListener, filteredDataSet.get(i));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getListSize(filteredDataSet);
    }

    public void updateDateSet(List<EventParticipant> newList) {
        if (newList != null) {
            mDataset.clear();
            mDataset.addAll(newList);
            filteredDataSet.clear();
            filteredDataSet.addAll(newList);
            notifyDataSetChanged();
        }
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
                    List<EventParticipant> filteredList = new ArrayList<>();
                    for (EventParticipant row : mDataset) {

                        switch (filterType) {
                            case FILTER_BY_SEARCH:
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match
                                if (row.displayName.toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
                            case FILTER_BY_SKILL_LEVEL:
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match
                                if (!TextUtils.isEmpty(row.skillLevel) && row.skillLevel.toLowerCase().equalsIgnoreCase(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;

                            case FILTER_BY_TRAINING:
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match
                                if (!ListUtils.isEmpty(row.trainingList)) {
                                    for(String training: row.trainingList)
                                        if(training.toLowerCase().contains(charString.toLowerCase()))
                                            filteredList.add(row);
                                }
                                break;
                            case FILTER_BY_RENTAL:
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match
                                if (!ListUtils.isEmpty(row.rentals)) {
                                    for(EventParticipant.Rental rental: row.rentals)
                                        if(rental.name.toLowerCase().contains(charString.toLowerCase()))
                                            filteredList.add(row);
                                }
                                break;
                            case FILTER_BY_CLASSES:
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match

                                if (!ListUtils.isEmpty(row.motoClasses)) {
                                    for(EventParticipant.MotoClass motoClass: row.motoClasses)
                                        for(EventParticipant.RaceClass race: motoClass.raceClasses)
                                            if(race.className.toLowerCase().contains(charString.toLowerCase()))
                                                filteredList.add(row);
                                }


                                break;
                            case FILTER_BY_NOT_SIGNED_USERS:
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match

                                if(!row.eWaiver){
                                    filteredList.add(row);
                                }

                                break;
                            case FILTER_BY_RACERS:
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match

                                if(row.motoPurchased){
                                    filteredList.add(row);
                                }

                                break;
                            case FILTER_BY_DUTIES:
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match


                                String duties = row.getConsolidatedDuties();
                                Timber.i("%s, %s", charSequence, duties);
                                if((duties != null && duties.toLowerCase().contains(charString.toLowerCase())) ||
                                        charString.toLowerCase().equalsIgnoreCase(row.jobAssigned)){
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
                filteredDataSet = (ArrayList<EventParticipant>) filterResults.values;
                notifyDataSetChanged();

                participantActionListener.onFilterApplied(filteredDataSet, charSequence.toString());
            }
        };
    }

    public void setFilterType(int type){
        filterType = type;
    }

    public void setParticipant(boolean participant) {
        isParticipant = participant;
    }
}