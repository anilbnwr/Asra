package com.asra.mobileapp.common;

import android.text.TextUtils;

import com.asra.mobileapp.BuildConfig;

import timber.log.Timber;

public class AppUtils {

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




    public static boolean isAppLatest(){


        String latestVersion = MessageProvider.getInstance().getText(MessageProvider.app_latest_version);
        String appVersion = BuildConfig.VERSION_NAME;
        if(TextUtils.isEmpty(latestVersion)){
            return true;
        }else{
            return appVersion.equalsIgnoreCase(latestVersion);
        }

    }

    public static boolean isDigitsOnly(String digits){

        return (!TextUtils.isEmpty(digits) && TextUtils.isDigitsOnly(digits));
    }


    public static double getDouble(String doubleInString){
        double result = 0d;

        try{

            result = Double.parseDouble(doubleInString);
        }catch (Exception e){
            Timber.e("Double format error %s- ", doubleInString);
        }


        return result;
    }

    public static int getInteger(String value){
        int result = 0;

        try{

            result = Integer.parseInt(value);
        }catch (Exception e){
            Timber.e("Integer convert error %s- ", value);
        }


        return result;
    }

    public static boolean isTrue(String data){

        return "1".equals(data) || "True".equalsIgnoreCase(data) || "Yes".equalsIgnoreCase(data) ;
    }
}
