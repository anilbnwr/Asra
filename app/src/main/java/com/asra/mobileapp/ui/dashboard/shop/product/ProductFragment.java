package com.asra.mobileapp.ui.dashboard.shop.product;

import android.os.Bundle;
import android.view.MenuItem;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentEtProductBinding;
import com.asra.mobileapp.model.Product;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.ui.dashboard.shop.productdetails.ProductDetailFragment;

public class ProductFragment extends ShoppeFragment<ProductViewModel, FragmentEtProductBinding>
        implements ProductClickListener {

    private static final String KEY_CATEGORY_ID = "com.evolvegt.mobileapp.cat.id";
    private static final String KEY_CATEGORY_TYPE = "com.evolvegt.mobileapp.cat.type";

    private ProductListAdapter productListAdapter;
    public static ProductFragment newInstance(String slug, String type){
        ProductFragment fragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY_ID, slug);
        bundle.putString(KEY_CATEGORY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }


    @Override
    protected Class<ProductViewModel> getViewModel() {
        return ProductViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_et_product;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();

        viewModel.productsObservable.observe(this, products ->
                productListAdapter.updateDateSet(products));
        viewModel.productListErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        dataBinding.etProductRecyclerView.setLayoutManager(getGridLayoutManager());
        dataBinding.etProductRecyclerView.setHasFixedSize(true);

        String categoryId = getArguments().getString(KEY_CATEGORY_ID);
        String categoryHeader = getArguments().getString(KEY_CATEGORY_TYPE);

        productListAdapter = new ProductListAdapter(this);
        dataBinding.etProductRecyclerView.setAdapter(productListAdapter);
        productListAdapter.displayAsGrid();
        dataBinding.etProductRecyclerView.addItemDecoration(itemGridDecoration);
        viewModel.getProductList(categoryHeader, categoryId);
    }

    @Override
    public void onViewDetails(Product item) {
        loadFragment(ProductDetailFragment.newInstance(item.title, item.slug));
    }
}
