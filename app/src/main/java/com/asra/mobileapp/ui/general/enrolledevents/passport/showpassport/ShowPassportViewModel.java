package com.asra.mobileapp.ui.general.enrolledevents.passport.showpassport;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.PassportInfo;
import com.asra.mobileapp.model.StampPassword;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShowPassportViewModel extends BaseViewModel {

    public SingleLiveEvent<PassportInfo> passportInfoObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<StampPassword> stampPasswordObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> passportErrorObserver = new SingleLiveEvent<>();
    public SingleLiveEvent<String> trackNameObserver = new SingleLiveEvent<>();

    MemberUseCase memberUseCase;
    @Inject
    public ShowPassportViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher, MemberUseCase memberUseCase) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    public void getPassport(String passportId) {

        showProgressBar();
        Disposable disposable = memberUseCase.getPassport(passportId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPassportFetched,
                        this::onPassportFetchFailed);
        addDisposable(disposable);
        trackNameObserver.postValue(appEngine.getTrackName());
    }

    public void getStampPassport(String passportId) {

        showProgressBar();
        Disposable disposable = memberUseCase.getStampPassport(passportId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onStampPassportFetched,
                        this::onPassportFetchFailed);
        addDisposable(disposable);
//        trackNameObserver.postValue(appEngine.getTrackName());
    }

    private void onPassportFetchFailed(Throwable throwable) {
        hideProgressBar();
        passportErrorObserver.postValue(throwable.getMessage());

    }

    private void onPassportFetched(PassportInfo passportInfo) {
        hideProgressBar();
        if(passportInfo != null){
            passportInfoObservable.postValue(passportInfo);
        }else{
            passportErrorObserver.postValue(resourceFetcher.getConfigString(MessageProvider.error_generic_message));
        }

    }

    private void onStampPassportFetched(StampPassword stampPassword) {
        hideProgressBar();
        if(stampPassword != null){
            stampPasswordObservable.postValue(stampPassword);
        }else{
            passportErrorObserver.postValue(resourceFetcher.getConfigString(MessageProvider.error_generic_message));
        }

    }
}
