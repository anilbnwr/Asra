package com.asra.mobileapp.ui.dashboard.shop.shopcards;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.ShopCard;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class CardsViewHolder extends RecyclerView.ViewHolder {

   public TextView title;
    public TextView price;

    public ImageView ivEventImage;
    public View progressBar;

    public CardView cardView;

    private int type;


    public CardsViewHolder(@NonNull View itemView, int type) {
        super(itemView);
        this.type = type;

        title = itemView.findViewById(R.id.cards_item_title);
        price = itemView.findViewById(R.id.cards_item_price);
        ivEventImage = itemView.findViewById(R.id.cards_item_image);
        progressBar = itemView.findViewById(R.id.card_item_progressbarContainer);

        cardView = itemView.findViewById(R.id.card_item_main_container);

        int color = UiUtils.getColorFromResource(cardView.getContext(), R.color.colorPrimaryDark);
        if(!ETApplication.getInstance().isEvApp()){
            color = UiUtils.getColorFromResource(cardView.getContext(), R.color.motoPrimaryDark);
        }
        title.setTextColor(color);
        price.setTextColor(color);
    }

    public void setView(ShopCard item){

        progressBar.setVisibility(View.VISIBLE);

        Timber.d("Card Image - %s", item.imagePath);

        GlideHelper.setImage(ivEventImage, item.imagePath, progressBar);


        if(type == CardListAdapter.TYPE_ARCHIE_CARD){
            price.setVisibility(View.GONE);
            title.setText(item.content);
        }else{
            price.setText("$"+item.price);
            title.setText(item.title);
            price.setVisibility(View.VISIBLE);
        }

    }

    public void setEventListener(final CardItemClickListener listener, final ShopCard item){
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
