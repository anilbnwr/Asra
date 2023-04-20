package com.asra.mobileapp.common;

import android.content.Context;
import android.util.AttributeSet;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SwitchCompat;

public class ETSwitch extends SwitchCompat {

    public ETSwitch(Context context) {

        super(new ContextThemeWrapper(context, ETApplication.getInstance().isEvApp() ?
                R.style.EvSwitch : R.style.MotoSwitch));
    }

    public ETSwitch(Context context, AttributeSet attrs) {
        super(new ContextThemeWrapper(context, ETApplication.getInstance().isEvApp() ?
                R.style.EvSwitch : R.style.MotoSwitch), attrs);
    }

    public ETSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(new ContextThemeWrapper(context, ETApplication.getInstance().isEvApp() ?
                R.style.EvSwitch : R.style.MotoSwitch), attrs, defStyleAttr);
    }
}
