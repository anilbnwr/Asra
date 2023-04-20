package com.asra.mobileapp.ui.dashboard.events;

import com.asra.mobileapp.model.Event;

public interface EventItemClickListener {

    void onViewDetails(Event item);
    void onAddToCart(Event item);

}
