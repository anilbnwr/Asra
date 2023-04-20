package com.asra.mobileapp.ui.dashboard.events;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventHost;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.StringUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class ETEventsViewHolder extends RecyclerView.ViewHolder {

   public TextView title;
    private TextView hostName;
    public TextView price;
    private TextView tvEventDate;
    private TextView tvExternalInfo;

    private View tvAddToCart;
    private View btnAddToCart;
    private View tvView;
    private ImageView ivEventImage;
    private ImageView ivEventCancllation;
    private View progressBar;

    private RecyclerView eventHostList;


    private String userRole;

    ETEventsViewHolder(@NonNull View itemView, String userRole, boolean shouldApplyTheme) {
        super(itemView);

        this.userRole = userRole;


        title = itemView.findViewById(R.id.event_item_name);
        hostName = itemView.findViewById(R.id.event_item_type);
        price = itemView.findViewById(R.id.event_item_price);

        ivEventCancllation = itemView.findViewById(R.id.event_cancel_status);
        eventHostList = itemView.findViewById(R.id.eventHostsList);
        tvExternalInfo = itemView.findViewById(R.id.event_external_host);

        tvEventDate = itemView.findViewById(R.id.event_item_date);

        tvAddToCart = itemView.findViewById(R.id.event_item_btn_add_to_cart);
        btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        tvView = itemView.findViewById(R.id.event_item_info_container);

        ivEventImage = itemView.findViewById(R.id.event_item_image);
        progressBar = itemView.findViewById(R.id.event_item_progressbarContainer);

        eventHostList.setHasFixedSize(true);
        eventHostList.setLayoutManager(new LinearLayoutManager(eventHostList.getContext(),
                RecyclerView.HORIZONTAL, false));

        if(shouldApplyTheme)
            updateTheme();

    }

    public void setView(Event item){
        title.setText(item.title);
        hostName.setText(String.format("Hosted By %s", item.eventType));

        double roleTotal = AppUtils.getDouble(item.price);

        if(item.roleBasedPrice != null){
            roleTotal = item.roleBasedPrice.getRoleBasedPrice(
                    userRole, roleTotal);

        }
        Timber.d("Role Based Price- %s", roleTotal);
        price.setText(String.format("Starting From: %s", StringUtils.formatAmount(roleTotal)));

        String eventDate = DateUtils.getDateAsString(item.eventDate,
                DateUtils.SIMPLE_DATE_NO_TIME,
                DateUtils.DD_MMM_YYYY_DATE_PATTERN);

        tvEventDate.setText(String.format("Event Date: %s", eventDate));
        tvEventDate.setVisibility(StringUtils.isEmpty(item.eventDate) ? View.GONE : View.VISIBLE);

        progressBar.setVisibility(View.VISIBLE);

        Timber.d("Event Item Image - %s", item.logoPath);
        String imageUrl = UiUtils.getETAbsoluteURL(item.logoPath);
        GlideHelper.setImage(ivEventImage, imageUrl, progressBar);

        /*
        if(TextUtils.isEmpty(item.eventMotoUrl)){
            ivEventMotoImage.setVisibility(View.GONE);
        }else{
            ivEventMotoImage.setVisibility(View.VISIBLE);
            GlideHelper.setImage(ivEventMotoImage, item.eventMotoUrl, null);
        }*/

        boolean addToCartEnabled = ETApplication.getInstance().isEvApp() && !item.isCancelled;
        tvAddToCart.setVisibility(addToCartEnabled ? View.VISIBLE : View.INVISIBLE);


        ivEventCancllation.setVisibility(item.isCancelled ? View.VISIBLE : View.GONE);


        if(item.isMotoEvent()){
            //tvAddToCart.setBackgroundResource(getDeleteIcon());
            tvAddToCart.setVisibility(View.GONE);
            btnAddToCart.setVisibility(View.GONE);
        }else{

            if(AppUtils.isTrue(item.inCart) || item.isCancelled){
                tvAddToCart.setVisibility(View.GONE);
                btnAddToCart.setVisibility(View.GONE);
            }else {
                btnAddToCart.setVisibility(View.VISIBLE);
                tvAddToCart.setVisibility(View.VISIBLE);
        //        tvAddToCart.setBackgroundResource(R.drawable.ic_drawer_cart);
            }
        }

        List<EventHost> hostList = item.getActiveEventHosts();
        if(ListUtils.isEmpty(hostList)){
            eventHostList.setVisibility(View.GONE);
        }else{
            eventHostList.setVisibility(View.VISIBLE);
            EventHostsAdapter adapter = new EventHostsAdapter(hostList);
            eventHostList.setAdapter(adapter);
        }

        if(item.externalEventHost != null){
            tvExternalInfo.setVisibility(View.VISIBLE);
            tvExternalInfo.setText(item.externalEventHost.text);
        }else
            tvExternalInfo.setVisibility(View.GONE);
    }



    public void setEventListener(final EventItemClickListener listener, final Event item){
        if(listener != null){
            btnAddToCart.setOnClickListener(view -> listener.onAddToCart(item));

            /*tvView.setOnClickListener(view -> listener.onViewDetails(item));

            ivEventImage.setOnClickListener(view -> listener.onViewDetails(item));*/
        }
    }

    private void updateTheme(){
        title.setTextColor(getPrimaryColor());
    }

    public int getPrimaryColor(){
        int colorCode = ETApplication.getInstance().isEvApp() ? R.color.colorGreen
                :R.color.colorBlue;
        return UiUtils.getColorFromResource(title.getContext(), colorCode);
    }
}