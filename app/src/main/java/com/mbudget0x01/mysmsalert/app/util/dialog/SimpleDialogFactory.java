package com.mbudget0x01.mysmsalert.app.util.dialog;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

public class SimpleDialogFactory {

    public static AlertDialog getDialog(DialogTypes dialogType,
                                        String message, String title, Activity activity){
        ISimpleDialog dialog;
        switch (dialogType){
            case Alert:
                dialog  = new DialogAlert(activity);
                break;
            case Information:
                dialog  = new DialogInformation(activity);
                break;
            default:
                dialog  = new DialogInformation(activity);
                break;
        }

        dialog.setValues(message,title);
        return dialog.getDialog();
    }

    public enum DialogTypes{
        Information,Alert
    }

}
