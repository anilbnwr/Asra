package com.asra.mobileapp.ui.base;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.model.AppVersionStatus;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.ui.admin.AdminActivity;
import com.asra.mobileapp.ui.base.model.ProgressData;
import com.asra.mobileapp.ui.dashboard.DashboardActivity;
import com.asra.mobileapp.ui.guest.GuestActivity;
import com.asra.mobileapp.ui.login.LoginActivity;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import dagger.android.AndroidInjection;
import timber.log.Timber;

public abstract class BaseActivity<D extends ViewDataBinding, V extends ActivityViewModel> extends AppCompatActivity {


    public static final long INFO_LAYOUT_DELAY = 5*1000;
    private Runnable infoLayoutRunnable = () -> {
        View view = getInfoContainer();
        if(view != null) {
            view.setVisibility(View.GONE);
        }
    };

    public V viewModel;

    public abstract View getInfoContainer();

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    protected boolean backButtonEnabled = false;
    @SuppressWarnings("unused")
    public D dataBinding;


    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());

        viewModel = new ViewModelProvider(this, viewModelFactory).get(getViewModel());
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());

        View infoLayout = getInfoContainer();
        if(infoLayout != null) {
            infoLayout.setOnClickListener(v -> infoLayout.setVisibility(View.GONE));
        }

        Toolbar toolbar = getToolbar();
        if(toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionbar = getSupportActionBar();
            if (actionbar != null) {
                actionbar.setDisplayHomeAsUpEnabled(true);
                actionbar.setDisplayShowHomeEnabled(true);
                actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            }
        }


        initializeViews();

        if(viewModel != null){
            new BaseObserver(viewModel).observeLiveData(this);
            viewModel.onViewStarted();
            observeViewModelLiveData();
        }

    }

    public void observeViewModelLiveData() {
        if(viewModel != null){
            viewModel.exitActivityObservable.observe(this, exit -> finish());
            viewModel.optionAppUpdateObservable.observe(this, appVersionStatus -> {
                handleAppUpdate(appVersionStatus, true);
            });
            viewModel.mandatoryAppUpdateObservable.observe(this, appVersionStatus -> {
                handleAppUpdate(appVersionStatus, false);
            });
        }
    }

    public void handleAppUpdate(AppVersionStatus appVersionStatus, boolean optional){
        showAppUpdateLayout(appVersionStatus.displayMessage);
        handleUpdateButtonEvents(appVersionStatus.playStoreURL, optional);
    }

    public void initializeViews() {

    }


    protected abstract Class<V> getViewModel();


    public void updateTitle(String title) {
        ActionBar actionBar = getETActionbar();
        if (actionBar != null && !TextUtils.isEmpty(title)) {
            actionBar.setTitle(title);
        }
    }



    private ActionBar getETActionbar() {
        return getSupportActionBar();
    }



    public void showHomeIcon(boolean show) {
        ActionBar actionBar = getETActionbar();
        if (actionBar != null) {
            backButtonEnabled = false;
            actionBar.setDisplayHomeAsUpEnabled(show);
            actionBar.setDisplayShowHomeEnabled(show);
        }
    }

    public void loadFragment(Fragment fragment, int viewId){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public Toolbar getToolbar() {
        return findViewById(R.id.toolbar);
    }


    public void showToolbar(boolean show) {
        Toolbar toolbar = getToolbar();
        if(toolbar != null){
            toolbar.setVisibility(show? View.VISIBLE : View.GONE);
        }
    }

    public void showBackButton() {

        ActionBar actionBar = getETActionbar();
        if(actionBar != null) {
            backButtonEnabled = true;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }
    }


    public void hideBackButton() {
        //getSupportActionBar().setDisplayShowHomeEnabled(false);
        ActionBar actionBar = getETActionbar();
        if(actionBar != null) {
            backButtonEnabled = false;

            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (backButtonEnabled) {
                    onBackPressed();
                }
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public void updateToEvAppTheme() {
        Toolbar toolbar = getToolbar();
        if(toolbar != null){
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    public void updateToMotoAppTheme() {
        Toolbar toolbar = getToolbar();
        if(toolbar != null){
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_moto_background));
        }
    }

    public void showEmptyMessage(boolean show, String message){

    }
    public abstract void showProgressBar(ProgressData progressData);

    public String getConfigString(String key){
        return  MessageProvider.getInstance().getText(key);
    }

    public void showSuccessInfo(String message){

        View infoLayout = getInfoContainer();
        if(infoLayout != null) {
            infoLayout.setVisibility(View.VISIBLE);

            if(ETApplication.getInstance().isEvApp()) {
                infoLayout.setBackgroundColor(UiUtils.getColorFromResource(this, R.color.colorPrimaryTransparent));
            }else{
                infoLayout.setBackgroundColor(getColor(R.color.moto_primary));
            }
            getMessageBarTextView().setText(message);
            infoLayout.postDelayed(infoLayoutRunnable, INFO_LAYOUT_DELAY);
        }
    }

    public void showErrorMessage(String message){
        View infoLayout = getInfoContainer();
        if(infoLayout != null) {
            infoLayout.setVisibility(View.VISIBLE);
            infoLayout.setBackgroundColor(UiUtils.getColorFromResource(this, R.color.colorRedTransparent));
            getMessageBarTextView().setText(message);
            infoLayout.postDelayed(infoLayoutRunnable, INFO_LAYOUT_DELAY);
        }
    }

    public TextView getMessageBarTextView(){
        return findViewById(R.id.infoMessage);
    }

    public void showMessageBar(boolean show){
        View infoLayout = getInfoContainer();
        if(infoLayout != null) {
            infoLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void showLoginMenu(boolean show){

    }

    public void doLogout(){
        launchLoginActivity();
    }

    public void launchGuestDashboard() {
        Intent intent = new Intent(this, GuestActivity.class);
        startActivity(intent);
        finish();
    }

    public void handlePushNotification(Intent intent){
        onNewIntent(intent);
    }

    public void launchUserDashboard(boolean canFinish) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);

        if(canFinish)
            finish();
    }

    public void launchAdminDashboard(boolean canFinish) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        if(canFinish)
            finish();

    }

    public void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    public void showAppUpdateLayout(String message){
        findViewById(R.id.appUpdateInfoLayout).setVisibility(View.VISIBLE);
        TextView messageView = findViewById(R.id.appUpdateMessage);
        messageView.setText(message);

        TextView messageViewTitle = findViewById(R.id.appUpdateMessageTitle);
        messageViewTitle.setText(getConfigString(MessageProvider.app_update_title));

        boolean isEvApp = ETApplication.getInstance().isEvApp();
        findViewById(R.id.btn_app_update).setBackgroundResource(isEvApp ? R.drawable.selector_button_primary :
                R.drawable.selector_button_moto_primary);


        if(getToolbar() != null){
            getToolbar().setVisibility(View.GONE);
        }
        showTabBar(false);
    }


    public void handleUpdateButtonEvents(final String storeURL, boolean updateOptional){
        View updateButton = findViewById(R.id.btn_app_update);

        updateButton.setOnClickListener((View v)->{
            openPlayStore(storeURL);
        });

        TextView dismissButton = findViewById(R.id.btn_app_update_dismiss);
        String dismissText = updateOptional ? getString(R.string.label_ignore) : getString(R.string.label_exit);
        dismissButton.setText(dismissText);
        dismissButton.setOnClickListener((View dismissBtn)->{
            if(updateOptional){
                findViewById(R.id.appUpdateInfoLayout).setVisibility(View.GONE);
                if(getToolbar() != null){
                    getToolbar().setVisibility(View.VISIBLE);
                    showTabBar(true);
                }
            }else{
                finish();
            }
        });
    }
    private void openPlayStore(String appURL){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appURL)));
            Timber.e(e, "Could not open the app url");
        }
    }

    public void switchToCart(){

    }
    public void showTabBar(boolean show){

    }

    public void popFragment(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTaskDescription();
    }

    public ETDialog showDialog(String message, String button, ETDialog.DialogListener listener) {
        ETDialog dialog = new ETDialog(this, listener);
        dialog.setDialogBtnPositive(button);
        dialog.setDialogMessage(message);

        dialog.show();
        return dialog;


    }
    public void setTaskDescription(){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ev_logo);

        ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription
                (getTitle().toString(), bm, UiUtils.getPrimaryColor(this));
        this.setTaskDescription(tDesc);
        bm.recycle();
    }

    public void onAppModeSwitched() {

    }

    public void showDialogFragment(DialogFragment dialog, DialogInterface.OnDismissListener listener){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        dialog.show(ft, "dialog");

    }

    public void onUserLoggedIn(User user) {
        viewModel.launchDashboard();
    }


    public void requestPermission(String permission, String description){
        if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    showPermissionAlert(permission, description);
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{permission}, 0);
                }

        }
    }

    public void requestPermissions(String[] permissions){
        ActivityCompat.requestPermissions(this,permissions, 0);
    }

    private void showPermissionAlert(String permission, String description) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Permission Required");
        alertDialog.setMessage(description);

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DON'T ALLOW",
                (dialog, which) -> {
                    dialog.dismiss();
               //     finish();
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                (dialog, which) -> {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(BaseActivity.this,
                            new String[]{permission},
                            0);
                });
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
