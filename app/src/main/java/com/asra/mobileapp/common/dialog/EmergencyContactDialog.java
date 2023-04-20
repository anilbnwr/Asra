package com.asra.mobileapp.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.DialogEmergencyContactBinding;
import com.asra.mobileapp.model.UserDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import timber.log.Timber;

public class EmergencyContactDialog extends Dialog {


    private final DialogListener dialogListener;


    private UserDetails userDetails;
    DialogEmergencyContactBinding dataBinding;
    public EmergencyContactDialog(Activity activity, DialogListener dialogListener){
        super(activity);
        this.dialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.dialog_emergency_contact);
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_emergency_contact, null, false);
        setContentView(dataBinding.getRoot());
        setDialogMargin();

        dataBinding.btnSave.setOnClickListener((v)->{
            if(dialogListener != null ){
                EmergencyContact contact = validateData();
                if(contact != null) {
                    dialogListener.onSave(contact);
                }
            }
            dismiss();

        });
        Objects.requireNonNull(dataBinding.tilEmergencyRelationship.getEditText()).setOnClickListener((View v) -> {
            showRelationPicker();
        });

        int btnDrawable = ETApplication.getInstance().isEvApp() ?
                R.drawable.selector_button_primary : R.drawable.selector_button_moto_primary;
        dataBinding.btnSave.setBackgroundResource(btnDrawable);


        if(userDetails != null){
            dataBinding.tilEmergencyFirstName.getEditText().setText(userDetails.emergencyFirstName);
            dataBinding.tilEmergencyLastName.getEditText().setText(userDetails.emergencyLastName);
            dataBinding.tilEmergencyPhone.getEditText().setText(userDetails.emergencyPhone);
            dataBinding.tilEmergencyRelationship.getEditText().setText(userDetails.emergencyRelationship);
        }
    }

    private void setDialogMargin() {
        try{
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }catch (Exception e){
            Timber.e(e, "Dialog Margin Adjustment failed");
        }
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public static abstract class DialogListener{

        public void onSave(EmergencyContact emergencyContact){

        }
        public void onDismiss(){

        }
    }

    private EmergencyContact validateData(){
        EmergencyContact contact = new EmergencyContact();
        if (TextUtils.isEmpty(dataBinding.tilEmergencyFirstName.getEditText().getText())) {

            dataBinding.tilEmergencyFirstName.setError(getContext().getString(R.string.error_emergency_first_name_empty));
            return null;
        } else {
            dataBinding.tilEmergencyFirstName.setError(null);
            contact.firstName = dataBinding.tilEmergencyFirstName.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.tilEmergencyLastName.getEditText().getText())) {

            dataBinding.tilEmergencyLastName.setError(getContext().getString(R.string.error_emergency_last_name_empty));
            return null;
        } else {
            dataBinding.tilEmergencyLastName.setError(null);
            contact.lastName = dataBinding.tilEmergencyLastName.getEditText().getText().toString();
        }

        if (!UiUtils.isValidPhone(dataBinding.tilEmergencyPhone.getEditText().getText().toString())) {

            dataBinding.tilEmergencyPhone.setError(getContext().getString(R.string.error_invalid_emergency_phone));
            return null;
        } else {
            dataBinding.tilEmergencyPhone.setError(null);
            contact.phone = dataBinding.tilEmergencyPhone.getEditText().getText().toString();
        }

        if (TextUtils.isEmpty(dataBinding.tilEmergencyRelationship.getEditText().getText())) {
            dataBinding.tilEmergencyRelationship.setError(getContext().getString(R.string.error_invalid_emergency_relation));
            return null;
        } else {
            dataBinding.tilEmergencyPhone.setError(null);
            contact.relationShip = dataBinding.tilEmergencyRelationship.getEditText().getText().toString();
        }
        return contact;
    }

    private void showRelationPicker() {
        String relationArrayJson = MessageProvider.getInstance().getText(MessageProvider.emergency_relation_ships);
        List<String> relationsList = new Gson().fromJson(relationArrayJson, new TypeToken<List<String>>() {
        }.getType());

        Collections.sort(relationsList);
        final String[] arrayOfRelations = relationsList.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Relationship");

        int checkedIndex = -1; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayOfRelations, checkedIndex, (dialog, which) ->
                dataBinding.tilEmergencyRelationship.getEditText().setText(arrayOfRelations[which]));

        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static class EmergencyContact{
        public String firstName = "";
        public String lastName = "";
        public String phone = "";
        public String relationShip = "";
    }

}
