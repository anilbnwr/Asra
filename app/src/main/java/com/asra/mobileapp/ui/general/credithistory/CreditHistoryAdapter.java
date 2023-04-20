package com.asra.mobileapp.ui.general.credithistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.CreditHistory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CreditHistoryAdapter extends RecyclerView.Adapter<CreditViewHolder> {

    private List<CreditHistory> mDataset;

    public CreditHistoryAdapter() {
        if(mDataset == null){
            mDataset = new ArrayList<>();
        }

    }

    public void updateData(List<CreditHistory> creditList){

        if(creditList != null && !creditList.isEmpty()) {
            this.mDataset.clear();
            this.mDataset.addAll(creditList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CreditViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.credit_list_item, viewGroup, false);

        CreditViewHolder vh = new CreditViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull CreditViewHolder creditViewHolder, int i) {

        creditViewHolder.setView(mDataset.get(i));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
