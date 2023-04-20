package com.asra.mobileapp.network.adapter.apiservices;

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
import com.asra.mobileapp.network.retrofit.request.EmergencyContactRequest;
import com.asra.mobileapp.network.retrofit.request.LoginRequest;
import com.asra.mobileapp.network.retrofit.request.SignUpRequest;
import com.asra.mobileapp.network.retrofit.request.TransferCreditRequest;
import com.asra.mobileapp.network.retrofit.request.WaiverSaveRequest;
import com.asra.mobileapp.network.retrofit.response.LoginResponse;
import com.asra.mobileapp.network.retrofit.response.WaiverDetailsResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserApiServices {

    Single<LoginResponse> login(LoginRequest loginRequest);

    Single<LoginResponse> signUp(SignUpRequest signUpRequest);

    Single<String> forgotPassword(String email);

    Single<UserDetails> getUserDetails(String userId);

    Single<List<EnrolledEvent>> getEventsHistory(String userId);

    Single<List<CreditHistory>> getCreditHistory(String userId);

    Single<List<NotificationType>> getNotificationTypes(String userId);

    Single<String> changePassword(String old, String newPwd, String userId);

    Single<List<Membership>> getAvailableMembershipList();

    Single<MrlMessage> getMrlMessage();

    Single<UserMembership> getCurrentUserMembership(String userId);

    Single<MembershipDetail> getMembershipDetail(String slug);

    Completable updateNotificationPreferences(List<NotificationType> notificationTypes, String userId);

    Completable updateProfile(UserDetails userDetails);

    Completable updateProfileImage(String imagePath, String userId);

    Completable savePassport(String imagePath, String signature, String userId, String eventId);

    Completable updateBillingAddress(Address address, String userId);

    Completable updateMailingAddress(Address address, String userId);

    Single<List<Country>> getSupportedCountries();

    Single<List<State>> getStates(String countryId);

    Completable cancelEvent(String orderItemId, String skillLevel, String userId);

    Completable updateDeviceToken(String userId, String token, String deviceType);

    Single<AppVersionStatus> checkForUpdate();

    Completable sendReferralInvitation(String userId, String email);

    Single<PolicyAgreement> checkForPolicyAgreementStatus(String userId);

    Completable updatePolicyAgreementStatus(String userId, boolean agreed);

    Single<List<WaiverEvent>> getWaiverEvents();

    Single<WaiverDetailsResponse> getWaiverDetails(String userId, String waiverEventId);

    Completable saveWaiverSignature(WaiverSaveRequest request);

    Completable transferCredit(TransferCreditRequest request);

    Single<PassportInfo> viewPassport(String passportId);
    Single<StampPassword> viewStampPassport(String passportId);

    Completable updateEmergencyContact(EmergencyContactRequest request);
}
