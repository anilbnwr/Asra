package com.asra.mobileapp.ui.dashboard.home;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.DateUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.common.dialog.ReferAFriendDialog;
import com.asra.mobileapp.common.imageloader.GlideHelper;
import com.asra.mobileapp.databinding.FragmentDashboardBinding;
import com.asra.mobileapp.model.CreditHistory;
import com.asra.mobileapp.model.EnrolledEvent;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.dashboard.ShoppeFragment;
import com.asra.mobileapp.ui.dashboard.duties.TabbedDutyEventsFragment;
import com.asra.mobileapp.ui.general.credithistory.CreditHistoryFragment;
import com.asra.mobileapp.ui.general.enrolledevents.EventConstants;
import com.asra.mobileapp.ui.general.enrolledevents.FlipperEventFragment;
import com.asra.mobileapp.ui.general.policy.PolicyActivity;
import com.asra.mobileapp.util.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import androidx.annotation.Nullable;

import static com.asra.mobileapp.ui.general.policy.PolicyActivity.KEY_AGREEMENT_STATUS;

public class HomeFragment extends ShoppeFragment<HomeViewModel, FragmentDashboardBinding> {

    private MenuItem cartMenuItem;
    private MenuItem switchModuleMenu;
    private MenuItem switchAppMenu;

    private ExpandableCardHelper upcomingEventHelper;
    private ExpandableCardHelper pastEventHelper;
    private ExpandableCardHelper creditHistoryHelper;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private Runnable scrollToBottom = () -> dataBinding.homeScrollView.smoothScrollBy(0, 500);
    private ExpandableCardHelper.ETExpandListener expandedListener = expanded -> {
        if (expanded) {
            dataBinding.homeScrollView.post(scrollToBottom);
        }
    };

    @Override
    public MenuItem getCartToolbarMenu() {
        return cartMenuItem;
    }

