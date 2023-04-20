package com.asra.mobileapp.ui.admin.signature;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.exceptions.ETApiException;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.AdminUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignatureViewModel extends BaseViewModel {

    private final static String encodedString = "data:image/png;base64,";

    SingleLiveEvent<EventParticipant> participantObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> desclaimerObservable = new SingleLiveEvent<>();

    SingleLiveEvent<Boolean> signatureSavedObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> signatureSaveFailureObservable = new SingleLiveEvent<>();


    SingleLiveEvent<Bitmap> signatureFetchObservable = new SingleLiveEvent<>();
    SingleLiveEvent<String> signatureFetchFailureObservable = new SingleLiveEvent<>();


    private AdminUseCase adminUseCase;
    @Inject
    public SignatureViewModel(AdminUseCase adminUseCase,
                              AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.adminUseCase = adminUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();

        getDisclaimerText();
    }

    private void getDisclaimerText() {
        String disclaimer = getConfigString(MessageProvider.signature_disclaimer_text);

        if(StringUtils.isEmpty(disclaimer)){
            disclaimer = resourceFetcher.readFileFromRawDirectory(R.raw.event_signature_t_n_c);

        }
        desclaimerObservable.postValue(disclaimer);
    }

    public void processEventParticipantJson(String json){
        if(StringUtils.isEmpty(json)){
            showErrorMessage(getConfigString(MessageProvider.error_generic_message));
        }else{
            EventParticipant participant = gson.fromJson(json, EventParticipant.class);
            if(participant != null){
                participantObservable.postValue(participant);
            }
        }
    }

    public void fetchUserSignature() {
        showProgressBar(getConfigString(MessageProvider.reading_user_signature));
        EventParticipant participant = participantObservable.getValue();
        if(participant != null) {
            Disposable disposable = adminUseCase.getSignature(participant.signatureId)
                    .map(signature -> {
                        signature = signature.replace(encodedString, "");

                        byte[] decodedString = Base64.decode(signature, Base64.DEFAULT);
                        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSignatureFetched,
                            this::onSignatureReadFailed);
            addDisposable(disposable);
        }else{
            onSignatureReadFailed(new ETApiException(getConfigString(MessageProvider.error_generic_message)));
        }
    }

    private void onSignatureReadFailed(Throwable throwable) {
        hideProgressBar();
        signatureFetchFailureObservable.postValue(throwable.getMessage());
    }

    private void onSignatureFetched(Bitmap signature) {
        hideProgressBar();

        signatureFetchObservable.postValue(signature);
    }

    public void submitSignature(Bitmap signatureBitmap) {

        EventParticipant participant = participantObservable.getValue();
        if(participant != null) {

            showProgressBar(getConfigString(MessageProvider.msg_saving_signature));

            Disposable disposable = adminUseCase.saveSignature(signatureBitmap, participant.signatureId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSignatureSaved,
                            this::onSignatureSaveFailed);
            addDisposable(disposable);
        }else{
            onSignatureSaveFailed(new ETApiException(getConfigString(MessageProvider.error_generic_message)));
        }
    }

    private void onSignatureSaveFailed(Throwable throwable) {
        hideProgressBar();
        signatureSaveFailureObservable.postValue(throwable.getMessage());
    }

    private void onSignatureSaved() {
        hideProgressBar();
        signatureSavedObservable.postValue(true);


    }
}
