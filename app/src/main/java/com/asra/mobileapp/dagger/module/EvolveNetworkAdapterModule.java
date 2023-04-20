package com.asra.mobileapp.dagger.module;

import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveAdminApiServices;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveCartApiServices;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveShopsApiServices;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveUserApiServices;
import com.asra.mobileapp.network.adapter.evovle.impl.EvolveAdminApiServiceImpl;
import com.asra.mobileapp.network.adapter.evovle.impl.EvolveCartApiServiceImpl;
import com.asra.mobileapp.network.adapter.evovle.impl.EvolveShopsApiServiceImpl;
import com.asra.mobileapp.network.adapter.evovle.impl.EvolveUserApiServiceImpl;
import com.asra.mobileapp.network.api.EvolveAdminApi;
import com.asra.mobileapp.network.api.EvolveCartApi;
import com.asra.mobileapp.network.api.EvolveShopApis;
import com.asra.mobileapp.network.api.EvolveUserApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EvolveNetworkAdapterModule {

    @Singleton
    @Provides
    static EvolveUserApiServices provideEvolveUserApiService(EvolveUserApi api, AppEngine appEngine) {
        return new EvolveUserApiServiceImpl(api, appEngine);
    }

    @Singleton
    @Provides
    static EvolveAdminApiServices provideEvolveAdminApiService(EvolveAdminApi api, AppEngine appEngine) {
        return new EvolveAdminApiServiceImpl(api, appEngine);
    }

    @Singleton
    @Provides
    static EvolveCartApiServices provideEvolveCartApiService(EvolveCartApi api) {
        return new EvolveCartApiServiceImpl(api);
    }

    @Singleton
    @Provides
    static EvolveShopsApiServices provideEvolveShopsApiService(EvolveShopApis api) {
        return new EvolveShopsApiServiceImpl(api);
    }
}
