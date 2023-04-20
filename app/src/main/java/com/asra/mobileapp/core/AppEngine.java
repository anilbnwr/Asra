package com.asra.mobileapp.core;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.Constants;
import com.asra.mobileapp.model.AppVersionStatus;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.Country;
import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.model.UserMembership;
import com.asra.mobileapp.model.WaiverEventDetails;
import com.asra.mobileapp.network.retrofit.request.SignUpRequest;
import com.asra.mobileapp.network.retrofit.response.CoachDutyResponse;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.SharedPrefKeys;
import com.asra.mobileapp.util.SharedPrefsHelper;
import com.asra.mobileapp.util.StringUtils;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class AppEngine {


    private static final int APP_EV = Constants.APP_EVOLVE;
    private static final int APP_MOTO = Constants.APP_MOTO;
    public List<String> generalSkills;
    private User currentUser;
    private UserDetails userDetails;
    private UserMembership currentUserMembership;
    private int selectedApp = -1;
    private Gson gson;
    private double walletBalance = 0;
    private boolean walletEnabled = false;
    private List<Country> countries;
    private List<CartItem> cartItems;
    private AppVersionStatus appVersionStatus;
    private SignUpRequest signUpRequest;
    private EnrolledEvent enrolledEvent;


    private CoachDutyResponse.DutyTypes coachDutyTypes;
    private WaiverEventDetails.WaiverData waiverData;
    private List<WaiverEventDetails.WaiverState> licenseIssuedState;


    private String authToken;
    private SharedPrefsHelper sharedPrefsHelper;
    private String selfiePath;
    private String trackName = "";

    @Inject
    public AppEngine(SharedPrefsHelper prefsHelper) {
        this.sharedPrefsHelper = prefsHelper;
        gson = new Gson();
        initAppData();
    }

    private void initAppData() {
        authToken = sharedPrefsHelper.get(SharedPrefKeys.KEY_AUTH_TOKEN, null);
        if (!StringUtils.isEmpty(authToken)) {
            String userJson = sharedPrefsHelper.get(SharedPrefKeys.KEY_LOGGED_IN_USER, "");
            if (!StringUtils.isEmpty(userJson)) {
                currentUser = gson.fromJson(userJson, User.class);
            }
        }
        selectedApp = sharedPrefsHelper.get(SharedPrefKeys.KEY_SELECTED_APP, APP_EV);
        ETApplication.getInstance().setSelectedApp(selectedApp);

        Timber.i("App Engine initialized");
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public boolean isUserAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public boolean hasAdminPrevilege(){
        return currentUser != null && currentUser.hasAdminPrevilege;
    }
    public boolean isUserAdminOrCoach() {
        return currentUser != null && (currentUser.isAdmin() || currentUser.isCoach());
    }

    public boolean isUserCoach() {
        return currentUser != null && currentUser.isCoach();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getUserId() {
        if (currentUser != null)
            return currentUser.id;
        else return "user_not_available";
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    public void setThemeEvApp() {
        selectedApp = APP_EV;
        ETApplication.getInstance().setSelectedApp(selectedApp);
        sharedPrefsHelper.put(SharedPrefKeys.KEY_SELECTED_APP, selectedApp);
    }

    public void setThemeMotoApp() {
        selectedApp = APP_MOTO;
        ETApplication.getInstance().setSelectedApp(selectedApp);
        sharedPrefsHelper.put(SharedPrefKeys.KEY_SELECTED_APP, selectedApp);
    }

    public boolean isEvApp() {
        return selectedApp == APP_EV;
    }

    public String getAuthToken() {
        return authToken;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void logout() {
        setAuthToken("");

        currentUser = null;
        userDetails = null;
        currentUserMembership = null;
        walletEnabled = false;
        walletBalance = 0;
        cartItems = null;
        sharedPrefsHelper.put(SharedPrefKeys.KEY_AUTH_TOKEN, "");
        sharedPrefsHelper.put(SharedPrefKeys.KEY_LOGGED_IN_USER, "");
        sharedPrefsHelper.put(SharedPrefKeys.KEY_AGREEMENT_STATUS, false);
        saveUserDetails(null);
    }

    public int getCartCount() {
        if(ListUtils.isEmpty(cartItems)){
            return 0;
        }
        return cartItems.size();
    }

    @Deprecated
    public void setCartCount(int cartCount) {

    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public UserDetails getCachedUserDetails(){
        String savedUser = sharedPrefsHelper.get(SharedPrefKeys.KEY_USER_DETAILS, "");
        if(!StringUtils.isEmpty(savedUser)) {
            this.userDetails = gson.fromJson(savedUser, UserDetails.class);
            return userDetails;
        }else{
            return null;
        }
    }
    public void saveUserDetails(UserDetails userDetails){
        this.userDetails = userDetails;
        if(userDetails != null) {
            currentUser.role = userDetails.role;
            String userDetailsJson = gson.toJson(userDetails);
            sharedPrefsHelper.put(SharedPrefKeys.KEY_USER_DETAILS, userDetailsJson);
        }else{
            sharedPrefsHelper.deleteSavedData(SharedPrefKeys.KEY_USER_DETAILS);
        }
    }

    public void saveWalletBalance(String walletBalance) {
        this.walletBalance = AppUtils.getDouble(walletBalance);
    }

    public void setWalletEnabled(String walletEnabled) {
        this.walletEnabled = AppUtils.isTrue(walletEnabled);
    }

    public String getUserRole() {
        if(isUserLoggedIn())
            return currentUser.role;
        else
            return "Guest";

    }

    public int getVersionCode() {
        return appVersionStatus == null ? AppVersionStatus.VERSION_NOT_AVAILABLE : AppUtils.getInteger(appVersionStatus.versionCode);
    }

    public UserMembership getCurrentUserMembership() {
        return currentUserMembership;
    }

    public void setCurrentUserMembership(UserMembership currentUserMembership) {
        this.currentUserMembership = currentUserMembership;
    }

    public boolean canCancelEvents(){
        return sharedPrefsHelper.get(SharedPrefKeys.KEY_CANCEL_PRIVILEGE, false);
    }

    public void saveCancelEventPrivilege(boolean canCancel){
        sharedPrefsHelper.put(SharedPrefKeys.KEY_CANCEL_PRIVILEGE, canCancel);
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public boolean isWalletEnabled() {
        return walletEnabled;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void saveDeviceToken(String token) {
        sharedPrefsHelper.put(SharedPrefKeys.KEY_DEVICE_TOKEN, token);
    }

    public String getDeviceToken() {
        return sharedPrefsHelper.get(SharedPrefKeys.KEY_DEVICE_TOKEN, "");
    }

    public void saveDeviceTokenSyncStatus(boolean synced) {
        sharedPrefsHelper.put(SharedPrefKeys.KEY_DEVICE_TOKEN_SYNC_STATUS, synced);
    }

    public boolean getDeviceTokenSyncStatus() {
        return sharedPrefsHelper.get(SharedPrefKeys.KEY_DEVICE_TOKEN_SYNC_STATUS, false);
    }

    public void setAppVersionStatus(AppVersionStatus appVersionStatus) {
        this.appVersionStatus = appVersionStatus;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void switchApp() {
        if (isEvApp())
            setThemeMotoApp() ;
        else setThemeEvApp();

    }

    public SignUpRequest getSignUpRequest() {
        return signUpRequest;
    }

    public void setSignUpRequest(SignUpRequest signUpRequest) {
        this.signUpRequest = signUpRequest;
    }

    public WaiverEventDetails.WaiverData getWaiverData() {
        return waiverData;
    }

    public void setWaiverData(WaiverEventDetails.WaiverData waiverData) {
        this.waiverData = waiverData;
    }

    public List<WaiverEventDetails.WaiverState> getLicenseIssuedStates() {
        return licenseIssuedState;
    }

    public void setLicenseIssuedState(List<WaiverEventDetails.WaiverState> licenseIssuedState) {
        this.licenseIssuedState = licenseIssuedState;
    }

    public CoachDutyResponse.DutyTypes getCoachDutyTypes() {
        return coachDutyTypes;
    }

    public void setCoachDutyTypes(CoachDutyResponse.DutyTypes coachDutyTypes) {
        this.coachDutyTypes = coachDutyTypes;
    }

    public EnrolledEvent getEnrolledEvent() {
        return enrolledEvent;
    }

    public void setEnrolledEvent(EnrolledEvent enrolledEvent) {
        this.enrolledEvent = enrolledEvent;
    }

    public void setSelfie(String selfiePath) {
        this.selfiePath = selfiePath;
    }

    public String getSelfiePath() {
        return selfiePath;
    }

    public void setSelfiePath(String selfiePath) {
        this.selfiePath = selfiePath;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }
}
