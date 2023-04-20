package com.asra.mobileapp.usecase;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.analytics.FirebaseAnalyticsHelper;
import com.asra.mobileapp.core.AppEngine;
import com.google.gson.Gson;

public abstract class BaseUseCase {

    public Gson gson;

    public FirebaseAnalyticsHelper analyticsHelper;
    AppEngine appEngine;
    public BaseUseCase(AppEngine appEngine){
        this.appEngine = appEngine;
        gson = new Gson();
        analyticsHelper = FirebaseAnalyticsHelper.getInstance(ETApplication.getInstance());
    }



    public String convertToJson(Object object){
        return gson.toJson(object);
    }
}
