package com.asra.mobileapp.ui.login.regisrtation;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.network.retrofit.request.SignUpRequest;
import com.asra.mobileapp.network.retrofit.response.LoginResponse;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.LoginUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RegistrationViewModel extends BaseViewModel {




    private LoginUseCase loginUseCase;
    private CartUseCase cartUseCase;


    public SingleLiveEvent<User> registerSuccessObservable = new SingleLiveEvent<>();


    @Inject
    public RegistrationViewModel(AppEngine appEngine, LoginUseCase loginUseCase, CartUseCase cartUseCase,
                                  ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.loginUseCase = loginUseCase;
        this.cartUseCase = cartUseCase;

    }


    public void register(SignUpRequest request) {


            showProgressBar(resourceFetcher.getConfigString(MessageProvider.msg_signing_up));

            Disposable disposable = loginUseCase.signUp(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSignUpCompleted,
                            this::onSignUpFailed);
            addDisposable(disposable);

    }


    private void onSignUpFailed(Throwable throwable){
        hideProgressBar();
        Timber.e(throwable, "Login Failed");
        showErrorMessage(throwable.getMessage());
    }

    private void onLocalCartSynced(){

        onSignUpCompleted();
    }

    private void onSignUpCompleted(LoginResponse loginResponse) {
        if(appEngine.isEvApp()) {
            Disposable disposable = cartUseCase.syncLocalCart()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onLocalCartSynced,
                            this::onSignUpFailed);
            addDisposable(disposable);
        }else{
            onSignUpCompleted();
        }
    }

    private void onSignUpCompleted() {
        hideProgressBar();
        registerSuccessObservable.postValue(appEngine.getCurrentUser());

    }
}
