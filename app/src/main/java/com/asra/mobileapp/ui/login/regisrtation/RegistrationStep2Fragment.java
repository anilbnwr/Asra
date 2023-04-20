package com.asra.mobileapp.ui.login.regisrtation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.FragmentRegistrationStep2Binding;
import com.asra.mobileapp.model.User;
import com.asra.mobileapp.network.retrofit.request.SignUpRequest;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.general.settings.TermsNConditionsFragment;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;

public class RegistrationStep2Fragment
        extends ETFragment<RegistrationViewModel, FragmentRegistrationStep2Binding> {


    SignUpRequest request = new SignUpRequest();


    @Override
    protected Class<RegistrationViewModel> getViewModel() {
        return RegistrationViewModel.class;
    }

    private static final String KEY_SIGN_UP_DATA = "registration_first_step";

    public static RegistrationStep2Fragment newInstance(SignUpRequest signUpRequest) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SIGN_UP_DATA, signUpRequest);
        RegistrationStep2Fragment fragment = new RegistrationStep2Fragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_registration_step2;
    }

    @Override
    public void initializeViews() {
        request = getArguments().getParcelable(KEY_SIGN_UP_DATA);
        dataBinding.btnRegister.setOnClickListener(view -> {
            SignUpRequest request = getValidatedRequest();
            if (request != null) {
                viewModel.register(request);
            }
        });

        Objects.requireNonNull(dataBinding.tilSkillLevel.getEditText()).setOnClickListener((View v) -> {
            showSkillLevelPicker();
        });

        dataBinding.trackYes.setChecked(request.everBeenTrack == 0);
        dataBinding.licenceYes.setChecked(request.raceLicense == 0);
        dataBinding.tilSkillLevel.getEditText().setText(request.skillLevel);

        dataBinding.btnRegister.setEnabled(false);
        dataBinding.cbAgreeTerms.setOnCheckedChangeListener((compoundButton, b) -> dataBinding.btnRegister.setEnabled(b));

        dataBinding.textTermsNConditions.setOnClickListener(view -> loadFragment(TermsNConditionsFragment.newInstance()));
    }

    private void showSkillLevelPicker() {
        String relationArrayJson = getConfigString(MessageProvider.track_yes_skill_levels);
        if(!dataBinding.trackYes.isChecked()){
            relationArrayJson = getConfigString(MessageProvider.track_no_skill_levels);

        }
        List<String> relationsList = gson.fromJson(relationArrayJson, new TypeToken<List<String>>() {
        }.getType());

        Collections.sort(relationsList);
        final String[] arrayOfRelations = relationsList.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Select Skill Level");

        int checkedIndex = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayOfRelations, checkedIndex, (dialog, which) -> {

            UiUtils.hideKeyboard(getActivity());
            dataBinding.tilSkillLevel.getEditText().setText(arrayOfRelations[which]);
            dataBinding.tilSkillLevel.setError(null);
        });

        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void scrollTop() {
        dataBinding.scrollContainer.post(() ->
                dataBinding.scrollContainer.fullScroll(ScrollView.FOCUS_UP));
    }

    private void scrollToView(View v) {
        dataBinding.scrollContainer.post(() ->
                dataBinding.scrollContainer
                        .smoothScrollTo((int) v.getX(), (int) v.getY()));
    }

    private SignUpRequest getValidatedRequest() {


        UiUtils.hideKeyboard(getActivity());
        if (TextUtils.isEmpty(dataBinding.tilSkillLevel.getEditText().getText())) {
            dataBinding.tilSkillLevel.setError(getString(R.string.error_skill_level_empty));
            scrollTop();
            return null;
        } else {
            dataBinding.tilSkillLevel.setError(null);
            request.skillLevel = dataBinding.tilSkillLevel.getEditText().getText().toString();
        }

        String password = dataBinding.tilPassword.getEditText().getText().toString();
        String passwdRegExpn = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9]).{6,20}$";

        Pattern p = Pattern.compile(passwdRegExpn);


        if (!p.matcher(password).matches()) {
            dataBinding.tilPassword.setError(getString(R.string.error_invalid_password));
            scrollToView(dataBinding.tilPassword);
            return null;
        } else {
            dataBinding.tilPassword.setError(null);
            request.password = dataBinding.tilPassword.getEditText().getText().toString();
        }

        if (!password.equalsIgnoreCase(dataBinding.tilConfirmPassword.getEditText().getText().toString())) {
            dataBinding.tilConfirmPassword.setError(getString(R.string.error_password_mismatch));
            scrollToView(dataBinding.tilConfirmPassword);
            return null;
        } else {
            request.confirmPassword = password;
        }

        request.everBeenTrack = dataBinding.trackYes.isChecked() ? 1 : 0;
        request.raceLicense = dataBinding.licenceYes.isChecked() ? 1 : 0;


        return request;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_sign_up);
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.registerSuccessObservable.observe(this, this::onUserCreated);
    }

    public void onUserCreated(User user) {
        getBaseActivity().onUserLoggedIn(user);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.btnRegister.setBackgroundResource(R.drawable.selector_button_primary);

    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.btnRegister.setBackgroundResource(R.drawable.selector_button_moto_primary);

    }
}
