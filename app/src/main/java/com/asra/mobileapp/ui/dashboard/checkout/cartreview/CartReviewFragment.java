package com.asra.mobileapp.ui.dashboard.checkout.cartreview;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.databinding.FragmentCheckoutStep1Binding;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.model.CheckoutData;
import com.asra.mobileapp.model.Coupon;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.ui.dashboard.checkout.payment.PaymentFragment;
import com.asra.mobileapp.ui.general.address.AddressConstants;
import com.asra.mobileapp.ui.general.address.AddressFragment;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.StringUtils;

import java.util.List;

public class CartReviewFragment extends ShoppeFragment<CheckoutViewModel, FragmentCheckoutStep1Binding> {

    public static CartReviewFragment newInstance(){
        return new CartReviewFragment();
    }

    @Override
    public String getTitle() {
        return getString(R.string.checkout_title_step1);
    }

    @Override
    protected Class<CheckoutViewModel> getViewModel() {
        return CheckoutViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_checkout_step1;
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.cartListObservable.observe(this, this::setCartSummaryView);
        viewModel.subTotalObservable.observe(this, subTotal ->
                dataBinding.checkoutValueSubTotal.setText(subTotal));
        viewModel.totalPriceObservable.observe(this, total ->
                dataBinding.checkoutValueTotal.setText(total));
        viewModel.hasOutOfStockObservable.observe(this, hasOutOfStock ->{
            if(hasOutOfStock){
                showErrorMessage(getConfigString(
                        MessageProvider.error_out_of_stock_found));
            }
        });
        viewModel.couponObservable.observe(this, this:: setCouponView);
        viewModel.walletObservable.observe(this, wallet ->
                dataBinding.checkoutWalletValue.setText(wallet));

        viewModel.couponErrorObservable.observe(this, this::showErrorToast);
        viewModel.currentUserObservable.observe(this, this::setBillingAddress);
        viewModel.paymentObservable.observe(this, this::launchPaymentScreen);
        viewModel.couponDiscountObservable.observe(this, couponDiscount ->{
            if(TextUtils.isEmpty(couponDiscount)){
                dataBinding.checkoutCouponContainer.setVisibility(View.GONE);
            }else{
                dataBinding.checkoutCouponContainer.setVisibility(View.VISIBLE);
                dataBinding.checkoutValueCoupon.setText(couponDiscount);
            }

        });
    }

    private void launchPaymentScreen(CheckoutData checkoutData) {
        loadFragment(PaymentFragment.newInstance(checkoutData));
    }

    private void setCouponView(Coupon coupon) {
        if(!StringUtils.isEmpty(coupon.couponCode)) {
            dataBinding.couponViewLayout.setVisibility(View.VISIBLE);
            dataBinding.couponInputLayout.setVisibility(View.GONE);
            dataBinding.checkoutLabelCoupon.setText(
                    getString(R.string.checkout_coupon_applied));

            dataBinding.checkoutTilCoupon.getEditText().setText(coupon.couponCode);
        }else{
            dataBinding.couponInputLayout.setVisibility(View.VISIBLE);
            dataBinding.couponViewLayout.setVisibility(View.GONE);
            dataBinding.checkoutLabelCoupon.setText(
                    getString(R.string.checkout_coupon_label));
        }

        dataBinding.checkoutCouponValue.setText(String.format("Coupon Code: %s", coupon.couponCode));
        dataBinding.checkoutCouponBalance.setText(String.format("Coupon Balance:%s",
                StringUtils.formatAmount(coupon.couponBalance)));


    }

