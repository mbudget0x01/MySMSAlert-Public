package com.mbudget0x01.mysmsalert.app;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.mbudget0x01.mysmsalert.app.util.SettingsHandler;

import java.util.Timer;
import java.util.TimerTask;

public class PhysicalAlert {

    public PhysicalAlert(Context pContext, boolean pVibrationEnabled, boolean pSoundEnabled) {
        myContext = pContext;
        vibrate = pVibrationEnabled;
        sound = pSoundEnabled;
        myVibrator = (Vibrator) myContext.getSystemService(myContext.VIBRATOR_SERVICE);
    }
    public String TAG = this.getClass().getSimpleName();

    public boolean vibrate, sound;
    static private Context myContext;
    static private Vibrator myVibrator;
    static private Ringtone myRingtone;
    //legacy support
    static private Timer LegacyRingtoneTimer;
    //vibrator support > 26
    static private Timer VibratorTimer;
    static private boolean VibratorTimerInvert = false;
    //audio filter stuff
    private AudioManager audioManager;
    private int originalRingerMode;
    private NotificationManager notificationManager;
    private int getOriginalNotificationFilter;

    /****************************************************
     *
     * Vibrator Stuff
     *
     ****************************************************/
    private void vibrateStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            VibratorTimer = new Timer();
            VibratorTimer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    if (!VibratorTimerInvert) {
                        myVibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        VibratorTimerInvert = true;
                    } else {
                        VibratorTimerInvert = false;
                    }
                }
            }, 1000, 1000);

        } else {
            //deprecated in API 26
            myVibrator.vibrate(500);
        }
    }

    private void vibrateStop() {
        if (VibratorTimer != null){
            VibratorTimer.cancel();
            VibratorTimer = null;
        }
        myVibrator.cancel();

    }

    /****************************************************
     *
     * Sound Stuff
     *
     ****************************************************/

    private Ringtone soundStart(){

        if (SettingsHandler.getDisableDoNotDisturb(myContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                notificationManager = (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);
                getOriginalNotificationFilter = notificationManager.getCurrentInterruptionFilter();
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }

            audioManager = (AudioManager) myContext.getSystemService(Context.AUDIO_SERVICE);
            originalRingerMode = audioManager.getRingerMode();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }


        if (myRingtone == null){
        //custom Ringtone Support
            Uri notification;

            if (SettingsHandler.getCustomRingtone(myContext).isEmpty()) {
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            } else {
                //exception wird abgefangen?
                notification =  Uri.parse(SettingsHandler.getCustomRingtone(myContext));
            }

        Ringtone r = RingtoneManager.getRingtone(myContext.getApplicationContext(), notification);
        r.setStreamType(AudioManager.STREAM_ALARM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setAudioAttributes(new AudioAttributes.Builder()
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                    .build());
            r.setVolume((float) 1.0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(true);
        }
        r.play();

            //legacy support
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                LegacyRingtoneTimer = new Timer();
                LegacyRingtoneTimer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (!myRingtone.isPlaying()) {
                            myRingtone.play();
                        }
                    }
                }, 1000, 1000);
            }

        return r;
        } else {
            //in case already ringing
            soundStop();
            return soundStart();
        }
    }

    private void soundStop(){
        if (myRingtone != null){
            if (LegacyRingtoneTimer != null){
                LegacyRingtoneTimer.cancel();
                LegacyRingtoneTimer = null;
            }
            myRingtone.stop();
            myRingtone = null;}
        if(audioManager != null){
            audioManager.setRingerMode(originalRingerMode);
        }
        if(notificationManager != null){
            //compiler needs this
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                notificationManager.setInterruptionFilter(getOriginalNotificationFilter);
            }
        }
    }

    /****************************************************
     *
     * Public Stuff
     *
     ****************************************************/

    public void startAlert() {
        if (vibrate) {
            vibrateStart();
        }
        if (sound) {
            myRingtone = soundStart();
        }
    }

    public void stopAlert(){
        vibrateStop();
        soundStop();
    }
}
