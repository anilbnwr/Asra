package com.asra.mobileapp.database.dao;


import com.asra.mobileapp.database.entity.GuestCart;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface GuestDBHandler {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addToCart(GuestCart cart);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToCart(List<GuestCart> cartList);

    @Query("SELECT * FROM GuestCart")
    Single<List<GuestCart>> getGuestCartList();

    @Query("DELETE FROM GuestCart where id == :cartId")
    Completable deleteCart(String cartId);

    @Query("DELETE FROM GuestCart")
    Completable clearTable();


}
