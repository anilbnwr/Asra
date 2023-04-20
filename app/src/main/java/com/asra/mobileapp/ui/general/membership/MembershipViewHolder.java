package com.asra.mobileapp.ui.general.membership;

import android.view.View;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.UserMembership;
import com.asra.mobileapp.util.StringUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MembershipViewHolder extends RecyclerView.ViewHolder {

    private String userMembership;

    public TextView membershipName;
    public TextView memberShipPrice;
    public TextView membershipSeason;
    public TextView btnUpgrade;
    public TextView btnViewMore;
    public View viewHighlight;

    private MembershipActionsCallback membershipActionsCallback;
    private UserMembership currentMembership;
    private boolean isEvApp;

    public MembershipViewHolder(@NonNull View itemView, String role, UserMembership currentMembership,
                                MembershipActionsCallback membershipActionsCallback) {
        super(itemView);

        this.userMembership = role;
        this.membershipActionsCallback = membershipActionsCallback;
        this.currentMembership = currentMembership;

        membershipName = itemView.findViewById(R.id.membershipName);
        memberShipPrice = itemView.findViewById(R.id.membershipPrice);
        membershipSeason = itemView.findViewById(R.id.membershipTenure);
        btnUpgrade = itemView.findViewById(R.id.btn_upgrade);
        btnViewMore = itemView.findViewById(R.id.btn_view_more);
        viewHighlight = itemView.findViewById(R.id.membershipInfo);

        isEvApp = ETApplication.getInstance().isEvApp();

        if(isEvApp){
            applyEvAppTheme();
        }else{
            applyMotoAppTheme();
        }
    }

    private void applyMotoAppTheme() {
        btnViewMore.setBackgroundResource(R.drawable.selector_border_blue);
        btnViewMore.setTextColor( UiUtils.getColorFromResource(btnUpgrade.getContext(), R.color.moto_primary));
        btnUpgrade.setBackgroundColor(
                UiUtils.getColorFromResource(btnUpgrade.getContext(), R.color.moto_primary));
    }

    private void applyEvAppTheme() {
        btnViewMore.setBackgroundResource(R.drawable.selector_border_green);
        btnViewMore.setTextColor( UiUtils.getColorFromResource(btnUpgrade.getContext(), R.color.colorPrimary));
        btnUpgrade.setBackgroundColor(
                UiUtils.getColorFromResource(btnUpgrade.getContext(), R.color.colorPrimary));
    }

    public void setView(Membership item) {
        membershipName.setText(item.getTitle());
        memberShipPrice.setText(StringUtils.formatAmount(AppUtils.getDouble(item.getPrice())));
        membershipSeason.setText(item.getSeason());
        btnViewMore.setOnClickListener(view -> {
            if (membershipActionsCallback != null) {
                membershipActionsCallback.onViewMore(item);
            }
        });
        btnUpgrade.setOnClickListener(view -> {
            if (membershipActionsCallback != null) {
                membershipActionsCallback.onUpgrade(item);
            }
        });

        btnUpgrade.setVisibility(item.canPurchase(currentMembership)  ? View.VISIBLE : View.INVISIBLE);

        btnUpgrade.setEnabled(!item.isOutOfStock());
        int btnText = item.isOutOfStock() ? R.string.tracks_label_sold_out : R.string.btn_purchase;
        btnUpgrade.setText(btnUpgrade.getContext().getResources().getString(btnText) );

        if (item.shouldHighlight(userMembership)) {

            membershipName.setTextColor(ContextCompat.getColor(
                    membershipName.getContext(), R.color.colorTextWhite));

            memberShipPrice.setTextColor(ContextCompat.getColor(
                    membershipName.getContext(), R.color.colorTextWhite));

            membershipSeason.setTextColor(ContextCompat.getColor(
                    membershipName.getContext(), R.color.colorTextWhite));

            if(isEvApp)
                viewHighlight.setBackgroundResource(R.color.colorPrimary);
            else
                viewHighlight.setBackgroundResource(R.color.moto_primary);
        } else {
            viewHighlight.setBackgroundResource(R.color.colorTextWhite);
            membershipName.setTextColor(ContextCompat.getColor(
                    membershipName.getContext(), R.color.colorTextBlack));

            memberShipPrice.setTextColor(ContextCompat.getColor(
                    membershipName.getContext(), R.color.colorTextBlack));

            membershipSeason.setTextColor(ContextCompat.getColor(
                    membershipName.getContext(), R.color.colorTextBlack));
        }
    }
}
