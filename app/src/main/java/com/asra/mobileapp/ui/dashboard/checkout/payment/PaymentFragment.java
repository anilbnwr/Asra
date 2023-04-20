package com.asra.mobileapp.ui.dashboard.checkout.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.asra.mobileapp.R;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.FragmentCheckoutStep2Binding;
import com.asra.mobileapp.model.CheckoutData;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.ui.dashboard.checkout.CheckoutConstants;
import com.asra.mobileapp.ui.dashboard.checkout.receipt.ReceiptFragment;

import androidx.annotation.Nullable;
import timber.log.Timber;

public class PaymentFragment extends ShoppeFragment<PaymentViewModel, FragmentCheckoutStep2Binding> {

    private static final String KEY_CHECKOUT_DATA = "com.evolvegt.mobileapp.checkout.checkoutData";

    public static PaymentFragment newInstance(CheckoutData data) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_CHECKOUT_DATA, data);

        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckoutData checkoutData = getArguments().getParcelable(KEY_CHECKOUT_DATA);
        viewModel.setCheckoutData(checkoutData);
    }

    @Override
    public String getTitle() {
        return getString(R.string.checkout_title_step1);
    }

    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }

    @Override
    protected Class<PaymentViewModel> getViewModel() {
        return PaymentViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_checkout_step2;
    }

    private void setCardForm() {
        dataBinding.cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .setup(getDashboardActvity());
        dataBinding.cardForm.setVisibility(View.GONE);
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();
        viewModel.canApplyWallet.observe(this, this::showWalletSummaryView);

        viewModel.hasAppliedFromCouponObservable.observe(this, hasApplied -> {
            int visibility = hasApplied ? View.VISIBLE : View.GONE;
            dataBinding.checkoutCouponDiscount.setVisibility(visibility);
            dataBinding.checkoutCouponDiscountLabel.setVisibility(visibility);
        });
        viewModel.paymentRequiredObservable.observe(this, required ->
                dataBinding.paymentLayout.setVisibility(required ?
                        View.VISIBLE : View.GONE));

        viewModel.subTotalObservable.observe(this, price ->
                dataBinding.checkoutSubTotalValue.setText(price));
        viewModel.totalPayableObservable.observe(this, price ->
                dataBinding.checkoutTotalPrice.setText(price));
        viewModel.discountFromCouponObservable.observe(this, discount -> {
            dataBinding.checkoutCouponDiscount.setText(discount);
        });
        viewModel.appliedFromWalletObservable.observe(this, amount ->
                dataBinding.checkoutSummaryWalletDiscount.setText(amount));

        viewModel.enableWalletPaymentObservable.observe(this, enable -> {
            dataBinding.paymentWalletLayout.setVisibility(enable ? View.VISIBLE : View.GONE);
            dataBinding.checkoutPaymentMethodWallet.setVisibility(enable ? View.VISIBLE : View.GONE);
        });

        viewModel.cartModifiedErrorObservable.observe(this, this::showErrorMessage);
        viewModel.outOfStockObservable.observe(this, hasOutOfStock -> {
            if (hasOutOfStock)
                showErrorMessage(getConfigString(MessageProvider.error_out_of_stock_found));
        });

        viewModel.dropInRequestObservable.observe(this, this::triggerDropInPayment);
        viewModel.paymentErrorObservable.observe(this, error ->
                showTransactionError(error, false));
        viewModel.paymentErrorClearTabsObservable.observe(this, error ->
                showTransactionError(error, true));

        viewModel.paymentCompleteObservable.observe(this, checkoutData ->
                loadFragment(ReceiptFragment.newInstance(checkoutData)));
        viewModel.walletAmount.observe(this, walletBalance ->{
            dataBinding.checkoutWalletValue.setText(walletBalance);
        });
    }

    private void showTransactionError(String error, boolean clearCart) {
        ETDialog.DialogListener listener = null;
        String button = getString(R.string.et_ok);
        if(clearCart){
            listener = new ETDialog.DialogListener() {
                @Override
                public void onPositiveButtonClicked() {
                    super.onPositiveButtonClicked();
                    popFragmentsToRoot();
                }
            };
            button = getString(R.string.btn_continue_shopping);
        }
        showErrorDialog("Transaction Error", error,button, listener );
    }



    @Override
    public void initializeViews() {
        super.initializeViews();
        setCardForm();
        dataBinding.btnCheckoutPlaceOrder.setOnClickListener(view -> onPlaceOrder());
        dataBinding.checkoutReviewCart.setOnClickListener((View review) ->
                getDashboardActvity().clearTabStack(UiUtils.TAB_CART));

        dataBinding.txtPaypalDescription.setText(
                getConfigString(MessageProvider.checkout_label_paypal_description));

        dataBinding.checkoutCreditCardMethodPaypal.setOnCheckedChangeListener(
                (compoundButton, b) -> showCreditCardView(b));

        viewModel.onViewStarted();

    }

    private void onPlaceOrder() {
        int code;
        if (dataBinding.checkoutPaymentMethodWallet.isChecked()) {
            code = CheckoutConstants.PAY_WITH_WALLET;
        } else if (dataBinding.checkoutCreditCardMethodPaypal.isChecked()) {
            code = CheckoutConstants.PAY_WITH_CREDIT;
        } else {
            code = CheckoutConstants.PAY_WITH_PAYPAL;
        }
        viewModel.onPlaceOrder(code);
    }

    public void triggerDropInPayment(DropInRequest dropInRequest) {

        startActivityForResult(
                dropInRequest.getIntent(getContext()),
                CheckoutConstants.BRAINTREE_DROP_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.i("Dashboard - onActivityResult");

        if (requestCode == CheckoutConstants.BRAINTREE_DROP_IN) {
            if (Activity.RESULT_OK == resultCode) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentNonce = result.getPaymentMethodNonce().getNonce();

                viewModel.completeDropInPayment(paymentNonce);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Timber.d("User cancelled payment");
            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                if (error != null) {
                    Timber.e(error, " error exception");
                } else {
                    Timber.e(" error exception");
                }
            }
        }
    }

    private void showCreditCardView(boolean show) {
        dataBinding.cardForm.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            dataBinding.scrollContainer.post(() ->
                    dataBinding.scrollContainer.smoothScrollTo(
                            (int) dataBinding.cardForm.getX(),
                            (int) dataBinding.cardForm.getY()));
        }
    }

    private void showWalletSummaryView(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        dataBinding.checkoutSummaryWalletLabel.setVisibility(visibility);
        dataBinding.checkoutSummaryWalletDiscount.setVisibility(visibility);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.btnCheckoutPlaceOrder.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.checkoutWalletValue.setTextColor(getColor(R.color.colorPrimary));
        dataBinding.checkoutTotalPrice.setTextColor(getColor(R.color.colorPrimary));
        dataBinding.paypalLabel.setTextColor(getColor(R.color.colorPrimary));
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.btnCheckoutPlaceOrder.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.checkoutWalletValue.setTextColor(getColor(R.color.moto_primary));
        dataBinding.checkoutTotalPrice.setTextColor(getColor(R.color.moto_primary));
        dataBinding.paypalLabel.setTextColor(getColor(R.color.moto_primary));
    }
}
