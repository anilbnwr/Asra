package com.asra.mobileapp.ui.dashboard.cart;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.analytics.AnalyticsModel;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.ToastHelper;
import com.asra.mobileapp.common.dialog.EmergencyContactDialog;
import com.asra.mobileapp.databinding.FragmentCartBinding;
import com.asra.mobileapp.model.CartItem;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.ui.dashboard.checkout.cartreview.CartReviewFragment;
import com.asra.mobileapp.ui.dashboard.events.eventdetails.EventDetailsFragment;
import com.asra.mobileapp.ui.dashboard.shop.archiecards.ArchieCardsDetailsFragment;
import com.asra.mobileapp.ui.dashboard.shop.giftcards.GiftCardsDetailsFragment;
import com.asra.mobileapp.ui.dashboard.shop.productdetails.ProductDetailFragment;
import com.asra.mobileapp.ui.general.membershipdetails.MembershipDetailsFragment;
import com.asra.mobileapp.ui.login.login.LoginFragment;
import com.asra.mobileapp.util.ListUtils;

import java.util.List;

import androidx.annotation.NonNull;

public class CartFragment extends ShoppeFragment<CartViewModel, FragmentCartBinding>
        implements CartItemClickListener{

    private MenuItem switchAppMenu;

    public static CartFragment newInstance(){
        return new CartFragment();
    }

    private CartAdapter cartAdapter;
    @Override
    public MenuItem getCartToolbarMenu() {
        return null;
    }

    @Override
    protected Class<CartViewModel> getViewModel() {
        return CartViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_cart;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();

        viewModel.cartListObservable.observe(this, this::setUpCartListView);
        viewModel.isGuestObservable.observe(this, isGuest ->{
            if(!isGuest){
                showBackButton();
                dataBinding.btnCartCheckout.setText(R.string.login_label);
            }else{
                hideBackButton();
            }

        });

        viewModel.guestUserObservable.observe(this, guestLoggedIn ->{
            if(switchAppMenu != null){
                switchAppMenu.setVisible(!guestLoggedIn);
            }
        });
        viewModel.cartTotalObservable.observe(this, total ->
                dataBinding.cartTotalValue.setText(total));
        viewModel.hasOutOfStockObservable.observe(this, hasOutOfStock ->{
            dataBinding.tvCartOutOfStock.setVisibility(hasOutOfStock ?
                    View.VISIBLE :View.GONE);
            dataBinding.btnCartCheckout.setEnabled(!hasOutOfStock);
        });

        viewModel.cartListErrorObservable.observe(this, error ->
                showEmptyMessage(true, error));
        viewModel.cartDeleteErrorObservable.observe(this, error ->
                ToastHelper.getInstance().showError(getContext(), error));
        viewModel.cartDeleteSuccessObservable.observe(this,
                message -> ToastHelper.getInstance().showSuccess(getContext(), message));

        viewModel.proceedToPaymentObservable.observe(this, proceed ->
                loadFragment(CartReviewFragment.newInstance()));
        viewModel.loginRequiredObservable.observe(this, login ->
                loadFragment(LoginFragment.newInstance(false)));
        viewModel.emergencyContactRequiredObservable.observe(this, userDetails -> {
            EmergencyContactDialog dialog = new EmergencyContactDialog(getActivity(), new EmergencyContactDialog.DialogListener() {
                @Override
                public void onSave(EmergencyContactDialog.EmergencyContact emergencyContact) {
                    viewModel.saveEmergencyContact(emergencyContact);
                }
            });
            dialog.setUserDetails(userDetails);
            dialog.show();
        });
    }

    private void setUpCartListView(List<CartItem> cartList) {
        if(ListUtils.isEmpty(cartList)){
            showEmptyMessage(true, getConfigString(MessageProvider.cart_empty_message));
        }else {
            cartAdapter.updateDateSet(cartList);
            showEmptyMessage(false, null);
            dataBinding.cartRecyclerView.setVisibility(View.VISIBLE);
            analyticsHelper.logViewListEvent(AnalyticsModel.CATEGORY_CART);
        }
    }


    @Override
    public String getTitle() {
        return getString(R.string.title_cart);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        dataBinding.cartRecyclerView.setLayoutManager(getLinearLayoutManager());

        dataBinding.cartRecyclerView.setHasFixedSize(true);
        cartAdapter = new CartAdapter(this);
        dataBinding.cartRecyclerView.setAdapter(cartAdapter);


        dataBinding.cartRecyclerView.removeItemDecoration(itemDecoration);
        dataBinding.cartRecyclerView.addItemDecoration(itemDecoration);
        dataBinding.btnCartCheckout.setOnClickListener(view -> {
           viewModel.proceedToPayment();
        });

        viewModel.onViewStarted();
    }

    @Override
    public void onItemClicked(CartItem item) {
        ETFragment fragment = null;
        switch (item.method.toLowerCase()) {

            case CartConstants.TYPE_ARCHIE:
                fragment = ArchieCardsDetailsFragment.newInstance(item);
                break;
            case CartConstants.TYPE_EVENT:
                if(item.isMotoEvent() && !isEvApp())
                    fragment = EventDetailsFragment.newInstance(item.slug, item.title, false);
                else  if(!item.isMotoEvent() && isEvApp()){
                    fragment = EventDetailsFragment.newInstance(item.slug, item.title, true);
                }
                break;
            case CartConstants.TYPE_GIFT:
                fragment = GiftCardsDetailsFragment.newInstance(item);
                break;
            case CartConstants.TYPE_RENTAL:
            case CartConstants.TYPE_TRAINING:
                if(isEvApp())
                    fragment = EventDetailsFragment.newInstance(item.parentSlug, item.parentTitle, true);
                break;

            case CartConstants.TYPE_TRANSPONDER:
                if(!isEvApp())
                    fragment = EventDetailsFragment.newInstance(item.parentSlug, item.parentTitle, false);
                break;

            case CartConstants.TYPE_MEMBERSHIP:
                fragment = MembershipDetailsFragment.newInstance(item.slug);
                break;

            case CartConstants.TYPE_PRODUCT:
                fragment = ProductDetailFragment.newInstance(item.title, item.slug);
                break;
        }
        if(fragment != null) {
            loadFragment(fragment);
        }
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.btnCartCheckout.setBackgroundResource(R.drawable.selector_button_moto_primary);
        dataBinding.cartTotalValue.setTextColor(getPrimaryColor());

        if (switchAppMenu != null)
            switchAppMenu.setIcon(R.drawable.ic_app_switch_ev);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.btnCartCheckout.setBackgroundResource(R.drawable.selector_button_primary);
        dataBinding.cartTotalValue.setTextColor(getPrimaryColor());

       /* if (switchAppMenu != null)
            switchAppMenu.setIcon(R.drawable.ic_app_switch_moto);*/
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        /*
        //Switch App Menu disabled
        inflater.inflate(R.menu.menu_toolbar_cart, menu);

        switchAppMenu = menu.findItem(R.id.menu_switch_app);
        switchAppMenu.setIcon(UiUtils.getSwitchAppMenuIcon(getContext()));
        switchAppMenu.setVisible(isEvApp());

         */
        viewModel.updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_switch_app) {
            viewModel.onSwitchApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isToolbarMenuEnabled() {
        return true;
    }

    @Override
    public void addQuantity(CartItem item) {

    }

    @Override
    public void removeQuantity(CartItem item) {

    }

    @Override
    public void deleteItem(CartItem item) {
        viewModel.deleteFromCart(item);
    }

}
