package com.asra.mobileapp.ui.general.credithistory;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.CreditHistory;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreditHistoryViewModel extends BaseViewModel {

    private MemberUseCase memberUseCase;
    public SingleLiveEvent<List<CreditHistory>> creditHistoryListObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> creditHistoryErrorObservable = new SingleLiveEvent<>();

    @Inject
    public CreditHistoryViewModel(MemberUseCase memberUseCase, AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();
        fetchCreditHistory();
    }

    private void fetchCreditHistory() {
        showProgressBar();
        Disposable disposable = memberUseCase.getCreditHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(creditHistoryList -> {
                            hideProgressBar();
                            creditHistoryListObservable.postValue(creditHistoryList);
                        },
                        throwable -> {
                            hideProgressBar();
                            creditHistoryErrorObservable.postValue(throwable.getMessage());
                        });
        addDisposable(disposable);
    }
}
