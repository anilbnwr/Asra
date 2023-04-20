package com.asra.mobileapp.ui.general.credithistory;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.model.CreditHistory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CreditViewHolder extends RecyclerView.ViewHolder {

    TextView tvAmount;
    TextView tvDate;
    TextView tvDescription;

    ImageView ivTransactionType;
    private boolean isEvapp;

    CreditViewHolder(@NonNull View itemView){
        super(itemView);

         tvAmount = itemView.findViewById(R.id.credit_amount);
         tvDate = itemView.findViewById(R.id.credit_posted_date);
         tvDescription = itemView.findViewById(R.id.credit_description);

         ivTransactionType = itemView.findViewById(R.id.iv_transaction_type);
        isEvapp = ETApplication.getInstance().isEvApp();
         if(isEvapp){
             applyEvAppTheme();
         }else{
             applyMotoAppTheme();
         }
    }

    public void setView(CreditHistory credit){

        String formattedOrderDate = DateUtils.getDateAsString(credit.postDate,
                DateUtils.SIMPLE_DATE_FORMAT, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        tvDate.setText("Posted on "+formattedOrderDate);
        tvAmount.setText("$"+ credit.amount);
        tvDescription.setText(credit.description);

        if(isEvapp) {
            if (credit.mode.equalsIgnoreCase("wallet")) {
                ivTransactionType.setImageResource(R.drawable.ic_credit_wallet);
            } else {
                ivTransactionType.setImageResource(R.drawable.ic_credit_paypal);
            }
        }else{
            if (credit.mode.equalsIgnoreCase("wallet")) {
                ivTransactionType.setImageResource(R.drawable.ic_moto_wallet);
            } else {
                ivTransactionType.setImageResource(R.drawable.ic_credit_paypal);
            }
        }

    }

    private void applyEvAppTheme(){
        tvAmount.setTextColor(UiUtils.getColorFromResource(
                tvAmount.getContext(), R.color.colorPrimary));

    }
    private void applyMotoAppTheme(){
        tvAmount.setTextColor(UiUtils.getColorFromResource(
                tvAmount.getContext(), R.color.moto_primary));
    }
}
