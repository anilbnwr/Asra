package com.asra.mobileapp.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.util.StringUtils;

import timber.log.Timber;

public class ETConfirmationDialog<T> extends Dialog {


    private T passthrough;
    private ConfirmationListener dialogListener;

    private TextView tvTitle;
    private TextView tvMessage;
    private TextView btnPositive;
    private TextView btnNegative;
    private TextView tvSecondaryMessage;

    private String txtTitle;
    private CharSequence txtMessage;
    private String txtBtnPositive;
    private String txtBtnNegative;
    private String txtSecondaryMessage;

    private boolean inputMode = false;


    private int headerTextColor = R.color.colorTextBlack;
    private int buttonBackgroundResource = R.drawable.selector_button_primary;


    public void setDialogTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public void setDialogMessage(CharSequence txtMessage) {
        this.txtMessage = txtMessage;
    }

    public void setDialogBtnPositive(String txtBtnPositive) {
        this.txtBtnPositive = txtBtnPositive;
    }

    public void setDialogBtnNegative(String txtBtnNegative) {
        this.txtBtnNegative = txtBtnNegative;
    }



    public ETConfirmationDialog(Activity activity, ConfirmationListener<T> dialogListener, T passthrough){
        super(activity);
        this.dialogListener = dialogListener;
        this.passthrough = passthrough;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_et_generic);
        setDialogMargin();

        tvTitle = findViewById(R.id.dialog_title);
        tvMessage = findViewById(R.id.dialog_message);
        btnPositive = findViewById(R.id.dialog_btn_positive);

        btnNegative = findViewById(R.id.dialog_btn_negative);
        btnNegative.setVisibility(View.VISIBLE);

        tvSecondaryMessage = findViewById(R.id.dialog_secondary_message);
        findViewById(R.id.dialog_icon).setVisibility(View.GONE);
        View divider = findViewById(R.id.dialog_divider);
        View divider1 = findViewById(R.id.dialog_divider1);
        findViewById(R.id.textInput).setVisibility(View.GONE);

        tvTitle.setText(TextUtils.isEmpty(txtTitle) ?
                getContext().getString(R.string.app_name) : txtTitle);

        tvMessage.setText(txtMessage);

        if(StringUtils.isEmpty(txtSecondaryMessage)){
            tvSecondaryMessage.setVisibility(View.GONE);
        }else{
            tvSecondaryMessage.setVisibility(View.VISIBLE);
            tvSecondaryMessage.setText(txtSecondaryMessage);
        }

        btnPositive.setText(TextUtils.isEmpty(txtBtnPositive) ?
                getContext().getString(android.R.string.ok) : txtBtnPositive);

        btnNegative.setText(TextUtils.isEmpty(txtBtnNegative) ?
                getContext().getString(android.R.string.cancel) : txtBtnNegative);

        btnPositive.setOnClickListener((v)->{
            if(dialogListener != null){
                dialogListener.onConfirmed(passthrough);
                dismiss();
            }else{
                dismiss();
            }

        });
        btnNegative.setOnClickListener(view -> {
            if(dialogListener != null){
                dialogListener.onCancelled(passthrough);
                dismiss();
            }else{
                dismiss();
            }
        });

        tvTitle.setTextColor(UiUtils.getColorFromResource(getContext(), headerTextColor));
        btnPositive.setBackgroundResource(buttonBackgroundResource);
        int drawable = ETApplication.getInstance().isEvApp() ?
                R.drawable.item_divider : R.drawable.item_divider_moto;

        int btnDrawable = ETApplication.getInstance().isEvApp() ?
                R.drawable.selector_button_primary : R.drawable.selector_button_moto_primary;
        btnPositive.setBackgroundResource(btnDrawable);
        divider.setBackgroundResource(drawable);
        divider1.setBackgroundResource(drawable);


    }



    private void setDialogMargin() {
        try{
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }catch (Exception e){
            Timber.e(e, "Dialog Margin Adjustment failed");
        }
    }

    public void setHeaderTextColor(int headerTextColor) {
        this.headerTextColor = headerTextColor;
    }

    public void setButtonBackgroundResource(int buttonBackgroundResource) {
        this.buttonBackgroundResource = buttonBackgroundResource;
    }

    public void setInputMode(boolean inputMode) {
        this.inputMode = inputMode;
    }

    public void setSecondaryMessage(String secondaryMessage) {
        this.txtSecondaryMessage = secondaryMessage;
    }


    public static abstract class ConfirmationListener<T>{

        public void onConfirmed(T passthrough){

        }

        public void onCancelled(T passthrough){

        }
    }

    public void setTitle(String title){
        this.txtTitle = title;
    }
}
