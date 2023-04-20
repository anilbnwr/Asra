package com.asra.mobileapp.usecase;

import android.os.Bundle;

import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.analytics.FirebaseAnalyticsHelper;
import com.asra.mobileapp.common.FabricUtils;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.network.adapter.apiservices.UserApiServices;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveUserApiServices;
import com.asra.mobileapp.network.retrofit.request.LoginRequest;
import com.asra.mobileapp.network.retrofit.request.SignUpRequest;
import com.asra.mobileapp.network.retrofit.response.LoginResponse;
import com.asra.mobileapp.util.SharedPrefKeys;
import com.asra.mobileapp.util.SharedPrefsHelper;

import javax.inject.Inject;

import io.reactivex.Single;


public class LoginUseCase extends BaseUseCase {

    private EvolveUserApiServices evolveApiServices;
    private SharedPrefsHelper sharedPrefsHelper;

    @Inject
    public LoginUseCase(AppEngine appEngine,
                        EvolveUserApiServices evolveApiServices,
                        SharedPrefsHelper sharedPrefsHelper) {
        super(appEngine);
        this.evolveApiServices = evolveApiServices;
        this.sharedPrefsHelper = sharedPrefsHelper;
    }

    private UserApiServices getApiServices(){
        return evolveApiServices;
    }

    public Single<LoginResponse> login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.email = username;
        loginRequest.password = password;

        return getApiServices().login(loginRequest)
                .doOnSuccess(loginResponse -> {
            sharedPrefsHelper.put(SharedPrefKeys.KEY_AUTH_TOKEN, loginResponse.token);
            sharedPrefsHelper.put(SharedPrefKeys.KEY_LOGGED_IN_USER,
                    convertToJson(loginResponse.currentUser));

            appEngine.setCurrentUser(loginResponse.currentUser);
            appEngine.setAuthToken(loginResponse.token);

            Bundle bundle = AnalyticsModel.loggedInEvent(loginResponse.currentUser.id);
            FirebaseAnalyticsHelper.getInstance(null).logEvent(AnalyticsModel.EVENT_LOGIN, bundle);
            FirebaseAnalyticsHelper.getInstance(null).setUserProperties(loginResponse.currentUser.id,
                    loginResponse.currentUser.role);
            FabricUtils.logUserId(loginResponse.currentUser.id,
                    loginResponse.currentUser.displayName);


        });
    }

    public Single<LoginResponse> signUp(SignUpRequest signUpRequest) {


        return getApiServices().signUp(signUpRequest)
                .doOnSuccess(loginResponse -> {
                    sharedPrefsHelper.put(SharedPrefKeys.KEY_AUTH_TOKEN, loginResponse.token);
                    sharedPrefsHelper.put(SharedPrefKeys.KEY_LOGGED_IN_USER,
                            convertToJson(loginResponse.currentUser));

                    appEngine.setCurrentUser(loginResponse.currentUser);
                    appEngine.setAuthToken(loginResponse.token);

                    Bundle bundle = AnalyticsModel.loggedInEvent(loginResponse.currentUser.id);
                    FirebaseAnalyticsHelper.getInstance(null).logEvent(AnalyticsModel.EVENT_LOGIN, bundle);
                    FirebaseAnalyticsHelper.getInstance(null).setUserProperties(loginResponse.currentUser.id,
                            loginResponse.currentUser.role);
                    FabricUtils.logUserId(loginResponse.currentUser.id,
                            loginResponse.currentUser.displayName);


                });
    }

    public Single<String> forgoPassword(String email) {

        return getApiServices().forgotPassword(email);

    }
}
