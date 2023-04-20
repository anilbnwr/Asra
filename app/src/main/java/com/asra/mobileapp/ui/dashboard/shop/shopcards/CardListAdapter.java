package com.asra.mobileapp.ui.dashboard.shop.shopcards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.ShopCard;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardListAdapter extends RecyclerView.Adapter<CardsViewHolder>{


    private List<ShopCard> mDataset;
    private CardItemClickListener cardItemClickListener;

    public static final int TYPE_ARCHIE_CARD = 1;
    public static final int TYPE_GIFT_CARD = 2;

    int type;

    private boolean showAsList = false;

    public CardListAdapter(int type, CardItemClickListener cardItemClickListener) {
        if(mDataset == null){
            mDataset = new ArrayList<>();
        }
        this.cardItemClickListener = cardItemClickListener;
        this.type = type;
    }

    public void updateDateSet(List<ShopCard> newList){
        if(newList != null){
            mDataset.clear();
            mDataset.addAll(newList);
            notifyDataSetChanged();
        }
    }

    public void addDateSet(List<ShopCard> newList){
        if(newList != null){
            mDataset.addAll(0,newList);
            notifyDataSetChanged();
        }
    }

    public List<ShopCard> getDataset(){
        return mDataset;
    }

    @NonNull
    @Override
    public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        int layoutId = showAsList ? R.layout.list_item_et_card : R.layout.list_item_et_card_grid;
        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(layoutId, viewGroup, false);

        CardsViewHolder vh = new CardsViewHolder(v, type);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull CardsViewHolder eventsViewHolder, int i) {

        eventsViewHolder.setView(mDataset.get(i));

        eventsViewHolder.setEventListener(cardItemClickListener, mDataset.get(i));
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