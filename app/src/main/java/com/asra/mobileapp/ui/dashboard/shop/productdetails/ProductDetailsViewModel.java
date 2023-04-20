package com.asra.mobileapp.ui.dashboard.shop.productdetails;

import android.text.TextUtils;

import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.ui.dashboard.ShoppeViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.ShopUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailsViewModel extends ShoppeViewModel {


    public SingleLiveEvent<ProductDetail> productDetailObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> productDetailErrorObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Integer> quantityObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> totalPriceObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> itemPriceObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> cartEnableObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> stockAvailabilityObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> productAddedToCartObservable = new SingleLiveEvent<>();


    private int qty = 1;
    private ProductDetail productDetail = null;
    private double productPrice = 0;

    private ShopUseCase shopUseCase;


    @Inject
    ProductDetailsViewModel(ShopUseCase shopUseCase,
                            CartUseCase cartUseCase,
                            AppEngine appEngine,
                            ResourceFetcher resourceFetcher) {
        super(cartUseCase, appEngine, resourceFetcher);
        this.shopUseCase = shopUseCase;
    }

    public void getProductDetails(String slug){

        showProgressBar();
        Disposable disposable = shopUseCase.getProductDetails(slug)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onProductDetailsFetched,
                        this::onFetchingProductDetailsFailed);
        addDisposable(disposable);

    }

    private void onFetchingProductDetailsFailed(Throwable throwable) {
        hideProgressBar();
        productDetailErrorObservable.postValue(throwable.getMessage());
    }

    private void onProductDetailsFetched(ProductDetail productDetail) {
        hideProgressBar();
        this.productDetail = productDetail;

        if(!ListUtils.isEmpty(productDetail.variations)){
           productDetail.preSelectVariants();
        }

        productDetailObservable.postValue(productDetail);
        quantityObservable.postValue(qty);

        postItemPrice();
        productPrice = AppUtils.getDouble(productDetail.price);
        updateTotal();
        updateStockStatus(!productDetail.isOutOfStock());
    }

    private void postItemPrice() {
        if(ListUtils.isNotEmpty(productDetail.variations)){
            itemPriceObservable.postValue("Price:  " + StringUtils.formatAmount(getProductPrice()));
        }else {
            itemPriceObservable.postValue("Price:  " + StringUtils.formatAmount(getPriceRange()));
        }
    }


    private String getProductPrice(){
        try {
            return productDetail.getVariantPrice();
        }catch (NullPointerException e){
            return "0";
        }
    }

    private String getPriceRange(){
        double minPrice = TextUtils.isEmpty(productDetail.price) ? 0 :
                AppUtils.getDouble(productDetail.price);
        double maxPrice = TextUtils.isEmpty(productDetail.price) ? 0 :
                AppUtils.getDouble(productDetail.price);
        if(!ListUtils.isEmpty(productDetail.variations)){
            minPrice = minPrice == 0 ? AppUtils.getDouble((getProductPrice())) :
                    minPrice;
            for(ProductDetail.ItemStatus itemStatus: productDetail.itemStatusList){
                double price = TextUtils.isEmpty(itemStatus.price) ? 0 :
                        AppUtils.getDouble(itemStatus.price);

                minPrice = Math.min(price, minPrice);
                maxPrice = Math.max(price, maxPrice);
            }
        }

        if(minPrice == maxPrice){
            return StringUtils.formatAmount(minPrice);
        }else{
            return StringUtils.formatAmount(minPrice) + " - " + StringUtils.formatAmount(maxPrice);
        }
    }
    @Override
    protected void loadCartList(boolean ignoreCache) {

    }

    public void incrementQtyBy1() {
        qty += 1;
        quantityObservable.postValue(qty);
        updateTotal();

    }

    public void decrementQtyBy1() {
        if(qty > 0){
            --qty;
        }
        quantityObservable.postValue(qty);
        updateTotal();

        if(ListUtils.isEmpty(productDetail.variations)){
            cartEnableObservable.postValue(qty > 0);
        }else{
            cartEnableObservable.postValue(qty > 0 && productDetail.isVariantAvailable());
        }

    }

    private void updateTotal() {
        if(productDetail != null) {
            double total = productPrice * qty;
            String formatted = "Total: " + StringUtils.formatAmount(total);

           totalPriceObservable.postValue(formatted);
        }
    }



    private void updateStockStatus(boolean available) {
        stockAvailabilityObservable.postValue(available);
    }


    public void onAttributeChanged(ProductDetail.Variation variation, ProductDetail.VariantItem variantItem) {
        variation.selectedVariantItem = variantItem;
        productPrice = AppUtils.getDouble(getProductPrice());
        updateTotal();
        postItemPrice();
        updateStockStatus(productDetail.isVariantAvailable());
    }

    public void addToCart() {
        productDetail.quantity = qty;
        showProgressBar();
        Disposable disposable = cartUseCase.addProductToCart(productDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onProductAdded,
                        this::onAddingProductToCartFailed);
        addDisposable(disposable);
    }

    private void onAddingProductToCartFailed(Throwable throwable) {
        hideProgressBar();
        showErrorMessage(throwable.getMessage());
    }

    private void onProductAdded() {
        hideProgressBar();
        productAddedToCartObservable.postValue(getConfigString(
                MessageProvider.msg_cart_product_added));
    }
}
