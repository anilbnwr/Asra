package com.asra.mobileapp.usecase;

import android.graphics.Bitmap;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.analytics.FirebaseAnalyticsHelper;
import com.asra.mobileapp.common.FabricUtils;
import com.asra.mobileapp.common.dialog.EmergencyContactDialog;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Address;
import com.asra.mobileapp.model.AppVersionStatus;
import com.asra.mobileapp.model.Country;
import com.asra.mobileapp.model.CreditHistory;
import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.MembershipDetail;
import com.asra.mobileapp.model.MrlMessage;
import com.asra.mobileapp.model.NotificationType;
import com.asra.mobileapp.model.PassportInfo;
import com.asra.mobileapp.model.PolicyAgreement;
import com.asra.mobileapp.model.StampPassword;
import com.asra.mobileapp.model.State;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.model.UserMembership;
import com.asra.mobileapp.model.WaiverEvent;
import com.asra.mobileapp.network.adapter.apiservices.UserApiServices;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveUserApiServices;
import com.asra.mobileapp.network.retrofit.request.EmergencyContactRequest;
import com.asra.mobileapp.network.retrofit.request.SignatureUploadRequest;
import com.asra.mobileapp.network.retrofit.request.TransferCreditRequest;
import com.asra.mobileapp.network.retrofit.request.WaiverSaveRequest;
import com.asra.mobileapp.network.retrofit.response.WaiverDetailsResponse;
import com.asra.mobileapp.util.SharedPrefsHelper;
import com.asra.mobileapp.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

public class MemberUseCase extends BaseUseCase {

    SharedPrefsHelper sharedPrefsHelper;
    EvolveUserApiServices evolveServices;

    @Inject
    public MemberUseCase(AppEngine appEngine,
                         EvolveUserApiServices evolveApiServices,
                         SharedPrefsHelper sharedPrefsHelper) {
        super(appEngine);
        this.sharedPrefsHelper = sharedPrefsHelper;
        this.evolveServices = evolveApiServices;

    }

    private UserApiServices getApiServices(){
        return evolveServices;
    }
    public Single<UserDetails> getUserDetails() {

        return getApiServices().getUserDetails(appEngine.getUserId())
                .doOnSuccess(user -> {
                    appEngine.saveUserDetails(user);
                    appEngine.saveCancelEventPrivilege(user.canCancelEvents);
                    FabricUtils.logUserId(user.userId, user.displayName);
                    FirebaseAnalyticsHelper.getInstance(ETApplication.getInstance())
                            .setUserProperties(user.userId, user.role);
                });

    }

    public Single<List<EnrolledEvent>> getEventsHistory() {

        return getApiServices().getEventsHistory(appEngine.getUserId());
    }

    public Single<List<CreditHistory>> getCreditHistory() {

        return getApiServices().getCreditHistory(appEngine.getUserId());
    }

    public Single<List<NotificationType>> getNotificationTypes() {

        return getApiServices().getNotificationTypes(appEngine.getUserId());
    }

    public Completable updateNotificationSettings(List<NotificationType> notificationsTypes) {

        return getApiServices().updateNotificationPreferences(notificationsTypes, appEngine.getUserId());
    }

    public Single<String> changePassword(String currentPwd, String newPwd) {
        return getApiServices().changePassword(currentPwd, newPwd, appEngine.getUserId());
    }

    public Single<List<Membership>> getAvailableMembershipList() {
        return getApiServices().getAvailableMembershipList();
    }

    public Single<MrlMessage> getMrlMessage() {

        return getApiServices().getMrlMessage();
    }
    public Single<UserMembership> getCurrentUserMembership() {
        return getApiServices().getCurrentUserMembership(appEngine.getUserId())
                .doOnSuccess(userMembership -> {
                    appEngine.setCurrentUserMembership(userMembership);
                });
    }

    public Single<MembershipDetail> getMembershipDetail(String slug) {
        return getApiServices().getMembershipDetail(slug);
    }

    public Completable updateProfile(UserDetails userDetails) {
        userDetails.userId = appEngine.getUserId();
        return getApiServices().updateProfile(userDetails);
    }

