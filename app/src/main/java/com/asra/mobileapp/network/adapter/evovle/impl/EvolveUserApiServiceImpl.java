package com.asra.mobileapp.network.adapter.evovle.impl;


import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Address;
import com.asra.mobileapp.model.AppVersionStatus;
import com.asra.mobileapp.model.Country;
import com.asra.mobileapp.model.CreditHistory;
import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.MembershipDetail;
import com.asra.mobileapp.model.MrlMessage;
import com.asra.mobileapp.model.NotificationPreference;
import com.asra.mobileapp.model.NotificationType;
import com.asra.mobileapp.model.PassportInfo;
import com.asra.mobileapp.model.PolicyAgreement;
import com.asra.mobileapp.model.StampPassword;
import com.asra.mobileapp.model.State;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.model.UserMembership;
import com.asra.mobileapp.model.WaiverEvent;
import com.asra.mobileapp.network.adapter.BaseApiService;
import com.asra.mobileapp.network.adapter.evovle.apiservices.EvolveUserApiServices;
import com.asra.mobileapp.network.api.EvolveUserApi;
import com.asra.mobileapp.network.retrofit.request.BillingAddressUpdateRequest;
import com.asra.mobileapp.network.retrofit.request.CreditHistoryRequest;
import com.asra.mobileapp.network.retrofit.request.DeviceTokenRequest;
import com.asra.mobileapp.network.retrofit.request.EmergencyContactRequest;
import com.asra.mobileapp.network.retrofit.request.EnrolledEventRequest;
import com.asra.mobileapp.network.retrofit.request.EventCancelRequest;
import com.asra.mobileapp.network.retrofit.request.ForgotPasswordRequest;
import com.asra.mobileapp.network.retrofit.request.LoginRequest;
import com.asra.mobileapp.network.retrofit.request.MembershipDetailsRequest;
import com.asra.mobileapp.network.retrofit.request.NotificationSyncRequest;
import com.asra.mobileapp.network.retrofit.request.NotificationTypesRequest;
import com.asra.mobileapp.network.retrofit.request.PasswordChangeRequest;
import com.asra.mobileapp.network.retrofit.request.PolicyAgreementCheckRequest;
import com.asra.mobileapp.network.retrofit.request.PolicyAgreementUpdateRequest;
import com.asra.mobileapp.network.retrofit.request.ProfileUpdateRequest;
import com.asra.mobileapp.network.retrofit.request.ReferralRequest;
import com.asra.mobileapp.network.retrofit.request.ShippingAddressUpdateRequest;
import com.asra.mobileapp.network.retrofit.request.SignUpRequest;
import com.asra.mobileapp.network.retrofit.request.StateRequest;
import com.asra.mobileapp.network.retrofit.request.TransferCreditRequest;
import com.asra.mobileapp.network.retrofit.request.UserDetailsRequest;
import com.asra.mobileapp.network.retrofit.request.UserMembershipRequest;
import com.asra.mobileapp.network.retrofit.request.ViewPassportRequest;
import com.asra.mobileapp.network.retrofit.request.WaiverDetailsRequest;
import com.asra.mobileapp.network.retrofit.request.WaiverSaveRequest;
import com.asra.mobileapp.network.retrofit.response.LoginResponse;
import com.asra.mobileapp.network.retrofit.response.WaiverDetailsResponse;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

public class EvolveUserApiServiceImpl extends BaseApiService implements EvolveUserApiServices {

    private EvolveUserApi apiServices;
    private AppEngine appEngine;
    @Inject
    public EvolveUserApiServiceImpl(EvolveUserApi apiServices, AppEngine appEngine){
        this.apiServices = apiServices;
        this.appEngine = appEngine;
    }

    @Override
    public Single<LoginResponse> login(LoginRequest loginRequest) {

         return apiServices.login(loginRequest)
                 .map(loginResponse -> {
            checkApiError(loginResponse);
            return loginResponse;
        }).doOnError(Timber::w);
    }

