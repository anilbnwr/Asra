package com.asra.mobileapp.ui.dashboard.duties;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DutyListAdapter extends RecyclerView.Adapter<DutyViewHolder> {


    private List<DutyListViewModel.DutyDisplayItem> mDataset;

    public DutyListAdapter() {
        if (mDataset == null) {
            mDataset = new ArrayList<>();
        }
    }

    public void updateDateSet(List<DutyListViewModel.DutyDisplayItem> newList) {
        if (newList != null) {
            mDataset.clear();
            mDataset.addAll(newList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public DutyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_duty, viewGroup, false);

        return new DutyViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull DutyViewHolder eventsViewHolder, int i) {

        eventsViewHolder.setView(mDataset.get(i));

    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}