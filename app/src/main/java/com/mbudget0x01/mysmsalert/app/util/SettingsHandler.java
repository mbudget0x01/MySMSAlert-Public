package com.mbudget0x01.mysmsalert.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class SettingsHandler {

    private static boolean GetBooleanTrueSetting(String pName, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean setting = sharedPreferences.getBoolean(pName, true);
        return setting;
    }
    private static boolean GetBooleanFalseSetting(String pName, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean setting = sharedPreferences.getBoolean(pName, false);
        return setting;
    }

    private static String GetStringCustomSetting(String pSetting, String defaultValue, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(pSetting, defaultValue);
    }

    private static String GetStringEmptySetting(String pSetting, Context context){ return GetStringCustomSetting(pSetting, "", context); }

    public static boolean getAlertIsEnabled(Context context){ return GetBooleanTrueSetting("enable_alert", context); }

    public static boolean getVibrate(Context context){ return GetBooleanTrueSetting("vibrate", context); }

    public static boolean getRingtone(Context context){
        return GetBooleanTrueSetting("ringtone", context);
    }

    public static boolean getMapButtonEnabled(Context context){
        return GetBooleanTrueSetting("showMap_button", context);
    }

    // TODO: Check if normalize necessary!!
    public static String getAlarmNumberSetting(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String setting = sharedPreferences.getString("alarm_number", "");
        setting = normalizePhoneNumber(setting);
        return setting;
    }

    public static String getDebugAlarmNumberSetting(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String setting = sharedPreferences.getString("alarm_number_debug", "");
        setting = normalizePhoneNumber(setting);
        return setting;
    }

    public static String normalizePhoneNumber(String pNumber){
        //replace + with 00
        pNumber = pNumber.replaceAll("[+]","00");
        //return only digits
        return pNumber.replaceAll("[^\\d]", "" );
    }

    public static String getLastAlertMessage(Context context){ return  GetStringCustomSetting("last_alert", "No Message now!", context); }

    public static void setLastAlertMessage(String pMsg, Context context){
        setStringSetting("last_alert",pMsg,context);
    }

    private static void setStringSetting(String Setting, String Value, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(Setting,Value).commit();
    }

    public static String getUserCity(Context context){ return  GetStringEmptySetting("city", context); };

    public static boolean getLockscreenAutoUnlock(Context context){ return GetBooleanTrueSetting("unlock_screen", context);}

    public static boolean useInternalMap(Context context){ return GetBooleanTrueSetting("useInternalMap", context);}

    public static boolean useSatelliteMap(Context context){return GetBooleanFalseSetting("useSatelliteMap", context);}

    public static String getCustomRingtone(Context context){return GetStringEmptySetting("CustomRingtone", context);}

    public static void setCustomRingtone(String Uri, Context context){
        setStringSetting("CustomRingtone", Uri, context);
    }

    public static void setAlertNumber(String Number, Context context){setStringSetting("alarm_number", Number, context);}

    public static void setDebugAlertNumber(String Number, Context context){setStringSetting("alarm_number_debug", Number, context);}

    public static boolean getDisableDoNotDisturb(Context context){return GetBooleanFalseSetting("disableDoNotDisturb", context);}

    public static String getSelectedAdressParser(Context context){ return  GetStringCustomSetting("list_AdressParser", "MyCustom", context); }
}
