package com.asra.mobileapp.dagger.module;


import com.asra.mobileapp.network.api.EvolveAdminApi;
import com.asra.mobileapp.network.api.EvolveCartApi;
import com.asra.mobileapp.network.api.EvolveShopApis;
import com.asra.mobileapp.network.api.EvolveUserApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class EvolveApiServiceModule {

    @Singleton
    @Provides
    static EvolveUserApi provideEvolveUserApiService(Retrofit retrofit) {
        return retrofit.create(EvolveUserApi.class);
    }

    @Singleton
    @Provides
    static EvolveAdminApi provideEvolveAdminApiService(Retrofit retrofit) {
        return retrofit.create(EvolveAdminApi.class);
    }

    @Singleton
    @Provides
    static EvolveCartApi provideEvolveCartApiService(Retrofit retrofit) {
        return retrofit.create(EvolveCartApi.class);
    }

    @Singleton
    @Provides
    static EvolveShopApis provideEvolveShopsApiService(Retrofit retrofit) {
        return retrofit.create(EvolveShopApis.class);
    }
}
