package com.asra.mobileapp.dagger.module;

import com.asra.mobileapp.BuildConfig;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.network.RequestInterceptor;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private static final int TIME_OUT = 2; //2 minutes
    @Provides
    Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    OkHttpClient getOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                                 RequestInterceptor requestInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(requestInterceptor)
                .connectTimeout(TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(TIME_OUT, TimeUnit.MINUTES)
                .build();
    }

    @Provides
    HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if(BuildConfig.TIMBER_LOG_ENABLED)
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        return httpLoggingInterceptor;
    }

    @Provides
    RequestInterceptor getHttpRequestInterceptor(AppEngine appEngine) {
        return new RequestInterceptor(appEngine);
    }
}