    private void setCartSummaryView(List<CartItem> cartDataSet) {
        if (!ListUtils.isEmpty(cartDataSet)) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            dataBinding.cartItems.removeAllViews();
            for (CartItem item : cartDataSet) {
                View view = inflater.inflate(R.layout.list_item_cart_review, null, false);
                TextView tvTitle = view.findViewById(R.id.review_cart_item_name);
                TextView tvPrice = view.findViewById(R.id.review_cart_item_price);
                TextView tvTotal = view.findViewById(R.id.review_cart_item_total);
                TextView tvStockStatus = view.findViewById(R.id.review_cart_item_stock_status);

                if(isEvApp()){
                    tvTotal.setTextColor(getColor(R.color.colorPrimary));
                }else{
                    tvTotal.setTextColor(getColor(R.color.moto_primary));
                }
                tvTitle.setText(item.title);
                String priceText = "Qty:" + item.quantity + "  |  Price: $" + item.price;

                double itemTotal = Integer.valueOf(item.quantity) * AppUtils.getDouble(item.price);
                if (!TextUtils.isEmpty(item.feeAmount)) {

                    double feeAmount = AppUtils.getDouble(item.feeAmount);
                    if (feeAmount > 0) {
                        priceText += "  |  Fee: $" + item.feeAmount;
                        itemTotal += feeAmount;
                    }
                }
                tvPrice.setText(priceText);
                if (CartItem.IN_STOCK.equalsIgnoreCase(item.stockStatus)) {
                    tvStockStatus.setVisibility(View.GONE);

                } else {
                    tvStockStatus.setVisibility(View.VISIBLE);
                }
                tvTotal.setText(String.format("Total :%s", StringUtils.formatAmount(itemTotal)));

                dataBinding.cartItems.addView(view);
            }

        }

    }

    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }

    @Override
    public void initializeViews() {
        dataBinding.checkoutWalletValue.setText(R.string.text_loading);
        showBackButton();


        dataBinding.checkoutCouponApply.setOnClickListener((View btn) ->
                applyCoupon());

        dataBinding.btnCheckoutProceedToPayment.setOnClickListener((View v) ->
                proceedToPayment());



        dataBinding.checkoutBillingAddressEdit.setOnClickListener((View btn) ->
                editBillingAddress(false));
        dataBinding.checkoutLabelAddBilling.setOnClickListener((View btn) ->
                editBillingAddress(true));


        dataBinding.checkoutLabelNoBilling.setText(
                MessageProvider.getInstance()
                        .getText(MessageProvider.checkout_label_no_billing));


        dataBinding.checkoutCouponDelete.setOnClickListener((View v) ->
                viewModel.onCouponDelete());


        viewModel.loadCartList();
    }

    private void applyCoupon() {
        String couponText = dataBinding.checkoutTilCoupon.getEditText().getText().toString();
        if (!StringUtils.isEmpty(couponText)) {
            viewModel.applyCoupon(couponText);
        } else {
            dataBinding.checkoutTilCoupon.setError(
                    getConfigString(MessageProvider.checkout_error_empty_coupon));
            //showErrorToast();
        }

    }

    private void editBillingAddress(boolean b) {
        loadFragment(AddressFragment.newInstance(AddressConstants.TYPE_BILLING, b));
    }

    private void proceedToPayment() {
        viewModel.proceedToPayment();
    }

    private void setBillingAddress(UserDetails currentUser) {
        if (currentUser != null) {


            String newline = "\n";
            String space = " ";
            if (!TextUtils.isEmpty(currentUser.billingAddress1)) {
                StringBuilder builder = new StringBuilder();
                builder = builder.append(currentUser.firstName).append(space)
                        .append(currentUser.lastName).append(newline);

                builder = builder.append(currentUser.billingAddress1).append(newline);

                if (!TextUtils.isEmpty(currentUser.billingAddress2))
                    builder = builder.append(currentUser.billingAddress2).append(newline);


                if (!TextUtils.isEmpty(currentUser.billingCompany))
                    builder = builder.append(currentUser.billingCompany).append(newline);

                if (!TextUtils.isEmpty(currentUser.billingCity))
                    builder = builder.append(currentUser.billingCity).append(", ")
                            .append(currentUser.billingState).append(" - ")
                            .append(currentUser.billingPostcode).append(newline);
                builder = builder.append(currentUser.billingCountry).append(newline);

                if (!TextUtils.isEmpty(currentUser.billingPhone))
                    builder = builder.append(currentUser.billingPhone);

                dataBinding.checkoutBillingAddress.setText(builder.toString());

                dataBinding.checkoutBillingAddressLayout.setVisibility(View.VISIBLE);
                dataBinding.checkoutNoBillingAddressLayout.setVisibility(View.GONE);

            } else {
                dataBinding.checkoutBillingAddressLayout.setVisibility(View.GONE);
                dataBinding.checkoutLabelNoBilling.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.btnCheckoutProceedToPayment.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.checkoutLabelAddBilling.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.checkoutBillingAddressEdit.setImageResource(R.drawable.selector_button_edit_moto);
        dataBinding.checkoutValueSubTotal.setTextColor(getColor(R.color.moto_primary));
        dataBinding.checkoutValueTotal.setTextColor(getColor(R.color.moto_primary));
        dataBinding.checkoutWalletValue.setTextColor(getColor(R.color.moto_primary));
        dataBinding.checkoutCouponDelete.setImageResource(R.drawable.moto_delete);
        dataBinding.checkoutCouponApply.setBackgroundResource(R.drawable.selector_border_blue);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.btnCheckoutProceedToPayment.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.checkoutLabelAddBilling.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.checkoutBillingAddressEdit.setImageResource(R.drawable.selector_button_edit_ev);
        dataBinding.checkoutValueSubTotal.setTextColor(getColor(R.color.colorPrimary));
        dataBinding.checkoutValueTotal.setTextColor(getColor(R.color.colorPrimary));
        dataBinding.checkoutWalletValue.setTextColor(getColor(R.color.colorPrimary));
        dataBinding.checkoutCouponApply.setBackgroundResource(R.drawable.selector_border_green);

    }
}
