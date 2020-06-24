package com.mbudget0x01.mysmsalert.app.util.dialog;

import androidx.appcompat.app.AlertDialog;

public interface ISimpleDialog {
    AlertDialog getDialog();
    void setValues(String message, String title);
}
