package com.asra.mobileapp.common.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

@GlideModule
public class CustomGlideModule extends AppGlideModule {

    private static final long IMAGE_TIMEOUT = 30;

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(IMAGE_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(IMAGE_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(IMAGE_TIMEOUT, TimeUnit.SECONDS);
        registry.append(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));
    }

}