package com.asra.mobileapp.model;

import androidx.annotation.NonNull;

import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.util.ListUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ProductDetail extends ETResponse {

    public static final String IN_STOCK = "instock";

    @SerializedName("product_id")
    @Expose
    public String productId;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("is_rental")
    @Expose
    public String isRental;

    @SerializedName("is_variant")
    @Expose
    public String hasVariants;

    @SerializedName("stock_status")
    @Expose
    public String stockStatus;

    @SerializedName("variations")
    @Expose
    public List<Variation> variations = null;

    @SerializedName("pricings")
    @Expose
    public List<ItemStatus> itemStatusList = null;

    @SerializedName("category_id")
    @Expose
    public String categoryId;

    public int quantity;


    public void preSelectVariants(){
        if(ListUtils.isNotEmpty(variations)){
            for(Variation  variation : variations){
                variation.selectedVariantItem = variation.variantItems.get(0);
            }
        }
    }


    public String getVariantPrice() {

        if(AppUtils.isTrue(hasVariants)) {
            ItemStatus itemStatus = getItemStatus();
            return itemStatus == null ? "0" : itemStatus.price;
        }else{
            return price;
        }
    }

    public boolean isVariantAvailable() {
        if(AppUtils.isTrue(hasVariants)) {
            ItemStatus itemStatus = getItemStatus();
            return itemStatus != null && itemStatus.isAvailable();
        }else{
            return isAvailable();
        }
    }

    public static class Variation {

        @SerializedName("variantName")
        @Expose
        public String variantName;

        @SerializedName("data")
        @Expose
        public List<VariantItem> variantItems;

        public transient VariantItem selectedVariantItem;


        public List<String> getValuesList(){
            List<String> values = new ArrayList<>();
            if(ListUtils.isNotEmpty(variantItems)){
                for(VariantItem item: variantItems){
                    values.add(item.value);
                }
            }
            return values;
        }

    }


    public static class VariantItem{

        @SerializedName("variantId")
        @Expose
        public String variantId;


        @SerializedName("value")
        @Expose
        public String value;


    }

    public static class ItemStatus{

        @SerializedName("price")
        @Expose
        public String price;


        @SerializedName("stock_status")
        @Expose
        public String stockStatus;

        @SerializedName("variants")
        @Expose
        public List<String> variantList;


        @NonNull
        @Override
        public String toString() {
            String toString =  "Price - "+ price + "\n Stock Status - "+ stockStatus+" \n Variants - ";
            if(ListUtils.isNotEmpty(variantList)){
                for(String item: variantList){
                    toString = toString+"\t"+ item;
                }
            }
            return toString;
        }

        private boolean isVariantPresent(String variantCode){
            if(ListUtils.isNotEmpty(variantList)){
                for(String variant: variantList){
                    if(variant.equalsIgnoreCase(variantCode)){
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean isAvailable(){
            return stockStatus.equalsIgnoreCase(IN_STOCK);
        }

    }

    private ItemStatus getItemStatus(){
        ItemStatus  itemStatusPresent = null;
        debugPrintSelectedVariants();
        if(ListUtils.isNotEmpty(itemStatusList)){
            for(ItemStatus itemStatus : itemStatusList){
                for(int i=0; i< variations.size(); i++){
                    Variation variation = variations.get(i);
                    if(itemStatus.isVariantPresent(variation.selectedVariantItem.variantId)){
                        itemStatusPresent = itemStatus;
                        Timber.i(itemStatusPresent.toString());
                        for(int j= i+1; j< variations.size(); j++){
                            Variation variation1 = variations.get(j);
                            if(!itemStatus.isVariantPresent(variation1.selectedVariantItem.variantId)){
                                itemStatusPresent = null;
                            }
                        }
                        if(itemStatusPresent != null){
                            return itemStatusPresent;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void debugPrintSelectedVariants(){
        Timber.i("Selected Variants: ");
        if(ListUtils.isNotEmpty(variations)){
            for(Variation variation: variations){
                Timber.i(" \t %s", variation.selectedVariantItem.variantId);
            }
        }
    }

    public boolean isOutOfStock(){
        if(!ListUtils.isEmpty(variations)){
            for(Variation variation : variations){
                if(!ListUtils.isEmpty(variation.variantItems)){
                    for(VariantItem item : variation.variantItems){
                        ItemStatus itemStatus = getItemStatus();
                        if(itemStatus != null && itemStatus.isVariantPresent(item.variantId)){
                            return false;
                        }
                    }
                }
            }
        }else{
            return !isAvailable();
        }
        return true;
    }

    private  boolean isAvailable(){
       return  stockStatus.equalsIgnoreCase(IN_STOCK);
    }
}