    @Override
    protected Class getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void observeEventsFromViewModel() {
        super.observeEventsFromViewModel();
        viewModel.canSwicthModuleObserver.observe(this, canSwitch -> switchModuleMenu.setVisible(canSwitch));
        viewModel.membershipStatusObservable.observe(this, this::updateMembershipStatus);
        viewModel.userDetailsObserver.observe(this, this::setUpDashboardUi);
        viewModel.userDetailsErrorObserver.observe(this, error -> showEmptyMessage(true, error));

        viewModel.allEventCountObserver.observe(this, this::setAlltimeEventShortCard);
        viewModel.pastEventObserver.observe(this, this::setUpPastEventCard);
        viewModel.pastEventCountObserver.observe(this, pastEventsCount -> {
            setPastEventShortCard(pastEventsCount);

            if (pastEventsCount == 0)
                showNoEventError(pastEventHelper);
        });

        viewModel.upcomingEventObserver.observe(this, this::setUpComingEventCard);
        viewModel.upcomingEventCountObserver.observe(this, count -> {
            setUpcomingShortCard(count);
            if (count == 0)
                showNoEventError(upcomingEventHelper);
        });

        viewModel.creditListErrorObserver.observe(this, error -> showNoEventError(creditHistoryHelper));
        viewModel.creditHistoryObserver.observe(this, this::setUpExpandableCreditHistoryCard);

        viewModel.policyCheckFailed.observe(this, message ->{
            showDialog(message, "Exit", new ETDialog.DialogListener() {
                @Override
                public void onPositiveButtonClicked() {
                    super.onPositiveButtonClicked();
                    getBaseActivity().finish();
                }
            });
        });

        viewModel.policyNotAcceptedObservable.observe(this, policyAgreement ->{
            if(!policyAgreement.agreementStatus){
                Intent intent = new Intent(getContext(), PolicyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 101);
            }
        });

        viewModel.refernalInviationObservable.observe(this, status -> showSuccessToast(getString(R.string.msg_referral_sent)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(data != null) {
                boolean agreed = data.getBooleanExtra(KEY_AGREEMENT_STATUS, false);
                if (!agreed) {
                    getActivity().finish();
                }
            }else{
                getActivity().finish();
            }
        }
    }

    @Override
    public void initializeViews() {

        super.initializeViews();
        setUpExpandableCards();

        hideBackButton();

        setAlltimeEventShortCard(0);
        setPastEventShortCard(0);
        setUpcomingShortCard(0);

        viewModel.loadUserDetails();
        viewModel.loadEventHistory();
        viewModel.loadCreditHistory();

        dataBinding.btnCardDutyList.setOnClickListener(view ->
                loadFragment(TabbedDutyEventsFragment.newInstance()));
        dataBinding.btnReferAFriend.setOnClickListener(view -> {
            showReferAFriendDialog();
        });
    }

    private void showReferAFriendDialog() {
        ReferAFriendDialog dialog = new ReferAFriendDialog(getActivity(),
                new ReferAFriendDialog.DialogListener() {
                    @Override
                    public void onSend(String email) {
                        viewModel.sendInvitation(email);
                    }
                });
        dialog.show();
    }

    private void setUpExpandableCards() {

        setUpExpandableUpcomingEventsCard();
        setUpExpandablePastEventsCard();
        setUpExpandableCreditHistoryCard();


    }

    private void setUpExpandableUpcomingEventsCard() {

        upcomingEventHelper = new ExpandableCardHelper(dataBinding.dashboardExpandableUpcomingEvents, () -> {
            //Load EnrolledEvent Flipper
            FlipperEventFragment fragment = FlipperEventFragment.newInstance(EventConstants.TYPE_UPCOMING);
            loadFragment(fragment);
        });
        upcomingEventHelper.setEmptyMessage(MessageProvider.getInstance().getText(MessageProvider.error_no_event));
        upcomingEventHelper.setTitle(getString(R.string.label_upcoming_event));

    }

    @Override
    public String getTitle() {
        return getString(R.string.title_home);
    }


    private void setUpExpandablePastEventsCard() {

        pastEventHelper = new ExpandableCardHelper(dataBinding.dashboardExpandablePastEvents, () -> {
            FlipperEventFragment fragment = FlipperEventFragment.newInstance(EventConstants.TYPE_PAST);
            loadFragment(fragment);
        });

        pastEventHelper.setEmptyMessage(MessageProvider.getInstance().getText(MessageProvider.error_no_event));
        pastEventHelper.setTitle(getString(R.string.label_past_event));
        pastEventHelper.setExpandListener(expandedListener);

    }


    private void setUpExpandableCreditHistoryCard() {
        creditHistoryHelper = new ExpandableCardHelper(dataBinding.dashboardExpandableCreditHistory, () -> {
            CreditHistoryFragment fragment = CreditHistoryFragment.newInstance();
            loadFragment(fragment);
        });

        creditHistoryHelper.setEmptyMessage(MessageProvider.getInstance().getText(MessageProvider.error_no_credit));
        creditHistoryHelper.setExpandListener(expandedListener);
        creditHistoryHelper.setTitle(getString(R.string.label_credit_history));

    }

    private void updateMembershipStatus(String status){
        dataBinding.dashboardMemberLevelValue.setText(status);

    }

    private void setUpDashboardUi(UserDetails user) {
        dataBinding.dashboardMemberName.setText(AppUtils.toTitleCase(user.firstName + " " + user.lastName));
        dataBinding.btnCardDutyList.setVisibility(user.enableMyDuties ? View.VISIBLE : View.GONE);

        if(StringUtils.isEmpty(user.customerID)){
            dataBinding.dashboardCustomerId.setVisibility(View.GONE);
        }else{
            dataBinding.dashboardCustomerId.setText(String.format("Cust. ID: #%s", user.customerID));
            dataBinding.dashboardCustomerId.setVisibility(View.VISIBLE);
        }

        String memberSince = DateUtils.getDateAsString(user.registeredDate,
                DateUtils.SIMPLE_DATE_FORMAT, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        memberSince = "Member Since: " + memberSince;
        dataBinding.dashboardMemberTenure.setText(memberSince);

        dataBinding.dashboardMemberValueCredits.setText((StringUtils.formatAmount(user.walletAmount)));


        String expiryDate = DateUtils.getDateAsString(user.membershipExpDate,
                DateUtils.SIMPLE_DATE_FORMAT, DateUtils.DD_MMM_YYYY_DATE_PATTERN);
        dataBinding.dashboardMemberExpiryValue.setText(expiryDate);

        if (DateUtils.isBeforeToday(user.membershipExpDate,
                DateUtils.SIMPLE_DATE_FORMAT)) {
            dataBinding.dashboardMemberExpiryValue.setTextColor(Color.RED);
        }

        setProfileImage(user.profileImagePath);

        setSkillLevelCard(user);


    }

    private void setProfileImage(String url) {
        if (!TextUtils.isEmpty(url)) {
            url = UiUtils.getETAbsoluteURL(url);

            GlideHelper.setImage(this, dataBinding.dashboardProfileImage,
                    url, R.drawable.avatar, dataBinding.profileProgressbar);
        } else {
            dataBinding.profileProgressbar.setVisibility(View.GONE);
            dataBinding.dashboardProfileImage.setImageResource(R.drawable.avatar);
        }
    }

    @Override
    public boolean isToolbarMenuEnabled() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_tab_home, menu);
        cartMenuItem = menu.findItem(R.id.menu_toolbar_cart);
        switchModuleMenu = menu.findItem(R.id.menu_switch_module);
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
            case R.id.menu_switch_module:
                viewModel.switchToAdminModule();
                return true;
            case R.id.menu_switch_app:
                viewModel.onSwitchApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setSkillLevelCard(UserDetails user) {
        ImageView icon = dataBinding.cardDashboardSkillLevel.findViewById(R.id.event_card_icon);
        TextView title = dataBinding.cardDashboardSkillLevel.findViewById(R.id.event_card_title);
        TextView description = dataBinding.cardDashboardSkillLevel.findViewById(R.id.event_card_description);

        icon.setImageResource(isEvApp() ? R.drawable.starnew : R.drawable.moto_skill_level);

        if(isEvApp() || user.isCoach() || user.isAdmin()){
            title.setText(user.skillLevel);
        }else{
            title.setText(user.motoSkill);
        }


        description.setText("Skill Level");
    }

    private void setUpcomingShortCard(int eventCount) {
        ImageView icon = dataBinding.cardDashboardUpcomingEvents.findViewById(R.id.event_card_icon);
        TextView title = dataBinding.cardDashboardUpcomingEvents.findViewById(R.id.event_card_title);
        TextView description = dataBinding.cardDashboardUpcomingEvents.findViewById(R.id.event_card_description);

        icon.setImageResource(isEvApp() ? R.drawable.eventsnew : R.drawable.moto_upcoming_events);
        title.setText(String.format(Locale.CANADA, "%02d", eventCount));
        description.setText("Upcoming Events");

        dataBinding.cardDashboardUpcomingEvents.setOnClickListener(view -> {
            FlipperEventFragment fragment = FlipperEventFragment.newInstance(EventConstants.TYPE_UPCOMING);
            loadFragment(fragment);
        });
    }

    private void setAlltimeEventShortCard(int count) {
        ImageView icon = dataBinding.cardDashboardAllTimeEvents.findViewById(R.id.event_card_icon);
        TextView title = dataBinding.cardDashboardAllTimeEvents.findViewById(R.id.event_card_title);
        TextView description = dataBinding.cardDashboardAllTimeEvents.findViewById(R.id.event_card_description);

        icon.setImageResource(isEvApp() ? R.drawable.eventsnew : R.drawable.moto_events_alltime);
        title.setText(String.format(Locale.CANADA, "%02d", count));
        description.setText(R.string.txt_events_all_time);

        dataBinding.cardDashboardAllTimeEvents.setOnClickListener(view -> {
            FlipperEventFragment fragment = FlipperEventFragment.newInstance(EventConstants.TYPE_ALL);
            loadFragment(fragment);
        });
    }

    private void setPastEventShortCard(int count) {
        ImageView icon = dataBinding.cardDashboardPastEvents.findViewById(R.id.event_card_icon);
        TextView title = dataBinding.cardDashboardPastEvents.findViewById(R.id.event_card_title);
        TextView description = dataBinding.cardDashboardPastEvents.findViewById(R.id.event_card_description);

        icon.setImageResource(isEvApp() ? R.drawable.eventsnew : R.drawable.moto_past_events);
        title.setText(String.format(Locale.CANADA, "%02d", count));
        description.setText("Past Events");

        dataBinding.cardDashboardPastEvents.setOnClickListener(view -> {
            FlipperEventFragment fragment = FlipperEventFragment.newInstance(EventConstants.TYPE_PAST);
            loadFragment(fragment);
        });
    }

    private void setUpPastEventCard(EnrolledEvent event) {
        pastEventHelper.setContents(event);
        pastEventHelper.collapse();

    }

    private void setUpComingEventCard(EnrolledEvent event) {
        upcomingEventHelper.setContents(event);
        upcomingEventHelper.expand();
    }

    public void setUpExpandableCreditHistoryCard(CreditHistory credit) {
        creditHistoryHelper.setContents(credit);
        creditHistoryHelper.collapse();

    }

    private void showNoEventError(ExpandableCardHelper cardHelper) {
        cardHelper.showError(true);
    }

    @Override
    public void switchModule(Boolean canSwitch) {
        switchToAdminDashboard();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onViewStarted();
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();

        dataBinding.dashboardContainerProfile.setBackgroundColor(getPrimaryColor());
        dataBinding.dutyText.setBackgroundResource(R.drawable.border_green);
        dataBinding.dutyText.setTextColor(getColor(R.color.colorPrimary));
        if (switchAppMenu != null)
          //  switchAppMenu.setIcon(R.drawable.ic_app_switch_moto);

        dataBinding.referAFriendContainer.setBackgroundResource(R.drawable.border_green);
        dataBinding.textReferAFriend.setTextColor(getColor(R.color.colorPrimary));
        dataBinding.iconReferAFriend.setImageResource(R.drawable.refer_a_friend_moto);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.dashboardContainerProfile.setBackgroundColor(getPrimaryColor());
        dataBinding.dutyText.setBackgroundResource(R.drawable.border_moto_blue);
        dataBinding.dutyText.setTextColor(getColor(R.color.colorBlue));
        if (switchAppMenu != null)
            switchAppMenu.setIcon(R.drawable.ic_app_switch_ev);

        dataBinding.referAFriendContainer.setBackgroundResource(R.drawable.border_moto_blue);
        dataBinding.textReferAFriend.setTextColor(getColor(R.color.colorBlue));
        dataBinding.iconReferAFriend.setImageResource(R.drawable.refer_a_friend_moto);
    }
}
