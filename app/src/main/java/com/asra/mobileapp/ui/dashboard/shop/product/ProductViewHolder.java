package com.asra.mobileapp.ui.dashboard.shop.product;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.Product;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public TextView title;

    public ImageView ivEventImage;
    public View progressBar;

    public CardView cardView;



    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.product_item_title);
        ivEventImage = itemView.findViewById(R.id.product_item_image);
        progressBar = itemView.findViewById(R.id.product_item_progressbarContainer);

        cardView = itemView.findViewById(R.id.product_item_main_container);

    }

    public void setView(Product item){

        progressBar.setVisibility(View.VISIBLE);

        Timber.d("Card Image - %s", item.image);
        item.image = UiUtils.getETAbsoluteURL(item.image);

        GlideHelper.setImage(ivEventImage, item.image, R.drawable.et_fallback_image, progressBar);

        title.setText(item.title);

    }

    public void setEventListener(final ProductClickListener listener, final Product item){
        if(listener != null){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onViewDetails(item);
                }
            });

        }
    }

}

