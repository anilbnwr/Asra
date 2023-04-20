package com.asra.mobileapp.ui.dashboard.checkout.receipt;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.databinding.FragmentCheckoutPostPurchaseBinding;
import com.asra.mobileapp.model.CheckoutData;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReceiptFragment extends ShoppeFragment<ReceiptViewModel,
        FragmentCheckoutPostPurchaseBinding> {

    private static final String KEY_CHECKOUT_DATA = "checkout.data";

    public static ReceiptFragment newInstance(CheckoutData checkoutData){
        ReceiptFragment receiptFragment = new ReceiptFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_CHECKOUT_DATA, checkoutData);
        receiptFragment.setArguments(bundle);
        return receiptFragment;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_cart);
    }

    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }

    @Override
    protected Class<ReceiptViewModel> getViewModel() {
        return ReceiptViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_checkout_post_purchase;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckoutData checkoutData = getArguments().getParcelable(KEY_CHECKOUT_DATA);
        viewModel.setCheckoutData(checkoutData);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        hideBackButton();

        dataBinding.tvCheckoutTransactionAck.setText(
                getConfigString(MessageProvider.checkout_transaction_success));

        dataBinding.continueShopping.setOnClickListener(view -> {
            getDashboardActvity().clearTabStack();
        });
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();
        viewModel.cartTotalObservable.observe(this, total ->
                dataBinding.tvTractionAmountValue.setText(total));
        viewModel.userObservable.observe(this, this::setUserDetails);
        viewModel.transactionObservable.observe(this, checkoutData -> {
            dataBinding.tvTractionNumberValue.setText("#"+checkoutData.orderNumber);
        });
    }

    private void setUserDetails(UserDetails currentUser) {
        dataBinding.tvTractionUserEmail.setText(currentUser.email);
        dataBinding.tvTractionUsername.setText(currentUser.getFullName());
        dataBinding.tvTractionDateValue.setText(
                DateUtils.getToday(DateUtils.DD_MM_YYYY_DATE_PATTERN));
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.tvCheckoutThankYou.setTextColor(getColor(R.color.moto_primary));
        dataBinding.tvTractionNumberValue.setTextColor(getColor(R.color.moto_primary));
        dataBinding.continueShopping.setBackgroundResource(R.drawable.selector_button_moto_primary);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.tvCheckoutThankYou.setTextColor(getColor(R.color.colorPrimary));
        dataBinding.tvTractionNumberValue.setTextColor(getColor(R.color.colorPrimary));
        dataBinding.continueShopping.setBackgroundResource(R.drawable.selector_button_primary);

    }

    @Override
    public boolean isToolbarMenuEnabled() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done_only, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                getDashboardActvity().clearTabStack();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
