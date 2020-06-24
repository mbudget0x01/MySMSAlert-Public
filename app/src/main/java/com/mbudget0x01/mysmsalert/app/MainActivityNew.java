package com.mbudget0x01.mysmsalert.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mbudget0x01.mysmsalert.R;
import com.mbudget0x01.mysmsalert.app.addressparsing.AdressParserFactory;
import com.mbudget0x01.mysmsalert.app.addressparsing.IAdressParser;
import com.mbudget0x01.mysmsalert.app.mapintegration.DisplayAdressActivity;
import com.mbudget0x01.mysmsalert.app.util.PermissionHandler;
import com.mbudget0x01.mysmsalert.app.util.SettingsHandler;
import com.mbudget0x01.mysmsalert.sms.SMSMessage;

public class MainActivityNew extends AppCompatActivity {



    public PhysicalAlert MyPhysicalAlert;


    public static String ReceiverTAG_Close = "MY_SMS_ALERT_NEW_ALERT";
    public String TAG = this.getClass().getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        //permission Stuff
        checkPermissions();
        //setUp Actors
        MyPhysicalAlert = new PhysicalAlert(this.getBaseContext(),
                SettingsHandler.getVibrate(this),
                SettingsHandler.getRingtone(this));


        //check if its service alert
        Intent myinten = getIntent();

        if (myinten.getStringExtra("newAlert") != null)
        {
            Log.d(TAG, "new Alert");
            SMSMessage alm = new SMSMessage(myinten.getStringExtra("sentFrom"), myinten.getStringExtra("message"),myinten.getLongExtra("timeStamp", 0));
            NewAlertReceived(alm);
        } else {
            Log.d(TAG, "Just Startup");
            msgTimeStamp = 1;
            //set up service
        }
        //register gui Handlers
        final Button button_ack = findViewById(R.id.button_ack2);
        button_ack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ack_pressed();
            }
        });

        final Button button_settings = findViewById(R.id.button_showsettings);
        button_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSettings();
            }
        });

        //set last Message
        setAlertMsg(SettingsHandler.getLastAlertMessage(this));

        //register broadcast Alert for better gui intsances
        registerReceiver(broadcastAlertReceiver, new IntentFilter(ReceiverTAG_Close));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastAlertReceiver);
        Log.d(TAG, "destroy");
        if (MyPhysicalAlert != null){MyPhysicalAlert.stopAlert();}
    }


    private void checkPermissions() {
        PermissionHandler.checkAndRequestAllPermissions(this);
    }

    public void NewAlertReceived(SMSMessage msg) {
        msgTimeStamp = msg.timeStamp;
        MyPhysicalAlert.stopAlert();
        setAlertMsg(msg.message);
        SettingsHandler.setLastAlertMessage(msg.message, this);
        MyPhysicalAlert.startAlert();
        unlockScreen();
    }

    private void ack_pressed(){
        MyPhysicalAlert.stopAlert();
        if (SettingsHandler.getMapButtonEnabled(this)){
            showMap();
        }
    }

    private void setAlertMsg(String pMsg){
        TextView myTextView = findViewById(R.id.label_msg2);
        myTextView.setText(pMsg);
    }

    private void showSettings(){
        Intent myIntent = new Intent(this, CustomSettingsActivity.class);
        startActivity(myIntent);
        MyPhysicalAlert.stopAlert();
    }

    private void displayToastInfo( String pMsg){
        Toast.makeText(this ,pMsg,Toast.LENGTH_LONG).show();
    }


    private void showMap(){
        MyPhysicalAlert.stopAlert();
        String mapErrorMsg = "Could not resolve Addeess!";
        String Address;
        //trygetAddress
        //can be extended cause of the interface
        IAdressParser ap = AdressParserFactory.getAddressParser(this);
        try {
            Address = ap.tryParseAddress(SettingsHandler.getLastAlertMessage(this));
            Log.d(TAG, "showMap: "+Address);
        } catch (Exception e){
            displayToastInfo(mapErrorMsg);
            return;
        }

        //Map Stuff
        if(SettingsHandler.useInternalMap(this)){
        Intent myIntent = new Intent(this, DisplayAdressActivity.class);
        myIntent.putExtra("address", Address);
        startActivity(myIntent);
        } else {
            try {
                //launch google Maps here
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } catch(Exception e){
                String ErrMsg = "Error Opening Google Maps externally. Is the package Installed?";
                Log.e(TAG, ErrMsg,e);
                displayToastInfo(ErrMsg);
            }
        }
    }


    private void unlockScreen(){
        if (!SettingsHandler.getLockscreenAutoUnlock(this)){return;}
        Window myWindow = this.getWindow();
        myWindow.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        myWindow.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        myWindow.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        myWindow.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void checkCloseInstance(long pTimeStamp){
        //checks if instance must be closed
        Log.d(TAG,"Time stamps " + msgTimeStamp + " | " + pTimeStamp);
        try {
            if (msgTimeStamp == 0){return;}
            // finish()

        } catch (Exception e){
            Log.d(TAG, "Exeception cauth", e);
        }

    }

    private long msgTimeStamp = 0;

    // Time Stamps sind unterschiedlich wiso auch immer
    private final BroadcastReceiver broadcastAlertReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
         Log.d(TAG, "Broadcast received");
         checkCloseInstance(intent.getLongExtra("timeStamp", 0));
        }
    };


}
