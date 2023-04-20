package com.asra.mobileapp.ui.dashboard.shop.productdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.databinding.FragmentProductDetailsBinding;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.util.ListUtils;

public class ProductDetailFragment extends ShoppeFragment<ProductDetailsViewModel, FragmentProductDetailsBinding> {

    private static final String TXT_SELECT_OPTION = "Select an option";

    private static final String KEY_TITLE = "com.evolvegt.mobileapp.title";
    private static final String KEY_SLUG = "com.evolvegt.mobileapp.slug";


    private String title;
    public static ProductDetailFragment newInstance(String title, String slug){
        ProductDetailFragment fragment = new ProductDetailFragment();

        Bundle argument = new Bundle();
        argument.putString(KEY_TITLE, title);
        argument.putString(KEY_SLUG, slug);

        fragment.setArguments(argument);
        return fragment;
    }


    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }

    @Override
    protected Class<ProductDetailsViewModel> getViewModel() {
        return ProductDetailsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_product_details;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();
        viewModel.productDetailObservable.observe(this, this::setUpViews);
        viewModel.quantityObservable.observe(this, qty ->
                dataBinding.tvQuantity.setText(String.valueOf(qty)));

        viewModel.totalPriceObservable.observe(this, price ->
                dataBinding.totalPrice.setText(price));
        viewModel.productDetailErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));

        viewModel.itemPriceObservable.observe(this, priceRange ->
                dataBinding.productPrice.setText(priceRange));
        viewModel.stockAvailabilityObservable.observe(this, this::updateStockStatus);
        viewModel.productAddedToCartObservable.observe(this, this::showSuccessMessage);

        viewModel.cartEnableObservable.observe(this, enable -> dataBinding.productBtnAddToCart.setEnabled(enable));
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        Bundle arguments = getArguments();
        title = arguments.getString(KEY_TITLE);
        String slug = arguments.getString(KEY_SLUG);
        viewModel.getProductDetails(slug);

        updateTitle();

        dataBinding.btnPlus.setOnClickListener(view -> {
            viewModel.incrementQtyBy1();
        });

        dataBinding.btnMinus.setOnClickListener(view -> {
            viewModel.decrementQtyBy1();
        });

        dataBinding.productBtnAddToCart.setOnClickListener(view -> viewModel.addToCart());
    }

    private void updateStockStatus(boolean available){
        dataBinding.productStockStatus.setVisibility(available ? View.GONE : View.VISIBLE);
        dataBinding.productBtnAddToCart.setEnabled(available);
    }

    private void setUpViews(ProductDetail detail){


        String url = UiUtils.getETAbsoluteURL(detail.image);
        GlideHelper.setImage(this, dataBinding.productDetailImage, url, R.drawable.et_fallback_image, dataBinding.progressContainer);


        dataBinding.productTitle.setText(detail.title);

        setUpVariations(detail);

        analyticsHelper.logViewItemDetailEvent(AnalyticsModel.VIEW_ITEM_PRODUCT_DETAILS);
    }


    public void setUpVariations(ProductDetail detail) {

        dataBinding.variationsContainer.removeAllViews();
        if(ListUtils.isNotEmpty(detail.variations)){
            for(ProductDetail.Variation variation : detail.variations){
                View view = createVariationSpinner(variation);
                if(view != null){
                    dataBinding.variationsContainer.addView(view);
                }
            }
        }
    }

    private View createVariationSpinner(ProductDetail.Variation variation) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.product_variations, null, false);

        TextView title = view.findViewById(R.id.variation_title);
        title.setText(String.format("Select %s", variation.variantName));

        Spinner spinner = view.findViewById(R.id.variationSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item,
                        variation.getValuesList());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.onAttributeChanged(variation, variation.variantItems.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }


    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.productBtnAddToCart.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.btnPlus.setImageResource(R.drawable.selector_button_plus_moto);
        dataBinding.btnMinus.setImageResource(R.drawable.selector_button_minus_moto);
        dataBinding.productPrice.setTextColor(getColor(R.color.moto_primary));
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.productBtnAddToCart.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.btnPlus.setImageResource(R.drawable.selector_button_plus_moto);
        dataBinding.btnMinus.setImageResource(R.drawable.selector_button_minus_moto);
        dataBinding.productPrice.setTextColor(getColor(R.color.colorPrimary));
    }
}
