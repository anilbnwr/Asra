package com.asra.mobileapp.network.retrofit.request;

import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.util.ListUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartProductRequest {

    @SerializedName("selectedAttributes")
    public List<Attribute> selectedAttributes;

    @SerializedName("price")
    public String price;

    @SerializedName("quantity")
    public int quantity;

    @SerializedName("slug")
    public String slug;

    @SerializedName("serial")
    public String userId;

    public List<Attribute> getAttributes(ProductDetail productDetail) {
        List<Attribute> attributes = new ArrayList<>();

        if(ListUtils.isNotEmpty(productDetail.variations)) {
            for (ProductDetail.Variation variation : productDetail.variations) {
                Attribute attribute = new Attribute();
                attribute.name = variation.variantName;
                attribute.value = variation.selectedVariantItem.value;

                attributes.add(attribute);

            }
        }
        return attributes;
    }


    public static class Attribute{

        @SerializedName("name")
        public String name;

        @SerializedName("value")
        public String value;


    }


}
