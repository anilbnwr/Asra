package com.asra.mobileapp.usecase;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.Category;
import com.asra.mobileapp.model.CategoryHeader;
import com.asra.mobileapp.model.Product;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.network.adapter.apiservices.ShopsApiServices;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveShopsApiServices;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ShopUseCase extends BaseUseCase {

    private EvolveShopsApiServices evolveShopsApiServices;

    private List<CategoryHeader> categoryHeaders;
    private List<ShopCard> giftCards;
    private List<ShopCard> archieCards;
    private ResourceFetcher resourceFetcher;


    @Inject
    public ShopUseCase(EvolveShopsApiServices evolveShopsApiServices,
                       AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine);
        this.evolveShopsApiServices = evolveShopsApiServices;
        this.resourceFetcher = resourceFetcher;
    }

    private ShopsApiServices getAPiServices(){
        return evolveShopsApiServices;
    }

    public Single<List<ShopCard>> getArchieCard() {
        if (ListUtils.isEmpty(archieCards)) {
            return getAPiServices().getArchieCards()
                    .doOnSuccess(shopCards -> this.archieCards = shopCards);
        } else {
            return Single.just(archieCards);
        }
    }

    public Single<ShopCard> getArchieCardDetails(String slug) {
        return getAPiServices().getArchieCardDetails(slug);
    }

    public Single<ShopCard> getGiftCardDetails(String slug) {
        return getAPiServices().getGiftCardDetails(slug);
    }

    private Single<List<CategoryHeader>> getCategoryHeadersList() {
        if(ListUtils.isEmpty(categoryHeaders)) {
            return getAPiServices().getCategoryList()
                    .doOnSuccess(categoryHeaderList -> this.categoryHeaders = categoryHeaderList);
        }else{
            return Single.just(categoryHeaders);
        }
    }

    public Single<List<Category>> getGearCategoryList() {
        return getCategoryHeadersList().map(categoryHeaderList -> {
            for (CategoryHeader categoryHeader : categoryHeaderList) {
                if (categoryHeader.isGear()) {
                    return categoryHeader.categoryItemList;
                }
            }
            throw new ETApiException(
                    resourceFetcher.getConfigString(MessageProvider.error_generic_message));
        });

    }

    public Single<List<Category>> getRentalCategoryList() {
        return getCategoryHeadersList().map(categoryHeaderList -> {
            for (CategoryHeader categoryHeader : categoryHeaderList) {
                if (categoryHeader.isRentals()) {
                    return categoryHeader.categoryItemList;
                }
            }
            throw new ETApiException(
                    resourceFetcher.getConfigString(MessageProvider.error_generic_message));
        });

    }

    public Single<List<ShopCard>> getGiftCards() {
        if (ListUtils.isEmpty(giftCards)) {
            return getAPiServices().getGiftCards()
                    .doOnSuccess(shopCards -> this.giftCards = shopCards);
        } else {
            return Single.just(giftCards);
        }
    }

    public Single<List<Product>> getGearProducts(String category) {
            return evolveShopsApiServices
                    .getProductList(CategoryHeader.TITLE_GEAR, category);

    }

    public Single<List<Product>> getRentalProducts(String category) {
        return evolveShopsApiServices
                .getProductList(CategoryHeader.TITLE_RENTAL, category);

    }

    public Single<ProductDetail> getProductDetails(String slug) {
        return evolveShopsApiServices
                .getProductDetails(slug);

    }

}
