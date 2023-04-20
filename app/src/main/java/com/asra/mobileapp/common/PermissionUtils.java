package com.asra.mobileapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import timber.log.Timber;



public class PermissionUtils {
    public static final int PERMISSION_LOCATION = 8004;
    public static final int PERMISSION_STORAGE = 8006;

    private static final String locationPermissions[] = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    private static final String storagePermissions[] = {"android.permission.WRITE_EXTERNAL_STORAGE"};

    public static boolean isGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasAllPermissions(Context context, String[] permissions) {

        if(context == null){
            return false;
        }
        if(hasAboveSDK23()) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean checkLocationPermission(Context context){
        return (PermissionUtils.hasAllPermissions(context, locationPermissions));
    }

    public static boolean checkStoragePermission(Context context){
        return (PermissionUtils.hasAllPermissions(context, storagePermissions));
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode){
        if(hasAboveSDK23()) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    public static void requestLocationPermission(Activity activity){
        requestPermissions(activity, locationPermissions, PermissionUtils.PERMISSION_LOCATION);
    }
    public static void requestStoragePermission(Activity activity){
        requestPermissions(activity, storagePermissions, PermissionUtils.PERMISSION_STORAGE);
    }

    public static boolean shouldShowLocationPermissionRationale(Activity activity){

        for(String permission: locationPermissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                Timber.d("Should show rational, for denied Permission:"+ permission);
                return true;
            }
        }
        return false;
    }

    private static boolean hasAboveSDK23(){
        return android.os.Build.VERSION.SDK_INT >= 23;
    }

    public static void startPermissionSettingsActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, requestCode);
    }

    public static String[] getStoragePermission(){
        return new String[] { "android.permission.WRITE_EXTERNAL_STORAGE"};
    }

    public static boolean shouldShowRationale(Activity activity, String[] permissions){

        for(String permission: permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                Timber.d("Denied Permission:"+ permission);
                return true;
            }
        }
        return false;
    }
}
