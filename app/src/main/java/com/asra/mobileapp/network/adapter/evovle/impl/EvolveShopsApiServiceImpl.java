package com.asra.mobileapp.network.adapter.evovle.impl;

import com.asra.mobileapp.model.CategoryHeader;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.model.Product;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.network.adapter.BaseApiService;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveShopsApiServices;
import com.asra.mobileapp.network.api.EvolveShopApis;
import com.asra.mobileapp.network.retrofit.request.EventDetailRequest;
import com.asra.mobileapp.network.retrofit.request.ProductDetailRequest;
import com.asra.mobileapp.network.retrofit.request.ProductListRequest;
import com.asra.mobileapp.network.retrofit.request.ShopCardDetailRequest;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import timber.log.Timber;

public class EvolveShopsApiServiceImpl extends BaseApiService
        implements EvolveShopsApiServices {

    private EvolveShopApis apiServices;

    @Inject
    public EvolveShopsApiServiceImpl(EvolveShopApis apiServices){

        this.apiServices = apiServices;
    }


    @Override
    public Single<List<Event>> getEvolveEvents() {
        return apiServices.getEvolveEventList()
                .map(response -> {
                    checkApiError(response);
                    return response.eventList;
                }).doOnError(Timber::w);

    }

    @Override
    public Single<EventDetails> getEvolveEventDetail(String eventSlug, String userId) {
        EventDetailRequest request = new EventDetailRequest();
        request.slug = eventSlug;
        return apiServices.getEvolveEventDetail(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<List<ShopCard>> getArchieCards() {
        return apiServices.getArchieCards()
                .map(response -> {
                    checkApiError(response);
                    return response.cardList;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<List<ShopCard>> getGiftCards() {
        return apiServices.getGiftCards()
                .map(response -> {
                    checkApiError(response);
                    return response.cardList;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<ShopCard> getGiftCardDetails(String slug) {
        ShopCardDetailRequest request = new ShopCardDetailRequest();
        request.slug = slug;
        return apiServices.getGiftCardDetails(request)
                .map(response -> {
                    checkApiError(response);
                    return response.shopCard;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<ShopCard> getArchieCardDetails(String slug) {
        ShopCardDetailRequest request = new ShopCardDetailRequest();
        request.slug = slug;
        return apiServices.getArchieCardDetails(request)
                .map(response -> {
                    checkApiError(response);
                    return response.shopCard;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<List<CategoryHeader>> getCategoryList() {
        return apiServices.getProductCategoryList()
                .map(response -> {
                    checkApiError(response);
                    return response.categoryList;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<List<Product>> getProductList(String categoryHeader, String categoryId) {
        ProductListRequest request = new ProductListRequest();
        request.categoryList = categoryId;
        request.category = categoryHeader;

        return apiServices.getProductList(request)
                .map(response -> {
                    checkApiError(response);
                    return response.productList;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<ProductDetail> getProductDetails(String slug) {

        ProductDetailRequest request = new ProductDetailRequest();
        request.slug = slug;

        return apiServices.getProductDetail(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }


    @Override
    public Single<List<Event>> getMotoEvents() {
        return apiServices.getMotoEventList()
                .map(response -> {
                    checkApiError(response);
                    return response.eventList;
                }).doOnError(Timber::w);

    }

    @Override
    public Single<EventDetails> getMotoEventDetail(String eventSlug,String userId) {

        EventDetailRequest request = new EventDetailRequest();
        request.slug = eventSlug;
        request.userId = userId;
        return apiServices.getMotoEventDetail(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }
}
