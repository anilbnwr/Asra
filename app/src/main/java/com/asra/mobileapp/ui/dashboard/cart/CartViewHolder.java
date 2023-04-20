package com.asra.mobileapp.ui.dashboard.cart;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.StringUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder {


    //Event
    public TextView cartTitle;
    public TextView cartPrice;
    public TextView cartProperty1;
    public TextView cartProperty2;
    public TextView tvStockStatus;
    public View progressbar;

    ImageView deleteButton;
    ImageView cartItemImage;


    private String outOfStockMessage;
    public View contentLayout;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        contentLayout = itemView.findViewById(R.id.cart_item_info_container);
        cartItemImage = itemView.findViewById(R.id.cart_cell_item_image);
        tvStockStatus = itemView.findViewById(R.id.cart_item_stock_status);
        //Event Views
        cartTitle = itemView.findViewById(R.id.cartTitle);
        cartPrice = itemView.findViewById(R.id.cartPrice);
        cartProperty1 = itemView.findViewById(R.id.cartProperty1);
        cartProperty2 = itemView.findViewById(R.id.cartProperty2);
        deleteButton = itemView.findViewById(R.id.cart_item_delete);
        progressbar = itemView.findViewById(R.id.progressContainer);

        outOfStockMessage = MessageProvider.getInstance()
                .getText(MessageProvider.item_out_of_stock);


        updateTheme();
    }

    public void setView(CartItem item) {


        cartTitle.setText(item.title);
        loadImage(item.image);
        cartPrice.setText(String.format("Price:%s", StringUtils.formatAmount(item.price)));

        if (item.isOutOfStock()) {
            tvStockStatus.setText(outOfStockMessage);
            tvStockStatus.setVisibility(View.VISIBLE);
        } else {
            tvStockStatus.setVisibility(View.GONE);
        }

        cartProperty2.setVisibility(View.GONE);
        switch (item.method.toLowerCase()) {
            case CartConstants.TYPE_ARCHIE:
                setUpArchieCardView(item);
                break;
            case CartConstants.TYPE_EVENT:
                setUpEventView(item);
                break;
            case CartConstants.TYPE_GIFT:
                setUpGiftCardView(item);
                break;
            case CartConstants.TYPE_RENTAL:
                setUpRentalView(item);
                break;
            case CartConstants.TYPE_TRAINING:
                setUpTrainingView(item);
                break;
            case CartConstants.TYPE_MEMBERSHIP:
                setUpMembershipView(item);
                break;

            case CartConstants.TYPE_TRANSPONDER:
                setUpTransponderView(item);
                break;

            case CartConstants.TYPE_RACE_FEE:
                setUpRaceFeeView(item);
                break;
            case CartConstants.TYPE_PRODUCT:
                setUpProductView(item);
                break;
        }

        deleteButton.setVisibility(item.canRemove ?View.VISIBLE : View.INVISIBLE);

    }

    private void setUpProductView(CartItem item) {
        cartProperty1.setText(String.format("Quantity : %s", item.quantity));
        cartProperty1.setVisibility(View.VISIBLE);

        if(ListUtils.isNotEmpty(item.attributes)){
            StringBuilder attributeText = new StringBuilder();
            for(CartItem.Attribute attribute : item.attributes){
                attributeText = attributeText.append(StringUtils.toTitleCase(attribute.name))
                        .append(" : ").append(StringUtils.toTitleCase(attribute.value)).append(" | ");
            }
            cartProperty2.setText(attributeText.substring(0, attributeText.length() - 2));
            cartProperty2.setVisibility(View.VISIBLE);
        }else {
            cartProperty2.setVisibility(View.GONE);
        }
    }

    private void setUpRaceFeeView(CartItem item) {
        cartProperty1.setVisibility(View.GONE);
        cartProperty2.setVisibility(View.GONE);
    }

    private void setUpTransponderView(CartItem item) {

        cartProperty1.setText( item.parentTitle);
    }

    private void loadImage(String url) {
        GlideHelper.setImage(cartItemImage, url, progressbar);
    }

    private void setUpArchieCardView(CartItem item) {
        cartProperty1.setText(String.format("Quantity: %s", item.quantity));
    }

    private void setUpGiftCardView(CartItem item) {
        if (!ListUtils.isEmpty(item.attributes)) {
            CartItem.Attribute attribute = item.attributes.get(0);
            CartItem.Attribute attribute1 = item.attributes.get(1);
            String name;
            String email;
            if(attribute.isValueName()){
                name = attribute.value;
                email = attribute1.value;
            }else{
                name = attribute1.value;
                email = attribute.value;
            }
            cartProperty1.setText(String.format("Gift to: %s", name));
            cartProperty2.setText(email);

            if (item.isOutOfStock()) {
                cartProperty1.setVisibility(View.GONE);
                cartProperty2.setVisibility(View.GONE);
            } else {
                cartProperty1.setVisibility(View.VISIBLE);
                cartProperty2.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setUpEventView(CartItem item) {
        cartProperty1.setText(String.format("Hosted By : %s", item.eventType));
        if(!ListUtils.isEmpty(item.attributes)){
            CartItem.Attribute attribute = item.attributes.get(0);
            if(!StringUtils.isEmpty(attribute.value)){
                cartProperty2.setText(attribute.value);
                cartProperty2.setVisibility(View.VISIBLE);
            }else{
                cartProperty2.setVisibility(View.GONE);
            }
        }
    }

    private void setUpRentalView(CartItem item) {

        cartProperty1.setText(String.format("Event: %s", item.parentTitle));
        if (!ListUtils.isEmpty(item.attributes)) {
            CartItem.Attribute cartAttribute = item.attributes.get(0);
            cartProperty2.setText(String.format("%s:%s", cartAttribute.name, cartAttribute.value));
            cartProperty2.setVisibility(View.VISIBLE);
        } else {
            cartProperty2.setVisibility(View.GONE);

        }
    }


    private void setUpTrainingView(CartItem item) {
        cartProperty1.setText(String.format("Event: %s", item.parentTitle));
    }

    private void setUpMembershipView(CartItem item) {
        cartProperty1.setVisibility(View.GONE);
    }

    public void setEventListener(final CartItemClickListener listener, final CartItem item) {
        if (listener != null) {


            if (deleteButton != null)
                deleteButton.setOnClickListener(view -> listener.deleteItem(item));

            if (contentLayout != null)
                contentLayout.setOnClickListener(view -> listener.onItemClicked(item));

            if (cartItemImage != null)
                cartItemImage.setOnClickListener(view -> listener.onItemClicked(item));
        }
    }

    private void updateTheme(){
        boolean isEvTheme = ETApplication.getInstance().isEvApp();
        int colorCode = isEvTheme ? R.color.colorGreen : R.color.colorBlue;
        int color = UiUtils.getColorFromResource(contentLayout.getContext(), colorCode);
        cartPrice.setTextColor(color);

        deleteButton.setImageResource(isEvTheme ? R.drawable.deletenew : R.drawable.moto_delete);


    }
}
