package com.asra.mobileapp.network.adapter.apiservices;

import com.asra.mobileapp.model.CategoryHeader;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.model.Product;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.model.ShopCard;

import java.util.List;

import io.reactivex.Single;

public interface ShopsApiServices {

    Single<List<Event>> getEvolveEvents();
    Single<EventDetails> getEvolveEventDetail(String eventSlug, String userId);

    Single<List<Event>> getMotoEvents();
    Single<EventDetails> getMotoEventDetail(String eventId, String userId);


    Single<List<ShopCard>> getArchieCards();
    Single<List<ShopCard>> getGiftCards();

    Single<ShopCard> getGiftCardDetails(String slug);
    Single<ShopCard> getArchieCardDetails(String slug);

    Single<List<CategoryHeader>> getCategoryList();
    Single<List<Product>> getProductList(String categoryHeader, String categoryId);

    Single<ProductDetail> getProductDetails(String slug);


}
