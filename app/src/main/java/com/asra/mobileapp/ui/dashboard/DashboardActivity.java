package com.asra.mobileapp.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.FirebaseAnalyticsHelper;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.databinding.ActivityDashboardBinding;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.retrofit.DeleteUserRequestModel;
import com.asra.mobileapp.retrofit.DeleteUserResponseModel;
import com.asra.mobileapp.retrofit.MyErrorMessage;
import com.asra.mobileapp.retrofit.RetrofitAPI;
import com.asra.mobileapp.retrofit.RetrofitHelper;
import com.asra.mobileapp.services.PnConstants;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.base.FragmentNavigatorActivity;
import com.asra.mobileapp.ui.base.model.ProgressData;
import com.asra.mobileapp.ui.dashboard.cart.CartFragment;
import com.asra.mobileapp.ui.dashboard.events.EventListFragment;
import com.asra.mobileapp.ui.dashboard.events.eventdetails.EventDetailsFragment;
import com.asra.mobileapp.ui.dashboard.home.HomeFragment;
import com.asra.mobileapp.ui.dashboard.shop.ShopFragment;
import com.asra.mobileapp.ui.general.ETFragmentHostActivity;
import com.asra.mobileapp.ui.login.appselector.FragmentAppSelector;
import com.asra.mobileapp.ui.splash.SplashActivity;
import com.asra.mobileapp.util.SharedPrefKeys;
import com.asra.mobileapp.util.SharedPrefsHelper;
import com.asra.mobileapp.util.StringUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DashboardActivity extends FragmentNavigatorActivity<ActivityDashboardBinding, DashboardViewModel>
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener {

    @Inject
    AppEngine appEngine;

    @Override
    public int getFragmentContainer() {
        return R.id.fragment_container;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void showProgressBar(ProgressData progressData) {
        if (progressData == null || !progressData.show) {
            dataBinding.progressContainer.setVisibility(View.GONE);
        } else {
            dataBinding.progressContainer.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(progressData.progressText)) {
                dataBinding.progressMessage.setText(progressData.progressText);
            } else {
                dataBinding.progressMessage.setText("");
            }
        }
    }

    public void showEmptyMessage(boolean show, String message) {

        dataBinding.emptyMessage.setText(message);

        if (show) {
            dataBinding.progressContainer.setVisibility(View.GONE);
            dataBinding.emptyViewContainer.setVisibility(View.VISIBLE);
        } else {
            dataBinding.progressContainer.setVisibility(View.GONE);
            dataBinding.emptyViewContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getNumberOfRootFragments() {
        return 4;
    }

    @NotNull
    @Override
    public Fragment getRootFragment(int index) {

        switch (index) {
            case UiUtils.TAB_HOME:
                return HomeFragment.newInstance();
            case UiUtils.TAB_EVENTS:
                return EventListFragment.newInstance();
            case UiUtils.TAB_SHOP:
                return ShopFragment.newInstance();
            case UiUtils.TAB_CART:
                return CartFragment.newInstance();
        }
        return FragmentAppSelector.newInstance();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }

        onNewIntent(getIntent());
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        setUpSliderMenu();

        setUpTabs();
    }

    @Override
    public void observeViewModelLiveData() {
        super.observeViewModelLiveData();

        viewModel.logoutObservable.observe(this, loggedOut -> {
            launchLoginActivity();
        });

        viewModel.switchToAdminObserver.observe(this, toAdmin -> {
            launchAdminDashboard(false);
        });

        viewModel.adminLoggedInObservable.observe(this, status ->
                dataBinding.navView.getMenu().findItem(R.id.menu_nav_switch_module).setVisible(true));

        viewModel.userLoggedInObservable.observe(this, status ->
                dataBinding.navView.getMenu().findItem(R.id.menu_nav_switch_module).setVisible(false));

        viewModel.coachLoggedInObservable.observe(this, status ->
                dataBinding.navView.getMenu().findItem(R.id.menu_nav_switch_module).setVisible(true));

    }


    @Override
    protected Class<DashboardViewModel> getViewModel() {
        return DashboardViewModel.class;
    }

    private void setUpSliderMenu() {

        dataBinding.navView.setItemIconTintList(null);

        dataBinding.navView.setCheckedItem(R.id.menu_nav_home);
        dataBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                dataBinding.drawerLayout.closeDrawers();
                handleDrawerEvents(menuItem);
                return true;
            }
        });

    }

    private void setUpTabs() {
        dataBinding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        dataBinding.bottomNavigation.setOnNavigationItemReselectedListener(this);
    }

    private void handleDrawerEvents(@NonNull MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {

            case R.id.menu_nav_home:
                dataBinding.bottomNavigation.setSelectedItemId(R.id.navigation_home);
                switchTab(UiUtils.TAB_HOME);
                break;

            case R.id.menu_nav_past_events:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_PAST_EVENTS);
                startActivity(intent);
                break;

            case R.id.menu_nav_upcoming_events:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_UPCOMING_EVENTS);
                startActivity(intent);
                break;
            case R.id.menu_nav_credit_history:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_CREDIT_HISTORY);
                startActivity(intent);
                break;
            case R.id.menu_nav_settings:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_SETTINGS);
                startActivity(intent);
                break;
            case R.id.menu_nav_help:
