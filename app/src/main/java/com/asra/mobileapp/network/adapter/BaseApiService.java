package com.asra.mobileapp.network.adapter;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.analytics.FirebaseAnalyticsHelper;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.network.retrofit.response.ETResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public abstract class BaseApiService {

    public final static String TYPE_TEXT_PLAIN = "text/plain";

    public static final String KEY_PUBLISH = "publish";

    public FirebaseAnalyticsHelper analyticsHelper;

    public BaseApiService(){
        analyticsHelper = FirebaseAnalyticsHelper.getInstance(ETApplication.getInstance());

    }

    public void checkApiError(ETResponse response) throws ETApiException {

        if(response.dataError()){
            throw new ETApiException(response);
        }
    }

    public RequestBody convertIntoRequestBody(String data) {

        return RequestBody.create(data, MediaType.parse(TYPE_TEXT_PLAIN));

    }
}
