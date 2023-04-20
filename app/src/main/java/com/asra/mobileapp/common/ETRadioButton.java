package com.asra.mobileapp.common;

import android.content.Context;
import android.util.AttributeSet;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatRadioButton;

public class ETRadioButton extends AppCompatRadioButton {

    public ETRadioButton(Context context) {

        super(new ContextThemeWrapper(context, ETApplication.getInstance().isEvApp() ?
                R.style.EvRaidoButton : R.style.MotoRaidoButton));
    }

    public ETRadioButton(Context context, AttributeSet attrs) {
        super(new ContextThemeWrapper(context, ETApplication.getInstance().isEvApp() ?
                R.style.EvRaidoButton : R.style.MotoRaidoButton), attrs);
    }

    public ETRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(new ContextThemeWrapper(context, ETApplication.getInstance().isEvApp() ?
                R.style.EvRaidoButton : R.style.MotoRaidoButton), attrs, defStyleAttr);
    }
}
