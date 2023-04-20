package com.asra.mobileapp.ui.login.resetpassword;

import android.text.TextUtils;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.databinding.ForgotPasswordBinding;
import com.asra.mobileapp.ui.base.ETFragment;

import java.util.Objects;

public class ForgotPasswordFragment extends ETFragment<ResetPasswordViewModel, ForgotPasswordBinding> {


    public static ForgotPasswordFragment newInstance(){
        return new ForgotPasswordFragment();
    }



    @Override
    public String getTitle() {
        return getString(R.string.title_reset_password);
    }


    @Override
    protected Class<ResetPasswordViewModel> getViewModel() {
        return ResetPasswordViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.forgot_password;
    }

    @Override
    public void initializeViews() {
        showToolbar();
        showBackButton();

        dataBinding.messageContainer.setVisibility(View.GONE);
        dataBinding.inputContainer.setVisibility(View.VISIBLE);

        dataBinding.userInfoMessageHeader.setText(AppUtils.toTitleCase(
                MessageProvider.getInstance().getText(MessageProvider.password_reset_successfully)));

        dataBinding.messageDescription.setText(getConfigString(MessageProvider.text_description_reset_password));

        dataBinding.btnResetPassword.setOnClickListener((View v)->{
            String email = dataBinding.tilEmail.getEditText().getText().toString();
            viewModel.onPasswordResetRequest(email);
        });

        dataBinding.closeApp.setOnClickListener((View v) ->{
            Objects.requireNonNull(getActivity()).finish();
        });

    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.errorMessageObservable.observe(this, this::setErrorMessage);
        viewModel.passwordResetObservable.observe(this, message ->{
            dataBinding.messageContainer.setVisibility(View.VISIBLE);
            dataBinding.inputContainer.setVisibility(View.GONE);

            dataBinding.userInfoMessage.setText(message);
        });
    }

    private void setErrorMessage(String message){
        if(TextUtils.isEmpty(message)){
            dataBinding.errorMessage.setVisibility(View.GONE);
        }else{
            dataBinding.errorMessage.setText(message);
            dataBinding.errorMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.logo.setImageResource(R.drawable.common_logo_reset_password);
        dataBinding.btnResetPassword.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.successLogo.setImageResource(R.drawable.ic_common_action_success);
        dataBinding.closeApp.setBackgroundResource(R.drawable.selector_button_primary);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.logo.setImageResource(R.drawable.moto_forgot_password);
        dataBinding.btnResetPassword.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.successLogo.setImageResource(R.drawable.ic_moto_action_success);
        dataBinding.closeApp.setBackgroundResource(R.drawable.selector_button_moto_primary);

    }

    @Override
    public void onDetach() {
        hideToolbar();
        super.onDetach();
    }
}
