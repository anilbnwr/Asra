package com.asra.mobileapp.dagger.module;

import android.content.Context;

import com.asra.mobileapp.database.AppDatabase;
import com.asra.mobileapp.database.adapter.GuestCartHelper;
import com.asra.mobileapp.database.dao.GuestDBHandler;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Singleton
    @Provides
    public AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, AppDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    public GuestDBHandler provideDBHandler(AppDatabase database) {
        return database.guestDBHandler();
    }


    @Singleton
    @Provides
    public GuestCartHelper provideGuestCartHelper() {
        return new GuestCartHelper();
    }

}
