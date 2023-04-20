package com.asra.mobileapp.ui.dashboard.duties;

import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.network.retrofit.response.CoachDutyResponse;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.AdminUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CoachEventDutyListViewModel extends BaseViewModel {

    public SingleLiveEvent<CoachDutyResponse.DutyTypes> dutyListObservable = new SingleLiveEvent<>();
    private AdminUseCase adminUseCase;
    @Inject
    public CoachEventDutyListViewModel(AdminUseCase adminUseCase,
                                       AppEngine appEngine,
                                       ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.adminUseCase = adminUseCase;
    }

    public void getDutyEventList() {
        CoachDutyResponse.DutyTypes dutyList = dutyListObservable.getValue();
        if(dutyList == null) {
            showProgressBar();
            Disposable disposable = adminUseCase.getDutyList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onDutyListFetched,
                            this::onFetchingDutyListFailed);
            addDisposable(disposable);
        }else{
            onDutyListFetched(dutyList);
        }
    }

    private void onFetchingDutyListFailed(Throwable throwable) {
        hideProgressBar();
        showEmptyMessage(throwable.getMessage());
    }

    private void onDutyListFetched(CoachDutyResponse.DutyTypes eventDuties) {
        hideProgressBar();
        appEngine.setCoachDutyTypes(eventDuties);
        dutyListObservable.postValue(eventDuties);
    }
}
