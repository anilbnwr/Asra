package com.asra.mobileapp.ui.dashboard.shop;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.databinding.FragmentTabShopBinding;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.ui.dashboard.shop.archiecards.ArchieCardFragment;
import com.asra.mobileapp.ui.dashboard.shop.giftcards.GiftCardFragment;

public class ShopFragment extends ShoppeFragment<ShopViewModel, FragmentTabShopBinding> {
    private MenuItem cartMenuItem;
    private MenuItem switchAppMenu;

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public MenuItem getCartToolbarMenu() {
        return cartMenuItem;
    }

    @Override
    protected Class<ShopViewModel> getViewModel() {
        return ShopViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_tab_shop;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        hideBackButton();
        setListeners();
    }

    private void setListeners() {
        dataBinding.gearArchieCards.setOnClickListener(view ->
                loadFragment(ArchieCardFragment.newInstance()));
        dataBinding.gearGifts.setOnClickListener(view ->
                loadFragment(GiftCardFragment.newInstance()));
        dataBinding.gearRentals.setOnClickListener(view ->
                loadFragment(RentalFragment.newInstance()));
        dataBinding.gearGear.setOnClickListener(view ->
                loadFragment(GearFragment.newInstance()));
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_shop);
    }

    @Override
    public boolean isToolbarMenuEnabled() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_shop, menu);
        cartMenuItem = menu.findItem(R.id.menu_toolbar_cart);

        switchAppMenu = menu.findItem(R.id.menu_switch_app);
        switchAppMenu.setIcon(UiUtils.getSwitchAppMenuIcon(getContext()));

        super.onCreateOptionsMenu(menu, inflater);
        viewModel.updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_cart:
                switchToCart();
                return true;
            case R.id.menu_switch_app:
                viewModel.onSwitchApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.shopBackgroundBanner.setImageResource(R.drawable.banner_tab_gear);

        dataBinding.gearIconArchieCards.setImageResource(R.drawable.cardnew);
        dataBinding.gearIconEvents.setImageResource(R.drawable.bikenew);
        dataBinding.gearIconGear.setImageResource(R.drawable.gearnew);
        dataBinding.gearIconGift.setImageResource(R.drawable.giftnew);
        /*if (switchAppMenu != null)
            switchAppMenu.setIcon(R.drawable.ic_app_switch_moto);
*/
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.shopBackgroundBanner.setImageResource(R.drawable.moto_shop_background);
        dataBinding.gearIconArchieCards.setImageResource(R.drawable.moto_shop_archie_cards);
        dataBinding.gearIconEvents.setImageResource(R.drawable.moto_shop_rentals);
        dataBinding.gearIconGear.setImageResource(R.drawable.moto_shop_gear);
        dataBinding.gearIconGift.setImageResource(R.drawable.moto_shop_gifts);

        if (switchAppMenu != null)
            switchAppMenu.setIcon(R.drawable.ic_app_switch_ev);

    }
}
