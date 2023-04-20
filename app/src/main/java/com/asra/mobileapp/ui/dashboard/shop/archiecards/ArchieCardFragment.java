package com.asra.mobileapp.ui.dashboard.shop.archiecards;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentEtCardsBinding;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.ui.dashboard.shop.shopcards.CardListAdapter;
import com.asra.mobileapp.ui.dashboard.shop.shopcards.ShopCardFragment;

import androidx.recyclerview.widget.RecyclerView;

public class ArchieCardFragment extends ShopCardFragment<FragmentEtCardsBinding> {


    public static ArchieCardFragment newInstance(){
        return new ArchieCardFragment();
    }
    @Override
    protected RecyclerView getRecyclerView() {
        return dataBinding.etCardsRecyclerView;
    }

    @Override
    protected int getAdapterType() {
        return CardListAdapter.TYPE_ARCHIE_CARD;
    }

    @Override
    protected void showDetailsPage(ShopCard selectedCard) {
        loadFragment(ArchieCardsDetailsFragment.newInstance(selectedCard.slug, selectedCard.title));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_et_cards;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        viewModel.getArchieCards();
    }

    @Override
    public String getTitle() {
        return getString(R.string.shop_label_archie_cards);
    }
}
