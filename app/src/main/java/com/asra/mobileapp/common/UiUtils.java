package com.asra.mobileapp.common;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.R;

import java.util.List;

import androidx.core.content.ContextCompat;
import timber.log.Timber;

public class UiUtils {


    public static final int TAB_HOME = 0;
    public static final int TAB_EVENTS = 1;
    public static final int TAB_SHOP = 2;
    public static final int TAB_CART = 3;


    public static int getColorFromResource(Context context, int colorResource) {
        return ContextCompat.getColor(context, colorResource);
    }

    public static ColorStateList getColorStateFromResource(Context context, int colorResource) {
        return ContextCompat.getColorStateList(context, colorResource);
    }


    public static void setCartBadgeCount(Context context, MenuItem menuItem, int count) {

        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        DrawableBadge badge;

        // Reuse drawable if possible
       /* Drawable reuse = icon.findDrawableByLayerId(R.id.ic_cart_count);
        if (reuse instanceof DrawableBadge) {
            badge = (DrawableBadge) reuse;
        } else {
            badge = new DrawableBadge(context);
        }

        badge.setCount(count + "");

        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_cart_count, badge);*/

    }

    public static boolean isListEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }


    public static String getETAbsoluteURL(String url) {
        String fullUrl = url;

        if (TextUtils.isEmpty(url) ||
                Constants.ET_NO_PROFILE.equalsIgnoreCase(url)) {
            return "";
        }
        if (fullUrl.startsWith("http") || fullUrl.startsWith("https")) {
            return fullUrl;
        } else {
            fullUrl = Constants.ET_HOSTNAME + url;
        }
        Timber.d("Full URL - %s", fullUrl);
        return fullUrl;
    }

    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean isValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return Patterns.PHONE.matcher(phone).matches();
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.findViewById(android.R.id.content);
            if (view != null) {

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                else
                    Timber.e("Hiding keyboard failed - InputMethodManager == NULL");
            }
        } catch (Exception e) {
            Timber.e(e, "Hiding keyboard failed");
        }
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.et_pn_channel_name);
            String description = context.getString(R.string.et_pn_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }

    public static int getPrimaryColor(Context context) {
        int colorCode = ETApplication.getInstance().isEvApp() ? R.color.colorGreen
                : R.color.colorBlue;
        return UiUtils.getColorFromResource(context, colorCode);
    }

    public static int getSwitchAppMenuIcon(Context context) {
       /* return ETApplication.getInstance().isEvApp() ?
                R.drawable.ic_app_switch_moto
                : R.drawable.ic_app_switch_ev;*/
        return 0;
    }

}
