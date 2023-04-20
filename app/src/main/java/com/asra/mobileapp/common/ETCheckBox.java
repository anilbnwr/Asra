package com.asra.mobileapp.common;

import android.content.Context;
import android.util.AttributeSet;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatCheckBox;

public class ETCheckBox extends AppCompatCheckBox {

    public ETCheckBox(Context context) {

        super(new ContextThemeWrapper(context, ETApplication.getInstance().isEvApp() ?
                R.style.EvCheckBox : R.style.MotoCheckBox));
    }

    public ETCheckBox(Context context, AttributeSet attrs) {
        super(new ContextThemeWrapper(context, ETApplication.getInstance().isEvApp() ?
                R.style.EvCheckBox : R.style.MotoCheckBox), attrs);
    }

    public ETCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(new ContextThemeWrapper(context, ETApplication.getInstance().isEvApp() ?
                R.style.EvCheckBox : R.style.MotoCheckBox), attrs, defStyleAttr);
    }
}
