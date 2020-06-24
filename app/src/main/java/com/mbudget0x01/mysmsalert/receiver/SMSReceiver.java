package com.mbudget0x01.mysmsalert.receiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

import com.mbudget0x01.mysmsalert.app.MainActivityNew;
import com.mbudget0x01.mysmsalert.app.util.SettingsHandler;
import com.mbudget0x01.mysmsalert.sms.SMSMessage;

public class SMSReceiver extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();
    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + msgs[i].getMessageBody());
                newMessage(msgs[i].getOriginatingAddress(),msgs[i].getMessageBody(), msgs[i].getTimestampMillis(), context);
            }
        }
    }

    private boolean newMessage(String origin, String message, long pTimeStamp, Context context){
        boolean _res = true;
        try{
            SMSMessage msg = new SMSMessage(origin, message,pTimeStamp );
            if (!isAlertMessage(msg, context)){return _res;}
            notify(msg, context);

        }
        catch (Exception e){
            Log.e(TAG,"Error creating Alert", e );
            _res = false;
        }
        return  _res;
    }



    void notify(SMSMessage msg, Context context){
        if (lastMessage!= null){
            if (lastMessage.sentFrom.equals(msg.sentFrom) && lastMessage.message.equals(msg.message) && lastMessage.timeStamp == msg.timeStamp){return;}}
        lastMessage = msg;

        Log.d(TAG, "Sending finish broadcast.");
        Intent myMSGIntent = new Intent(MainActivityNew.ReceiverTAG_Close);
        myMSGIntent.putExtra("timeStamp", msg.timeStamp);
       context.sendBroadcast(myMSGIntent);

        Log.d(TAG, "Starting new activity");
        Intent myIntent = new Intent(context, MainActivityNew.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra("newAlert", "newAlert");
        myIntent.putExtra("sentFrom", msg.sentFrom);
        myIntent.putExtra("message", msg.message);
        myIntent.putExtra("timeStamp", msg.timeStamp);
        context.startActivity(myIntent);




    }

    private SMSMessage lastMessage;

    private boolean isAlertMessage(SMSMessage pMsg, Context context){
        if (!SettingsHandler.getAlertIsEnabled(context)){return false;}
        if (PhoneNumberUtils.compare(SettingsHandler.getAlarmNumberSetting(context), pMsg.sentFrom)){return  true;}
        if (PhoneNumberUtils.compare(SettingsHandler.getDebugAlarmNumberSetting(context), pMsg.sentFrom)){return  true;}
        return false;
    }
}
