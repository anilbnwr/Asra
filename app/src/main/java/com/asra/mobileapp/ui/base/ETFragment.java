package com.asra.mobileapp.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.FirebaseAnalyticsHelper;
import com.asra.mobileapp.common.ItemOffsetDecoration;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.ThemeHelper;
import com.asra.mobileapp.common.ToastHelper;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.dialog.ETConfirmationDialog;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.services.PnConstants;
import com.asra.mobileapp.util.StringUtils;
import com.google.gson.Gson;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public abstract class ETFragment<V extends BaseViewModel, D extends ViewDataBinding> extends
        Fragment {


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    public ThemeHelper themeHelper;

    protected V viewModel;

    protected D dataBinding;

    protected Gson gson;

    protected FirebaseAnalyticsHelper analyticsHelper;

    protected abstract Class<V> getViewModel();

    @LayoutRes
    protected abstract int getLayoutRes();


    public ItemOffsetDecoration itemDecoration;
    public ItemOffsetDecoration itemGridDecoration;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(PnConstants.ACTION_NOTIFICATION)) {
                shouldOpenNotification(intent);
            }
        }
    };

    private void shouldOpenNotification(Intent intent) {
        String body = intent.getStringExtra(PnConstants.DATA_PUSH_BODY);
        if(!StringUtils.isEmpty(body)) {
            ETConfirmationDialog<Intent> dialog = new ETConfirmationDialog<>(getActivity(),
                    new ETConfirmationDialog.ConfirmationListener<Intent>() {
                        @Override
                        public void onConfirmed(Intent passthrough) {
                            super.onConfirmed(passthrough);
                            getBaseActivity().handlePushNotification(intent);
                        }
                    }, intent);
            dialog.setDialogTitle("New Notification");
            dialog.setDialogMessage(body);
            dialog.setSecondaryMessage("Would you like to open this?");
            dialog.show();
        }else{
            Timber.e("PUSH - PN received. Body was null");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        AndroidSupportInjection.inject(this);

        setHasOptionsMenu(isToolbarMenuEnabled());
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(getViewModel());

        analyticsHelper = FirebaseAnalyticsHelper.getInstance(getContext());

        gson = new Gson();

        itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.padding_list_item, false);
        itemGridDecoration = new ItemOffsetDecoration(getContext(), R.dimen.padding_list_item, true);


    }


    public boolean isToolbarMenuEnabled() {
        return false;
    }


    public String getTitle() {
        return "";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);

        if (dataBinding != null) {
            return dataBinding.getRoot();
        } else {
            return inflater.inflate(getLayoutRes(), container, false);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (!TextUtils.isEmpty(getTitle())) {
            getBaseActivity().updateTitle(getTitle());
        }

        showEmptyMessage(false, null);


        initializeViews();
        updateAppTheme(isEvApp());

        viewModel.onViewStarted();
        new BaseObserver(viewModel).observeLiveData(this);
        observeEventsFromViewModel();

    }

    public abstract void initializeViews();

    public abstract void observeEventsFromViewModel();


    public void loadFragment(ETFragment fragment) {
        if (getActivity() instanceof FragmentNavigatorActivity) {
            ((FragmentNavigatorActivity) getActivity()).loadFragmentToTab(fragment);
        } else {
            ((BaseActivity) Objects.requireNonNull(getActivity()))
                    .loadFragment(fragment, R.id.fragment_container);
        }
    }

    public void showToolbar() {
        getBaseActivity().showToolbar(true);
    }

    public void showBackButton() {
        getBaseActivity().showBackButton();
    }

    public void hideBackButton() {
        getBaseActivity().hideBackButton();
    }

    public void hideToolbar() {
        getBaseActivity().showToolbar(false);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public String getConfigString(String key) {
        return MessageProvider.getInstance().getText(key);
    }

    public void showHomeIcon(boolean show) {
        getBaseActivity().showHomeIcon(false);
    }

    public final void updateAppTheme(boolean isEvApp) {
        if (isEvApp) {
            getBaseActivity().updateToEvAppTheme();
            updateToEvAppTheme();
        } else {
            getBaseActivity().updateToMotoAppTheme();
            updateToMotoAppTheme();
        }
    }


    public void updateToEvAppTheme() {

    }

    public void updateToMotoAppTheme() {

    }

    public void showEmptyMessage(boolean show, String message) {
        getBaseActivity().showEmptyMessage(show, message);
    }


    public void finish() {
        Objects.requireNonNull(getActivity()).finish();
    }

    public void launchGuestDashboard() {
        getBaseActivity().launchGuestDashboard();
    }


    public void launchUserDashboard() {
        getBaseActivity().launchUserDashboard(true);
    }

    public void switchToUserDashboard() {
        getBaseActivity().launchUserDashboard(false);
    }


    public void launchAdminDashboard() {
        getBaseActivity().launchAdminDashboard(true);
    }

    public void switchToAdminDashboard() {
        getBaseActivity().launchAdminDashboard(false);

    }


    public void launchLoginActivity() {
        getBaseActivity().launchLoginActivity();
    }

    public int getColor(int resourceId) {
        return UiUtils.getColorFromResource(getContext(), resourceId);
    }

    public void showLoginMenu(boolean show) {
        getBaseActivity().showLoginMenu(show);
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    public StaggeredGridLayoutManager getGridLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    }

    public void switchModule(Boolean isAdmin) {

    }

    private void showInfoToast(String message) {
        ToastHelper.getInstance().showInfo(getActivity(), message);
    }

    public void showErrorToast(String message) {
        ToastHelper.getInstance().showError(getActivity(), message);
    }

    public void updateTitle() {
        if (!TextUtils.isEmpty(getTitle())) {
            getBaseActivity().updateTitle(getTitle());
        }
    }

    public void showSuccessToast(String message) {
        ToastHelper.getInstance().showSuccess(getActivity(), message);
    }

    public void showSuccessMessage(String message) {
        getBaseActivity().showSuccessInfo(message);

    }

    public void showErrorMessage(String message) {
        getBaseActivity().showErrorMessage(message);
    }


    public void showSimpleDialog(String title, String message, String positiveButton) {
        ETDialog dialog = new ETDialog(getActivity(), null);
        dialog.setDialogBtnPositive(positiveButton);
        dialog.setDialogMessage(message);
        dialog.setDialogTitle(title);

        dialog.show();
    }

    public void showDialog(String message, String button, ETDialog.DialogListener listener) {
        ETDialog dialog = new ETDialog(getActivity(), listener);
        dialog.setDialogBtnPositive(button);
        dialog.setDialogMessage(message);

        dialog.show();


    }

    public void showSimpleAlert(String title, String message, String positiveBtn, ETDialog.DialogListener listener){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveBtn, (dialog, which) -> {
            dialog.dismiss();
            if(listener != null){
                listener.onPositiveButtonClicked();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public void showSimpleAlert(String title, String message, ETDialog.DialogListener listener){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.et_ok), (dialog, which) -> {
            dialog.dismiss();
            if(listener != null){
                listener.onPositiveButtonClicked();
            }
        });



        alertDialog.show();
    }
    public void showErrorDialog(String title, String message, String button, ETDialog.DialogListener listener) {
        ETDialog dialog = new ETDialog(getActivity(), listener);
        dialog.setTitle(title);
        dialog.setDialogBtnPositive(button);
        dialog.setDialogMessage(message);
        dialog.setErrorMode();
        dialog.show();


    }

    public void popFragment() {
        getBaseActivity().popFragment();
    }

    public boolean isEvApp() {
        return ETApplication.getInstance().isEvApp();
    }

    public void switchToCart() {
        getBaseActivity().switchToCart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
        updateAppTheme(isEvApp());
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter(PnConstants.ACTION_NOTIFICATION));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    public int getPrimaryColor() {
        return UiUtils.getPrimaryColor(getContext());
    }

    public void exitScreen(Boolean exit) {
        if (exit)
            getBaseActivity().popFragment();
    }

    public void invalidateUi() {
        initializeViews();
        viewModel.invalidate();
    }

    public void onAppModeSwitched() {
        getBaseActivity().onAppModeSwitched();
        updateAppTheme(isEvApp());
    }

    public void openWebPage(String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    public void onUserLoggedIn(User user) {
        getBaseActivity().onUserLoggedIn(user);
    }

    public void onLoginRequested(Boolean aBoolean) {
        launchLoginActivity();
    }

    public void showDialog(DialogFragment dialog, DialogInterface.OnDismissListener listener){
        getBaseActivity().showDialogFragment(dialog, listener);
    }


}