    public Completable updateProfileImage(String imagePath) {
        return getApiServices().updateProfileImage(imagePath, appEngine.getUserId());
    }

    public Completable savePassport(String selfie, String signature, String eventId) {
        return getApiServices().savePassport(selfie, signature, appEngine.getUserId(), eventId);
    }

    public Single<List<Country>> getCountries() {
        return getApiServices().getSupportedCountries().onErrorResumeNext(throwable -> {
            List<Country> countries = new ArrayList<>();
            countries.add(Country.getUnitedStates());
            Timber.e(throwable, "Error - returning minimum countries list only.");
            return Single.just(countries);
        });
    }

    public Single<List<State>> getStates(String countryCode) {
        return getApiServices().getStates(countryCode);
    }

    public Single<PassportInfo> getPassport(String passportId) {
        return getApiServices().viewPassport (passportId);
    }

    public Single<StampPassword> getStampPassport(String passportId) {
        return getApiServices().viewStampPassport(passportId);
    }

    public Completable updateBillingAddress(Address address) {
        return getApiServices().updateBillingAddress(address, appEngine.getUserId());
    }

    public Completable updateMailingAddress(Address address) {
        return getApiServices().updateMailingAddress(address, appEngine.getUserId());
    }

    public Completable acceptPolicyAgreement(boolean accepted) {
        return getApiServices().updatePolicyAgreementStatus(appEngine.getUserId(), accepted);
    }

    public Single<PolicyAgreement> checkPolicyAccepted() {
        return getApiServices().checkForPolicyAgreementStatus(appEngine.getUserId());
    }

    public Completable cancelEvent(EnrolledEvent event) {
        return getApiServices()
                .cancelEvent(event.orderItemId,
                        appEngine.getCurrentUser().skillLevel,
                        appEngine.getCurrentUser().getUserId());

    }

    public Completable updateDeviceToken(String token, String deviceType) {
        appEngine.saveDeviceToken(token);
        return getApiServices().updateDeviceToken(appEngine.getUserId(),
                token, deviceType)
                .doOnComplete(() -> {
                    appEngine.saveDeviceTokenSyncStatus(true);
                }).doOnError(throwable -> {
                    appEngine.saveDeviceTokenSyncStatus(false);
                });
    }

    public Single<AppVersionStatus> checkForUpdate() {
        return getApiServices().checkForUpdate()
                .doOnSuccess(appVersionStatus ->
                        appEngine.setAppVersionStatus(appVersionStatus));
    }

    public Completable sendReferralInvitation(String email) {
        return getApiServices().sendReferralInvitation(appEngine.getUserId(), email);

    }

    public Single<List<WaiverEvent>> fetchWaiverEvents(){
        return getApiServices().getWaiverEvents();

    }

    public Single<WaiverDetailsResponse> fetchWaiverDetails(String eventId){
        return getApiServices().getWaiverDetails(appEngine.getUserId(), eventId);

    }

    public Completable saveWaiverSignature(Bitmap signature, final WaiverSaveRequest request) {

        return Single.just(signature).map(processingSignature -> {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    signature, 500, 500, false);
            return StringUtils.bitMapToString(resizedBitmap);
        }).flatMapCompletable(signatureText -> {
            request.signature = SignatureUploadRequest.IMAGE_DATA + signatureText;
            return getApiServices().saveWaiverSignature(request);
        });

    }

    public Completable transferCredit(double amount, String recipientEmail) {

        TransferCreditRequest request = new TransferCreditRequest();
        request.amount = amount;
        request.recipientEmail = recipientEmail;
        request.userId = appEngine.getUserId();

        return getApiServices().transferCredit(request);

    }

    public Completable updateEmergencyContact(EmergencyContactDialog.EmergencyContact contact) {

        EmergencyContactRequest request = new EmergencyContactRequest();
        request.firstName = contact.firstName;
        request.lastName = contact.lastName;
        request.phone = contact.phone;
        request.relationShip = contact.relationShip;
        request.userId = appEngine.getUserId();
        return getApiServices().updateEmergencyContact(request);

    }
}
