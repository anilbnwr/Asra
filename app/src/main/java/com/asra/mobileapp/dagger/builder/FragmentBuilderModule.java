package com.asra.mobileapp.dagger.builder;


import com.asra.mobileapp.ui.admin.adminduties.AdminDutyFragment;
import com.asra.mobileapp.ui.admin.completedevents.CompletedEventsFragment;
import com.asra.mobileapp.ui.admin.eventparticipants.EventParticipantsFragment;
import com.asra.mobileapp.ui.admin.signature.SignatureFragment;
import com.asra.mobileapp.ui.dashboard.cart.CartFragment;
import com.asra.mobileapp.ui.dashboard.checkout.cartreview.CartReviewFragment;
import com.asra.mobileapp.ui.dashboard.checkout.payment.PaymentFragment;
import com.asra.mobileapp.ui.dashboard.checkout.receipt.ReceiptFragment;
import com.asra.mobileapp.ui.dashboard.duties.DutyListFragment;
import com.asra.mobileapp.ui.dashboard.duties.TabbedDutyEventsFragment;
import com.asra.mobileapp.ui.dashboard.events.EventListFragment;
import com.asra.mobileapp.ui.dashboard.events.eventdetails.EventDetailsFragment;
import com.asra.mobileapp.ui.dashboard.events.motoaccessoryselection.EventAccessorySelectionFragment;
import com.asra.mobileapp.ui.dashboard.home.HomeFragment;
import com.asra.mobileapp.ui.dashboard.shop.GearFragment;
import com.asra.mobileapp.ui.dashboard.shop.RentalFragment;
import com.asra.mobileapp.ui.dashboard.shop.ShopFragment;
import com.asra.mobileapp.ui.dashboard.shop.archiecards.ArchieCardFragment;
import com.asra.mobileapp.ui.dashboard.shop.archiecards.ArchieCardsDetailsFragment;
import com.asra.mobileapp.ui.dashboard.shop.giftcards.GiftCardFragment;
import com.asra.mobileapp.ui.dashboard.shop.giftcards.GiftCardsDetailsFragment;
import com.asra.mobileapp.ui.dashboard.shop.product.ProductFragment;
import com.asra.mobileapp.ui.dashboard.shop.productdetails.ProductDetailFragment;
import com.asra.mobileapp.ui.general.aboutus.AboutUsFragment;
import com.asra.mobileapp.ui.general.address.AddressFragment;
import com.asra.mobileapp.ui.general.changepassword.ChangePasswordFragment;
import com.asra.mobileapp.ui.general.credithistory.CreditHistoryFragment;
import com.asra.mobileapp.ui.general.enrolledevents.EnrolledEventListFragment;
import com.asra.mobileapp.ui.general.enrolledevents.FlipperEventFragment;
import com.asra.mobileapp.ui.general.enrolledevents.passport.showpassport.ShowPassportFragment;
import com.asra.mobileapp.ui.general.enrolledevents.passport.uploadpassport.PassportSignatureFragment;
import com.asra.mobileapp.ui.general.ewaiver.EWaiverListFragment;
import com.asra.mobileapp.ui.general.ewaiver.waiverdetails.WaiverDetailsFragment;
import com.asra.mobileapp.ui.general.ewaiver.waiverdetails.WaiverSignatureFragment;
import com.asra.mobileapp.ui.general.membership.MembershipFragment;
import com.asra.mobileapp.ui.general.membershipdetails.MembershipDetailsFragment;
import com.asra.mobileapp.ui.general.policy.PolicyFragment;
import com.asra.mobileapp.ui.general.profile.ProfileFragment;
import com.asra.mobileapp.ui.general.settings.SettingsFragment;
import com.asra.mobileapp.ui.general.settings.TermsNConditionsFragment;
import com.asra.mobileapp.ui.general.transfercredit.TransferCreditFragment;
import com.asra.mobileapp.ui.guest.home.GuestHomeFragment;
import com.asra.mobileapp.ui.login.appselector.FragmentAppSelector;
import com.asra.mobileapp.ui.login.login.LoginFragment;
import com.asra.mobileapp.ui.login.regisrtation.RegistrationStep1Fragment;
import com.asra.mobileapp.ui.login.regisrtation.RegistrationStep2Fragment;
import com.asra.mobileapp.ui.login.resetpassword.ForgotPasswordFragment;
import com.asra.mobileapp.ui.main.MainFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class FragmentBuilderModule {

    @SuppressWarnings("unused")
    @ContributesAndroidInjector
    abstract MainFragment contributeMainFragment();

    @ContributesAndroidInjector
    abstract FragmentAppSelector contributeAppSelectorFragment();

    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragment();

    @ContributesAndroidInjector
    abstract ForgotPasswordFragment contributeForgotPasswordFragment();

    @ContributesAndroidInjector
    abstract CompletedEventsFragment contributeAdminEventListFragment();

    @ContributesAndroidInjector
    abstract GuestHomeFragment contributeGuestHomeFragment();

    @ContributesAndroidInjector
    abstract EventParticipantsFragment contributeEventParticiapntsFragment();

    @ContributesAndroidInjector
    abstract SignatureFragment contributeSignatureFragment();

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract EventListFragment contributeEventListFragment();

    @ContributesAndroidInjector
    abstract EventDetailsFragment contributeEventDetailsFragment();

    @ContributesAndroidInjector
    abstract ShopFragment contributeShopFragment();

    @ContributesAndroidInjector
    abstract ArchieCardFragment contributeArchieCardFragment();

    @ContributesAndroidInjector
    abstract GiftCardFragment contributeGiftCardFragment();

    @ContributesAndroidInjector
    abstract ArchieCardsDetailsFragment contributeArchieCardsDetailsFragment();

    @ContributesAndroidInjector
    abstract GiftCardsDetailsFragment contributeGiftCardsDetailsFragment();

    @ContributesAndroidInjector
    abstract GearFragment contributeGearFragment();


    @ContributesAndroidInjector
    abstract RentalFragment contributeRentalFragment();

    @ContributesAndroidInjector
    abstract ProductFragment contributeProductFragment();

    @ContributesAndroidInjector
    abstract ProductDetailFragment contributeProductDetailFragment();

    @ContributesAndroidInjector
    abstract CartFragment contributeCartFragment();

    @ContributesAndroidInjector
    abstract AboutUsFragment contributeAboutUsFragment();

    @ContributesAndroidInjector
    abstract SettingsFragment contributeSettingsFragment();

    @ContributesAndroidInjector
    abstract ChangePasswordFragment contributeChangePasswordFragment();

    @ContributesAndroidInjector
    abstract MembershipFragment contributeMembershipFragment();

    @ContributesAndroidInjector
    abstract MembershipDetailsFragment contributeMembershipDetailsFragment();

    @ContributesAndroidInjector
    abstract CreditHistoryFragment contributeCreditHistoryFragment();

    @ContributesAndroidInjector
    abstract FlipperEventFragment contributeFlipperEventFragment();

    @ContributesAndroidInjector
    abstract EnrolledEventListFragment contributeEnrolledEventListFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract AddressFragment contributeAddressFragment();

    @ContributesAndroidInjector
    abstract CartReviewFragment contributeCartReviewFragment();

    @ContributesAndroidInjector
    abstract TermsNConditionsFragment contributeTermsNCondtionsFragment();

    @ContributesAndroidInjector
    abstract PaymentFragment contributePaymentFragment();

    @ContributesAndroidInjector
    abstract ReceiptFragment contributeReceiptFragment();

    @ContributesAndroidInjector
    abstract EventAccessorySelectionFragment contributeEventAccessorySelectionFragment();

    @ContributesAndroidInjector
    abstract TabbedDutyEventsFragment contributeTabbedDutyEventsFragment();

    @ContributesAndroidInjector
    abstract DutyListFragment contributeDutyListFragment();

    @ContributesAndroidInjector
    abstract PolicyFragment contributePolicyFragment();

    @ContributesAndroidInjector
    abstract RegistrationStep1Fragment contributeReg1Fragment();

    @ContributesAndroidInjector
    abstract RegistrationStep2Fragment contributeReg2Fragment();

    @ContributesAndroidInjector
    abstract EWaiverListFragment contributeWaiverListFragment();

    @ContributesAndroidInjector
    abstract WaiverDetailsFragment contributeWaiverDetailsFragment();

    @ContributesAndroidInjector
    abstract WaiverSignatureFragment contributeWaiverSignatureFragment();

    @ContributesAndroidInjector
    abstract TransferCreditFragment contributeTransferCreditFragment();

    @ContributesAndroidInjector
    abstract AdminDutyFragment contributeAdminDutyFragment();

    @ContributesAndroidInjector
    abstract PassportSignatureFragment contributePassportSignatureFragment();

    @ContributesAndroidInjector
    abstract ShowPassportFragment contributeShowPassportFragment();



}
