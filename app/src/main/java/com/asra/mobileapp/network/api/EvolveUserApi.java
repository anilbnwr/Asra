package com.asra.mobileapp.network.api;


import com.asra.mobileapp.model.AppVersionStatus;
import com.asra.mobileapp.model.MembershipDetail;
import com.asra.mobileapp.model.MrlMessage;
import com.asra.mobileapp.model.StampPassword;
import com.asra.mobileapp.model.UserMembership;
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
import com.asra.mobileapp.network.retrofit.response.CountryListResponse;
import com.asra.mobileapp.network.retrofit.response.CreditHistoryResponse;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.network.retrofit.response.EventHistoryResponse;
import com.asra.mobileapp.network.retrofit.response.LoginResponse;
import com.asra.mobileapp.network.retrofit.response.MembershipResponse;
import com.asra.mobileapp.network.retrofit.response.NotificationConfigResponse;
import com.asra.mobileapp.network.retrofit.response.PolicyAgreementResponse;
import com.asra.mobileapp.network.retrofit.response.StateListResponse;
import com.asra.mobileapp.network.retrofit.response.UserDetailsResponse;
import com.asra.mobileapp.network.retrofit.response.ViewPassportResponse;
import com.asra.mobileapp.network.retrofit.response.WaiverDetailsResponse;
import com.asra.mobileapp.network.retrofit.response.WaiverListResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface EvolveUserApi  {

    @POST("/evolve-api/public/app/v3/user/auth")
    Single<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/evolve-api/public/app/v3/user/create")
    Single<LoginResponse> register(@Body SignUpRequest signUpRequest);

    @POST("/evolve-api/public/app/v3/user/forgotPassword")
    Single<ETResponse> forgotPassword(@Body ForgotPasswordRequest request);


    @POST("/evolve-api/public/app/v3/user/changePassword")
    Single<ETResponse> changePassword(@Body PasswordChangeRequest request);

    @POST("/evolve-api/public/app/v3/user/details")
    Single<UserDetailsResponse> getUserDetails(@Body UserDetailsRequest userId);

    @POST("/evolve-api/public/app/v3/user/allEvents")
    Single<EventHistoryResponse> getUserEventHistory(@Body EnrolledEventRequest request);

    @POST("/evolve-api/public/app/v3/user/creditHistory")
    Single<CreditHistoryResponse> getCreditHistory(@Body CreditHistoryRequest request);


    //Preference Settings
    @POST("/evolve-api/public/app/v3/notifications/types")
    Single<NotificationConfigResponse> getNotificationTypes(@Body NotificationTypesRequest request);

    @POST("/evolve-api/public/app/v3/notifications/updateNotificationPreference")
    Single<ETResponse> updateNotificationPreference(@Body NotificationSyncRequest request);


    @POST("/evolve-api/public/app/v3/user/cancelEvent")
    Single<ETResponse> cancelEvent(@Body EventCancelRequest eventCancelRequest);

    @POST("/evolve-api/public/app/v3/notifications/updateDeviceToken")
    Single<ETResponse> updateDeviceToken(@Body DeviceTokenRequest request);

    //Profile

    @GET("/evolve-api/public/app/v3/membership/list")
    Single<MembershipResponse> getAvailableMemberships();

    @GET("/evolve-api/public/app/v3/membership/mrlMessage")
    Single<MrlMessage> getMRLMessage();

    @POST("/evolve-api/public/app/v3/membership/details")
    Single<MembershipDetail> getMembershipDetails(@Body MembershipDetailsRequest request);

    @POST("/evolve-api/public/app/v3/membership/userMembership")
    Single<UserMembership> getCurrentMembership(@Body UserMembershipRequest request);

    @POST("/evolve-api/public/app/v3/user/updateProfile")
    Single<ETResponse> updateProfile(@Body ProfileUpdateRequest request);

    @POST("/evolve-api/public/app/v3/user/updateProfile")
    Single<ETResponse> updateBillingAddress(@Body BillingAddressUpdateRequest request);

    @POST("/evolve-api/public/app/v3/user/updateProfile")
    Single<ETResponse> updateShippingAddress(@Body ShippingAddressUpdateRequest request);

    @Multipart
    @POST("/evolve-api/public/app/v3/user/profilepic")
    Single<ETResponse> updateProfileImage(@Part("user_id") RequestBody userId,
                                          @Part MultipartBody.Part image);

    @Multipart
    @POST("/evolve-api/public/app/v3/user/savePassport")
    Single<ETResponse> savePassport(@Part("user_id") RequestBody userId,
                                    @Part("event_id") RequestBody eventId,
                                    @Part("agree") RequestBody agree,
                                    @Part("signature") RequestBody signature,
                                    @Part MultipartBody.Part image);

    @POST("/evolve-api/public/app/v3/user/viewPassport")
    Single<ViewPassportResponse> getPassport(@Body ViewPassportRequest request);


    @POST("/evolve-api/public/app/v3/user/updatePassport")
    Single<StampPassword> getStampPassport(@Body ViewPassportRequest request);
    @GET("/evolve-api/public/app/v3/common/countries")
    Single<CountryListResponse> getSupportedCountries();

    //General

    @POST("/evolve-api/public/app/v3/common/states")
    Single<StateListResponse> getStateListOfCountry(@Body StateRequest stateRequest);


    @GET("/evolve-api/public/app/v3/store/checkForUpdates")
    Single<AppVersionStatus> checkForUpdate();

    @POST("/evolve-api/public/app/v3/user/checkTermsPolicy")
    Single<PolicyAgreementResponse> checkForPolicyAgreementStatus(@Body PolicyAgreementCheckRequest request);


    @POST("/evolve-api/public/app/v3/user/saveTermsPolicy")
    Single<ETResponse> updatePolicyAgreementStatus(@Body PolicyAgreementUpdateRequest request);

    @POST("/evolve-api/public/app/v3/user/referFriend")
    Single<ETResponse> sendReferralInvitation(@Body ReferralRequest request);

    @GET("/evolve-api/public/app/v3/waiverEvents/list")
    Single<WaiverListResponse> getWaiverEvents();

    @POST("/evolve-api/public/app/v3/waiverEvents/details")
    Single<WaiverDetailsResponse> getWaiverDetails(@Body WaiverDetailsRequest request);

    @POST("/evolve-api/public/app/v3/waiverEvents/saveData")
    Single<ETResponse> saveWaiverSignature(@Body WaiverSaveRequest request);

    @POST("/evolve-api/public/app/v3/user/transferCredits ")
    Single<ETResponse> transferCredit(@Body TransferCreditRequest request);

    @POST("/evolve-api/public/app/v3/user/saveEmergencyDetails")
    Single<ETResponse> updateEmergencyContact(@Body EmergencyContactRequest request);


}