//                intent = new Intent(this, ETFragmentHostActivity.class);
//                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_HELP);
//                startActivity(intent);
                break;
            case R.id.menu_nav_privacy:
//                intent = new Intent(this, ETFragmentHostActivity.class);
//                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_PRIVACY);
//                startActivity(intent);
                break;
            case R.id.menu_nav_about_us:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_ABOUT_US);
                startActivity(intent);
                break;
            case R.id.menu_nav_edit_profile:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_PROFILE_EDIT);
                startActivity(intent);
                break;
            case R.id.menu_nav_change_password:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_CHANGE_PASSWORD);
                startActivity(intent);
                break;

            case R.id.menu_nav_membership:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_MEMBERSHIP);
                startActivity(intent);
                break;

            case R.id.menu_nav_e_waiver:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_EWAIVER);
                startActivity(intent);
                break;

            case R.id.menu_nav_transfer_credit:
                intent = new Intent(this, ETFragmentHostActivity.class);
                intent.putExtra(ETFragmentHostActivity.EXTRA_NAV_ID, ETFragmentHostActivity.CODE_TRANSFER_CREDIT);
                startActivity(intent);
                break;

//            case R.id.menu_nav_switch_app:
//                viewModel.onSwitchApp();
//
//                break;
            case R.id.menu_nav_logout:
                viewModel.onLogout();
                break;

            case R.id.menu_nav_delete_account:
                deleteApiCall();

                break;


            case R.id.menu_nav_switch_module:

                viewModel.onSwitchToAdmin();
                break;
        }

    }


    private void deleteApiCall() {

        Log.d(TAG, "deleteApiCall: >> user id yash : "+ appEngine.getUserId());

       // Toast.makeText(this, ""+appEngine.getUserId(), Toast.LENGTH_SHORT).show();
        String token = null;

        String user_id = appEngine.getUserId();

        Log.d(TAG, "deleteApiCall: >> user id yash string : "+ user_id);
        DeleteUserRequestModel deleteUserRequestModel = new DeleteUserRequestModel();
        deleteUserRequestModel.setUserid(Integer.valueOf(appEngine.getUserId()));

        RetrofitHelper retrofitHelper = RetrofitAPI.getRetrofit(token).create(RetrofitHelper.class);
        retrofitHelper.deleteApiPost().enqueue(new Callback<DeleteUserResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<DeleteUserResponseModel> call, @NonNull Response<DeleteUserResponseModel> response) {

                if (response.code() == 200) {
                    if (response.isSuccessful()) {

                        viewModel.onLogout();
                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(intent);
                        finish();
                        finishAffinity();
                        Log.e(TAG, "onResponse deleteUser : 2 " + new Gson().toJson(response.body()));
                        Toast.makeText(getApplicationContext(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String error_message = String.valueOf(new Gson().fromJson(response.errorBody().charStream(), MyErrorMessage.class).getMsg());
                            Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeleteUserResponseModel> call, @NonNull Throwable t) {

            }
        });
    }


    @Override
    public void onAppModeSwitched() {
        super.onAppModeSwitched();
        ETFragment fragment = getCurrentFragment();
        if (fragment != null)
            fragment.invalidateUi();

        clearTabStack(UiUtils.TAB_HOME);
        clearTabStack(UiUtils.TAB_EVENTS);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int tab;

        switch (item.getItemId()) {
            case R.id.navigation_home:

                tab = UiUtils.TAB_HOME;
                switchTab(tab);
                break;

            case R.id.navigation_events:

                tab = UiUtils.TAB_EVENTS;
                switchTab(tab);
                break;

            case R.id.navigation_shop:

                tab = UiUtils.TAB_SHOP;
                switchTab(tab);
                break;

            case R.id.navigation_cart:
                tab = UiUtils.TAB_CART;
                switchTab(tab);
                clearTabStack(tab);
                break;

            case R.id.menu_nav_logout:
                viewModel.onLogout();
                break;


            case R.id.menu_nav_delete_account:

                deleteApiCall();
              //  viewModel.onLogout();
                break;
        }


        return true;
    }


    @Override
    public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
        clearTabStack(getSelectedTab());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (backButtonEnabled) {
                    onBackPressed();
                } else {
                    dataBinding.drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void switchTab(int tab, int tabResourceId) {

        showProgressBar(null);
        dataBinding.bottomNavigation.setSelectedItemId(tabResourceId);
        switchTab(tab);
        FirebaseAnalyticsHelper.getInstance(this).trackScreen(this, "Tab - " + (tab + 1));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String pnType = bundle.getString(PnConstants.DATA_TYPE);
            if (TextUtils.isEmpty(pnType)) return;

            switch (pnType.toLowerCase()) {
                case PnConstants.DATA_TYPE_EVENT:
                    String eventId = bundle.getString(PnConstants.DATA_EVENT_ID);
                    boolean isEvEvent = bundle.getBoolean(PnConstants.DATA_EVENT_TYPE, true);
                    String eventTitle = bundle.getString(PnConstants.DATA_EVENT_TITLE);

                    if (!TextUtils.isEmpty(eventId) &&
                            !TextUtils.isEmpty(eventTitle)) {
                        EventDetailsFragment fragment = EventDetailsFragment.newInstance(eventId, eventTitle, isEvEvent);
                        loadFragmentToTab(fragment);
                        Timber.i("Event Detail fragment loaded on PN click");
                    }
                    break;
                case PnConstants.DATA_TYPE_WEB:
                    String url = bundle.getString(PnConstants.DATA_WEB_URL);
                    Timber.i("URL : %s", url);
                    if (!TextUtils.isEmpty(url)) {
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                    break;
            }

        }
    }


    @Override
    public void switchToCart() {
        switchTab(UiUtils.TAB_CART, R.id.navigation_cart);
    }

    @Override
    public void showTabBar(boolean show) {
        super.showTabBar(show);
        if (dataBinding.bottomNavigation != null)
            dataBinding.bottomNavigation.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        //Tab Icon UI
        Menu menu = dataBinding.bottomNavigation.getMenu();
        MenuItem tabHome = menu.findItem(R.id.navigation_home);
        tabHome.setIcon(R.drawable.moto_tab_home);

        MenuItem tabEvents = menu.findItem(R.id.navigation_events);
        tabEvents.setIcon(R.drawable.moto_tab_events);

        MenuItem tabShop = menu.findItem(R.id.navigation_shop);
        tabShop.setIcon(R.drawable.moto_tab_shop);
        tabShop.setVisible(false);

        MenuItem tabCart = menu.findItem(R.id.navigation_cart);
        tabCart.setIcon(R.drawable.moto_tab_cart);
        tabShop.setVisible(false);


        dataBinding.bottomNavigation.setItemIconTintList(
                ContextCompat.getColorStateList(this, R.color.tab_evolve_icon_color));

        dataBinding.bottomNavigation.setItemTextColor(
                ContextCompat.getColorStateList(this, R.color.tab_evolve_color));

        dataBinding.bottomNavigation.setBackgroundColor(getColor(R.color.colorPrimaryDark));


        //End of Tab icon UI

        dataBinding.navView.setItemTextColor(getColorStateList(R.color.colorNavText));

        Menu drawerMenu = dataBinding.navView.getMenu();

        MenuItem menuHome = drawerMenu.findItem(R.id.menu_nav_home);
        menuHome.setIcon(R.drawable.homenew);

        MenuItem menuSettings = drawerMenu.findItem(R.id.menu_nav_settings);
        menuSettings.setIcon(R.drawable.settingsnew);

        MenuItem menuAboutUs = drawerMenu.findItem(R.id.menu_nav_about_us);
        menuAboutUs.setIcon(R.drawable.aboutnew);

        MenuItem menuLogout = drawerMenu.findItem(R.id.menu_nav_logout);
        menuLogout.setIcon(R.drawable.logoutnew);

        MenuItem menuDeleteAcc = drawerMenu.findItem(R.id.menu_nav_delete_account);
        menuDeleteAcc.setIcon(R.drawable.deleteacc);

        MenuItem pastEvent = drawerMenu.findItem(R.id.menu_nav_past_events);
        pastEvent.setIcon(R.drawable.past_events_new);

        MenuItem upComingEvents = drawerMenu.findItem(R.id.menu_nav_upcoming_events);
        upComingEvents.setIcon(R.drawable.upcoming_events_new);

        MenuItem profile = drawerMenu.findItem(R.id.menu_nav_edit_profile);
        profile.setIcon(R.drawable.profilenew);

        MenuItem creditHistory = drawerMenu.findItem(R.id.menu_nav_credit_history);
        creditHistory.setIcon(R.drawable.creditnew);

        MenuItem membership = drawerMenu.findItem(R.id.menu_nav_membership);
        membership.setIcon(R.drawable.membershipnew);

        MenuItem creditTransfer = drawerMenu.findItem(R.id.menu_nav_transfer_credit);
        creditTransfer.setIcon(R.drawable.creditnew);

        MenuItem changePassword = drawerMenu.findItem(R.id.menu_nav_change_password);
        changePassword.setIcon(R.drawable.passwordnew);

        MenuItem switchModule = drawerMenu.findItem(R.id.menu_nav_switch_module);
        switchModule.setIcon(R.drawable.ic_module_switch_blue);

        MenuItem ewaiver = drawerMenu.findItem(R.id.menu_nav_e_waiver);
        ewaiver.setIcon(R.drawable.e_waiver_moto);

    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        Menu menu = dataBinding.bottomNavigation.getMenu();
        MenuItem tabHome = menu.findItem(R.id.navigation_home);
        tabHome.setIcon(R.drawable.moto_tab_home);

        MenuItem tabEvents = menu.findItem(R.id.navigation_events);
        tabEvents.setIcon(R.drawable.moto_tab_events);

        MenuItem tabShop = menu.findItem(R.id.navigation_shop);
        tabShop.setIcon(R.drawable.moto_tab_shop);

        MenuItem tabCart = menu.findItem(R.id.navigation_cart);
        tabCart.setIcon(R.drawable.moto_tab_cart);
        tabCart.setVisible(false);


        dataBinding.bottomNavigation.setItemIconTintList(
                ContextCompat.getColorStateList(this, R.color.tab_moto_icon_color));

        dataBinding.bottomNavigation.setItemTextColor(
                ContextCompat.getColorStateList(this, R.color.tab_moto_color));

        dataBinding.bottomNavigation.setBackgroundColor(getColor(R.color.tab_moto_background));

        dataBinding.navView.setItemTextColor(getColorStateList(R.color.slider_moto_color));

        Menu drawerMenu = dataBinding.navView.getMenu();

        MenuItem menuHome = drawerMenu.findItem(R.id.menu_nav_home);
        menuHome.setIcon(R.drawable.mato_slider_home);

        MenuItem menuSettings = drawerMenu.findItem(R.id.menu_nav_settings);
        menuSettings.setIcon(R.drawable.moto_slider_settings);

        MenuItem menuAboutUs = drawerMenu.findItem(R.id.menu_nav_about_us);
        menuAboutUs.setIcon(R.drawable.mato_slider_about_us);

        MenuItem menuLogout = drawerMenu.findItem(R.id.menu_nav_logout);
        menuLogout.setIcon(R.drawable.mato_slider_logout);

        MenuItem menuDeleteAcc = drawerMenu.findItem(R.id.menu_nav_delete_account);
        menuDeleteAcc.setIcon(R.drawable.deleteacc);

        MenuItem pastEvent = drawerMenu.findItem(R.id.menu_nav_past_events);
        pastEvent.setIcon(R.drawable.moto_past_events);

        MenuItem upComingEvents = drawerMenu.findItem(R.id.menu_nav_upcoming_events);
        upComingEvents.setIcon(R.drawable.moto_upcoming_events);

        MenuItem profile = drawerMenu.findItem(R.id.menu_nav_edit_profile);
        profile.setIcon(R.drawable.moto_slider_profile);

        MenuItem switchModule = drawerMenu.findItem(R.id.menu_nav_switch_module);
        switchModule.setIcon(R.drawable.ic_module_switch_blue);

//        MenuItem switchApp = drawerMenu.findItem(R.id.menu_nav_switch_app);
//        switchApp.setIcon(R.drawable.ic_app_switch_to_ev);

        MenuItem creditHistory = drawerMenu.findItem(R.id.menu_nav_credit_history);
        creditHistory.setIcon(R.drawable.moto_slider_credit_hitory);

        MenuItem membership = drawerMenu.findItem(R.id.menu_nav_membership);
        membership.setIcon(R.drawable.moto_slider_membership);

        MenuItem creditTransfer = drawerMenu.findItem(R.id.menu_nav_transfer_credit);
        creditTransfer.setIcon(R.drawable.ic_credit_transfer_moto);

        MenuItem changePassword = drawerMenu.findItem(R.id.menu_nav_change_password);
        changePassword.setIcon(R.drawable.moto_slider_change_password);

        MenuItem ewaiver = drawerMenu.findItem(R.id.menu_nav_e_waiver);
        ewaiver.setIcon(R.drawable.e_waiver_moto);
    }


    @Override
    protected void onResume() {
        super.onResume();

        ETFragment fragment = getCurrentFragment();
        if (fragment instanceof HomeFragment)
            fragment.invalidateUi();
    }
}
