package com.asra.mobileapp.common;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;

import androidx.annotation.Nullable;

public class Divider extends View {


    public Divider(Context context) {
        super(context);
    }

    public Divider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Divider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Divider(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int color = ETApplication.getInstance().isEvApp() ?
                R.color.colorPrimary : R.color.moto_primary;
        setBackgroundColor(getContext().getColor(color));
    }
}
