package com.asra.mobileapp.ui.general.ewaiver.waiverdetails;

import android.graphics.Bitmap;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.State;
import com.asra.mobileapp.model.WaiverEventDetails;
import com.asra.mobileapp.network.retrofit.request.WaiverSaveRequest;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WaiverDetailsViewMoel extends BaseViewModel {

    public SingleLiveEvent<String> waiverDetailsError = new SingleLiveEvent<>();
    public SingleLiveEvent<String> waiverSignSaved = new SingleLiveEvent<>();
    public SingleLiveEvent<String> waiverSignError = new SingleLiveEvent<>();
    public SingleLiveEvent<WaiverEventDetails.WaiverData> waiverEventData = new SingleLiveEvent<>();
    public SingleLiveEvent<List<State>> stateListObservable = new SingleLiveEvent<>();

    private MemberUseCase memberUseCase;
    private State selectedState;

    @Inject
    public WaiverDetailsViewMoel(AppEngine appEngine, ResourceFetcher resourceFetcher, MemberUseCase memberUseCase) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    public void fetchWaiverDetails(String waiverId){
        showProgressBar(getConfigString(MessageProvider.loading_waiver_details));
        Disposable disposable = memberUseCase.fetchWaiverDetails(waiverId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(waiverEventDetails -> {
                            hideProgressBar();
                            waiverEventData.postValue(waiverEventDetails.waiverData);

                            appEngine.setWaiverData(waiverEventDetails.waiverData);
                            appEngine.setLicenseIssuedState(waiverEventDetails.waiverStates);
                        },
                        throwable -> {
                            hideProgressBar();
                            waiverDetailsError.postValue(throwable.getMessage());
                        });
        addDisposable(disposable);
    }

    public void getCachedWaiverDetails() {
        WaiverEventDetails.WaiverData waiverData = appEngine.getWaiverData();
        List<WaiverEventDetails.WaiverState> issuedStates = appEngine.getLicenseIssuedStates();
        waiverEventData.postValue(waiverData);

        List<State> stateList = new ArrayList<>();
        for(WaiverEventDetails.WaiverState waiverState: issuedStates){
            State state = new State();
            state.name = waiverState.name;
            state.shortName = waiverState.shortName;
            stateList.add(state);
        }
        stateListObservable.postValue(stateList);
    }

    public void onStateSelected(State state) {
        this.selectedState = state;
    }

    public void saveSignature(Bitmap signature, WaiverSaveRequest request) {
        request.issuingState = this.selectedState.shortName;
        request.userId = appEngine.getUserId();

        showProgressBar(getConfigString(MessageProvider.saving_waiver_signature));
        Disposable disposable = memberUseCase.saveWaiverSignature(signature, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            hideProgressBar();
                            waiverSignSaved.postValue("Signature saved successfully.");
},
                        throwable -> {
                            hideProgressBar();
                            waiverSignError.postValue(throwable.getMessage());
                        });
        addDisposable(disposable);
    }
}
