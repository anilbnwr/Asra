package com.asra.mobileapp.ui.guest.home;

import android.view.View;

import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentGuestHomeBinding;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.dashboard.cart.CartFragment;
import com.asra.mobileapp.ui.dashboard.events.EventListFragment;
import com.asra.mobileapp.ui.dashboard.shop.GearFragment;
import com.asra.mobileapp.ui.dashboard.shop.RentalFragment;
import com.asra.mobileapp.ui.dashboard.shop.archiecards.ArchieCardFragment;
import com.asra.mobileapp.ui.dashboard.shop.giftcards.GiftCardFragment;

public class GuestHomeFragment extends ETFragment<GuestHomeViewModel, FragmentGuestHomeBinding> {



    public static GuestHomeFragment newInstance(){
        return new GuestHomeFragment();
    }

    @Override
    protected Class getViewModel() {
        return GuestHomeViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_guest_home;
    }

    @Override
    public void initializeViews() {

        showHomeIcon(false);
        showLoginMenu(true);

        dataBinding.guestHomeEvents.setOnClickListener((View v)->
                loadEventsFragment());

        dataBinding.guestHomeArchie.setOnClickListener((View v)->
                loadFragment(ArchieCardFragment.newInstance()));

        dataBinding.guestHomeGear.setOnClickListener((View v)-> loadFragment(GearFragment.newInstance()));

        dataBinding.guestHomeGift.setOnClickListener((View v)-> loadFragment(GiftCardFragment.newInstance()));

        dataBinding.guestHomeRentals.setOnClickListener((View v)-> loadFragment(RentalFragment.newInstance()));

        dataBinding.guestHomeCart.setOnClickListener((View v)-> loadFragment(CartFragment.newInstance()));
    }

    private void loadEventsFragment() {
        loadFragment(EventListFragment.newInstance());
    }

    @Override
    public void observeEventsFromViewModel() {

    }

    @Override
    public String getTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.tvGuestWelcome.setBackgroundColor(getColor(R.color.colorPrimary));
        dataBinding.gearIconArchieCards.setImageResource(R.drawable.cardnew);
        dataBinding.gearIconEvents.setImageResource(R.drawable.ic_shop_events);
        dataBinding.gearIconGear.setImageResource(R.drawable.gearnew);
        dataBinding.gearIconGift.setImageResource(R.drawable.giftnew);
        dataBinding.gearIconRentals.setImageResource(R.drawable.bikenew);
   //     dataBinding.gearIconCart.setImageResource(R.drawable.ic_drawer_cart);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.tvGuestWelcome.setBackgroundColor(getColor(R.color.moto_primary));
        dataBinding.gearIconArchieCards.setImageResource(R.drawable.moto_shop_archie_cards);
        dataBinding.gearIconEvents.setImageResource(R.drawable.moto_shop_events);
        dataBinding.gearIconGear.setImageResource(R.drawable.moto_shop_gear);
        dataBinding.gearIconGift.setImageResource(R.drawable.moto_shop_gifts);
        dataBinding.gearIconRentals.setImageResource(R.drawable.moto_shop_rentals);
        dataBinding.gearIconCart.setImageResource(R.drawable.moto_shop_cart);

    }
}
