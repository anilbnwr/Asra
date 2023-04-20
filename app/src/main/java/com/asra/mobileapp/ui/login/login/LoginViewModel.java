package com.asra.mobileapp.ui.login.login;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.network.retrofit.response.LoginResponse;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.LoginUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.SharedPrefKeys;
import com.asra.mobileapp.util.SharedPrefsHelper;
import com.asra.mobileapp.util.StringUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LoginViewModel extends BaseViewModel {

    private LoginUseCase loginUseCase;
    private CartUseCase cartUseCase;

   public SingleLiveEvent<User> loginSuccessObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> loginError = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> enableGuestObservable = new SingleLiveEvent<>();

    SharedPrefsHelper sharedPrefsHelper;
    @Inject
    public LoginViewModel(AppEngine appEngine,
                          LoginUseCase loginUseCase,
                          CartUseCase cartUseCase,
                          ResourceFetcher resourceFetcher, SharedPrefsHelper sharedPrefsHelper){
        super(appEngine, resourceFetcher);
        this.loginUseCase = loginUseCase;
        this.cartUseCase = cartUseCase;

        this.sharedPrefsHelper = sharedPrefsHelper;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();

        enableGuestObservable.postValue(appEngine.isEvApp());
        sharedPrefsHelper.put(SharedPrefKeys.KEY_AGREEMENT_STATUS, false);

    }

    public void doLogin(String username, String password) {

        String errorMessage = null;
        if(StringUtils.isEmpty(username)){
            errorMessage = resourceFetcher.getConfigString(MessageProvider.error_empty_username);
        }else if(StringUtils.isEmpty(password)){
            errorMessage = resourceFetcher.getConfigString(MessageProvider.error_empty_password);
        }

        if(!StringUtils.isEmpty(errorMessage)){
            loginError.postValue(errorMessage);
        }else {

            showProgressBar("Logging in");
            Disposable disposable = loginUseCase.login(username, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onLoginSuccessful,
                            this::onLoginFailed);
            addDisposable(disposable);
        }
    }

    private void onLoginSuccessful(LoginResponse loginResponse) {
        Timber.i("Login Successful");

        if(appEngine.isEvApp()) {
            Disposable disposable = cartUseCase.syncLocalCart()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onLocalCartSynced,
                            this::onLoginFailed);
            addDisposable(disposable);
        }else{
            onLoginCompleted();
        }

    }

    private void onLoginFailed(Throwable throwable){
        hideProgressBar();
        Timber.e(throwable, "Login Failed");
        loginError.postValue(throwable.getMessage());
    }

    private void onLocalCartSynced(){

        onLoginCompleted();
    }

    private void onLoginCompleted(){
        hideProgressBar();
        loginSuccessObservable.postValue(appEngine.getCurrentUser());

    }
}
