package com.asra.mobileapp.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.model.EventDetails;
import com.asra.mobileapp.util.StringUtils;

import timber.log.Timber;

public class MRLPurchaseDialog extends Dialog {


    private DialogListener dialogListener;

    private boolean addToCartEnabled = true;
    private String warningMessage = "";

    private EventDetails.MrlData mrlData;
    public MRLPurchaseDialog(Activity activity, DialogListener dialogListener){
        super(activity);
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_mrl_purchase);
        setDialogMargin();

        TextView btnPositive = findViewById(R.id.dialog_btn_positive);

        TextView btnNegative = findViewById(R.id.dialog_btn_negative);

        btnPositive.setOnClickListener((v)->{
            if(dialogListener != null && addToCartEnabled){

                dialogListener.onPurchase(mrlData);
                dismiss();
            }else{
                dismiss();
            }

        });
        btnNegative.setOnClickListener(view -> {
            if(dialogListener != null){
                dialogListener.onDismiss();
            }
            dismiss();
        });


        int btnDrawable = ETApplication.getInstance().isEvApp() ?
                R.drawable.selector_button_primary : R.drawable.selector_button_moto_primary;
        btnPositive.setBackgroundResource(btnDrawable);


        TextView dialogTitle = findViewById(R.id.dialog_title);
        TextView dialogMessage = findViewById(R.id.dialog_message);
        TextView mrlPrice = findViewById(R.id.mrl_price);
        TextView dialogSeason = findViewById(R.id.dialog_season);

        dialogTitle.setText(R.string.title_mrl_validation_failed);
        ScrollView scrollView = findViewById(R.id.warningMessageView);
        if(addToCartEnabled) {

            scrollView.setVisibility(View.GONE);
            btnNegative.setVisibility(View.VISIBLE);
            mrlPrice.setVisibility(View.VISIBLE);
            dialogSeason.setVisibility(View.VISIBLE);
            if (mrlData != null) {
                if (StringUtils.isEmpty(mrlData.messageContent)) {
                    dialogMessage.setText(mrlData.title);

                } else {
                    dialogMessage.setText(Html.fromHtml(mrlData.messageContent, Html.FROM_HTML_MODE_COMPACT));
                }
                mrlPrice.setText(String.format("Price: $%s", mrlData.price));
                dialogSeason.setText(String.format("%s Season", mrlData.season));
            }
        }else{

            scrollView.setVisibility(View.VISIBLE);

            btnPositive.setText(android.R.string.ok);
            btnNegative.setVisibility(View.INVISIBLE);
            mrlPrice.setVisibility(View.GONE);
            dialogSeason.setVisibility(View.GONE);
            TextView warningMessageView = findViewById(R.id.warning_message);
            dialogMessage.setVisibility(View.GONE);
            warningMessageView.setText(Html.fromHtml(warningMessage, Html.FROM_HTML_MODE_COMPACT));

        }

    }

    private void setDialogMargin() {
        try{
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }catch (Exception e){
            Timber.e(e, "Dialog Margin Adjustment failed");
        }
    }

    public void setMrlData(EventDetails.MrlData mrlData) {
        this.mrlData = mrlData;
    }

    public void setAddToCartEnabled(boolean addToCartEnabled) {
        this.addToCartEnabled = addToCartEnabled;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public static abstract class DialogListener{

        public void onPurchase(EventDetails.MrlData mrlData){

        }
        public void onDismiss(){

        }
    }

}
