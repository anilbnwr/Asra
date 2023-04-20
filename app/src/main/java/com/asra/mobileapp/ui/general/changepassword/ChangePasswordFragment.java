package com.asra.mobileapp.ui.general.changepassword;

import android.text.TextUtils;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentChangePasswordBinding;
import com.asra.mobileapp.ui.base.ETFragment;

public class ChangePasswordFragment extends ETFragment<ChangePasswordViewModel, FragmentChangePasswordBinding> {
    public static ETFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    protected Class<ChangePasswordViewModel> getViewModel() {
        return ChangePasswordViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_change_password;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_change_password);
    }


    @Override
    public void initializeViews() {
        dataBinding.btnChangePassword.setOnClickListener((View v)->{
            String currentPassword = dataBinding.tilCurrentPassword.getEditText().getText().toString();
            String newPassword = dataBinding.tilNewPassword.getEditText().getText().toString();
            String confirmPassword = dataBinding.tilConfirmPassword.getEditText().getText().toString();


            viewModel.onPasswordChangeRequest(currentPassword, newPassword, confirmPassword);
        });
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.passwordChangeMessageObservable.observe(this, this::showSuccessToast);

        viewModel.validationObservable.observe(this, this::setErrorMessage);
        viewModel.passwordChangeErrorObservable.observe(this, this::setErrorMessage);
    }

    private void setErrorMessage(String message){
        if(TextUtils.isEmpty(message)){
            dataBinding.errorMessage.setVisibility(View.GONE);
        }else{
            dataBinding.errorMessage.setText(message);
        }
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.btnChangePassword.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.logo.setImageResource(R.drawable.moto_change_password);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.btnChangePassword.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.logo.setImageResource(R.drawable.common_forgot_password);
    }
}
