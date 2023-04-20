package com.asra.mobileapp.ui.general.enrolledevents.passport.uploadpassport;

import android.graphics.Bitmap;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PassportSignatureViewModel extends BaseViewModel {

    MemberUseCase memberUseCase;

    SingleLiveEvent<Boolean> passportSavedObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> passportSaveFailureObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> disclaimerObservable = new SingleLiveEvent<>();

    @Inject
    public PassportSignatureViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher,
                                      MemberUseCase memberUseCase) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
        getDisclaimerText();
    }

    private void getDisclaimerText() {
        String disclaimer = getConfigString(MessageProvider.signature_disclaimer_text);

        if(StringUtils.isEmpty(disclaimer)){
            disclaimer = resourceFetcher.readFileFromRawDirectory(R.raw.event_signature_t_n_c);

        }
        disclaimerObservable.postValue(disclaimer);
    }

    public void onImageCaptured(String imagePath) {
        appEngine.setSelfie(imagePath);
    }

    public void savePassport(Bitmap signatureBitmap) {
        String selfiePath = appEngine.getSelfiePath();
        if(StringUtils.isEmpty(selfiePath)){
            passportSaveFailureObservable.postValue(resourceFetcher.getConfigString(MessageProvider.error_no_selfie_passport));
            return;
        }
        showProgressBar();

        Disposable disposable = Single.just(signatureBitmap)
                .map(processingSignature -> {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    signatureBitmap, 500, 500, false);
            return StringUtils.bitMapToString(resizedBitmap);
        }).flatMapCompletable(signature -> memberUseCase.savePassport(selfiePath,
                        signature, appEngine.getEnrolledEvent().eventId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPassportSaved,
                        this::onPassportSavingFailed);
        addDisposable(disposable);


    }

    private void onPassportSavingFailed(Throwable throwable) {
        hideProgressBar();
        passportSaveFailureObservable.postValue(throwable.getMessage());
    }

    private void onPassportSaved() {
        hideProgressBar();
        passportSavedObservable.postValue(true);
    }
}
