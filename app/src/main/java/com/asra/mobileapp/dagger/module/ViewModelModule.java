package com.asra.mobileapp.dagger.module;


import com.asra.mobileapp.ui.admin.AdminViewModel;
import com.asra.mobileapp.ui.admin.adminduties.AdminDutyViewModel;
import com.asra.mobileapp.ui.admin.completedevents.CompletedEventsViewModel;
import com.asra.mobileapp.ui.admin.eventparticipants.EventParticipantViewModel;
import com.asra.mobileapp.ui.admin.signature.SignatureViewModel;
import com.asra.mobileapp.ui.base.ViewModelFactory;
import com.asra.mobileapp.ui.dashboard.DashboardViewModel;
import com.asra.mobileapp.ui.dashboard.cart.CartViewModel;
import com.asra.mobileapp.ui.dashboard.checkout.cartreview.CheckoutViewModel;
import com.asra.mobileapp.ui.dashboard.checkout.payment.PaymentViewModel;
import com.asra.mobileapp.ui.dashboard.checkout.receipt.ReceiptViewModel;
import com.asra.mobileapp.ui.dashboard.duties.CoachEventDutyListViewModel;
import com.asra.mobileapp.ui.dashboard.duties.DutyListViewModel;
import com.asra.mobileapp.ui.dashboard.events.EventsViewModel;
import com.asra.mobileapp.ui.dashboard.events.eventdetails.EventDetailsViewModel;
import com.asra.mobileapp.ui.dashboard.events.motoaccessoryselection.EventAccessorySelectionViewModel;
import com.asra.mobileapp.ui.dashboard.home.HomeViewModel;
import com.asra.mobileapp.ui.dashboard.shop.ShopViewModel;
import com.asra.mobileapp.ui.dashboard.shop.TabbedProductsViewModel;
import com.asra.mobileapp.ui.dashboard.shop.archiecards.ArchieCardsDetailViewModel;
import com.asra.mobileapp.ui.dashboard.shop.giftcards.GiftCardsDetailViewModel;
import com.asra.mobileapp.ui.dashboard.shop.product.ProductViewModel;
import com.asra.mobileapp.ui.dashboard.shop.productdetails.ProductDetailsViewModel;
import com.asra.mobileapp.ui.dashboard.shop.shopcards.ShopCardViewModel;
import com.asra.mobileapp.ui.general.HostAvtivityViewModel;
import com.asra.mobileapp.ui.general.aboutus.AboutusViewModel;
import com.asra.mobileapp.ui.general.address.AddressViewModel;
import com.asra.mobileapp.ui.general.changepassword.ChangePasswordViewModel;
import com.asra.mobileapp.ui.general.credithistory.CreditHistoryViewModel;
import com.asra.mobileapp.ui.general.enrolledevents.EnrolledEventViewModel;
import com.asra.mobileapp.ui.general.enrolledevents.FlipperViewModel;
import com.asra.mobileapp.ui.general.enrolledevents.passport.showpassport.ShowPassportViewModel;
import com.asra.mobileapp.ui.general.enrolledevents.passport.uploadpassport.PassportSignatureViewModel;
import com.asra.mobileapp.ui.general.ewaiver.EWaiverViewModel;
import com.asra.mobileapp.ui.general.ewaiver.waiverdetails.WaiverDetailsViewMoel;
import com.asra.mobileapp.ui.general.membership.MembershipViewModel;
import com.asra.mobileapp.ui.general.membershipdetails.MembershipDetailsViewModel;
import com.asra.mobileapp.ui.general.policy.PolicyViewModel;
import com.asra.mobileapp.ui.general.profile.ProfileViewModel;
import com.asra.mobileapp.ui.general.settings.SettingsViewModel;
import com.asra.mobileapp.ui.general.settings.TermsViewModel;
import com.asra.mobileapp.ui.general.transfercredit.TransferCreditViewModel;
import com.asra.mobileapp.ui.guest.GuestViewModel;
import com.asra.mobileapp.ui.guest.home.GuestHomeViewModel;
import com.asra.mobileapp.ui.login.LoginActivityViewModel;
import com.asra.mobileapp.ui.login.appselector.ViewModelAppSelector;
import com.asra.mobileapp.ui.login.login.LoginViewModel;
import com.asra.mobileapp.ui.login.regisrtation.RegistrationViewModel;
import com.asra.mobileapp.ui.login.resetpassword.ResetPasswordViewModel;
import com.asra.mobileapp.ui.main.MainViewModel;
import com.asra.mobileapp.ui.splash.SplashViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsSplashViewModel(SplashViewModel splashViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelAppSelector.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsAppSelectorViewModel(ViewModelAppSelector viewModelAppSelector);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsAppLoginViewModel(LoginViewModel loginViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(LoginActivityViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsLoginActivityViewModel(LoginActivityViewModel loginActivityViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsResetPasswordViewModel(ResetPasswordViewModel resetPasswordViewModel);



    @Binds
    @IntoMap
    @ViewModelKey(AdminViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsAdminViewModel(AdminViewModel adminViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CompletedEventsViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsAdminEventsViewModel(CompletedEventsViewModel completedEventsViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(GuestViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsGuestViewModel(GuestViewModel guestViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GuestHomeViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsGuestHomeViewModel(GuestHomeViewModel guestHomeViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(EventParticipantViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsEventParticipantViewModel(EventParticipantViewModel eventParticipantViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(SignatureViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsSignatureViewModel(SignatureViewModel signatureViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsDashboardViewModel(DashboardViewModel dashboardViewModel);



    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EventsViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsEventViewModel(EventsViewModel eventsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EventDetailsViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsEventDetailsViewModel(EventDetailsViewModel eventDetailsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ShopViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsShopViewModel(ShopViewModel shopViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ShopCardViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsShopCardViewModel(ShopCardViewModel shopCardViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ArchieCardsDetailViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsArchieCardsDetailViewModel(ArchieCardsDetailViewModel shopCardViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GiftCardsDetailViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsGiftCardsDetailViewModel(GiftCardsDetailViewModel giftCardsDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TabbedProductsViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsTabbedProductsViewModel(TabbedProductsViewModel tabbedProductsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsProductViewModel(ProductViewModel productViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailsViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsProductDetailsViewModel(ProductDetailsViewModel productDetailsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsCartViewModel(CartViewModel cartViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AboutusViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsAboutusViewModel(AboutusViewModel aboutusViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HostAvtivityViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsHostAvtivityViewModel(HostAvtivityViewModel hostAvtivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsSettingsViewModel(SettingsViewModel settingsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsChangePasswordViewModel(
            ChangePasswordViewModel changePasswordViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MembershipViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsMembershipViewModel(MembershipViewModel membershipViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(MembershipDetailsViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsMembershipDetailsViewModel(
            MembershipDetailsViewModel membershipDetailsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CreditHistoryViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsCreditHistoryViewModel(
            CreditHistoryViewModel creditHistoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FlipperViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsFlipperViewModel(
            FlipperViewModel flipperViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EnrolledEventViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsEnrolledEventViewModel(
            EnrolledEventViewModel enrolledEventViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsProfileViewModel(
            ProfileViewModel profileViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddressViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsAddressViewModel(
            AddressViewModel addressViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CheckoutViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsCheckoutViewModel(
            CheckoutViewModel checkoutViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PaymentViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsPaymentViewModel(
            PaymentViewModel paymentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ReceiptViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsReceiptViewModel(
            ReceiptViewModel receiptViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EventAccessorySelectionViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsEventAccessorySelectionViewModel(
            EventAccessorySelectionViewModel eventAccessorySelectionViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(CoachEventDutyListViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsCoachEventDutyListViewModel(
            CoachEventDutyListViewModel dutyListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DutyListViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsDutyListViewModel(
            DutyListViewModel dutyListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PolicyViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsPolicyViewModel(
            PolicyViewModel policyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TermsViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsTermsViewModel(
            TermsViewModel termsViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsRegistrationViewModel(
            RegistrationViewModel registrationViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(EWaiverViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsEWaiverViewModel(
            EWaiverViewModel ewaiverViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WaiverDetailsViewMoel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsWaiverDetailsViewMoel(
            WaiverDetailsViewMoel ewaiverViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransferCreditViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsTransferCreditViewModel(
            TransferCreditViewModel transferCreditViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AdminDutyViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsAdminDutyViewModel(
            AdminDutyViewModel adminDutyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PassportSignatureViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsPassportSignatureViewModel(
            PassportSignatureViewModel passportSignatureViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ShowPassportViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsShowPassportViewModel(
            ShowPassportViewModel showPassportViewModel);

}
