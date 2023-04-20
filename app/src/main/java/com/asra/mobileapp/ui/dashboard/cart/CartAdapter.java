package com.asra.mobileapp.ui.dashboard.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.CartItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {


    public static final int VIEW_TYPE_RENTAL = 1;
    public static final int VIEW_TYPE_EVENT = 2;
    public static final int VIEW_TYPE_TRAINING = 3;
    public static final int VIEW_TYPE_ARCHIE = 4;
    public static final int VIEW_TYPE_GIFT = 5;
    public static final int VIEW_TYPE_MEMBERSHIP = 6;


    private List<CartItem> mDataset;
    private CartItemClickListener cartItemClickListener;

    public CartAdapter(CartItemClickListener cartItemClickListener) {
        if (mDataset == null) {
            mDataset = new ArrayList<>();
        }
        this.cartItemClickListener = cartItemClickListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int cartType) {


        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_cart, viewGroup, false);

        return new CartViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

        cartViewHolder.setView(mDataset.get(i));

        cartViewHolder.setEventListener(cartItemClickListener, mDataset.get(i));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateDateSet(List<CartItem> newList) {
        if (newList != null) {
            mDataset.clear();
            mDataset.addAll(newList);
            notifyDataSetChanged();
        }
    }

    public void addDateSet(List<CartItem> newList) {
        if (newList != null) {
            mDataset.addAll(0, newList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {

        CartItem cart = mDataset.get(position);
        int viewType = VIEW_TYPE_EVENT;
        switch (cart.method.toLowerCase()) {
            case CartConstants.TYPE_ARCHIE:
                viewType = VIEW_TYPE_ARCHIE;
                break;
            case CartConstants.TYPE_EVENT:
                viewType = VIEW_TYPE_EVENT;
                break;
            case CartConstants.TYPE_GIFT:
                viewType = VIEW_TYPE_GIFT;
                break;
            case CartConstants.TYPE_RENTAL:
                viewType = VIEW_TYPE_RENTAL;
                break;
            case CartConstants.TYPE_TRAINING:
                viewType = VIEW_TYPE_TRAINING;
                break;
            case CartConstants.TYPE_MEMBERSHIP:
                viewType = VIEW_TYPE_MEMBERSHIP;
                break;
        }
        return viewType;
    }
}