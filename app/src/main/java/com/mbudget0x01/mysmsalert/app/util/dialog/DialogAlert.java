package com.mbudget0x01.mysmsalert.app.util.dialog;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class DialogAlert extends AlertDialog.Builder implements ISimpleDialog {

    public DialogAlert(@NonNull Activity activity) {
        super(activity);
        super.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        return;
                    }
                });
        super.setIcon(android.R.drawable.ic_dialog_alert);
    }

    @Override
    public AlertDialog getDialog() {
        return super.create();
    }

    @Override
    public void setValues(String message, String title) {
        super.setMessage(message);
        super.setTitle(title);
    }
}
