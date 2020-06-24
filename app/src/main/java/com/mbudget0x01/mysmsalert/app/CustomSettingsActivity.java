package com.mbudget0x01.mysmsalert.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.mbudget0x01.mysmsalert.R;
import com.mbudget0x01.mysmsalert.app.util.PermissionHandler;
import com.mbudget0x01.mysmsalert.app.util.SettingsHandler;
import com.mbudget0x01.mysmsalert.app.util.dialog.SimpleDialogFactory;

public class CustomSettingsActivity extends AppCompatActivity {

    public static String TAG =  "CustomSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        PermissionHandler.checkAndRequestAllPermissions(this);
        super.onStop();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.user_settings, rootKey);
             Preference pref = this.findPreference("infoAlarm");
             pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                 @Override
                 public boolean onPreferenceClick(Preference preference) {
                     displayInfoAlert();
                     return true;
                 }
             });
             pref = this.findPreference("infoLockscreen");
             pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    displayInfoLockScreen();
                    return true;
                }
            });
            pref = this.findPreference("CustomRingtone");
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    displayRingtoneDialog();
                    return true;
                }
            });
            pref = this.findPreference("pick_contact");
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DisplayContactsDialog();
                    return true;
                }
            });
            pref = this.findPreference("disableDoNotDisturb");
            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if((boolean) newValue) {
                       if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                           displayLegacyOS();
                           return false;
                       }
                        showUpdatePermissionsDialog();
                    }
                    return true;
                }
            });
        }

        private void displayInfoAlert(){
            showDialog("Up to now the sound will be reirected trough the alarm Ringtone channel " +
                    "The Volume has to be set there. This will be changed in the future if possible.",
                    "Alert Information");

        }

        private  void displayInfoLockScreen(){
            showDialog("The Lockscreen can't be disabled for Google Maps due to Google regulations." +
                    "You have to do this manually.","Lockscreen Information");

        }

        private void displayLegacyOS(){
            showDialog("Sorry! your current Android Version does not support this " +
                    "feauture.","Legacy Error");
        }

        private void displayPickedContact(String Name, String PhoneNumber ){
            showDialog(Name +"\n" + PhoneNumber,"Selected Contact");
        }

        private void displayRingtoneDialog(){
            Intent myIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            startActivityForResult(myIntent,1 );


        }
        private void DisplayContactsDialog(){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(intent, 2);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case 1:
                    if (resultCode == Activity.RESULT_OK){
                try {
                   SettingsHandler.setCustomRingtone(String.valueOf(data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)),getContext());
                } catch (Exception e){
                    Log.d(TAG,"Exception caught", e);
                }}
                break;
                case 2:
                    if (resultCode == Activity.RESULT_OK) {
                        String phoneNo;
                        String name;

                        Uri uri = data.getData();
                        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            phoneNo = cursor.getString(phoneIndex);
                            name = cursor.getString(nameIndex);
                            SettingsHandler.setAlertNumber(phoneNo, getContext());
                            displayPickedContact(name, phoneNo);
                            //Log.e("onActivityResult()", phoneIndex + " " + phoneNo + " " + nameIndex + " " + name);
                        }
                        cursor.close();

                    }
                    break;


            }

        }
        private void showDialog(String pMessage, String pTitle) {
            SimpleDialogFactory.getDialog(SimpleDialogFactory.DialogTypes.Information,pMessage,pTitle,this.getActivity()).show();
        }

        private void showUpdatePermissionsDialog() {
            SimpleDialogFactory.getDialog(SimpleDialogFactory.DialogTypes.Alert,
                    "Some permissions, need to be set externally. " +
                            "Please proceed an do this on exiting the Window.",
                    "Set Permissions",
                    this.getActivity()).show();
        }
    }
}