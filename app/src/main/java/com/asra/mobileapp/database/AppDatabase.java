package com.asra.mobileapp.database;


import com.asra.mobileapp.database.dao.GuestDBHandler;
import com.asra.mobileapp.database.entity.GuestCart;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities =
        {
                GuestCart.class
        },
        version = AppDatabase.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "EvolveGT";
    static final int DATABASE_VERSION = 2;


    public abstract GuestDBHandler guestDBHandler();

}
