package com.asra.mobileapp.ui.dashboard.shop.giftcards;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.databinding.FragmentGiftCardsDetailsBinding;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;

import timber.log.Timber;

public class GiftCardsDetailsFragment extends
        ShoppeFragment<GiftCardsDetailViewModel, FragmentGiftCardsDetailsBinding> {

    private static final String KEY_GIFT_SLUG = "com.evolvegt.mobileapp.giftslug";
    private static final String KEY_GIFT_NAME = "com.evolvegt.mobileapp.gifttitle";

    private static final String KEY_GIFT_CART_ID = "com.evolvegt.mobileapp.gifttitle.cartId";
    private static final String KEY_GIFT_RECEIVER_NAME = "com.evolvegt.mobileapp.receivername";
    private static final String KEY_GIFT_RECEIVER_EMAIL = "com.evolvegt.mobileapp.receiveremail";


    private String title;
    private String receiverName;
    private String receiverEmail;

    public static GiftCardsDetailsFragment newInstance(String slug, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_GIFT_SLUG, slug);
        bundle.putString(KEY_GIFT_NAME, title);

        GiftCardsDetailsFragment fragment = new GiftCardsDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public static GiftCardsDetailsFragment newInstance(CartItem cart) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_GIFT_SLUG, cart.slug);
        bundle.putString(KEY_GIFT_NAME, cart.title);

        bundle.putString(KEY_GIFT_CART_ID, cart.title);
        bundle.putString(KEY_GIFT_RECEIVER_NAME, cart.attributes.get(0).value);
        bundle.putString(KEY_GIFT_RECEIVER_EMAIL, cart.attributes.get(1).value);

        GiftCardsDetailsFragment fragment = new GiftCardsDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }

    @Override
    protected Class<GiftCardsDetailViewModel> getViewModel() {
        return GiftCardsDetailViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_gift_cards_details;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();

        viewModel.cardDetailsObservable.observe(this, this::setUpViews);
        viewModel.cardDetailsErrorObservable.observe(this,
                error -> showEmptyMessage(true, error));

        viewModel.priceObservable.observe(this,
                formattedPrice ->
                        dataBinding.giftCardsPrice.setText(formattedPrice));
        viewModel.validationObservable.observe(this, error -> {
            dataBinding.giftCardErrorView.setText(error);
            dataBinding.giftCardErrorView.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        showBackButton();

        Bundle bundle = getArguments();
        String slug = bundle.getString(KEY_GIFT_SLUG, "");

        this.title = bundle.getString(KEY_GIFT_NAME, "Gift Card Details");

        updateTitle();

        receiverEmail = getArguments().getString(KEY_GIFT_RECEIVER_EMAIL, "");
        receiverName = getArguments().getString(KEY_GIFT_RECEIVER_NAME, "");


        viewModel.recreateShopCard(slug);


        dataBinding.giftBtnAddToCart.setOnClickListener(view -> {
                    String receiverName = dataBinding.giftCardReceiver.getEditText().getText().toString();
                    String receiverEmail = dataBinding.giftCardReceiverEmail.getEditText().getText().toString();

                    viewModel.onAddToCart(receiverName, receiverEmail);
                }
        );
    }

    @Override
    public String getTitle() {
        return title;
    }

    private void setUpViews(ShopCard card) {
        if (card != null) {
            loadImage(card.imagePath);
            dataBinding.giftCardsItemContent.setText(card.title);

            if (!TextUtils.isEmpty(receiverName)) {
                dataBinding.giftCardReceiver.getEditText().setText(receiverName);
            }

            if (!TextUtils.isEmpty(receiverEmail)) {
                dataBinding.giftCardReceiverEmail.getEditText().setText(receiverEmail);
            }

            analyticsHelper.logViewItemDetailEvent(AnalyticsModel.VIEW_ITEM_ARCHIE_DETAILS);

        }
    }

    private void loadImage(String url) {
        Timber.d("Image URL: %s", url);
        GlideHelper.setImage(this, dataBinding.giftCardImage,
                url, R.drawable.et_fallback_image, dataBinding.giftCardProgress);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.giftBtnAddToCart.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.giftCardsPrice.setTextColor(getColor(R.color.colorPrimary));
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.giftBtnAddToCart.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.giftCardsPrice.setTextColor(getColor(R.color.moto_primary));
    }
}
