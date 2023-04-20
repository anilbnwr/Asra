package com.asra.mobileapp.common;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.marcoscg.materialtoast.MaterialToast;


public class ToastHelper {

    private static ToastHelper instance;

    public static ToastHelper getInstance() {
        if (instance == null) {
            instance = new ToastHelper();
        }
        return instance;
    }

    private ToastHelper() {

    }

    public void showError(Context context, String message) {
        //Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
        new MaterialToast(context)
                .setMessage(message)
                .setIcon(android.R.drawable.stat_notify_error)
                .setDuration(Toast.LENGTH_SHORT)
                .setBackgroundColor(Color.RED)
                .show();
    }

    public void showSuccess(Context context, String message) {

       // Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
        new MaterialToast(context)
                .setMessage(message)
               // .setIcon(R.mipmap.ic_launcher)
                .setDuration(Toast.LENGTH_SHORT)
                .setBackgroundColor(UiUtils.getPrimaryColor(context))
                .show();
    }

    public void showInfo(Context context, String message) {
        new MaterialToast(context)
                .setMessage(message)
                // .setIcon(R.mipmap.ic_launcher)
                .setDuration(Toast.LENGTH_SHORT)
                .setBackgroundColor(UiUtils.getPrimaryColor(context))
                .show();
    }

    public void showToast(Context context, String message) {
        new MaterialToast(context)
                .setMessage(message)
                // .setIcon(R.mipmap.ic_launcher)
                .setDuration(Toast.LENGTH_SHORT)
                .setBackgroundColor(UiUtils.getPrimaryColor(context))
                .show();
    }

    public void resetConfig() {
    }
}
