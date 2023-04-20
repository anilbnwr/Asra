package com.asra.mobileapp.ui.general.ewaiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.WaiverEvent;
import com.asra.mobileapp.util.StringUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class EWaiverViewHolder extends RecyclerView.ViewHolder {

   public TextView title;
    private TextView hostName;
    private TextView tvEventDate;

    private View tvView;
    private View btnEWaiver;
    private ImageView ivEventImage;
    private View progressBar;




    EWaiverViewHolder(@NonNull View itemView) {
        super(itemView);



        title = itemView.findViewById(R.id.event_item_name);
        hostName = itemView.findViewById(R.id.event_item_type);

        tvEventDate = itemView.findViewById(R.id.event_item_date);

        tvView = itemView.findViewById(R.id.event_item_info_container);
        btnEWaiver = itemView.findViewById(R.id.btn_e_waiver);

        ivEventImage = itemView.findViewById(R.id.event_item_image);
        progressBar = itemView.findViewById(R.id.event_item_progressbarContainer);


        updateTheme();

    }

    public void setView(WaiverEvent item){
        title.setText(item.getTitle());
        hostName.setText(String.format("Hosted By %s", item.getEventType()));


        String eventDate = DateUtils.getDateAsString(item.getEventDate(),
                DateUtils.SIMPLE_DATE_NO_TIME,
                DateUtils.DD_MMM_YYYY_DATE_PATTERN);

        tvEventDate.setText(String.format("Event Date: %s", eventDate));
        tvEventDate.setVisibility(StringUtils.isEmpty(item.getEventDate()) ? View.GONE : View.VISIBLE);

        progressBar.setVisibility(View.VISIBLE);

        Timber.d("Event Item Image - %s", item.getFullEventLogo());
        String imageUrl = UiUtils.getETAbsoluteURL(item.getFullEventLogo());
        GlideHelper.setImage(ivEventImage, imageUrl, progressBar);

    }



    public void setEventListener(final WaiverListAdapter.EWaiverListener listener, final WaiverEvent item){
        if(listener != null){

            tvView.setOnClickListener(view -> listener.onItemClicked(item));

        }
    }

    private void updateTheme(){
        title.setTextColor(getPrimaryColor());
        if(ETApplication.getInstance().isEvApp()) {
            btnEWaiver.setBackgroundResource(R.drawable.selector_button_primary);
        }else{
            btnEWaiver.setBackgroundResource(R.drawable.selector_button_moto_primary);

        }

    }

    public int getPrimaryColor(){
        int colorCode = ETApplication.getInstance().isEvApp() ? R.color.colorGreen
                :R.color.colorBlue;
        return UiUtils.getColorFromResource(title.getContext(), colorCode);
    }
}