    @Override
    public Single<LoginResponse> signUp(SignUpRequest signUpRequest) {
        return apiServices.register(signUpRequest)
                .map(loginResponse -> {
                    checkApiError(loginResponse);
                    return loginResponse;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<String> forgotPassword(String email) {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.email = email;
        return apiServices.forgotPassword(request).map(response -> {
            checkApiError(response);
            return response.message;
        }).doOnError(Timber::w);
    }

    @Override
    public Single<UserDetails> getUserDetails(String userId) {
        UserDetailsRequest request = new UserDetailsRequest();
        request.userId = userId;
        return apiServices.getUserDetails(request)
                .map(response -> {
            checkApiError(response);
            return response.userDetails;
        }).doOnError(Timber::w);
    }

    @Override
    public Single<List<EnrolledEvent>> getEventsHistory(String userId) {
        EnrolledEventRequest request = new EnrolledEventRequest();
        request.userId = userId;
        request.appCode = appEngine.isEvApp() ? 0 : 1;
        return apiServices.getUserEventHistory(request)
                .map(response -> {
                    checkApiError(response);
                    return response.eventList;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<List<CreditHistory>> getCreditHistory(String userId) {
        CreditHistoryRequest request = new CreditHistoryRequest();
        request.userId = userId;
        return apiServices.getCreditHistory(request)
                .map(response -> {
                    checkApiError(response);
                    return response.creditHistoryList;
                }).doOnError(Timber::w);

    }

    @Override
    public Single<List<NotificationType>> getNotificationTypes(String userId) {
        NotificationTypesRequest request = new NotificationTypesRequest();
        request.userId = userId;
        return apiServices.getNotificationTypes(request)
                .map(response -> {
                    checkApiError(response);
                    return response.notificationList;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<String> changePassword(String old, String newPwd, String userId) {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.userId = userId;
        request.passwordRequest = new PasswordChangeRequest.PasswordRequest();
        request.passwordRequest.confirmPassword = newPwd;
        request.passwordRequest.newPassword = newPwd;
        request.passwordRequest.oldPassword = old;
        return apiServices.changePassword(request)
                .map(response -> {
                    checkApiError(response);
                    return response.message;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<List<Membership>> getAvailableMembershipList() {
        return apiServices.getAvailableMemberships()
                .map(response -> {
                    checkApiError(response);
                    for(Membership membership : response.membershipList){
                        membership.setSeason(response.getSeason());
                    }
                    return response.membershipList;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<MrlMessage> getMrlMessage() {
        return apiServices.getMRLMessage()
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }


    @Override
    public Single<UserMembership> getCurrentUserMembership(String userId) {
        UserMembershipRequest request = new UserMembershipRequest();
        request.userId = userId;
        return apiServices.getCurrentMembership(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<MembershipDetail> getMembershipDetail(String slug) {
        MembershipDetailsRequest request = new MembershipDetailsRequest();
        request.slug = slug;

        return apiServices.getMembershipDetails(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }

    @Override
    public Completable updateNotificationPreferences(List<NotificationType> notificationTypes, String userId) {

        NotificationSyncRequest request = new NotificationSyncRequest();
        request.userId = userId;
        for (NotificationType type : notificationTypes) {
            NotificationPreference preference = new NotificationPreference();
            preference.notificationId = type.notificationId;
            preference.notificationStatus = type.notificationStatus;
            preference.notificationType = type.notificationType;

            request.notificationPreferenceList.add(preference);
        }

        return apiServices.updateNotificationPreference(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Completable updateProfile(UserDetails userDetails) {
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
        ProfileUpdateRequest.ProfileRequest updateRequest = profileUpdateRequest.request;

        profileUpdateRequest.userId = userDetails.userId;
        updateRequest.dateOfBirth = userDetails.dateOfBirth;

        updateRequest.emergencyFirstName = userDetails.emergencyFirstName;
        updateRequest.emergencyLastName = userDetails.emergencyLastName;
        updateRequest.emergencyPhone = userDetails.emergencyPhone;
        updateRequest.emergencyRelationship = userDetails.emergencyRelationship;

        updateRequest.firstName = userDetails.firstName;
        updateRequest.lastName = userDetails.lastName;
        updateRequest.phone = userDetails.phone;

        updateRequest.gender = userDetails.gender;

        updateRequest.everBeenTrack = userDetails.everBeenTrack;

        updateRequest.motorcycle = userDetails.motorcycle;
        updateRequest.motorcycleNumber = userDetails.motorcycleNumber;

        updateRequest.raceLicence = userDetails.raceLicence;

        updateRequest.motoRaceNo = userDetails.motoRaceNo;
        updateRequest.motoTeamMates = userDetails.motoTeamMates;
        updateRequest.motoSponsors = userDetails.motoSponsors;
        updateRequest.motoAsraNo = userDetails.motoAsraNo;
        updateRequest.motoCCSNo = userDetails.motoCCSNo;
        updateRequest.motoAmaExpiryDate = userDetails.motoAmaExpiryDate;
        updateRequest.motoAmaNo = userDetails.motoAmaNo;
        updateRequest.nationality = userDetails.nationality;

        return apiServices.updateProfile(profileUpdateRequest)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }



    @Override
    public Completable updateProfileImage(String imagePath, String userId) {

        File imageFile = new File(imagePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("myFile", imageFile.getName(), requestFile);

        RequestBody userIdRequest = convertIntoRequestBody(userId);

        return apiServices.updateProfileImage(userIdRequest, body)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Completable savePassport(String imagePath, String signature, String userId, String eventId) {
        File imageFile = new File(imagePath);
        RequestBody requestFile =
                RequestBody.create(imageFile, MediaType.parse("multipart/form-data"));

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("myFile", imageFile.getName(), requestFile);

        RequestBody userIdRequest = convertIntoRequestBody(userId);
        RequestBody eventIdRequest = convertIntoRequestBody(eventId);
        RequestBody signatureRequest = convertIntoRequestBody(signature);
        RequestBody agreeRequest = convertIntoRequestBody("1");

        return apiServices.savePassport(userIdRequest, eventIdRequest, agreeRequest, signatureRequest, body)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Completable updateBillingAddress(Address address, String userId) {
        BillingAddressUpdateRequest request = new BillingAddressUpdateRequest();
        request.userId = userId;
        BillingAddressUpdateRequest.BillingAddressRequest addressRequest = request.addressRequest;
        addressRequest.firstName = address.firstName;
        addressRequest.lastName = address.lastName;
        addressRequest.company = address.company;
        addressRequest.country = address.country;//addressTilCountry.getEditText().getText().toString();
        addressRequest.address1 = address.address1;
        addressRequest.address2 = address.address2;
        addressRequest.city = address.city;
        addressRequest.state = address.state;
        addressRequest.postalCode = address.postalCode;
        addressRequest.email = address.email;
        addressRequest.phone = address.phone;

        request.addressRequest = addressRequest;

        return apiServices.updateBillingAddress(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Completable updateMailingAddress(Address address, String userId) {
        ShippingAddressUpdateRequest request = new ShippingAddressUpdateRequest();
        request.userId = userId;
        ShippingAddressUpdateRequest.ShippingAddressRequest addressRequest = request.addressRequest;
        addressRequest.firstName = address.firstName;
        addressRequest.lastName = address.lastName;
        addressRequest.company = address.company;
        addressRequest.country = address.country;//addressTilCountry.getEditText().getText().toString();
        addressRequest.address1 = address.address1;
        addressRequest.address2 = address.address2;
        addressRequest.city = address.city;
        addressRequest.state = address.state;
        addressRequest.postalCode = address.postalCode;
        addressRequest.email = address.email;
        addressRequest.phone = address.phone;

        request.addressRequest = addressRequest;

        return apiServices.updateShippingAddress(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Single<List<Country>> getSupportedCountries() {
        return apiServices.getSupportedCountries()
                .map(response -> {
                    checkApiError(response);
                    return response.countryList;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<List<State>> getStates(String countryId) {
        StateRequest request = new StateRequest();
        request.countryCode = countryId;
        return apiServices.getStateListOfCountry(request)
                .map(response -> {
                    checkApiError(response);
                    return response.stateList;
                }).doOnError(Timber::w);
    }

    @Override
    public Completable cancelEvent(String orderItemId, String skillLevel, String userId) {
        EventCancelRequest request = new EventCancelRequest();
        request.orderItemId = orderItemId;
        request.skillLevel = skillLevel;
        request.userId = userId;

        return apiServices.cancelEvent(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Completable updateDeviceToken(String userId, String token, String deviceType) {

        DeviceTokenRequest request = new DeviceTokenRequest();
        request.userId = userId;
        request.token = token;
        request.deviceType = deviceType;

        return apiServices.updateDeviceToken(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Single<AppVersionStatus> checkForUpdate() {
        return apiServices.checkForUpdate()
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }

    @Override
    public Completable sendReferralInvitation(String userId, String email) {
        ReferralRequest request = new ReferralRequest();
        request.userId = userId;
        request.email = email;

        return apiServices.sendReferralInvitation(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Single<PolicyAgreement> checkForPolicyAgreementStatus(String userId) {

        PolicyAgreementCheckRequest request = new PolicyAgreementCheckRequest();
        request.setSerial(userId);
        return apiServices.checkForPolicyAgreementStatus(request)
                .map(response -> {
                    checkApiError(response);
                    return response.agreement;
                }).doOnError(Timber::w);
    }

    @Override
    public Completable updatePolicyAgreementStatus(String userId, boolean agreed) {
        PolicyAgreementUpdateRequest request = new PolicyAgreementUpdateRequest();
        request.userId = userId;
        request.hasAgreed = agreed;

        return apiServices.updatePolicyAgreementStatus(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Single<List<WaiverEvent>> getWaiverEvents() {
        return apiServices.getWaiverEvents()
                .map(response -> {
                    checkApiError(response);
                    return response.waiverEvents;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<WaiverDetailsResponse> getWaiverDetails(String userId, String waiverEventId) {

        WaiverDetailsRequest request = new WaiverDetailsRequest();
        request.userId = userId;
        request.eventId = waiverEventId;
        return apiServices.getWaiverDetails(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w);
    }

    @Override
    public Completable saveWaiverSignature(WaiverSaveRequest request) {

        return apiServices.saveWaiverSignature(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Completable transferCredit(TransferCreditRequest request) {
        return apiServices.transferCredit(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }

    @Override
    public Single<PassportInfo> viewPassport(String passportId) {
        ViewPassportRequest request = new ViewPassportRequest(passportId);
        return apiServices.getPassport(request)
                .map(response -> {
                    checkApiError(response);
                    return response.passportInfo;
                }).doOnError(Timber::w);
    }

    @Override
    public Single<StampPassword> viewStampPassport(String passportId) {
        ViewPassportRequest request = new ViewPassportRequest(passportId);

        return apiServices.getStampPassport(request)
                .map(response -> {
                   checkApiError(response);
                   return  response;
                }).doOnError(Timber::w);
    }

    @Override
    public Completable updateEmergencyContact(EmergencyContactRequest request) {
        return apiServices.updateEmergencyContact(request)
                .map(response -> {
                    checkApiError(response);
                    return response;
                }).doOnError(Timber::w)
                .ignoreElement();
    }
}
