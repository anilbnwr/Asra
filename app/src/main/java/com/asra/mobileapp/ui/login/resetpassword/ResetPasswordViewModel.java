package com.asra.mobileapp.ui.login.resetpassword;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.LoginUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ResetPasswordViewModel extends BaseViewModel {

    public SingleLiveEvent<String> errorMessageObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> passwordResetObservable = new SingleLiveEvent<>();

    private LoginUseCase loginUseCase;

    @Inject
    public ResetPasswordViewModel(AppEngine appEngine,
                                  LoginUseCase loginUseCase,
                                  ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.loginUseCase = loginUseCase;
    }

    public void onPasswordResetRequest(String email) {

        if(!UiUtils.isValidEmail(email)){
            errorMessageObservable.postValue(resourceFetcher.getConfigString(MessageProvider.msg_pwd_reset_email_required));
            return;
        }
        showProgressBar(resourceFetcher.getConfigString(MessageProvider.msg_resetting_password));

        Disposable disposable = loginUseCase.forgoPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPasswordReset,
                        this::onError);
        addDisposable(disposable);
    }

    private void onError(Throwable throwable) {
        hideProgressBar();
        errorMessageObservable.postValue(throwable.getMessage());

    }

    private void onPasswordReset(String message) {
        hideProgressBar();

        passwordResetObservable.postValue(message);
    }
}
