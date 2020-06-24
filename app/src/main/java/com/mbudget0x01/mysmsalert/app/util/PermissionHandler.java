package com.mbudget0x01.mysmsalert.app.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHandler {

    public static final String[] BASE_PERMISSIONS = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
    };

    /****************************************************
     *
     * permission Checks
     *
     ****************************************************/

    //returns missing BasePermissions
    public static String[] checkBasePermissions(Activity activity){
        List<String> missingPermissions = new ArrayList<>();
        for (String permission: BASE_PERMISSIONS){
            if(ActivityCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED){
                missingPermissions.add(permission);
            }
        }
        String [] resp = new String[missingPermissions.size()];
        return missingPermissions.toArray(resp);
    }

    //return if Permission is OK
    public static boolean needNotificationPolicyPermission(Activity activity){
        //Corresponding Setting disabled
        if (!SettingsHandler.getDisableDoNotDisturb(activity)) {
            return false;
        }
        //check if Version compatible
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            return false;
        }

        //Check if permission granted
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if(!notificationManager.isNotificationPolicyAccessGranted()){
            return true;
        }

        //all failed
        return false;
    }

    /****************************************************
     *
     * permission Requests
     *
     ****************************************************/

    public static void requestBasePermissions(Activity activity, String[] permissions ){
        ActivityCompat.requestPermissions(activity, permissions, 1);
    }

    public static void requestBasePermissions(Activity activity){
        ActivityCompat.requestPermissions(activity, BASE_PERMISSIONS, 1);
    }

    @SuppressLint("NewApi")
    public static void requestNotificationPolicyPermission(Activity activity){
        Intent intent = new Intent(
                android.provider.Settings
                        .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        activity.startActivity(intent);
    }

    /****************************************************
     *
     * global Methodes
     *
     ****************************************************/


    public static void checkAndRequestAllPermissions(final Activity activity) {
        if (PermissionHandler.checkBasePermissions(activity).length != 0){
            PermissionHandler.requestBasePermissions(activity);
        }
        if (PermissionHandler.needNotificationPolicyPermission(activity)){
            requestNotificationPolicyPermission(activity);
        }
    }

}
