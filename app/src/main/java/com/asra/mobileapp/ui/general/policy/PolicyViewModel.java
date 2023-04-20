package com.asra.mobileapp.ui.general.policy;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.ActivityViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.SharedPrefKeys;
import com.asra.mobileapp.util.SharedPrefsHelper;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PolicyViewModel extends ActivityViewModel {

    public SingleLiveEvent<String> policyCheckFailed = new SingleLiveEvent();
    public SingleLiveEvent<Boolean> policyAgreementSuccess = new SingleLiveEvent();

    private SharedPrefsHelper sharedPrefsHelper;
    @Inject
    public PolicyViewModel(MemberUseCase memberUseCase, AppEngine appEngine,
                           ResourceFetcher resourceFetcher,
                           SharedPrefsHelper sharedPrefsHelper) {
        super(memberUseCase, appEngine, resourceFetcher);
        this.sharedPrefsHelper = sharedPrefsHelper;
    }


    public void acceptPolicyAgreement(boolean accepted) {
        showProgressBar(getConfigString(MessageProvider.saving_policy_agreement));
        Disposable disposable = memberUseCase.acceptPolicyAgreement(accepted)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            hideProgressBar();
                            sharedPrefsHelper.put(SharedPrefKeys.KEY_AGREEMENT_STATUS, true);
                            policyAgreementSuccess.postValue(true);
                        },
                        throwable -> {
                            policyCheckFailed.postValue(throwable.getMessage());
                            hideProgressBar();
                        }
                );
        addDisposable(disposable);
    }
}
