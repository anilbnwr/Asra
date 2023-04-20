package com.asra.mobileapp.ui.admin.eventparticipants;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.model.EventParticipant;
import com.asra.mobileapp.util.ListUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ParticipantViewHolder extends RecyclerView.ViewHolder {


    //EnrolledEvent
    public TextView userName;
    public TextView userId;
    public TextView dob;
    public TextView orderNumber;
    public TextView skillLevel;
    public TextView email;
    public TextView duties;
    public TextView dayWork;

    public TextView dobLabel;
    public TextView orderLabel;
    public TextView emailLabel;
    public TextView dutyLabel;
    public TextView dayWorkLabel;

    ImageView btnSign;
    ImageView  btnDelete;
    ImageView iconTdNotPurchased;
    ImageView btnMoto;
    ImageView btnStar;
    View btnSkillUpgrade;


    public ParticipantViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.userName);
        userId = itemView.findViewById(R.id.user_id_value);
        dob = itemView.findViewById(R.id.user_dob_value);
        orderNumber = itemView.findViewById(R.id.order_value);
        email = itemView.findViewById(R.id.email_value);
        duties = itemView.findViewById(R.id.duties_value);

        skillLevel = itemView.findViewById(R.id.skill_level_value);
        btnSkillUpgrade = itemView.findViewById(R.id.btn_upgrade_skill);

        dobLabel = itemView.findViewById(R.id.user_dob_label);
        orderLabel = itemView.findViewById(R.id.order_label);
        emailLabel = itemView.findViewById(R.id.email_label);
        dutyLabel = itemView.findViewById(R.id.duties_label);

        dayWorkLabel = itemView.findViewById(R.id.dayWorkLabel);
        dayWork = itemView.findViewById(R.id.dayWorkValue);

        btnSign = itemView.findViewById(R.id.btn_sign_event);
        btnDelete = itemView.findViewById(R.id.deleteBTN);
        btnStar = itemView.findViewById(R.id.btn_sign_star);
        btnMoto = itemView.findViewById(R.id.btn_sign_moto);
        iconTdNotPurchased = itemView.findViewById(R.id.btn_sign_td_not_purchased);

        if(ETApplication.getInstance().isEvApp()){
            updateToEvAppTheme();
        }else{
            updateToMotoAppTheme();
        }
    }

    private void updateToMotoAppTheme() {
        int color = UiUtils.getColorFromResource(userName.getContext(), R.color.moto_primary);
        userName.setTextColor(color);
        userId.setTextColor(color);
        dob.setTextColor(color);
        orderNumber.setTextColor(color);
        skillLevel.setTextColor(color);
        email.setTextColor(color);
        duties.setTextColor(color);

        btnSign.setImageResource(R.drawable.admin_moto_signature);
        btnStar.setImageResource(R.drawable.admin_moto_star);
        btnMoto.setImageResource(R.drawable.ic_moto_blue);
        iconTdNotPurchased.setImageResource(R.drawable.ic_td_not_purchased_blue);


    }

    private void updateToEvAppTheme() {
        int color = UiUtils.getColorFromResource(userName.getContext(), R.color.colorPrimary);
        userName.setTextColor(color);
        userId.setTextColor(color);
        dob.setTextColor(color);
        orderNumber.setTextColor(color);
        skillLevel.setTextColor(color);
        email.setTextColor(color);
        duties.setTextColor(color);

        btnSign.setImageResource(R.drawable.signature_green);
        btnStar.setImageResource(R.drawable.ic_admin_star);
        btnMoto.setImageResource(R.drawable.ic_moto_green);
        iconTdNotPurchased.setImageResource(R.drawable.ic_td_not_purchased);
    }

    public void setView(EventParticipant item, boolean isParticipant) {


        String userInfo = null;
        if(!TextUtils.isEmpty(item.displayName)){
            userInfo = item.displayName.trim().toUpperCase();
            if(!TextUtils.isEmpty(item.role)){
                userInfo += " ("+item.role.trim().toUpperCase()+")";
            }
        }
        if(!TextUtils.isEmpty(userInfo))
            userName.setText(userInfo);
        else{
            userName.setText("NA");
        }


        userId.setText(String.format("#%s", item.userId));
        if(!TextUtils.isEmpty(item.orderNumber)) {
            orderNumber.setText(String.format("#%s", item.orderNumber));
            orderNumber.setVisibility(View.VISIBLE);
        }else{
            orderLabel.setVisibility(View.INVISIBLE);
            orderNumber.setVisibility(View.INVISIBLE);
        }

        if(!TextUtils.isEmpty(item.skillLevel)) {
            skillLevel.setText(item.skillLevel.toUpperCase());
        }

        String formattedDate = DateUtils.getDateAsString(item.dateOfBirth, DateUtils.SIMPLE_DATE_NO_TIME, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        if(!TextUtils.isEmpty(formattedDate)) {

            dob.setText(formattedDate);

            dob.setVisibility(View.VISIBLE);
            dobLabel.setVisibility(View.VISIBLE);
        }else{
            dob.setVisibility(View.INVISIBLE);
            dobLabel.setVisibility(View.INVISIBLE);
        }



        if(TextUtils.isEmpty(item.email)){
            emailLabel.setVisibility(View.INVISIBLE);
            email.setVisibility(View.INVISIBLE);
        }else{
            email.setText(item.email);
            emailLabel.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
        }

        if(TextUtils.isEmpty(item.getConsolidatedDuties())){
            dutyLabel.setVisibility(View.GONE);
            duties.setVisibility(View.GONE);
        }else{
            duties.setText(item.getConsolidatedDuties());
            dutyLabel.setVisibility(View.VISIBLE);
            duties.setVisibility(View.VISIBLE);
        }

        int imageId = item.signature ? getThemedSignatureAvailableIcon() : R.drawable.signature_red;
        btnSign.setImageResource(imageId);

        btnSign.setVisibility(item.signEnabled == 1 ? View.VISIBLE : View.GONE);

        if (!ListUtils.isEmpty(item.rentals) || !ListUtils.isEmpty(item.trainingList)) {
            btnStar.setVisibility(View.VISIBLE);
        } else {
            btnStar.setVisibility(View.GONE);
        }



        if(ETApplication.getInstance().isEvApp()) {

            btnMoto.setVisibility(item.motoPurchased ? View.VISIBLE : View.GONE);
            iconTdNotPurchased.setVisibility(item.motoPurchased && !item.tdPurchased ? View.VISIBLE : View.GONE);
        }else{
            btnMoto.setVisibility(View.VISIBLE);
            iconTdNotPurchased.setVisibility(View.GONE);
        }

        btnSkillUpgrade.setVisibility(isParticipant ? View.VISIBLE : View.INVISIBLE);

        if(TextUtils.isEmpty(item.jobAssigned)){
            dayWorkLabel.setVisibility(View.GONE);
            dayWork.setVisibility(View.GONE);
        }else{
            dayWork.setText(item.jobAssigned);
            dayWorkLabel.setVisibility(View.VISIBLE);
            dayWork.setVisibility(View.VISIBLE);
        }
    }

    private int getThemedSignatureAvailableIcon(){
        if(ETApplication.getInstance().isEvApp()){
            return R.drawable.signature_green;
        }else{
            return R.drawable.admin_moto_signature;
        }
    }

    public void setEventListener(final ParticipantActionListener listener, final EventParticipant item) {
        if (listener != null) {

            if (btnSign != null)
                btnSign.setOnClickListener((View view)-> listener.onSignIconClicked(item));

            btnDelete.setOnClickListener((View view)-> listener.onDeleteButtonClicked(item));
            btnStar.setOnClickListener((View v) -> listener.onStarIconClicked(item));

            btnSkillUpgrade.setOnClickListener((View v) -> listener.onSkillUpgrade(item));
            btnMoto.setOnClickListener((View v) -> listener.onMotoIconClicked(item));
        }
    }

}
