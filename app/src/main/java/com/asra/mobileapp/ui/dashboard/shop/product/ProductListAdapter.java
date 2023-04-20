package com.asra.mobileapp.ui.dashboard.shop.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductViewHolder>{


    private List<Product> mDataset;
    private ProductClickListener ProductClickListener;



    private boolean showAsList = false;

    public ProductListAdapter(ProductClickListener ProductClickListener) {
        if(mDataset == null){
            mDataset = new ArrayList<>();
        }
        this.ProductClickListener = ProductClickListener;
    }

    public void updateDateSet(List<Product> newList){
        if(newList != null){
            mDataset.clear();
            mDataset.addAll(newList);
            notifyDataSetChanged();
        }
    }

    public void addDateSet(List<Product> newList){
        if(newList != null){
            mDataset.addAll(0,newList);
            notifyDataSetChanged();
        }
    }

    public List<Product> getDataset(){
        return mDataset;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        int layoutId = showAsList ? R.layout.list_item_et_card : R.layout.list_item_et_card_grid;
        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_product_grid, viewGroup, false);

        ProductViewHolder vh = new ProductViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder eventsViewHolder, int i) {

        eventsViewHolder.setView(mDataset.get(i));

        eventsViewHolder.setEventListener(ProductClickListener, mDataset.get(i));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void displayAsList(){
        showAsList = true;
    }

    public void displayAsGrid(){
        showAsList = false;
    }
}
