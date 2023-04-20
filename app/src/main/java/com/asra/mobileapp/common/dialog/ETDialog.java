package com.asra.mobileapp.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.util.StringUtils;
import com.google.android.material.textfield.TextInputLayout;

import timber.log.Timber;

public class ETDialog extends Dialog {


    private DialogListener dialogListener;

    private TextView tvTitle;
    private TextView tvMessage;
    private TextView btnPositive;
    private ImageView dialogIcon;
    private TextInputLayout textInputLayout;
    private String textInputHint;
    private String textInputError;

    private String txtTitle;
    private CharSequence txtMessage;
    private View divider;
    private View divider1;

    private boolean inputMode = false;


    private int headerTextColor = R.color.colorTextBlack;
    private int buttonBackgroundResource = R.drawable.selector_button_primary;

    private boolean errorTheme = false;

    public void setDialogTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public void setDialogMessage(CharSequence txtMessage) {
        this.txtMessage = txtMessage;
    }

    public void setDialogBtnPositive(String txtBtnPositive) {
        this.txtBtnPositive = txtBtnPositive;
    }

    private String txtBtnPositive;

    public ETDialog(Activity activity, DialogListener dialogListener){
        super(activity);
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_et_generic);
        setDialogMargin();

        tvTitle = findViewById(R.id.dialog_title);
        tvMessage = findViewById(R.id.dialog_message);
        btnPositive = findViewById(R.id.dialog_btn_positive);
        dialogIcon = findViewById(R.id.dialog_icon);
        divider = findViewById(R.id.dialog_divider);
        divider1 = findViewById(R.id.dialog_divider1);
        textInputLayout = findViewById(R.id.textInput);
        textInputLayout.setHint(textInputHint);

        tvTitle.setText(TextUtils.isEmpty(txtTitle) ?
                getContext().getString(R.string.app_name) : txtTitle);

        tvMessage.setText(txtMessage);

        btnPositive.setText(TextUtils.isEmpty(txtBtnPositive) ?
                getContext().getString(android.R.string.ok) : txtBtnPositive);

        btnPositive.setOnClickListener((v)->{
            if(dialogListener != null){
                if(inputMode){
                    String inputText = textInputLayout.getEditText().getText().toString();
                    if(StringUtils.isEmpty(inputText)){
                        textInputLayout.setError(textInputError);
                    }else{
                        dialogListener.onPositiveButtonClicked(inputText);
                        dismiss();
                    }
                }else {
                    dialogListener.onPositiveButtonClicked();
                    dismiss();
                }
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

        if(errorTheme)
            applyErrorTheme();

        if(inputMode){
            textInputLayout.setVisibility(View.VISIBLE);
            tvMessage.setVisibility(View.GONE);
        }else{
            textInputLayout.setVisibility(View.GONE);
            tvMessage.setVisibility(View.VISIBLE);
        }

    }


    public void setErrorMode(){
        errorTheme = true;
    }

    private void applyErrorTheme(){
        tvTitle.setTextColor(Color.RED);
        btnPositive.setTextColor(Color.WHITE);
        btnPositive.setBackgroundColor(Color.RED);
        dialogIcon.setVisibility(View.VISIBLE);
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

    public void setTextInputHint(String textInputHint) {
        this.textInputHint = textInputHint;
    }

    public void setTextInputError(String textInputError) {
        this.textInputError = textInputError;
    }

    public static abstract class DialogListener{

        public void onPositiveButtonClicked(){

        }

        public void onPositiveButtonClicked(String userInput){

        }
    }

    public void setTitle(String title){
        this.txtTitle = title;
    }
}
