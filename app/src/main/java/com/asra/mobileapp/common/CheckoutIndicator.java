package com.asra.mobileapp.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;

import androidx.annotation.Nullable;

public class CheckoutIndicator extends View {

    private static final String STEP_SHIPPING = "Shipping";
    private static final String STEP_PAYMENT = "Payment";
    private static final String STEP_COMPLETE = "Completed";

    private static final int PHASE_ACTIVE = 1;
    private static final int PHASE_INACTIVE = 2;
    private static final int PHASE_DONE = 3;

    private String startText ;
    private String middleText ;
    private String endText ;

    private TextPaint mTextPaint;

    private Paint textPaint;
    private Paint linePaint;
    private Paint circlePaint;


    private int dividingSpace = 20;
    private int fontSize = 30;
    int leftPadding = 0;
    int rightPadding = 0;
    float  strokeWidth = 10;

    float radius = 20;

    float startCirclePoint;
    float middleCirclePoint;
    float endCirclePoint;


    private int checkoutStatus = 0;

    int colorActive;
    int colorInactive;

    int phase1;
    int phase2;
    int phase3;

    public CheckoutIndicator(Context context) {
        super(context);
        init(null);
    }

    public CheckoutIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CheckoutIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attr) {

        TypedArray ta = getContext().obtainStyledAttributes(attr, R.styleable.CheckoutIndicator);
        checkoutStatus = ta.getInt(R.styleable.CheckoutIndicator_checkoutStatus, 0);
        startText = ta.getString(R.styleable.CheckoutIndicator_startText);
        middleText = ta.getString(R.styleable.CheckoutIndicator_middleText);
        endText = ta.getString(R.styleable.CheckoutIndicator_endText);

        fontSize = ta.getDimensionPixelSize(R.styleable.CheckoutIndicator_checkoutFontSize, 30);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.CheckoutIndicator_checkoutStrokeWidth, 10);


        ta.recycle();



        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(strokeWidth);

        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(strokeWidth);


        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);


        startText = TextUtils.isEmpty(startText) ? STEP_SHIPPING : startText;
        middleText = TextUtils.isEmpty(middleText) ? STEP_PAYMENT : middleText;
        endText = TextUtils.isEmpty(endText) ? STEP_COMPLETE : endText;

        switch (checkoutStatus) {
            case 0:
                phase1 = PHASE_ACTIVE;
                phase2 = phase3 = PHASE_INACTIVE;
                break;
            case 1:
                phase1 = PHASE_DONE;
                phase2 = PHASE_ACTIVE;
                phase3 = PHASE_INACTIVE;
                break;
            case 2:
                phase1 = phase2 = phase3 = PHASE_DONE;
                break;
        }

        if(ETApplication.getInstance().isEvApp()) {
            colorActive = UiUtils.getColorFromResource(getContext(), R.color.colorPrimary);
        }else{
            colorActive = UiUtils.getColorFromResource(getContext(), R.color.moto_primary);
        }
        colorInactive = UiUtils.getColorFromResource(getContext(), R.color.color_checkout_indicator_line_inactive);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setTextSize(fontSize);
        int leftTextWidth = (int) mTextPaint.measureText(startText);
        int endTextWidth = (int) mTextPaint.measureText(endText);
        int canvasWidth = getWidth();

        startCirclePoint = leftPadding + (leftTextWidth/2);
        endCirclePoint = canvasWidth - rightPadding - (endTextWidth/2);
        middleCirclePoint = (endCirclePoint - startCirclePoint) /2 + (leftTextWidth/2) ;



        linePaint.setStyle(Paint.Style.STROKE);



        float line1Start =  startCirclePoint + radius;

        float line1End = middleCirclePoint -  radius;

        if(checkoutStatus != 0) {
            linePaint.setColor(UiUtils.getColorFromResource(getContext(), R.color.color_checkout_indicator_line_active));
        }else{
            linePaint.setColor(UiUtils.getColorFromResource(getContext(), R.color.color_checkout_indicator_line_inactive));
        }
        canvas.drawLine(line1Start, radius * 2, line1End, radius *2, linePaint);

        if(checkoutStatus != 2) {
            linePaint.setColor(UiUtils.getColorFromResource(getContext(), R.color.color_checkout_indicator_line_inactive));
        }else{
            linePaint.setColor(UiUtils.getColorFromResource(getContext(), R.color.color_checkout_indicator_line_active));
        }
        float line2Start = middleCirclePoint + radius;
        float line2End = endCirclePoint - (int)radius;
        canvas.drawLine(line2Start, radius * 2, line2End, radius * 2, linePaint);

        drawCircle(canvas, startCirclePoint, phase1);
        drawText(canvas, startCirclePoint, startText);

        drawCircle(canvas, middleCirclePoint, phase2);
        drawText(canvas, middleCirclePoint, middleText);

        drawCircle(canvas, endCirclePoint, phase3);
        drawText(canvas, endCirclePoint, endText);
    }






    private void drawMarker(Canvas canvas, int left, String text, Bitmap icon) {

        int textWidth = (int) mTextPaint.measureText(text);
        int midPoint = left + (textWidth / 2);

        //int bitmapLeft = midPoint - (icon.getWidth()/2) - (2 * imagePadding);

       // canvas.drawBitmap(icon, bitmapLeft, 0, null);

        circlePaint.setColor(Color.GRAY);
        canvas.drawCircle(midPoint, 0,20, circlePaint);

        canvas.drawText(text, left, icon.getHeight() + dividingSpace + textPaint.getTextSize(), textPaint);

    }

    private void drawCircle(Canvas canvas, float left, int state) {


        if(state == PHASE_INACTIVE){
            circlePaint.setColor(colorInactive);
            circlePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(left, (radius * 2),radius, circlePaint);
        }else if(state == PHASE_ACTIVE){
            circlePaint.setColor(colorActive);
            circlePaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(left, (radius * 2),radius, circlePaint);
        }else if(state == PHASE_DONE){
            circlePaint.setColor(colorActive);
            circlePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(left, (radius * 2),radius, circlePaint);
        }




    }

    private void drawText(Canvas canvas, float left, String text) {

        int textWidth = (int) mTextPaint.measureText(text);
        canvas.drawText(text, left - (textWidth/2) + radius, (radius * 2) + dividingSpace + textPaint.getTextSize(), textPaint);

    }
}
