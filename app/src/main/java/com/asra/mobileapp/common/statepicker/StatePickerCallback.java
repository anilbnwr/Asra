package com.asra.mobileapp.common.statepicker;


import com.asra.mobileapp.model.State;

public interface StatePickerCallback {
    void onStateSelected(State state, int position);
}
