package com.asra.mobileapp.ui.dashboard.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.CreditHistory;
import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.util.StringUtils;

public class ExpandableCardHelper {

    private View cardRootView;
    private ImageView cardIcon;
    private TextView tvErrorView;
    private TextView cardTitle;
    private View dataContainer;
    private View titleLayout;
    private ETExpandListener expandListener;

    public String emptyMessage;
    private ETExpandableCardListener listener;

    public ExpandableCardHelper(View cardRootView, ETExpandableCardListener listener ){
       this.cardRootView = cardRootView;
       this.listener = listener;
       init();
    }

    public void setExpandListener(ETExpandListener listener){
        this.expandListener = listener;
    }
    private void init(){
        cardIcon = cardRootView.findViewById(R.id.expandableRightIcon);

        tvErrorView = cardRootView.findViewById(R.id.expandableErrorView);
        cardTitle = cardRootView.findViewById(R.id.expandableTitle);
        dataContainer = cardRootView.findViewById(R.id.cardContentLayout);
        titleLayout = cardRootView.findViewById(R.id.titleLayout);

        TextView seeMore = cardRootView.findViewById(R.id.btn_see_more);
        seeMore.setTextColor(UiUtils.getPrimaryColor(seeMore.getContext()));
        seeMore.setOnClickListener(view -> {

            if(listener != null) {
               listener.onSeeMoreClicked();
            }
        });

        titleLayout.setOnClickListener((View v)->{
            if(!hasError()) {
                if (isExpanded()) {
                    collapse();
                } else {
                    expand();
                }
            }
        });
        collapse();
    }

    private boolean hasError(){
        return tvErrorView.getVisibility() == View.VISIBLE;
    }

    public void setContents(EnrolledEvent event){
        TextView eventTitle = cardRootView.findViewById(R.id.expandableDataTitle);
        TextView orderDate = cardRootView.findViewById(R.id.expandableOrderDate);
        TextView eventDate = cardRootView.findViewById(R.id.expandableDate);

        ImageView eventPhoto = cardRootView.findViewById(R.id.expandableDataImage);
        ImageView creditPhoto = cardRootView.findViewById(R.id.expandableCreditImage);
        creditPhoto.setVisibility(View.GONE);
        ProgressBar progressBar = cardRootView.findViewById(R.id.expandableImageProgressbar);

        eventTitle.setText("Event: "+event.productName);



        String formattedOrderDate = DateUtils.getDateAsString(event.orderDate,
                DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        orderDate.setText("Order Date: "+formattedOrderDate);

        String formattedEventDate = DateUtils.getDateAsString(event.eventDate,
                DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        eventDate.setText("Event Date: "+formattedEventDate);


        if (!TextUtils.isEmpty(event.eventImage)) {
            event.eventImage = UiUtils.getETAbsoluteURL(event.eventImage);
            progressBar.setVisibility(View.VISIBLE);
            GlideHelper.setImage(eventPhoto, event.eventImage, progressBar);

        } else {
            // make sure Glide doesn't load anything into this view until told otherwise

            // remove the placeholder (optional); read comments below
            progressBar.setVisibility(View.GONE);
            eventPhoto.setVisibility(View.VISIBLE);
            eventPhoto.setImageDrawable(null);
        }

        showError(false);

        eventTitle.setTextColor(UiUtils.getPrimaryColor(cardRootView.getContext()));

    }

    public void setContents(CreditHistory credit){
        TextView eventTitle = cardRootView.findViewById(R.id.expandableDataTitle);
        TextView description = cardRootView.findViewById(R.id.expandableOrderDate);
        TextView postedOn = cardRootView.findViewById(R.id.expandableDate);

        ImageView eventPhoto = cardRootView.findViewById(R.id.expandableDataImage);
        ProgressBar progressBar = cardRootView.findViewById(R.id.expandableImageProgressbar);
        progressBar.setVisibility(View.GONE);


        eventTitle.setText(StringUtils.formatAmount(credit.amount));

        description.setText(credit.description);

        String formattedEventDate = DateUtils.getDateAsString(credit.postDate,
                DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        postedOn.setText(String.format("Posted on: %s", formattedEventDate));


        ImageView creditPhoto = cardRootView.findViewById(R.id.expandableCreditImage);
        eventPhoto.setVisibility(View.GONE);

        if(credit.mode.equalsIgnoreCase("wallet")){
            creditPhoto.setImageResource(R.drawable.ic_credit_wallet);
        }else{
            creditPhoto.setImageResource(R.drawable.ic_credit_paypal);
        }

        eventTitle.setTextColor(UiUtils.getPrimaryColor(cardRootView.getContext()));
        collapse();


    }

    public void setEmptyMessage(String message){
        this.emptyMessage = message;
    }
    public void setTitle(String title){
        cardTitle.setText(title);
    }

    public void showError(boolean show){
        showError(show, emptyMessage);
    }
    public void showError(boolean show, String message){
        if(show){
            cardIcon.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(message)){
                tvErrorView.setText(message);
            }
            tvErrorView.setVisibility(View.VISIBLE);
            dataContainer.setVisibility(View.GONE);
        }else{
            cardIcon.setVisibility(View.VISIBLE);
            tvErrorView.setVisibility(View.GONE);
            dataContainer.setVisibility(View.VISIBLE);
        }
    }
    public void expand(){
        //new ViewAnimationUtils().expand(dataContainer);
        dataContainer.setVisibility(View.VISIBLE);
        if(isEvApp())
            cardIcon.setImageResource(R.drawable.ic_btn_minus_blue);
        else
            cardIcon.setImageResource(R.drawable.ic_btn_minus_blue);

        if(expandListener != null){
            expandListener.onExpand(true);
        }
    }

    public void collapse(){
        //new ViewAnimationUtils().collapse(dataContainer);
        dataContainer.setVisibility(View.GONE);
        if(isEvApp())
            cardIcon.setImageResource(R.drawable.selector_button_plus_moto);
        else
            cardIcon.setImageResource(R.drawable.selector_button_plus_moto);

        if(expandListener != null){
            expandListener.onExpand(false);
        }
    }

    public interface ETExpandableCardListener{
        void onSeeMoreClicked();
    }


    public interface ETExpandListener{
        void onExpand(boolean expanded);
    }

    public boolean isExpanded(){
        return dataContainer.getVisibility() == View.VISIBLE;
    }

    public View getRootView(){
        return cardRootView;
    }

    private boolean isEvApp(){
        return ETApplication.getInstance().isEvApp();
    }
}
