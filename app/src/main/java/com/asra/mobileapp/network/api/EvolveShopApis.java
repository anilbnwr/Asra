package com.asra.mobileapp.network.api;


import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.network.retrofit.request.EventDetailRequest;
import com.asra.mobileapp.network.retrofit.request.ProductDetailRequest;
import com.asra.mobileapp.network.retrofit.request.ProductListRequest;
import com.asra.mobileapp.network.retrofit.request.ShopCardDetailRequest;
import com.asra.mobileapp.network.retrofit.response.CategoryListResponse;
import com.asra.mobileapp.network.retrofit.response.EventsResponse;
import com.asra.mobileapp.network.retrofit.response.ProductListResponse;
import com.asra.mobileapp.network.retrofit.response.ShopCardItemRespose;
import com.asra.mobileapp.network.retrofit.response.ShopCardListResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EvolveShopApis  {
    //Events
    @POST("/evolve-api/public/app/v3/evolveEvents/list")
    Single<EventsResponse> getEvolveEventList();

    @POST("/evolve-api/public/app/v3/evolveEvents/details")
    Single<EventDetails> getEvolveEventDetail(@Body EventDetailRequest request);


    @POST("/evolve-api/public/app/v3/motoEvents/list")
    Single<EventsResponse> getMotoEventList();

    @POST("/evolve-api/public/app/v3/motoEvents/details")
    Single<EventDetails> getMotoEventDetail(@Body EventDetailRequest request);


    //
    @GET("/evolve-api/public/app/v3/archieCard/list")
    Single<ShopCardListResponse> getArchieCards();

    @POST("/evolve-api/public/app/v3/giftCard/details")
    Single<ShopCardItemRespose> getGiftCardDetails(@Body ShopCardDetailRequest request);

    @POST("/evolve-api/public/app/v3/archieCard/details")
    Single<ShopCardItemRespose> getArchieCardDetails(@Body ShopCardDetailRequest request);

    @GET("/evolve-api/public/app/v3/giftCard/list")
    Single<ShopCardListResponse> getGiftCards();

    //Gear
    @POST("/evolve-api/public/app/v3/products/categoryList")
    Single<CategoryListResponse> getProductCategoryList();


    //Product
    @POST("/evolve-api/public/app/v3/products/list")
    Single<ProductListResponse> getProductList(@Body ProductListRequest request);

    @POST("/evolve-api/public/app/v3/products/details")
    Single<ProductDetail> getProductDetail(@Body ProductDetailRequest request);
}
