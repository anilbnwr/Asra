package com.asra.mobileapp.ui.general.profile.model;

import com.asra.mobileapp.common.DateUtils;

public class SimpleDate {
    public int dayOfMonth = DateUtils.getDayOfMonth() - 1;
    public int month = DateUtils.getCurrentMonth();
    public int year = DateUtils.getCurrentYear();

}
