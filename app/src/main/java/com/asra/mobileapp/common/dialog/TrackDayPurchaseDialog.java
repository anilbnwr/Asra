package com.asra.mobileapp.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventHost;
import com.asra.mobileapp.ui.dashboard.events.EventHostsAdapter;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.StringUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class TrackDayPurchaseDialog extends Dialog {


    private DialogListener dialogListener;


    private List<Event> trackDays;

    public TrackDayPurchaseDialog(Activity activity, DialogListener dialogListener){
        super(activity);
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_track_day_purchase);
        setDialogMargin();

        TextView btnPositive = findViewById(R.id.dialog_btn_positive);

        TextView btnNegative = findViewById(R.id.dialog_btn_negative);

        btnPositive.setOnClickListener((v)->{
            if(dialogListener != null){
                dismiss();
            }else{
                dismiss();
            }

        });
        btnNegative.setOnClickListener(view -> {
            if(dialogListener != null){
                dialogListener.onDismiss();
            }
            dismiss();
        });


        int btnDrawable = ETApplication.getInstance().isEvApp() ?
                R.drawable.selector_button_primary : R.drawable.selector_button_moto_primary;
        btnPositive.setBackgroundResource(btnDrawable);


        setUpList();

    }

    private void setUpList() {

        ViewGroup container = findViewById(R.id.eventContainer);
        container.removeAllViews();
        if(ListUtils.isNotEmpty(trackDays)){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for(Event item: trackDays){

                View itemView = inflater.inflate(R.layout.list_item_et_event_as_list, container, false);

                TextView title = itemView.findViewById(R.id.event_item_name);
                TextView hostName = itemView.findViewById(R.id.event_item_type);
                TextView price = itemView.findViewById(R.id.event_item_price);

                ImageView ivEventCancllation = itemView.findViewById(R.id.event_cancel_status);
                RecyclerView eventHostList = itemView.findViewById(R.id.eventHostsList);
                TextView tvExternalInfo = itemView.findViewById(R.id.event_external_host);

                TextView tvEventDate = itemView.findViewById(R.id.event_item_date);

                View tvAddToCart = itemView.findViewById(R.id.event_item_btn_add_to_cart);
                View btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
                View tvView = itemView.findViewById(R.id.event_item_info_container);

                ImageView ivEventImage = itemView.findViewById(R.id.event_item_image);
                View progressBar = itemView.findViewById(R.id.event_item_progressbarContainer);


                title.setText(item.title);
                hostName.setText(String.format("Hosted By %s", item.eventType));

                double roleTotal = AppUtils.getDouble(item.price);

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



                boolean addToCartEnabled =  !item.isCancelled;
            //    tvAddToCart.setVisibility(addToCartEnabled ? View.VISIBLE : View.INVISIBLE);


                ivEventCancllation.setVisibility(item.isCancelled ? View.VISIBLE : View.GONE);




                if(AppUtils.isTrue(item.inCart) || item.isCancelled){
                    tvAddToCart.setVisibility(View.GONE);
                    btnAddToCart.setVisibility(View.GONE);
                }else {
                    btnAddToCart.setVisibility(View.GONE);
                    tvAddToCart.setVisibility(View.GONE);
                 //   tvAddToCart.setBackgroundResource(R.drawable.ic_drawer_cart);
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

                btnAddToCart.setOnClickListener(view -> {
                    dialogListener.onPurchase(item);
                    dismiss();
                });

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemView.getLayoutParams();
                params.bottomMargin = 3;
                params.leftMargin = 3;
                params.rightMargin = 3;
                params.topMargin = 3;
                container.addView(itemView);
            }
        }
    }

    private void setDialogMargin() {
        try{
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }catch (Exception e){
            Timber.e(e, "Dialog Margin Adjustment failed");
        }
    }


    public void setTrackDays(List<Event> trackDays) {
        this.trackDays = trackDays;
    }

    public static abstract class DialogListener{

        public void onPurchase(Event event){

        }
        public void onDismiss(){

        }
    }

}
