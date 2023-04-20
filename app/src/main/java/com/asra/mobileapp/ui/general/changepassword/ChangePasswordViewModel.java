package com.asra.mobileapp.ui.general.changepassword;

import android.text.TextUtils;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordViewModel extends BaseViewModel {

    public SingleLiveEvent<String> validationObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> passwordChangeMessageObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> passwordChangeErrorObservable = new SingleLiveEvent<>();

    private MemberUseCase memberUseCase;
    @Inject
    public ChangePasswordViewModel(MemberUseCase memberUseCase,
                                   AppEngine appEngine,
                                   ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    public void onPasswordChangeRequest(String currentPassword, String newPassword,
                                        String confirmPassword) {
        if(TextUtils.isEmpty(currentPassword)){
            validationObservable.postValue(getConfigString(MessageProvider.error_current_password_empty));
            return;
        }

        if(TextUtils.isEmpty(newPassword)){
            validationObservable.postValue(getConfigString(MessageProvider.error_new_password_empty));
            return;
        }

        if(!newPassword.equals(confirmPassword)){
            validationObservable.postValue(getConfigString(MessageProvider.error_confirm_password));
            return;
        }
        showProgressBar(getConfigString(MessageProvider.msg_changing_password));
        Disposable disposable = memberUseCase.changePassword(currentPassword, newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPasswordChanged,
                        this::onPasswordChangeFailed);
        addDisposable(disposable);
    }

    private void onPasswordChangeFailed(Throwable throwable) {
        hideProgressBar();
        passwordChangeErrorObservable.postValue(throwable.getMessage());
    }

    private void onPasswordChanged(String message) {
        hideProgressBar();
        passwordChangeMessageObservable.postValue(message);
    }
}
