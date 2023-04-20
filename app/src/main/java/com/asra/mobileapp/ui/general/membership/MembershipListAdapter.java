package com.asra.mobileapp.ui.general.membership;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.UserMembership;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MembershipListAdapter extends RecyclerView.Adapter<MembershipViewHolder>  {



    private List<Membership> mDataset;
    private UserMembership currentMembership;
    private MembershipActionsCallback membershipActionsCallback;

    private String role;

    public UserMembership getCurrentMembership() {
        return currentMembership;
    }

    public void setCurrentMembership(UserMembership currentMembership) {
        this.currentMembership = currentMembership;
    }

    public MembershipListAdapter(MembershipActionsCallback actionsCallback) {
        if(mDataset == null){
            mDataset = new ArrayList<>();
        }
        this.membershipActionsCallback = actionsCallback;
    }

    public void replaceDateSet(List<Membership> newList){
        if(newList != null){
            mDataset.clear();
            mDataset.addAll(newList);

            notifyDataSetChanged();
        }
    }

    public List<Membership> getDataset(){
        return mDataset;
    }
    @NonNull
    @Override
    public MembershipViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_membership, viewGroup, false);

        MembershipViewHolder vh = new MembershipViewHolder(v, role, currentMembership,
                membershipActionsCallback);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull MembershipViewHolder eventsViewHolder, int i) {

        eventsViewHolder.setView(mDataset.get(i));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setRole(String role) {
        this.role = role;
    }
}