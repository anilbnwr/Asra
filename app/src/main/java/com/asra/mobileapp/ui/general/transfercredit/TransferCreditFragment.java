package com.asra.mobileapp.ui.general.transfercredit;

import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.DialogTransferCreditBinding;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.util.MoneyValueFilter;
import com.asra.mobileapp.util.StringUtils;

public class TransferCreditFragment  extends ETFragment<TransferCreditViewModel, DialogTransferCreditBinding> {


    public static TransferCreditFragment newInstance() {
        return new TransferCreditFragment();
    }


    @Override
    protected Class<TransferCreditViewModel> getViewModel() {
        return TransferCreditViewModel.class;
    }

    @Override
    public String getTitle() {
        return getContext().getString(R.string.title_transfer_credit);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_transfer_credit;
    }

    private String currentAmount = "";

    @Override
    public void initializeViews() {

        dataBinding.inputContainer.setVisibility(View.VISIBLE);
        dataBinding.messageContainer.setVisibility(View.GONE);
        dataBinding.btnTransfer.setOnClickListener(view -> {
            UiUtils.hideKeyboard(getActivity());
            if(validated()) {

                String email = dataBinding.receiverEmail.getEditText().getText().toString();
                String amount = dataBinding.amount.getEditText().getText().toString().replace("$", "");
                viewModel.onTransferCredit(email, amount);
            }
        });

        EditText amountText = dataBinding.amount.getEditText();
        amountText.setFilters(new InputFilter[] { new MoneyValueFilter()});
        /*
        amountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(currentAmount)){
                    amountText.removeTextChangedListener(this);

                    String cleanString = charSequence.toString().replaceAll("[$]", "");


                    String formatted = "$ "+ cleanString;
                    amountText.setText(formatted);
                    amountText.setSelection(formatted.length() - 1);

                    amountText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

         */
        dataBinding.closeApp.setOnClickListener(view -> getActivity().onBackPressed());
    }

    private boolean validated() {

        String amount = dataBinding.amount.getEditText().getText().toString().replace("$", "");

        if(StringUtils.isEmpty(dataBinding.receiverEmail.getEditText().getText().toString())){
            dataBinding.receiverEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }else if(AppUtils.getDouble(amount) <= 0) {
            dataBinding.amount.setError(getString(R.string.error_invalid_amount));
            return false;
        }
            return true;
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.transferSuccessObservable.observe(this, message ->{
            dataBinding.inputContainer.setVisibility(View.GONE);
            dataBinding.messageContainer.setVisibility(View.VISIBLE);
        });

        viewModel.balanceCreditObservable.observe(this, balance ->{
            dataBinding.availableBalance.setText(String.format("Available Credit: %s", balance));
        });
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.btnTransfer.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.successLogo.setImageResource(R.drawable.ic_common_action_success);
        dataBinding.closeApp.setBackgroundResource(R.drawable.selector_button_primary);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.btnTransfer.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.successLogo.setImageResource(R.drawable.ic_moto_action_success);
        dataBinding.closeApp.setBackgroundResource(R.drawable.selector_button_moto_primary);

    }
}