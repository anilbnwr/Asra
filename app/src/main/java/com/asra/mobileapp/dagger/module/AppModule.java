package com.asra.mobileapp.dagger.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.asra.mobileapp.common.ThemeHelper;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.SharedPrefsHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {



    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences("Evolve-GT", Context.MODE_PRIVATE);
    }

    @Provides
    Resources provideResourcesCompat(Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    AppEngine provideAppEngine(SharedPrefsHelper prefsHelper) {
        return new AppEngine(prefsHelper);
    }



    @Provides
    @Singleton
    SharedPrefsHelper provideSharedPrefHelper(SharedPreferences sharedPreferences) {
        return new SharedPrefsHelper(sharedPreferences);
    }

    @Provides
    @Singleton
    ResourceFetcher provideResourceFetcher(Context context) {
        return new ResourceFetcher(context);
    }

    @Provides
    @Singleton
    ThemeHelper provideThemeHelper(ResourceFetcher resourceFetcher, AppEngine appEngine) {
        return new ThemeHelper(appEngine, resourceFetcher);
    }
}
