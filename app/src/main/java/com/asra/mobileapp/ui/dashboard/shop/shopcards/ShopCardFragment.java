package com.asra.mobileapp.ui.dashboard.shop.shopcards;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;

import java.util.List;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public  abstract class  ShopCardFragment<D extends ViewDataBinding> extends ShoppeFragment<ShopCardViewModel, D> {

    private RecyclerView recyclerView;
    private CardListAdapter cardAdapter;
    private MenuItem cartMenuItem;
    private MenuItem gridMenuItem;
    private MenuItem listMenuItem;

    @Override
    public MenuItem getCartToolbarMenu() {
        return cartMenuItem;
    }

    @Override
    public boolean isToolbarMenuEnabled() {
        return true;
    }

    @Override
    protected Class<ShopCardViewModel> getViewModel() {
        return ShopCardViewModel.class;
    }



    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();

        viewModel.archieCardsErrorObservale.observe(this, error ->{
                showEmptyMessage(true, error);
                getRecyclerView().setVisibility(View.GONE);
    });

        viewModel.giftCardsErrorObservale.observe(this, error -> {
            showEmptyMessage(true, error);
            getRecyclerView().setVisibility(View.GONE);
        });

        viewModel.archieCardsObservale.observe(this, this::updateAdapter);
        viewModel.giftCardsObservale.observe(this, this::updateAdapter);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        showBackButton();
        getRecyclerView().setLayoutManager(getLinearLayoutManager());

        getRecyclerView().setHasFixedSize(true);

        CardItemClickListener cardClickListener = this::showDetailsPage;
        cardAdapter = new CardListAdapter(getAdapterType(), cardClickListener);
        cardAdapter.displayAsList();
        getRecyclerView().setAdapter(cardAdapter);
        getRecyclerView().addItemDecoration(itemDecoration);


    }



    protected void updateAdapter(List<ShopCard> shopCards) {
        getRecyclerView().setVisibility(View.VISIBLE);
        showEmptyMessage(false, null);
        cardAdapter.updateDateSet(shopCards);

        if(getAdapterType() == CardListAdapter.TYPE_ARCHIE_CARD){
            analyticsHelper.logViewListEvent(AnalyticsModel.CATEGORY_ARCHIE_CARDS);
        }else{
            analyticsHelper.logViewListEvent(AnalyticsModel.CATEGORY_GIFT_CARDS);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_cards, menu);
        cartMenuItem = menu.findItem(R.id.menu_toolbar_cart);
        gridMenuItem = menu.findItem(R.id.menu_toolbar_as_grid);
        listMenuItem = menu.findItem(R.id.menu_toolbar_as_list);
        super.onCreateOptionsMenu(menu, inflater);
        viewModel.updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_cart:
                switchToCart();
                return true;
            case R.id.menu_toolbar_as_grid:
                RecyclerView cardListView = getRecyclerView();
                cardListView.setLayoutManager(getGridLayoutManager());
                cardListView.removeItemDecoration(itemDecoration);
                cardListView.addItemDecoration(itemGridDecoration);
                cardAdapter.displayAsGrid();
                cardListView.setAdapter(cardAdapter);
                gridMenuItem.setVisible(false);
                listMenuItem.setVisible(true);

                return true;
            case R.id.menu_toolbar_as_list:
                cardListView = getRecyclerView();
                cardListView.setLayoutManager(getLinearLayoutManager());
                cardListView.removeItemDecoration(itemGridDecoration);
                cardListView.addItemDecoration(itemDecoration);
                cardAdapter.displayAsList();
                cardListView.setAdapter(cardAdapter);
                gridMenuItem.setVisible(true);
                listMenuItem.setVisible(false);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    protected abstract RecyclerView getRecyclerView();

    protected abstract int getAdapterType();

    protected abstract void showDetailsPage(ShopCard selectedCard);
}
