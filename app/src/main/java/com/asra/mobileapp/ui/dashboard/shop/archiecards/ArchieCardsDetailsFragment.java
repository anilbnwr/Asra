package com.asra.mobileapp.ui.dashboard.shop.archiecards;

import android.os.Bundle;
import android.view.MenuItem;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.databinding.FragmentArchieCardsDetailsBinding;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;

import timber.log.Timber;

public class ArchieCardsDetailsFragment extends
        ShoppeFragment<ArchieCardsDetailViewModel, FragmentArchieCardsDetailsBinding> {

    private static final String KEY_ARCHIE_SLUG = "com.evolvegt.mobileapp.giftslug";
    private static final String KEY_ARCHIE_NAME = "com.evolvegt.mobileapp.gifttitle";

    private static final String KEY_ARCHIE_CART_ID = "com.evolvegt.mobileapp.gifttitle.cartId";
    private static final String KEY_ARCHIE_QUANTITY = "com.evolvegt.mobileapp.quantity";


    private String title;
    public static ArchieCardsDetailsFragment newInstance(String slug, String title){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARCHIE_SLUG, slug);
        bundle.putString(KEY_ARCHIE_NAME, title);

        ArchieCardsDetailsFragment fragment = new ArchieCardsDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }



    public static ArchieCardsDetailsFragment newInstance(CartItem cartItem){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARCHIE_SLUG, cartItem.slug);
        bundle.putString(KEY_ARCHIE_NAME, cartItem.title);

        bundle.putString(KEY_ARCHIE_CART_ID, cartItem.cartId);
        bundle.putString(KEY_ARCHIE_QUANTITY, cartItem.quantity);

        ArchieCardsDetailsFragment fragment = new ArchieCardsDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }

    @Override
    protected Class<ArchieCardsDetailViewModel> getViewModel() {
        return ArchieCardsDetailViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_archie_cards_details;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();

        viewModel.cardDetailsObservable.observe(this, this::setUpViews);
        viewModel.cardDetailsErrorObservable.observe(this,
                error -> showEmptyMessage(true, error));
        viewModel.quantityObservable.observe(this,
                qty -> {
                    dataBinding.archieTvQuantity.setText(String.valueOf(qty));
                    dataBinding.archieBtnAddToCart.setEnabled(qty > 0);
                });
        viewModel.priceObservable.observe(this,
                formattedPrice ->
                        dataBinding.archieCardsPrice.setText(formattedPrice));

        viewModel.totalObservable.observe(this,
                formattedPrice ->
                        dataBinding.archieCardsTotal.setText(formattedPrice));
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        showBackButton();

        Bundle bundle = getArguments();
        String slug = bundle.getString(KEY_ARCHIE_SLUG, "");
        this.title = bundle.getString(KEY_ARCHIE_NAME, "Gift Card Details");
        updateTitle();

        int qty = Integer.valueOf(bundle.getString(KEY_ARCHIE_QUANTITY, "1"));
        dataBinding.archieTvQuantity.setText(String.valueOf(qty));

        viewModel.recreateShopCard(slug, qty);

        dataBinding.archieBtnPlus.setOnClickListener(view -> viewModel.incrementQtyBy1());

        dataBinding.archieBtnMinus.setOnClickListener(view -> viewModel.decrementQtyBy1());

        dataBinding.archieBtnAddToCart.setEnabled(false);
        dataBinding.archieBtnAddToCart.setOnClickListener(view -> viewModel.onAddToCart());
    }

    @Override
    public String getTitle() {
        return title;
    }

    private void setUpViews(ShopCard card){
        if(card != null){
            loadImage(card.imagePath);
            dataBinding.archieCardsPrice.setText(getString(R.string.txt_price_label_with_currency,
                    card.price));
            dataBinding.archieCardsItemContent.setText(card.content);

            updateTotal();

            analyticsHelper.logViewItemDetailEvent(AnalyticsModel.VIEW_ITEM_ARCHIE_DETAILS);

        }


    }

    private void updateTotal() {

    }
    private void loadImage(String url){
        Timber.d("Image URL: %s", url);
        GlideHelper.setImage(this, dataBinding.archieCardImage,
                url, R.drawable.et_fallback_image, null);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.archieBtnAddToCart.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.archieBtnPlus.setImageResource(R.drawable.selector_button_plus_moto);
        dataBinding.archieBtnMinus.setImageResource(R.drawable.selector_button_minus_moto);
        dataBinding.archieCardsPrice
                .setTextColor(getPrimaryColor());
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.archieBtnAddToCart.setBackgroundResource(
                R.drawable.selector_button_moto_primary);
        dataBinding.archieBtnPlus.setImageResource(R.drawable.selector_button_plus_moto);
        dataBinding.archieBtnMinus.setImageResource(R.drawable.selector_button_minus_moto);
        dataBinding.archieCardsPrice
                .setTextColor(getPrimaryColor());
    }
}
