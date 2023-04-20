package com.asra.mobileapp.ui.dashboard.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.EventHost;
import com.asra.mobileapp.util.ListUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventHostsAdapter extends RecyclerView.Adapter<EventHostsAdapter.EventHostViewHolder> {

    private List<EventHost> eventHosts;
    public EventHostsAdapter(List<EventHost> eventHosts){
        super();
        this.eventHosts = eventHosts;

    }

    @NonNull
    @Override
    public EventHostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_host, parent, false);


        return new EventHostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHostViewHolder holder, int position) {
        holder.setImage(eventHosts.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(eventHosts) ?  0 : eventHosts.size();
    }

    public static class EventHostViewHolder extends RecyclerView.ViewHolder{

        ImageView hostImage;

        public EventHostViewHolder(@NonNull View itemView) {
            super(itemView);
            hostImage = itemView.findViewById(R.id.image);
        }
        public void setImage(String url){
            GlideHelper.setImage(hostImage, url, null);
        }
    }
}
