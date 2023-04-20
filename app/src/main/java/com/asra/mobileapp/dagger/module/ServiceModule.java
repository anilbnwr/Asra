package com.asra.mobileapp.dagger.module;

import com.asra.mobileapp.services.FCMService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract FCMService ProvideFCMService();
}
