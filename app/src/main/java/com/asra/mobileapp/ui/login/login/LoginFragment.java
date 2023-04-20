package com.asra.mobileapp.ui.login.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.FragmentLoginBinding;
import com.asra.mobileapp.ui.base.ActivityViewModel;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.login.LoginActivity;
import com.asra.mobileapp.ui.login.regisrtation.RegistrationStep1Fragment;
import com.asra.mobileapp.ui.login.resetpassword.ForgotPasswordFragment;

import androidx.annotation.Nullable;

public class LoginFragment extends ETFragment<LoginViewModel, FragmentLoginBinding> {


    private boolean guestEnabled = true;
    private static final String KEY_SKIP_GUEST = "login.skip.guest";

    ActivityViewModel activityViewModel;

    public static LoginFragment newInstance(){
        return newInstance(true);
    }

    public static LoginFragment newInstance(boolean skipGuest){
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_SKIP_GUEST, skipGuest);
        LoginFragment fragment =  new LoginFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        guestEnabled = getArguments().getBoolean(KEY_SKIP_GUEST, true);
    }

    @Override
    protected Class<LoginViewModel> getViewModel() {
        return LoginViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_login;
    }

    @Override
    public void initializeViews() {

        dataBinding.loginTvSkip.setOnClickListener(v -> launchGuestDashboard());

        dataBinding.loginForgotPassword.setOnClickListener(v -> launchResetPassword());

        dataBinding.loginBtnSubmit.setOnClickListener(v -> onLogin());

        if(!(getActivity() instanceof LoginActivity)){
            dataBinding.loginTvSkip.setVisibility(View.GONE);
            showBackButton();
            showLoginMenu(false);
        }

        dataBinding.loginTvSkip.setVisibility(guestEnabled ? View.VISIBLE : View.GONE);
        dataBinding.txtSignUp.setOnClickListener(view -> {
            loadFragment(RegistrationStep1Fragment.newInstance());
        });
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_login);
    }

    private void onLogin() {
        String username = dataBinding.loginTilEmail.getEditText().getText().toString();
        String password = dataBinding.loginTilPassword.getEditText().getText().toString();
        viewModel.doLogin(username, password);
    }

    private void launchResetPassword() {
        loadFragment(ForgotPasswordFragment.newInstance());
    }


    @Override
    public void observeEventsFromViewModel() {


        viewModel.loginError.observe(this, errorMessage ->{
            if(!TextUtils.isEmpty(errorMessage)){
                dataBinding.loginTvErrorView.setText(errorMessage);
                dataBinding.loginTvErrorView.setVisibility(View.VISIBLE);
            }else{
                dataBinding.loginTvErrorView.setVisibility(View.GONE);
            }
        });

        viewModel.enableGuestObservable.observe(this, enable ->{
                if(guestEnabled) {
                    dataBinding.loginTvSkip.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
                }else  dataBinding.loginTvSkip.setVisibility(View.INVISIBLE);

        });
        viewModel.loginSuccessObservable.observe(this, this::onUserLoggedIn);
    }

    public void updateToEvAppTheme() {
        dataBinding.loginIvLogo.setImageResource(R.drawable.splash_logoo);
        dataBinding.loginBtnSubmit.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.txtSignUp.setTextColor(UiUtils.getColorFromResource(getContext(), R.color.colorPrimary));
    }

    public void updateToMotoAppTheme() {
        dataBinding.loginIvLogo.setImageResource(R.drawable.moto_logo);
        dataBinding.loginBtnSubmit.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.txtSignUp.setTextColor(UiUtils.getColorFromResource(getContext(), R.color.moto_primary));

    }


}
