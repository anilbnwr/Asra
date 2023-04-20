package com.asra.mobileapp.util;

import android.graphics.Bitmap;
import android.util.Base64;

import com.asra.mobileapp.common.AppUtils;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class StringUtils {

    public static boolean isEmpty(String text){
        return  text == null || text.length() == 0;
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public static String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String formatAmount(double price){
        return "$"+String.format(Locale.CANADA, "%.2f", price);
    }

    public static String formatAmount(String price){
        return "$"+String.format(Locale.CANADA, "%.2f", AppUtils.getDouble(price));
    }
}
