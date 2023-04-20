package com.asra.mobileapp.ui.general.ewaiver;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.WaiverEvent;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EWaiverViewModel extends BaseViewModel {

    private MemberUseCase memberUseCase;

    public SingleLiveEvent<String> waiverListError = new SingleLiveEvent<>();

    public SingleLiveEvent<List<WaiverEvent>> waiverListObservable = new SingleLiveEvent<>();

    @Inject
    public EWaiverViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher,MemberUseCase memberUseCase) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();
        fetchWaiverList();
    }

    private void fetchWaiverList() {
        showProgressBar(getConfigString(MessageProvider.loading_waiver_events));
        Disposable disposable = memberUseCase.fetchWaiverEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(waiverEventList -> {
                            hideProgressBar();
                            waiverListObservable.postValue(waiverEventList);
                        },
                        throwable -> {
                            hideProgressBar();
                            waiverListError.postValue(throwable.getMessage());
                        });
        addDisposable(disposable);
    }
}
