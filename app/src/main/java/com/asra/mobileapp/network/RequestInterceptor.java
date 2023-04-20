package com.asra.mobileapp.network;


import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.NetworkUtils;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.exceptions.NoNetworkException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class RequestInterceptor implements Interceptor {

    private AppEngine appEngine;

    @Inject
    public RequestInterceptor(AppEngine appEngine){
        this.appEngine = appEngine;
    }

    private static final String KEY_AUTH = "authorization";
    private static final String KEY_BASIC_AUTH = "Bearer";

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        ensureNetwork();
        String token = appEngine.getAuthToken();
        Timber.i("Auth Token - %s", token);
        Request request = chain.request().newBuilder()
                .header(KEY_AUTH, KEY_BASIC_AUTH + " "+token)
                .header("Content-Type", "application/json")
                .build();

        okhttp3.Response response = chain.proceed(request);
        if (response.code() >= 500) {
            Timber.e("Server Error - Http code - %s", response.code());
            throw new ETApiException(MessageProvider.getInstance().
                    getText(MessageProvider.error_generic_message));
        }
        return response;
    }

    private void ensureNetwork() throws NoNetworkException {
        if(!NetworkUtils.isNetworkAvailable(ETApplication.getInstance())){
            throw new NoNetworkException(MessageProvider.getInstance().getText(MessageProvider.error_no_network));
        }
    }
}
