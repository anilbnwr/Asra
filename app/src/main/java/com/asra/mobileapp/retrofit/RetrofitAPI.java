package com.asra.mobileapp.retrofit;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPI {

    public static Retrofit getRetrofit(String token) {
        Retrofit retrofit;

        Log.e(TAG, "token11 >> received getRetrofit: " + token);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient1 = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        String tokenvalue;
                        tokenvalue = "Bearer " + token;
                        Request request = chain.request().newBuilder().header("Authorization", tokenvalue).build();
                        Log.e(TAG, "token11 >> sent intercept: " + token);
                        return chain.proceed(request);
                    }
                }).addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://race.asraracing.com/ev-angular-api/public/admin/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient1)
                .build();

        return retrofit;
    }
}