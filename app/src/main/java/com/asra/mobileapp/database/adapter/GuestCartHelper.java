package com.asra.mobileapp.database.adapter;

import com.asra.mobileapp.database.entity.GuestCart;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.Event;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.model.ProductDetail;
import com.asra.mobileapp.model.ShopCard;
import com.asra.mobileapp.util.ListUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class GuestCartHelper {

    private static final String TYPE_RENTAL = "rentals";
    private static final String TYPE_EVENT = "event";
    private static final String TYPE_TRAINING = "training";
    private static final String TYPE_ARCHIE = "archie";
    private static final String TYPE_GIFT = "giftcard";
    private static final String TYPE_PRODUCT= "product";
    private static final String TYPE_MEMBERSHIP = "membership";
    
    private Gson gson;


    public GuestCartHelper(){
        gson = new Gson();
    }

    
    public GuestCart convertEventToCart(Event event){
        CartItem cartItem = new CartItem();
        cartItem.slug = event.slug;
        cartItem.price = event.price;
        cartItem.method = TYPE_EVENT;
        cartItem.eventType = event.eventType;
        cartItem.quantity = "1";
        cartItem.title = event.title;
        cartItem.image = event.logoPath;
        cartItem.stockStatus = CartItem.IN_STOCK;
        cartItem.objectId = event.eventId;

        GuestCart guestCart = new GuestCart();
        guestCart.rawJson = gson.toJson(cartItem);
        guestCart.slug = event.slug;

        return guestCart;
    }

    public List<GuestCart> convertEventToCart(EventDetails event){
        
        List<GuestCart> guestCartList = new ArrayList<>();
        
        CartItem cartItem = new CartItem();
        cartItem.slug = event.slug;
        cartItem.price = event.price;
        cartItem.method = TYPE_EVENT;
        cartItem.eventType = event.eventType;
        cartItem.title = event.title;
        cartItem.image = event.eventBanner;
        cartItem.objectId = event.eventId;
        cartItem.parentId = "0";
        cartItem.quantity = "1";
        cartItem.stockStatus = CartItem.IN_STOCK;
        GuestCart guestCart = new GuestCart();
        guestCart.rawJson = gson.toJson(cartItem);
        guestCart.slug = event.slug;

        guestCartList.add(guestCart);
        //check if trainings selected
        if(!ListUtils.isEmpty(event.trainingData)){
            for(EventDetails.TrainingDatum training : event.trainingData){
                if(training.selected){
                    CartItem trainingItem = new CartItem();
                    trainingItem.slug = training.slug;
                    trainingItem.price = training.price;
                    trainingItem.method = TYPE_TRAINING;
                    trainingItem.eventType = "";
                    trainingItem.title = training.title;
                    trainingItem.image = training.image;
                    trainingItem.quantity = "1";
                    trainingItem.stockStatus = CartItem.IN_STOCK;
                    trainingItem.parentTitle = event.title;
                    trainingItem.parentSlug = event.slug;
                    trainingItem.parentId = event.eventId;
                    trainingItem.objectId = training.trainingId;
                    GuestCart guestTrainingCart = new GuestCart();
                    guestTrainingCart.rawJson = gson.toJson(trainingItem);
                    guestTrainingCart.slug = training.slug;

                    guestCartList.add(guestTrainingCart);
                }
            }
        }

        //check if trainings selected
        if(!ListUtils.isEmpty(event.rentalData)){
            for(EventDetails.RentalDatum rental : event.rentalData){
                if(rental.selected && rental.selectedVariant != null){
                    CartItem rentalItem = new CartItem();
                    rentalItem.slug = rental.slug;
                    rentalItem.price = rental.selectedVariant.price;
                    rentalItem.method = TYPE_RENTAL;
                    rentalItem.eventType = "";
                    rentalItem.title = rental.title;
                    rentalItem.image = rental.image;
                    rentalItem.quantity = "1";
                    rentalItem.parentTitle = event.title;
                    rentalItem.parentSlug = event.slug;
                    rentalItem.parentId = event.eventId;
                    rentalItem.objectId = rental.productId;

                    rentalItem.stockStatus = CartItem.IN_STOCK;
                    rentalItem.attributes = new ArrayList<>();


                    CartItem.Attribute attribute = new CartItem.Attribute();
                    attribute.name = rental.selectedVariant.variantName;
                    attribute.value = rental.selectedVariant.attributeValue;
                    rentalItem.attributes.add(attribute);

                    GuestCart guestRentalCart = new GuestCart();
                    guestRentalCart.rawJson = gson.toJson(rentalItem);
                    guestRentalCart.slug = rental.slug;

                    guestCartList.add(guestRentalCart);
                }
            }
        }
        return guestCartList;
    }


    public GuestCart convertGiftCardToCart(ShopCard card, String email, String name){
        CartItem cartItem = new CartItem();
        cartItem.slug = card.slug;
        cartItem.price = card.price;
        cartItem.method = TYPE_GIFT;
        cartItem.eventType = "";
        cartItem.title = card.title;
        cartItem.image = card.imagePath;
        cartItem.quantity = "1";
        cartItem.stockStatus = CartItem.IN_STOCK;
        cartItem.objectId = "1";
        cartItem.parentId = "0";
        cartItem.attributes = new ArrayList<>();
        CartItem.Attribute attribute = new CartItem.Attribute();
        attribute.name = "Name";
        attribute.value = name;
        cartItem.attributes.add(attribute);

        attribute = new CartItem.Attribute();
        attribute.name = "Email";
        attribute.value = email;
        cartItem.attributes.add(attribute);


        GuestCart guestCart = new GuestCart();
        guestCart.rawJson = gson.toJson(cartItem);
        guestCart.slug = card.slug;

        return (guestCart);
    }

    public GuestCart  convertArchieCardToCart(ShopCard card, int quantity){
        CartItem cartItem = new CartItem();
        cartItem.slug = card.slug;
        cartItem.price = card.price;
        cartItem.method = TYPE_ARCHIE;
        cartItem.eventType = "";
        cartItem.quantity = String.valueOf(quantity);
        cartItem.title = card.title;
        cartItem.image = card.imagePath;
        cartItem.objectId = "1";
        cartItem.parentId = "0";
        cartItem.stockStatus = CartItem.IN_STOCK;

        GuestCart guestCart = new GuestCart();
        guestCart.rawJson = gson.toJson(cartItem);
        guestCart.slug = card.slug;

        return (guestCart);
    }

    public GuestCart  convertProductToCart(ProductDetail productDetail){
        CartItem cartItem = new CartItem();
        cartItem.slug = productDetail.slug;
        cartItem.price = productDetail.getVariantPrice();
        cartItem.method = TYPE_PRODUCT;
        cartItem.eventType = "";
        cartItem.quantity = String.valueOf(productDetail.quantity);
        cartItem.title = productDetail.title;
        cartItem.image = productDetail.image;
        cartItem.objectId = "1";
        cartItem.parentId = "0";
        cartItem.stockStatus = CartItem.IN_STOCK;

        if(ListUtils.isNotEmpty(productDetail.variations)){
            cartItem.attributes = new ArrayList<>();
            for(ProductDetail.Variation variation : productDetail.variations){
                CartItem.Attribute attribute =new CartItem.Attribute();
                attribute.name = variation.variantName;
                attribute.value = variation.selectedVariantItem.value;
                cartItem.attributes.add(attribute);
            }
        }

        GuestCart guestCart = new GuestCart();
        guestCart.rawJson = gson.toJson(cartItem);
        guestCart.slug = productDetail.slug;

        return (guestCart);
    }

    public CartItem getCartItem(GuestCart guestCart){
        CartItem cartItem =  gson.fromJson(guestCart.rawJson, CartItem.class);
        cartItem.cartId = String.valueOf(guestCart.id);
        return cartItem;
    }
}
