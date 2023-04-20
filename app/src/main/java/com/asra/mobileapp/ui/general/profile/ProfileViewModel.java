package com.asra.mobileapp.ui.general.profile;

import android.text.TextUtils;

import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.ui.general.profile.model.SimpleDate;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;
import com.asra.mobileapp.util.StringUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends BaseViewModel {

    public SingleLiveEvent<UserDetails> userDetailsObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> userDetailsErrorObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> shippingAddressObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> billingAddressObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> profileUpdateObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> profileUpdateErrorObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<SimpleDate> dobObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<SimpleDate> amaExpiryChangeObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> showMotoInfoObservable = new SingleLiveEvent<>();


    private MemberUseCase memberUseCase;

    private String profileImagePath = null;

    private boolean updatingProfile = false;
    @Inject
    public ProfileViewModel(MemberUseCase memberUseCase,
                            AppEngine appEngine,
                            ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();

        if(appEngine.getUserDetails() != null){
            postUserDetails();
        }else{
            fetchUserDetails();
        }

        showMotoInfoObservable.postValue(!appEngine.isEvApp());

    }

    private void fetchUserDetails() {
        if(!updatingProfile)
            showProgressBar();

        Disposable disposable = memberUseCase.getUserDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUserDetailsFetched,
                        this::onFetchingUserDetailsFailed);
        addDisposable(disposable);
    }

    private void onFetchingUserDetailsFailed(Throwable throwable) {
        hideProgressBar();
        userDetailsErrorObservable.postValue(throwable.getMessage());
        updatingProfile = false;
    }

    private void postUserDetails(){

        userDetailsObservable.postValue(appEngine.getUserDetails());

        String shipping = buildMailingAddress(appEngine.getUserDetails());
        String billing = buildBillingAddress(appEngine.getUserDetails());

        shippingAddressObservable.postValue(shipping);
        billingAddressObservable.postValue(billing);

    }

    private void onUserDetailsFetched(UserDetails userDetails) {
        hideProgressBar();
        appEngine.saveUserDetails(userDetails);
        postUserDetails();

        if(updatingProfile)
            profileUpdateObservable.postValue(getConfigString(
                MessageProvider.profile_updated_successfully));
        updatingProfile = false;
    }

    private String buildMailingAddress(UserDetails user) {
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(user.shippingAddress1)) {
            String newline = "\n";
            String space = " ";
            builder = builder.append(user.firstName).append(space)
                    .append(user.lastName).append(newline);

            builder = builder.append(user.shippingAddress1).append(newline);

            if (!TextUtils.isEmpty(user.shippingAddress2))
                builder = builder.append(user.shippingAddress2).append(newline);


            if (!TextUtils.isEmpty(user.shippingCompany))
                builder = builder.append(user.shippingCompany).append(newline);

            if (!TextUtils.isEmpty(user.shippingCity))
                builder = builder.append(user.shippingCity)
                        .append(TextUtils.isEmpty(user.shippingState) ? "": ", "+ user.shippingState)
                        .append(" - ")
                        .append(user.shippingPostcode)
                        .append(newline);
            builder = builder.append(user.shippingCountry).append(newline);


        }

        return builder.toString();
    }

    private String buildBillingAddress(UserDetails user) {
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(user.billingAddress1)) {
            String newline = "\n";
            String space = " ";
            builder = builder.append(user.firstName).append(space)
                    .append(user.lastName).append(newline);

            builder = builder.append(user.billingAddress1).append(newline);

            if (!TextUtils.isEmpty(user.billingAddress2))
                builder = builder.append(user.billingAddress2).append(newline);


            if (!TextUtils.isEmpty(user.billingCompany))
                builder = builder.append(user.billingCompany).append(newline);

            if (!TextUtils.isEmpty(user.billingCity))
                builder = builder.append(user.billingCity)
                        .append(TextUtils.isEmpty(user.billingState) ? "": ", "+ user.billingState)
                        .append(" - ")
                        .append(user.billingPostcode)
                        .append(newline);


            builder = builder.append(user.billingCountry).append(newline);


        }

        return builder.toString();
    }

    public void changeDoB() {
        UserDetails user = appEngine.getUserDetails();
        if (user != null) {
            SimpleDate dob = new SimpleDate();
            dob.year = !AppUtils.isDigitsOnly(user.dobYear) ? 1975 : Integer.valueOf(user.dobYear);
            dob.month = !AppUtils.isDigitsOnly(user.dobMonth) ? 0 : Integer.valueOf(user.dobMonth) - 1;
            dob.dayOfMonth = !AppUtils.isDigitsOnly(user.dobDay) ? 0 : Integer.valueOf(user.dobDay);
            dobObservable.postValue(dob);
        }


    }

    public void updateDoB(int year, int month, int day) {

    }

    public void saveProfileChanges(UserDetails userDetails) {
        updatingProfile = true;

        showProgressBar(getConfigString(MessageProvider.updating_profile));
        Disposable disposable = memberUseCase.updateProfile(userDetails)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUserProfileUpdated,
                        this::onUserProfileUpdateFailed);
        addDisposable(disposable);
    }

    private void onUserProfileUpdateFailed(Throwable throwable) {
        hideProgressBar();
        profileUpdateErrorObservable.postValue(throwable.getMessage());
        updatingProfile = false;
    }

    private void onUserProfileUpdated() {
        if(StringUtils.isEmpty(profileImagePath)) {
            fetchUserDetails();
        }else{
            uploadImage();
        }
    }

    private void uploadImage(){

        if(!StringUtils.isEmpty(profileImagePath)){
            Disposable disposable = memberUseCase.updateProfileImage(profileImagePath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onUserProfileImageUpdated,
                            this::onUserProfileImageUpdateFailed);
            addDisposable(disposable);
        }
    }

    private void onUserProfileImageUpdateFailed(Throwable throwable) {
        hideProgressBar();
        appEngine.saveUserDetails(null);
        profileUpdateErrorObservable.postValue(throwable.getMessage());
        updatingProfile = false;
    }

    private void onUserProfileImageUpdated() {
        fetchUserDetails();
    }

    public void onImageCaptured(String filePath){
        this.profileImagePath = filePath;
    }

    public void updateAmaExpiry(int year, int month, int day) {

    }

    public void changeAmaExpiry() {
        UserDetails user = appEngine.getUserDetails();
        if (user != null) {
            SimpleDate amaExpiry = new SimpleDate();
            if(!StringUtils.isEmpty(user.motoAmaExpiryDate)){
                String[] datePieces = user.motoAmaExpiryDate.split("-");
                if(datePieces.length == 3){
                    amaExpiry.year = AppUtils.getInteger(datePieces[0]);
                    amaExpiry.month = AppUtils.getInteger(datePieces[1]) - 1;
                    amaExpiry.dayOfMonth = AppUtils.getInteger(datePieces[2]);
                }
            }
            amaExpiryChangeObservable.postValue(amaExpiry);
        }
    }
}
