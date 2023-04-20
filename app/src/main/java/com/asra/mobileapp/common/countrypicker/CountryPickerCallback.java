package com.asra.mobileapp.common.countrypicker;


import com.asra.mobileapp.model.Country;

public interface CountryPickerCallback {
    void onCountrySelected(Country var1, int position);
}
