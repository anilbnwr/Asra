package com.asra.mobileapp.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.google.android.material.textfield.TextInputLayout;

import timber.log.Timber;

public class ReferAFriendDialog extends Dialog {


    private DialogListener dialogListener;




    public ReferAFriendDialog(Activity activity, DialogListener dialogListener){
        super(activity);
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_refer_friend);
        setDialogMargin();

        TextView btnPositive = findViewById(R.id.dialog_btn_positive);

        TextView btnNegative = findViewById(R.id.dialog_btn_negative);

        btnPositive.setOnClickListener((v)->{
            if(dialogListener != null){

                TextInputLayout emailInput = findViewById(R.id.textInput);
                String email = emailInput.getEditText().getText().toString();

                if(!UiUtils.isValidEmail(email)){
                    emailInput.setError(getContext().getString(R.string.error_invalid_email));
                }else {

                    emailInput.setError("");
                    dialogListener.onSend(email);
                    dismiss();
                }
            }else{
                dismiss();
            }

        });
        btnNegative.setOnClickListener(view -> {
            dismiss();
        });


        int btnDrawable = ETApplication.getInstance().isEvApp() ?
                R.drawable.selector_button_primary : R.drawable.selector_button_moto_primary;
        btnPositive.setBackgroundResource(btnDrawable);


    }

    private void setDialogMargin() {
        try{
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }catch (Exception e){
            Timber.e(e, "Dialog Margin Adjustment failed");
        }
    }

    public static abstract class DialogListener{

        public void onSend(String email){

        }
    }

}
