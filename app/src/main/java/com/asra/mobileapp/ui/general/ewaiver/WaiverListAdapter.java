package com.asra.mobileapp.ui.general.ewaiver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.WaiverEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WaiverListAdapter extends RecyclerView.Adapter<EWaiverViewHolder>  {


    private List<WaiverEvent> mDataset;
    private EWaiverListener waiverListener;

    public WaiverListAdapter(EWaiverListener actionsCallback) {
        if(mDataset == null){
            mDataset = new ArrayList<>();
        }
        this.waiverListener = actionsCallback;
    }

    public void replaceDateSet(List<WaiverEvent> newList){
        if(newList != null){
            mDataset.clear();
            mDataset.addAll(newList);

            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public EWaiverViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_waiver, viewGroup, false);

        return new EWaiverViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull EWaiverViewHolder eWaiverViewHolder, int i) {

        eWaiverViewHolder.setView(mDataset.get(i));
        eWaiverViewHolder.setEventListener(waiverListener, mDataset.get(i));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public interface EWaiverListener{
        void onItemClicked(WaiverEvent event);
    }
}