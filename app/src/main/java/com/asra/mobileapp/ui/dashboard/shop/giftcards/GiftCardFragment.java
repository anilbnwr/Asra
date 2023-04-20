package com.asra.mobileapp.ui.dashboard.shop.giftcards;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentEtCardsBinding;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.ui.dashboard.shop.shopcards.CardListAdapter;
import com.asra.mobileapp.ui.dashboard.shop.shopcards.ShopCardFragment;

import androidx.recyclerview.widget.RecyclerView;

public class GiftCardFragment extends ShopCardFragment<FragmentEtCardsBinding> {


    public static GiftCardFragment newInstance() {
        return new GiftCardFragment();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return dataBinding.etCardsRecyclerView;
    }

    @Override
    protected int getAdapterType() {
        return CardListAdapter.TYPE_GIFT_CARD;
    }

    @Override
    protected void showDetailsPage(ShopCard selectedCard) {
        loadFragment(GiftCardsDetailsFragment.newInstance(selectedCard.slug, selectedCard.title));
    }

    @Override
    public String getTitle() {
        return getString(R.string.shop_label_gift);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_et_cards;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        viewModel.getGiftCards();
    }
}