package com.asra.mobileapp.ui.dashboard.cart;


import com.asra.mobileapp.model.CartItem;

public interface CartItemClickListener {

    void onItemClicked(CartItem item);
    void addQuantity(CartItem item);
    void removeQuantity(CartItem item);

    void deleteItem(CartItem item);

}
