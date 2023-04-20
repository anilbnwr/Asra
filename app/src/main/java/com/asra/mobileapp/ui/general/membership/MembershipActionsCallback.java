package com.asra.mobileapp.ui.general.membership;


import com.asra.mobileapp.model.Membership;

public interface MembershipActionsCallback {

    void onUpgrade(Membership selectedMembership);
    void onViewMore(Membership selectedMembership);
}
